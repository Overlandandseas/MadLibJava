import java.net.ServerSocket;
import java.net.Socket;

public class MadLibsServer {

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
			Thread[] handlers = new Thread[max_clients]; // Array of MadLibsHandler threads
			
			if (MadLibSet.loadAll()) {
				System.out.println("MadLibsServer: MadLibSet loaded from \"MadLibs.txt\"...");
				MadLibSet.printList();
			} else {
				System.out.println("MadLibsServer: MadLibSet is empty");
			}

			while (true) { // WE NEVER STOP SERVING QUALITY MADLIBS
				remote_socket = server_socket.accept();
				MadLibsHandler mlh = new MadLibsHandler(remote_socket);
				Thread handler_thread = new Thread(mlh);
				if (!insert_thread(handler_thread, handlers)) {
					System.out.println("Reached maximum number of threads");
					break;
				}
				handler_thread.start();
			}

			server_socket.close();
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
	private static int next_empty(Thread[] thread_array) {
		for (int i = 0; i < thread_array.length; i++) {
			if (thread_array[i] == null)
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
	private static boolean insert_thread(Thread thread, Thread[] thread_array) {
		int next_empty_index = next_empty(thread_array);
		if (next_empty_index == -1)
			return false;

		// Insert thread into array
		thread_array[next_empty_index] = thread;
		return true;
	}
}
