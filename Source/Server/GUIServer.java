import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A Basic Instant Messenger
 * Graphical Server
 */
public class GUIServer extends Server {	
	private GUIListener listen = new GUIListener();
	
	private JFrame frame;
	
	private JPanel connect = new JPanel();
	private JButton stopButton = new JButton("Stop");	
	
	private JPanel text = new JPanel();
	private JTextArea textBox = new JTextArea(20, 30);
	
	private JPanel talk = new JPanel();
	private JTextField toSend = new JTextField(24);
	private JButton sendButton = new JButton("Send");	
	
	public void start() {
		frame = new JFrame("Server");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		stopButton.addActionListener(listen);
		
		sendButton.addActionListener(listen);
		
		connect.add(stopButton, BorderLayout.EAST);
		
		text.add(textBox, BorderLayout.CENTER);
		textBox.setEditable(false);
		
		toSend.setText("Write your message here!");
		
		talk.add(toSend);
		talk.add(sendButton, BorderLayout.EAST);
		
		frame.getContentPane().add(connect, BorderLayout.NORTH);
		frame.getContentPane().add(text, BorderLayout.CENTER);
		frame.getContentPane().add(talk, BorderLayout.SOUTH);

		frame.pack();

		frame.setVisible(true);		
		
		begin();
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
		new GUIServer().start();
	}
	
	private class GUIListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == stopButton) {
				endServer();
			} if (e.getSource() == sendButton) {
				String input = toSend.getText();
				toSend.setText("");
				if (input.indexOf("exit_server") >= 0) endServer();
				else {
					for (int i = 0; i < outs.size(); i++) {
						outs.get(i).println("Server:" + input);
					}
				
					write("[Server]: " + input);
				}
			}
		}
	}

	public void endServer() {
		write("Exiting Server");
		if (accept.isAlive())
			accept.interrupt();
		
		for (int i = 0; i < numClients - 1; i++) {
			if (read.get(i).isAlive())
				read.get(i).interrupt();	

		}
		
		System.exit(0);
	}

	public void loop() {
	}

	@Override
	public void process(String line) {
		for (int i = 0; i < outs.size(); i++) {
			outs.get(i).println(line);
		}
		String un = line.split(":")[0];
		String uptext = line.split(":")[1];
		
		write("[" + un + "]: " + uptext);
	}
}
