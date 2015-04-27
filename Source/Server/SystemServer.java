/**
 * A Basic Instant Messenger
 * System Server
 */
public class SystemServer extends Server {
	public void process(String line) {
		for (int i = 0; i < outs.size(); i++) {
			outs.get(i).println(line);
		}
		
		System.out.println("Recieved: " + line);
	}
	
	public void loop() {		
		// loop input
		String in = scan.nextLine();
		
		if (in.indexOf("exit_server") >= 0) endServer();
		else {
			for (int i = 0; i < outs.size(); i++) {
				outs.get(i).println("Server:" + in);
			}
		
			System.out.println("Sent.");
		}
	}
	
	public void endServer() {
		System.out.println("Exiting Server");
		if (accept.isAlive())
			accept.interrupt();
		
		for (int i = 0; i < numClients - 1; i++) {
			if (read.get(i).isAlive())
				read.get(i).interrupt();	

		}
		
		System.exit(0);
	}
	
	public void write(String text) {
		System.out.println(text);
	}
	
	public static void main(String[] args) {
		System.out.println("Starting server\n===========");
		new SystemServer().begin();
	}
}
