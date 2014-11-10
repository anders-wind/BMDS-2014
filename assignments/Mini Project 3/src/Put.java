import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Anders on 03/11/14.
 */
public class Put {

	/**
	 * public static void put(int messageKey, String messageToPut, int portToSendTo) {
	 * //Send the message to a Node on the given port.
	 * //The message has a key, and message. This message is then stored at the Node.}
	 **/

	public static void put(int key, String message, int port) {

		String sendShit = "PUT: " + key + ":" + message;

		DataOutputStream dos = null;
		DataInputStream dis = null;
		Socket socket = null;

		try {

			socket = new Socket("localhost", port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());

			dos.writeBytes(sendShit + "\n");
			System.out.println("Sending");

			dis.readLine();
			
			socket.close();

		} catch (UnknownHostException e) {
			System.err.println("Unknown host: 'localhost'");
		} catch (IOException e) {
			System.err.println("Failed to retrieve I/O for the connection 'localhost'");
		}

	}

	public static void main(String[] args) {

		put(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));

	}
}
