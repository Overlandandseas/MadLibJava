/*
 * Hey Dan, wassup. Your work for the first milestone is with the client.
 * Start by looking at line 66
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MadLibsHandler implements Runnable {
	DataOutputStream output;
	DataInputStream input;
	Socket remote_socket;

	/**
	 * Constructor
	 * 
	 * @param remote_socket
	 */
	public MadLibsHandler(Socket remote_socket) {
		this.remote_socket = remote_socket;
		try {
			// Get input/output streams
			this.input = new DataInputStream(remote_socket.getInputStream());
			this.output = new DataOutputStream(remote_socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
		}
	}

	@Override
	public void run() {
		long id = Thread.currentThread().getId();
		
		int disconnect_status = -1;
		int mode;
		
		mode = chooseMode();
		while (mode > 0) {
			try {
				switch (mode) {
				case (1):
					beginPlayMode();
					break;
				case (2):
					beginCreateMode();
					break;
				case (3):
					beginReadMode();
					break;
				default:
					String error = "Thread-["+id+"]: Error switching modes";
					throw new Exception(error);
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e.getClass().toString());
				System.out.println("\t" + e.getMessage());
			}
			mode = chooseMode();
		}
		
		disconnect();
		disconnect_status = 0;
		
		// Print disconnect status before closing the thread
		if (disconnect_status == 0) {
			System.out.printf("Thread[%d]: Disconnected gracefully", id);
		} else {
			System.out.printf("Thread[%d]: Disconnected due to IOException\n",id);
		}

	}
	
	private int chooseMode () {
		// Declare variables used to store client communications
		int client_input = 1;

		/*
		 * Dan, the connection with the client starts here! Pay special
		 * attention to the order in which the server writes/reads to the client
		 * socket
		 */

		while ( (client_input >= 0) &&  (client_input < 4)) {
			// Write the game mode options to the remote socket
			sendString("Choose mode:\n\t1 - Play\n\t2 - Create\n\t3 - Read\n\t0 - Disconnect");

			// Read int from client
			client_input = receiveInt();

			// Depending on the int read, enter app mode accordingly
			switch (client_input) {
			case (1):
				sendInt(0);
				sendString("You are in \"Play\" mode.");
				return client_input;
			case (2):
				sendInt(0);
				sendString("You are in \"Create\" mode.");
				return client_input;
			case (3):
				sendInt(0);
				sendString("You are in \"Read\" mode.");
				return client_input;
			case (0):
				sendInt(0);
				sendString("Disconnecting...");
				return client_input;
			default:
				sendInt(1);
				sendString("There is no option available for that input");
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Begins running "play" mode
	 */
	private int beginPlayMode() {
		System.out.println("MODE NOT YET IMPLEMENTED");
		System.out.println("Exiting mode...");
		sendString("Exiting mode...");
		return 0;
	}

	/**
	 * Begins running "create" mode
	 */
	private int beginCreateMode() {
		System.out.println("MODE NOT YET IMPLEMENTED");
		System.out.println("Exiting mode...");
		sendString("Exiting mode...");
		return 0;
	}

	/**
	 * Begins running "read" mode
	 */
	private int beginReadMode() {
		System.out.println("MODE NOT YET IMPLEMENTED");
		System.out.println("Exiting mode...");
		sendString("Exiting mode...");
		return 0;
	}
	
	/**
	 * Disconnects the client from the server, and cleans up
	 */
	private void disconnect() {
		sendString("Disconnecting...");
	}
	
	private int sendString(String s) {
		try {
			output.writeUTF(s);
			return 0;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return 1;
		}
	}
	
	private int sendInt(int i) {
		try {
			output.writeInt(i);
			return 0;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return 1;
		}
	}
	
	private Integer receiveInt() {
		try {
			int i = input.readInt();
			return i;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return null;
		}
	}
	
	private String receiveString() {
		try {
			String s = input.readUTF();
			return s;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {
		// Usage
		System.out
				.println("This class is meant to be used by a MadLibsServer object");
		return;
	}
}
