import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A source for sending out messages to a server for forwarding the messages to
 * subscribed receiving sinks.
 */
public class Source {

	public Source(String address) {
		connectoToServer(address);
	}

	private void connectoToServer(String address) {
		try {
            //Set up receiving of user input.
            BufferedReader userIn = new BufferedReader((new InputStreamReader(
                    System.in)));

            // Send user input to the server.
            String userInput = "";
            while (!userInput.equals("exit")) {
                userInput = userIn.readLine();

                // Establish connection.
                Socket serverConnection = new Socket(address, 7777);

                DataOutputStream outToServer = new DataOutputStream(
                        serverConnection.getOutputStream());

                //Send the message and close the connection if delivered.
                //Otherwise retry.
                int delivered = -1;
                while (delivered == -1) {
                    outToServer.writeBytes(userInput + "\n");
                    delivered = serverConnection.getInputStream().read();
                }

                serverConnection.close();
            }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Create a Source and connect it to the server.
		Runnable r = () -> {
			try {
				new Source("localhost");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		new Thread(r).start();
	}
}
