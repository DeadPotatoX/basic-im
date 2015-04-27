import java.util.Scanner;

/**
 * A Basic Instant Messenger
 * System Client
 */
public class SystemClient extends Client {
	Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		new SystemClient().connect("localhost", 7777, true);
	}
	
	public boolean loop() {
		String input = in.nextLine();
		if (input.contains("exit")) return false;
		else print(input);
		
		System.out.println("Sent.");
		return true;
	}
	
	public String getUsername() {
		System.out.print("Username: ");
		String ret = in.nextLine();
		System.out.println();
		return ret;
	}
	
	public void recieve(String text) {
		System.out.println("Recieved: " + text);
	}
	
	public void write(String text) {
		System.out.println(text);
	}
}
