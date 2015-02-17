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
		
	}
	
	public static void main (String[] args) {
		//Usage
		System.out.println("This class is meant to be used by a MadLibsServer object");
		return;
	}
}
