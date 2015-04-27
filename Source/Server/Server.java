import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A Basic Instant Messenger
 * Server Backend
 */
public abstract class Server {
	protected ServerSocket server;
	protected ArrayList<Socket> clients = new ArrayList<Socket>();

	protected ArrayList<PrintWriter> outs = new ArrayList<PrintWriter>();
	protected ArrayList<BufferedReader> inputs = new ArrayList<BufferedReader>();

	protected Thread accept;
	protected ArrayList<Thread> read = new ArrayList<Thread>();

	protected Scanner scan = new Scanner(System.in);

	protected int numClients;

	public void begin() {
		startServer(7777);
		while (true) {
			loop();
		}
	}

	private void startServer(int port) {
		try {
			server = new ServerSocket(port);
			write("Waiting for client #1");
			clients.add(server.accept());
			write("Accepted client #1");
			outs.add(new PrintWriter(clients.get(0).getOutputStream(), true));
			inputs.add(new BufferedReader(new InputStreamReader(clients.get(0)
					.getInputStream())));
			write("==================");
		} catch (Exception e) {
			write("Problem With Creating Server");
			e.printStackTrace();
		}

		accept = new Thread() {
			int client;

			public void run() {
				try {
					clients.add(server.accept());
					numClients++;
					client = numClients;

					write("Accepted client #" + (client + 1));

					outs.add(new PrintWriter(clients.get(client)
							.getOutputStream(), true));
					inputs.add(new BufferedReader(new InputStreamReader(clients
							.get(client).getInputStream())));

					read.add(new Thread() {
						public void run() {
							try {
								String inLine;

								while ((inLine = inputs.get(client).readLine()) != null) {
									if (inLine.contains("-=final"))
										removeClient(client);
									else
										process(inLine);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

					read.get(numClients).start();
				} catch (IOException e) {
					write("Problem With Accepting Clients");
					e.printStackTrace();
				}
			}
		};

		accept.start();

		read.add(new Thread() {
			public void run() {
				try {
					String inLine;

					while ((inLine = inputs.get(0).readLine()) != null) {
						if (inLine.contains("-=final"))
							removeClient(0);
						else
							process(inLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		read.get(0).start();
	}

	public void removeClient(int client) {
		numClients--;

		read.get(client).interrupt();

		if (numClients == -1) {
			outs.get(0).println("server_exit");

			inputs.remove(client);
			outs.remove(client);
			clients.remove(client);

			endServer();
		}

		inputs.remove(client);
		outs.remove(client);
		clients.remove(client);

		write("Recieved exit code from: #" + (client + 1));
	}

	public abstract void endServer();

	public abstract void loop();

	public abstract void process(String line);

	public abstract void write(String text);
}
