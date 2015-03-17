import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class MadLibsServer {
	static HashMap<String,String> users = new HashMap<String,String>();
	static Thread[] handlers;

	public static void main(String[] args) {

		// Usage
		if (!(args.length >= 1)) {
			System.out.println("Usage:");
			System.out.println("     java MadLibsServer port [-max_clients n]");
			System.out.println("          port:int");
			System.out.println("          n:int");

			return;
		}

		// Get port number from args
		int port = Integer.valueOf(args[0]);

		// Get optional max_clients from args
		int max_clients = 100;
		if (args.length == 3) {
			String flag = String.valueOf(args[1]);
			if (flag.equals("-max_clients"))
				max_clients = Integer.valueOf(args[2]);
		}

		try {
			// Get socket and start the server
			ServerSocket server_socket = new ServerSocket(port);
			System.out.println("MadLibsServer: running...");

			// Declare variables to be used in server loop
			Socket remote_socket;
			handlers = new Thread[max_clients]; // Array of MadLibsHandler threads
			
			if (MadLibsServer.loadUsers()) {
				System.out.println("MadLibsServer: Loaded from \"Users.txt\"...");
			} else {
				System.out.println("MadLibsServer: Starting server with blank user log");
			}
			
			if (MadLibSet.loadMadLibs()) {
				System.out.println("MadLibsServer: MadLibSet loaded from \"MadLibs.txt\"...");
			} else {
				System.out.println("MadLibsServer: MadLibSet found no MadLibs");
			}
			
			if (MadLibSet.loadCompleted()) {
				System.out.println("MadLibsServer: MadLibSet loaded from \"CompletedMadLibs.txt\"...");
			} else {
				System.out.println("MadLibsServer: MadLibSet found no CompletedMadLibs");
			}

			while (true) { // WE NEVER STOP SERVING QUALITY MADLIBS
				remote_socket = server_socket.accept();
				MadLibsHandler mlh = new MadLibsHandler(remote_socket, users, Thread.currentThread());
				Thread handler_thread = new Thread(mlh);
				if (!insert_thread(handler_thread)) {
					System.out.println("Reached maximum number of threads");
					join_finished_threads();
				} else if (Thread.interrupted()) {
					MadLibsServer.saveUsers();
					join_finished_threads();
				}
				handler_thread.start();
			}

			//server_socket.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getClass().toString());
			System.out.println("\t" + e.getMessage());
		}
	}

	/**
	 * Finds the next index in a thread array that is not used up.
	 * 
	 * @param thread_array
	 * @return if not full, returns next empty index if full, returns -1
	 */
	private static int next_empty() {
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i] == null)
				return i;
		}
		return -1;
	}

	/**
	 * Inserts a thread into thread_array
	 * 
	 * @param thread
	 * @param thread_array
	 * @return if successful, returns true else, returns false
	 */
	private static boolean insert_thread(Thread thread) {
		int next_empty_index = next_empty();
		if (next_empty_index == -1)
			return false;

		// Insert thread into array
		handlers[next_empty_index] = thread;
		return true;
	}
	
	private static int join_finished_threads() {
		int num_threads_joined = 0;
		for (Thread handler : handlers) {
			if (handler != null) {
				if (handler.isInterrupted()) {
					try {
						handler.join();
						num_threads_joined++;
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.out.println("MadLibsServer: Server thread interrupted during join");
						e.printStackTrace();
					}
				}
			}
		}
		if (num_threads_joined > 0)
			System.out.println("MadLibsServer: Joined "+num_threads_joined+" finished threads");
		return num_threads_joined;
	}
	
	public static boolean saveUsers() {
		try {
			PrintWriter printWriter = new PrintWriter("Users.txt", "UTF-8");
			Set<String> keys = users.keySet();
			for (String i: keys) {
				printWriter.println( i+" : "+users.get(i) );
			}
			printWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadUsers() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Users.txt"));
			Scanner parser;
			String line;
			String addr;
			String username;
			while ( (line = reader.readLine()) != null) {
				parser = new Scanner(line);
				parser.useDelimiter(" : ");
				addr = parser.next();
				username = parser.next();
				users.put(addr, username);
				parser.close();
			}
			reader.close();
			return true;
		} catch (FileNotFoundException e) {
			MadLibsServer.saveUsers();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
