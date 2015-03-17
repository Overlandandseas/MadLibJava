import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MadLibsClient {
	private DataOutputStream output;
	private DataInputStream input;
	private Socket server_socket;
	private Scanner sc;
	private String user_name;
	private IOException disconnectException;

	/**
	 * Constructor
	 */
	public MadLibsClient(String hostname, int port) {
		System.out.printf("MadLibsClient: Attempting to connect to %s:%d\n", hostname, port);
		try {
			this.server_socket = new Socket(hostname, port);
			// Get input/output streams
			this.input = new DataInputStream(server_socket.getInputStream());
			this.output = new DataOutputStream(server_socket.getOutputStream());
			this.sc = new Scanner(System.in);
			this.disconnectException = null;
			
		} catch (IOException e) {
			System.out.println("MadLibsClient: Connection failed");
			this.disconnectException = e;
		}
	}


	/**
	 * Main method. Pass args according to usage to begin running a new MadLibsClient
	 * @param args
	 */
	public static void main(String[] args) {
		// Make sure args are passed
		String host;
		int portNumber;
		String[] hostList = {"192.168.2.5", "192.168.2.6", "192.168.2.4", "192.168.2.3", "192.168.2.2", "192.168.2.1", "192.168.2.7"};
		if ( (args.length == 1) && ((args[0].equals("help")) || (args[0].equals("usage"))) ) {
			System.out.println("Usage:");
			System.out.println("     java MadLibsClient hostname port");
			System.out.println("          hostname : String");
			System.out.println("          port : int");

			return;
		} else if (args.length == 2) {
			// Get variables from args
			host = args[0]; // Use "localhost" when testing with the server running locally
			portNumber = Integer.parseInt(args[1]);
		}

		// Create new MadLibClient
		MadLibsClient client;
		int i=0;
		portNumber = 3300;
		do {
			host = hostList[i];
			client = new MadLibsClient(host, portNumber);
			i++;
		} while ( (i < hostList.length) && (client.disconnectException != null) );
		if (client.disconnectException != null) return;
		
		String message;
		message = client.receiveString();
		if (Thread.interrupted()) {
			client.disconnect(client.disconnectException);
			System.out.println("MadLibsClient: Goodbye!");
			return;
		}
		if (message.equals("")) {
			System.out.print("MadLibsClient: What is your name?\n > (String) ");
			client.user_name = client.getLine();
			client.sendString(client.user_name);
		} else {
			System.out.print(message);
		}
		
		

		// Declare variables used for choosing mode
		int mode = 0;
		int interrupt = 0;

		// Enter main loop
		//	1) Look at int "mode"
		//	2) If not 0, enter according mode using method call
		//	3) If 0, exit loop
		
		do {
			// Get int "mode" from the user
			mode = client.chooseMode();
			switch (mode) {
			case (1):
				interrupt = client.beginPlayMode();
				break;
			case (2):
				interrupt = client.beginCreateMode();
				break;
			case (3):
				interrupt = client.beginReadMode();
				break;
			default:
				break;
			}
		} while (mode > 0 && interrupt != -1);

		// Disconnect from the server
		client.disconnect(client.disconnectException);
		System.out.println("MadLibsClient: Goodbye!");
	}

	/**
	 * Gets an integer from the client. This integer is sent to the server to choose a game mode.
	 * If the mode does not exist on the server (wrong int), then the client will ask the user to
	 * enter a different int.
	 * @return int
	 */
	private int chooseMode() {
		Integer mode_int = 1;
		Integer check = 1;
		String message;

		while ( check == 1 ) { // While the client hasn't chosen the disconnect option
			// Get message (game mode options) from server and print to screen
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);

			// Get int from the user (mode choice)
			mode_int = getInt();
			sendInt(mode_int);

			// Check that the mode option is a valid choice
			if ( (check=receiveInt()) == 0 ) {
				if (Thread.interrupted()) return -1;
				// NOTE: All checks are done on the server.
				//	     If a bad argument is sent, then "check" (an int read from the server) will not be 0

				// Get message (game mode confirmation) from server and print to screen
				if (mode_int != 0) {
					message = receiveString();
					if (Thread.interrupted()) return -1;
					System.out.print(message);
				}
				// Return chosen mode to the main loop
				return mode_int;
				// Exit this loop
			} else {
				// Get message ("that mode doesn't exist") from the server and print to screen
				message = receiveString();
				if (Thread.interrupted()) return -1;
				System.out.print(message);
			}
		}
		return -1;
	}

	/**
	 * Begins running "play" mode
	 */
	private int beginPlayMode () {
		Integer number;
		String message, line;
		Integer check = 0;
		message = receiveString();
		if (Thread.interrupted()) return -1;
		System.out.print(message);
		do {
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			number = getInt();
			sendInt(number);
			if (number == Integer.MIN_VALUE)
				break;
			check = receiveInt();
			if (Thread.interrupted()) return -1;
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			
			if (check == 0)
				continue;
			
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			for (int i=0; i<check; i++) {
				message = receiveString();
				if (Thread.interrupted()) return -1;
				System.out.print(message);
				line = getLine();
				sendString(line);
			}
			// Read the fininished MadLib
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			getLine();
			sendInt(0);
			
		} while ( !(number == Integer.MIN_VALUE) || (check == 0) );
		
		// Get message (exiting mode confirmation) from server and print to screen
		message = receiveString();
		if (Thread.interrupted()) return -1;
		System.out.print(message);
		return 0;
	}

	/**
	 * Begins running "create" mode
	 */
	private int beginCreateMode () {
		String madLib, message;
		Integer check = 1;
		do {
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			madLib = getLine();
			sendString(madLib);
			if ( !madLib.equals("") ) {
				check = receiveInt();
				if (Thread.interrupted()) return -1;
				message = receiveString();
				if (Thread.interrupted()) return -1;
				System.out.print(message);
				if (check == 1)
					continue;
			} else {
				break;
			}
			if (check == 0) {
				String name = getLine();
				sendString(name);
			}
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
		} while (!madLib.equals("") || check == 1);
		
		message = receiveString();
		if (Thread.interrupted()) return -1;
		System.out.print(message);
		return 0;
	}

	/**
	 * Begins running "read" mode
	 */
	private int beginReadMode () {
		Integer line;
		Integer check = 0;
		String message;
		message = receiveString();
		if (Thread.interrupted()) return -1;
		System.out.print(message);
		do {
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			line = getInt();
			sendInt(line);
			if (line == Integer.MIN_VALUE)
				break;
			check = receiveInt();
			if (Thread.interrupted()) return -1;
			message = receiveString();
			if (Thread.interrupted()) return -1;
			System.out.print(message);
			if (check == 1)
				continue;
		} while ( !(line == Integer.MIN_VALUE) );
		
		// Get message (exiting mode confirmation) from server and print to screen
		message = receiveString();
		if (Thread.interrupted()) return -1;
		System.out.print(message);
		return 0;
	}

	/**
	 * Disconnects the server from the client, and cleans up
	 */
	private void disconnect(IOException disconnectException) {
		// Get message (disconnect) from server and print to screen
		String message;
		if (disconnectException == null) {
			message = receiveString();
			if (Thread.interrupted()) return;
			System.out.print(message);
		} else {
			System.out.println("MadLibsClient: Connection lost");
		}
		try {
			input.close();
			output.close();
			server_socket.close();
		} catch (IOException e) {
			System.out.println("MadLibsClient: Client did not disconnect gracefully");
		}
		
		return;
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
			disconnectException = e;
			Thread.currentThread().interrupt();
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
			disconnectException = e;
			Thread.currentThread().interrupt();
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
			disconnectException = e;
			Thread.currentThread().interrupt();
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
			disconnectException = e;
			Thread.currentThread().interrupt();
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
				if (line.equals("")) {
					return Integer.MIN_VALUE;
				} else {
					System.out.println("MadLibsClient: Can't parse \""+line+"\" to integer");
					System.out.print(" > (int) ");
				}
			}
		}
		return i.intValue();
	}
}
