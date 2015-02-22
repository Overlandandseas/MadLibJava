import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MadLibsClient {
	DataOutputStream output;
	DataInputStream input;
	Socket server_socket;
	
	/**
	 * Constructor
	 */
	public MadLibsClient(String hostname, int port) {
		try {
			this.server_socket = new Socket(hostname, port);
			// Get input/output streams
			this.input = new DataInputStream(server_socket.getInputStream());
			this.output = new DataOutputStream(server_socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
		}
	}

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
		
		int mode;
		Scanner sc = new Scanner(System.in);
		
		mode = client.chooseMode(sc);
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
			mode = client.chooseMode(sc);
		}
		
		client.disconnect();
		
	}
	
	private int chooseMode(Scanner sc) {
		int mode_int = 1;
		int check = 0;
		String message;
		
		while ( check == 0 ) { // While the client hasn't chosen the disconnect option
			// Get message (game mode options) from server and print to screen
			message = receiveString();
			System.out.println(message);
			
			// Get int from the user (mode choice)
			mode_int = sc.nextInt();
			sendInt(mode_int);
			
			if ( (check=receiveInt()) == 0 ) { // Check that the mode option is a valid choice
				// NOTE: All checks are done on the server.
				//	     If a bad argument is sent, then "check" (an int read from the server) will not be 0
				
				// Get message (game mode confirmation) from server and print to screen
				message = receiveString();
				System.out.println(message);
				
				return mode_int;
			} else {
				message = receiveString();
				System.out.println(message);
			}
		}
		return -1;
	}
	
	private void beginPlayMode () {
		String message;
		
		// Get message (exiting mode confirmation) from server and print to screen
		message = receiveString();
		System.out.println(message);
	}
	
	private void beginCreateMode () {
	
	}
	
	private void beginReadMode () {
		
	}
	
	private void disconnect() {
		
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
}
