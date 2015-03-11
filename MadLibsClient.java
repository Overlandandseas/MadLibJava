import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MadLibsClient {
	DataOutputStream output;
	DataInputStream input;
	Socket server_socket;
	Scanner sc;

	/**
	 * Constructor
	 */
	public MadLibsClient(String hostname, int port) {
		try {
			this.server_socket = new Socket(hostname, port);
			// Get input/output streams
			this.input = new DataInputStream(server_socket.getInputStream());
			this.output = new DataOutputStream(server_socket.getOutputStream());
			this.sc = new Scanner(System.in);
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
		}
	}


	/**
	 * Main method. Pass args according to usage to begin running a new MadLibsClient
	 * @param args
	 */
	public static void main(String[] args) {
		// Make sure args are passed
		if (!(args.length >= 2)) {
			System.out.println("Usage:");
			System.out.println("     java MadLibsClient hostname port");
			System.out.println("          hostname:String");
			System.out.println("               Use \"localhost\" to connect on loopback address");
			System.out.println("          port:int");

			return;
		}

		// Get variables from args
		String hostName = args[0]; // Use "localhost" when testing with the server running locally
		int portNumber = Integer.parseInt(args[1]);

		// Create new MadLibClient
		MadLibsClient client = new MadLibsClient(hostName, portNumber);

		// Declare variables used for choosing mode
		int mode;

		// Get int "mode" from the user
		mode = client.chooseMode();

		// Enter main loop
		//	1) Look at int "mode"
		//	2) If not 0, enter according mode using method call
		//	3) If 0, exit loop
		while (mode > 0) {
			try {
				switch (mode) {
				case (1):
					client.beginPlayMode();
					break;
				case (2):
					client.beginCreateMode();
					break;
				case (3):
					client.beginReadMode();
					break;
				default:
					throw new Exception("Error switching modes");
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e.getClass().toString());
				System.out.println("\t" + e.getMessage());
			}
			mode = client.chooseMode();
		}

		// Disconnect from the server
		client.disconnect();

	}

	/**
	 * Gets an integer from the client. This integer is sent to the server to choose a game mode.
	 * If the mode does not exist on the server (wrong int), then the client will ask the user to
	 * enter a different int.
	 * @return int
	 */
	private int chooseMode() {
		int mode_int = 1;
		int check = 1;
		String message;

		while ( check == 1 ) { // While the client hasn't chosen the disconnect option
			// Get message (game mode options) from server and print to screen
			message = receiveString();
			System.out.print(message);

			// Get int from the user (mode choice)
			mode_int = getInt();
			sendInt(mode_int);

			// Check that the mode option is a valid choice
			if ( (check=receiveInt()) == 0 ) {
				// NOTE: All checks are done on the server.
				//	     If a bad argument is sent, then "check" (an int read from the server) will not be 0

				// Get message (game mode confirmation) from server and print to screen
				if (mode_int != 0)
					System.out.print(receiveString());

				// Return chosen mode to the main loop
				return mode_int;
				// Exit this loop
			} else {
				// Get message ("that mode doesn't exist") from the server and print to screen
				message = receiveString();
				System.out.print(message);
			}
		}
		// Not supposed to get here
		return -1;
	}

	/**
	 * Begins running "play" mode
	 */
	private void beginPlayMode () {

		//String message;
		//int i = receiveInt();
		//for(int c = 0; c < i; c++){
		//	System.out.print(receiveString());
		//	sendString(getLine());
		//}
		// Get message (exiting mode confirmation) from server and print to screen
		System.out.print(receiveString());
	}

	/**
	 * Begins running "create" mode
	 */
	private void beginCreateMode () {
		String madLib;
		int check = 1;
		do {
			//System.out.println("Client: Top of loop");
			System.out.print(receiveString());
			madLib = getLine();
			sendString(madLib);
			if ( !madLib.equals("") ) {
				check = receiveInt();
				System.out.print(receiveString());
				if (check == 1)
					continue;
			} else {
				break;
			}
			if (check == 0) {
				String name = getLine();
				sendString(name);
			}
			System.out.print(receiveString());
		} while (!madLib.equals("") || check == 1);
		
		/*String name;
		do {
			//System.out.println("Client: Top of loop");
			System.out.print(receiveString());
			name = getLine();
			sendString(name);
			if ( !name.equals("") )
				System.out.print(receiveString());
		} while (!name.equals(""));
		*/

		// Get message (exiting mode confirmation) from server and print to screen
		System.out.print(receiveString());
	}

	/**
	 * Begins running "read" mode
	 */
	private void beginReadMode () {
		String message;

		// Get message (exiting mode confirmation) from server and print to screen
		message = receiveString();
		System.out.print(message);
	}

	/**
	 * Disconnects the server from the client, and cleans up
	 */
	private void disconnect() {
		// Get message (disconnect) from server and print to screen
		System.out.print(receiveString());
	}

	/**
	 * Writes a string to the connected server socket
	 * @param s:String - string to be sent
	 * @return 	0 if successful
	 * 			1 if unsuccessful
	 */
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

	/**
	 * Writes an int to the connected server socket
	 * @param i:int - int to be sent
	 * @return 	0 if successful
	 * 			1 if unsuccessful
	 */
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

	/**
	 * Reads an int from the connected server socket
	 * @return 	received int if successful
	 * 			null if unsuccessful
	 */
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

	/**
	 * Reads a String from the connected server socket
	 * @return 	received String if successful
	 * 			null if unsuccessful
	 */
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

	private String getLine() {
		String inp = sc.nextLine();
		return inp;
	}

	private int getInt() {
		Integer i = null;
		String line;
		while (i == null) {
			line = getLine();
			try {
				i = Integer.valueOf(line);
			} catch (NumberFormatException e) {
				System.out.println("MadLibsClient: Can't parse\""+line+"\" to integer");
				System.out.print(" > (int) ");
			}
		}
		return i.intValue();
	}
}
