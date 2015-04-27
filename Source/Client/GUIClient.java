import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A Basic Instant Messenger
 * Graphical Client
 */
public class GUIClient extends Client {	
	private GUIListener listen = new GUIListener();
	
	private JFrame frame;
	
	private JPanel server = new JPanel();
	private JPanel address = new JPanel();
	private JLabel serverLabel = new JLabel("Enter Server");
	private JTextField serverField = new JTextField();
	private JTextField portField = new JTextField();
	
	private JPanel connect = new JPanel();
	private JButton connectButton = new JButton("Connect");
	private JButton stopButton = new JButton("Stop");	
	
	private JPanel text = new JPanel();
	private JTextArea textBox = new JTextArea(20, 30);
	
	private JPanel talk = new JPanel();
	private JTextField toSend = new JTextField(24);
	private JButton sendButton = new JButton("Send");	
	
	public void start() {
		frame = new JFrame("Client");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		stopButton.setEnabled(false);
		stopButton.addActionListener(listen);
		
		connectButton.addActionListener(listen);
		
		sendButton.setEnabled(false);
		sendButton.addActionListener(listen);
		
		serverField.setText("localhost");
		portField.setText("7777");
		
		address.add(serverLabel, BorderLayout.WEST);
		address.add(serverField, BorderLayout.CENTER);
		address.add(portField, BorderLayout.EAST);
		server.add(address, BorderLayout.NORTH);
		
		connect.add(connectButton, BorderLayout.WEST);
		connect.add(stopButton, BorderLayout.EAST);
		server.add(connect, BorderLayout.SOUTH);
		
		text.add(textBox, BorderLayout.CENTER);
		textBox.setEditable(false);
		
		toSend.setText("Write your message here!");
		
		talk.add(toSend);
		talk.add(sendButton, BorderLayout.EAST);
		
		frame.getContentPane().add(server, BorderLayout.NORTH);
		frame.getContentPane().add(text, BorderLayout.CENTER);
		frame.getContentPane().add(talk, BorderLayout.SOUTH);

		frame.pack();

		frame.setVisible(true);		
	}
	
	public boolean loop() {	
		return true;
	}
	
	public String getUsername() {
		return JOptionPane.showInputDialog(null, "Please enter a username", "Username", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void write(String toWrite) {
		textBox.append(toWrite + "\n");
	}
	
	public void recieve(String text) {
		String un = text.split(":")[0];
		String uptext = text.split(":")[1];
		
		write("[" + un + "]: " + uptext);
		System.out.println("Recieved: " + text);
	}
	
	public static void main(String[] args) {
		new GUIClient().start();
	}
	
	private class GUIListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == stopButton) {
				print("-=final");
				write("Exiting Server");
				stopButton.setEnabled(false);
				connectButton.setEnabled(true);
				sendButton.setEnabled(false);
			} else if (e.getSource() == connectButton) {
				stopButton.setEnabled(true);
				connectButton.setEnabled(false);
				sendButton.setEnabled(true);
				String server = serverField.getText();
			    int port = 7777;
				Integer.getInteger(portField.getText());
				connect(server, port, false);
				write("Joined Server \"" + server + ":" + port + "\"");
			} else if (e.getSource() == sendButton) {
				String input = toSend.getText();
				toSend.setText("");
				print(username + ": " + input);
			}
		}
	}
}
