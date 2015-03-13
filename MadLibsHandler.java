/*
 * Hey Dan, wassup. Your work for the first milestone is with the client.
 * Start by looking at line 66
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class MadLibsHandler implements Runnable {
	 DataOutputStream output;
	 DataInputStream input;
	 Socket remote_socket;
	 String connected_user_name;
	 HashMap<InetAddress, String> users;
	 long id;
	 
	/**
	 * Constructor
	 *
	 * @param remote_socket
	 * @param users 
	 */
	public MadLibsHandler(Socket remote_socket, HashMap<InetAddress, String> users) {
		this.remote_socket = remote_socket;
		this.users = users;
		try {
			// Get input/output streams
			this.input = new DataInputStream(remote_socket.getInputStream());
			this.output = new DataOutputStream(remote_socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		id = Thread.currentThread().getId();
		
		// Logging user connection
		connected_user_name = receiveString();
		if (users.get(remote_socket.getInetAddress()) == null)
			users.put(remote_socket.getInetAddress(), connected_user_name);
		System.out.printf("Thread[%d]: %s joined (ip%s)\n",
				id,
				connected_user_name,
				remote_socket.getInetAddress().toString() );

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
				e.printStackTrace();
			}
			mode = chooseMode();
		}

		disconnect();

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
				sendString("MadLibsServer: Starting \"Play\" mode.\n");
				return client_input;
			case (2):
				sendInt(0);
				sendString("MadLibsServer: Starting \"Create\" mode.\n");
				return client_input;
			case (3):
				sendInt(0);
				sendString("MadLibsServer: Starting \"Read\" mode.\n");
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
		String title;
		String filledBlank;
		MadLib choice = null;
		String blank = null;
		sendString(MadLibSet.getList());
		do {
			sendString("MadLibsServer: Pick a MadLib to play (or just return to exit):\n > (String) ");
			title = receiveString();
			if ( title.equals("") )
				break;
			choice = MadLibSet.getPlayable(title);
			if ( choice != null ) {
				sendInt(choice.getNumBlanks());
				sendString("MadLibsServer: Now playing \""+choice.getTitle()+"\"\n");
			} else {
				sendInt(0);
				sendString("MadLibsServer: MadLib \""+title+"\" does not exist.\n");
				continue;
			}
			sendString("MadLib: Fill in the blanks:\n");
			while ( (blank=choice.getNextBlank()) != null ) {
				sendString(" > ("+blank+") ");
				filledBlank = receiveString();
				choice.fillNextBlank(filledBlank);
			}
			sendString("MadLibsServer: Your finished MadLib reads...\n\""+choice.getFilledMadlib()+"\"\n");
			receiveInt();
			MadLibSet.addCompleted(choice, connected_user_name);
		} while ( !title.equals("") );
		
		sendString("MadLibsServer: Exiting mode...\n");
		return 0;
	}

	/**
	 * Begins running "create" mode
	 */
	private int beginCreateMode() {
		// sendString("Use %word% to show which words are the madlibs");
		String ent = "";
		String key = "";
		boolean key_in_use;
		do {
			//System.out.println("Server: Top of loop");
			sendString("MadLibsServer: Enter a new MadLib below (or just return to exit):\n > (String) ");
			ent = receiveString();
			if ( !(ent.equals("")) ) {
				try{
					MadLib new_m_l = new MadLib(ent);
					sendInt(0);
					sendString("MadLibsServer: MadLib title?\n > (String) ");
					key = receiveString();
					if ( !(key.equals("")) ) {
						new_m_l.setTitle(key);
						key_in_use = !MadLibSet.add(key, new_m_l);
					} else {
						key_in_use = !MadLibSet.add(null, new_m_l);
					}
					if (key_in_use) {
						sendString("MadLibsServer: Sorry, that name is already used! Upload cancelled\n");
					} else {
						MadLibSet.saveAll();
						sendString("MadLibsServer: Your new MadLib has been uploaded!\n");
					}
				} catch (BadMadLibDataException ex) {
					sendInt(1);
					sendString("MadLibsServer: MadLib formatting error!\n");
				}
			}
		} while ( !(ent.equals("")) );
		MadLibSet.saveAll();
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
	private void disconnect(Exception e) {
		// Get message (disconnect) from server and print to screen
		if (e == null) {
			System.out.print(receiveString());
			System.out.printf("Thread[%d]: %s disconnected gracefully\n", id, connected_user_name);
			System.exit(0);
		} else {
			System.out.printf("Thread[%d]: %s lost connection\n", id, connected_user_name);
			System.exit(1);
		}
	}
	private void disconnect() {
		disconnect(null);
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
			System.out.println("Connection lost");
			disconnect(e);
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
			System.out.println("Connection lost");
			disconnect(e);
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
			System.out.println("Connection lost");
			disconnect(e);
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
			System.out.println("Connection lost");
			disconnect(e);
			return null;
		}
	}

	public void main(String[] args) {
		// Usage
		System.out.println("This class is meant to be used by a MadLibsServer object");
		return;
	}
}
