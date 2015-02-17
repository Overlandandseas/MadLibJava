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
	 * @param remote_socket
	 */
	public MadLibsHandler (Socket remote_socket) {
		this.remote_socket = remote_socket;
		try {
			//Get input/output streams
			this.input = new DataInputStream(remote_socket.getInputStream());
			this.output = new DataOutputStream(remote_socket.getOutputStream());
			
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
		}
	}


	@Override
	public void run() {
		//Declare variables used to store client communications
		String message;
		int client_input = -1;
		
		while (client_input != 0) {
			try {
				output.writeUTF("Mode:\n\t1 - Play\n\t2 - Create\n\t3 - Read\n\t0 - Disconnect");
				client_input= input.readInt();
				
				switch (client_input) {
				case (1):
					output.writeUTF("You are in \"Play\" mode.");
					play();
					break;
				case (2):
					output.writeUTF("You are in \"Create\" mode.");
					create();
					break;
				case (3):
					output.writeUTF("You are in \"Read\" mode.");
					read();
					break;
				case (0):
					output.writeUTF("Disconnecting...");
					break;
				default:
					output.writeUTF("There is no option available for that input");
				}
		
			} catch (IOException e) {
				System.out.println("Exception: " + e.getClass().toString());
				System.out.println("\t" + e.getMessage());
			}
		}
		
	}
	
	/**
	 * Begins running "play" mode
	 */
	private void play() {
		
	}
	
	/**
	 * Begins running "create" mode
	 */
	private void create() {
		
	}
	
	/**
	 * Begins running "read" mode
	 */
	private void read() {
		
	}
	
	
	public static void main (String[] args) {
		//Usage
		System.out.println("This class is meant to be used by a MadLibsServer object");
		return;
	}
}
