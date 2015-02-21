import java.io.*;
import java.net.*;

public class MadLibsClient {

  public static void main (String[] args) {

    // Make sure args are passed
    if ( !(args.length >= 2) ) {
      System.out.println("Usage:");
      System.out.println("     java MadLibsClient");
      return;
    }

    // Various variables
    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);
    Socket MadSocket = null;
    DataInputStream input;
    DataOutputStream output;

    // Trying client type things, weep softly into my pillow
    try {
      MadSocket = new Socket (hostName, 19999);
      input = new DataInputStream(MadSocket.getInputStream());
      output = new DataOutputStream(MadSocket.getOutputStream());
    }
    catch (UnknownHostException e) {
      System.err.println("Don't know about that host: " + hostName);
    }
    catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to: " + hostName);
    }
  }
}
