import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A Basic Instant Messenger
 * Client Backend
 */
public abstract class Client {
	protected Socket serverSocket;
	protected PrintWriter writer;
	protected BufferedReader read;
	protected String username;
	
	protected Thread in = new Thread() {
		public void run() {
			try {
				String inLine;
				
				while ((inLine = read.readLine()) != null) {
					recieve(inLine);
				}
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
	};
	
	public void connect(String server, int port, boolean loop) {
		username = getUsername();
		
		boolean running = true;
		
		write("Starting Client\n================");
		
		try {
			serverSocket = new Socket(server, port);
			
			write("Connected");
			
			writer = new PrintWriter(serverSocket.getOutputStream(), true);
			read = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		write("==================");
		
		in.start();
		
		while (running && loop) {
			running = loop();
		}
		
		in.interrupt();
	}
	
	public void print(String toPrint) {
		writer.println(toPrint);
	}
	
	public abstract boolean loop();
	public abstract String getUsername();
	public abstract void recieve(String text);
	public abstract void write(String text);
}
