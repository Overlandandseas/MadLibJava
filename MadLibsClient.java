import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MadLibsClient {

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

		// Declare variables to be used for connection
		Socket MadSocket = null;
		DataInputStream input;
		DataOutputStream output;

		try {
			MadSocket = new Socket(hostName, portNumber);
			input = new DataInputStream(MadSocket.getInputStream());
			output = new DataOutputStream(MadSocket.getOutputStream());
			//int client_input;
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about that host: " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ hostName);
		}
	}
	
	private static int chooseMode(DataInputStream input, DataOutputStream output) throws IOException{
		int mode_int = 1;
		int check = 0;
		String message;
		Scanner sc = new Scanner(System.in);
		
		while ( check == 0 ) { // While the client hasn't chosen the disconnect option
			// Get message (game mode options) from server and print to screen
			message = input.readUTF();
			System.out.println(message);
			
			// Get int from the user (mode choice)
			mode_int = sc.nextInt();
			output.writeInt(mode_int);
			
			if ( (check=input.readInt()) == 0 ) { // Check that the mode option is a valid choice
				// NOTE: All checks are done on the server.
				//	     If a bad argument is sent, then check (an int read from the server) will not be 0
				message = input.readUTF();
				System.out.println(message);
				
				sc.close();
				return mode_int;
			} else {
				message = input.readUTF();
				System.out.println(message);
			}
		}
		sc.close();
		return -1;
	}
	
	private static void beginPlayMode (DataInputStream input, DataOutputStream output) throws IOException {
		String message;

		// Get message (game mode confirmation) from server and print to screen
		message = input.readUTF();
		System.out.println(message);
		
		// Get message (exiting mode confirmation) from server and print to screen
		message = input.readUTF();
		System.out.println(message);
	}
	
	private void beginCreateMode (DataInputStream input, DataOutputStream output) throws IOException {
	
	}
	
	private void beginReadMode (DataInputStream input, DataOutputStream output) throws IOException {
		
	}
	
	private static void disconnect() {
		
	}
}
