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
		System.out.printf("Thread[%d]: client joined (ip%s)\n", id, remote_socket.getRemoteSocketAddress().toString());

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
			System.out.printf("Thread[%d]: Disconnected gracefully\n", id);
		} else {
			System.out.printf("Thread[%d]: Disconnected due to IOException\n",id);
		}

	}

	private int chooseMode () {
		// Declare variables used to store client communications
		int client_input = 1;

		while ( true /*(client_input >= 0) &&  (client_input < 4)*/ ) {
			// Write the game mode options to the remote socket
			sendString("Choose mode:\n\t1 - Play\n\t2 - Create\n\t3 - Read\n\t0 - Disconnect\n > (int) ");

			// Read int from client
			client_input = receiveInt();

			// Depending on the int read, enter app mode accordingly
			switch (client_input) {
			case (1):
				sendInt(0);
				sendString("MadLibsServer: You are in \"Play\" mode.\n");
				return client_input;
			case (2):
				sendInt(0);
				sendString("MadLibsServer: You are in \"Create\" mode.\n");
				return client_input;
			case (3):
				sendInt(0);
				sendString("MadLibsServer: You are in \"Read\" mode.\n");
				return client_input;
			case (0):
				sendInt(0);
				return client_input;
			default:
				sendInt(1);
				sendString("MadLibsServer: There is no option available for that input\n");
			}
		}
	}

	/**
	 * Begins running "play" mode
	 */
	private int beginPlayMode() {
		//sendString(MadLibSet.giveRandom().play());
		sendString("MadLibsServer: Exiting mode...\n");
		return 0;
	}

	/**
	 * Begins running "create" mode
	 */
	private int beginCreateMode() {
		// sendString("Use %word% to show which words are the madlibs");
		String message = "";
		do {
			//System.out.println("Server: Top of loop");
			sendString("MadLibsServer: Enter a new MadLib below (or just return to exit):\n > (String) ");
			message = receiveString();
			if ( !message.equals("") ) {
				try{
					MadLibSet.add(new MadLib(message));
					MadLibSet.saveAll();
					sendString("MadLibsServer: Thanks! Your MadLib has been saved.\n");
				} catch (BadMadLibDataException ex) {
					sendString("MadLibsServer: "+ex.getMessage()+"\n");
				}
			}
		} while ( !(message.equals("")) );
		sendString("MadLibsServer: Exiting mode...\n");
		return 0;
	}

	/**
	 * Begins running "read" mode
	 */
	private int beginReadMode() {
		System.out.println("MODE NOT YET IMPLEMENTED");
		System.out.println("Exiting mode...");
		sendString("MadLibsServer: Exiting mode...\n");
		return 0;
	}

	/**
	 * Disconnects the client from the server, and cleans up
	 */
	private void disconnect() {
		sendString("MadLibsServer: Disconnecting...\n");
	}

	/**
	 * Writes a string to the connected client socket
	 * @param s:String - string to be sent
	 * @return 	0 if successful
	 * 			1 if unsuccessful
	 */
	public int sendString(String s) {
		try {
			output.writeUTF(s);
			return 0;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return 1;
		}
	}

	/**
	 * Writes an int to the connected client socket
	 * @param i:int - int to be sent
	 * @return 	0 if successful
	 * 			1 if unsuccessful
	 */
	public int sendInt(int i) {
		try {
			output.writeInt(i);
			return 0;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return 1;
		}
	}

	/**
	 * Reads an int from the connected client socket
	 * @return 	received int if successful
	 * 			null if unsuccessful
	 */
	public Integer receiveInt() {
		try {
			int i = input.readInt();
			return i;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return null;
		}
	}

	/**
	 * Reads a String from the connected client socket
	 * @return 	received String if successful
	 * 			null if unsuccessful
	 */
	public String receiveString() {
		try {
			String s = input.readUTF();
			return s;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			return null;
		}
	}

	public void main(String[] args) {
		// Usage
		System.out
				.println("This class is meant to be used by a MadLibsServer object");
		return;
	}
}
