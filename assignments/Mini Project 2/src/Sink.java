import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Sink {

	private int localPort;
	private String h;

	public Sink() {
		h = "";
		test();
		// makeConnection();
		// getMessage();
	}

	private void test() {
		localPort = 2000;
		Thread testThread = new Thread() {
			public void run() {
				testInput();
			}
		};
		Thread testThread2 = new Thread() {
			public void run() {
				getMessage();
			}
		};
		testThread2.start();
		testThread.start();
	}

	private void makeConnection() {
		try {
			String message = "subscribe \n"; // port:" + localPort + "\n";

			Socket theServer = new Socket("localhost", 7777);
			DataOutputStream out = new DataOutputStream(
					theServer.getOutputStream());
			out.writeBytes(message);

			DataInputStream in = new DataInputStream(theServer.getInputStream());
			String okMessage = in.readLine();

			localPort = theServer.getLocalPort();
			theServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getMessage() {
		ServerSocket socket;
		Socket connection;
		try {
			socket = new ServerSocket(localPort);
			connection = socket.accept();
			while (true) {
				DataInputStream in = new DataInputStream(
						connection.getInputStream());
				String messageFromSource = in.readLine();
				System.out.println(messageFromSource);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testInput() {
		try {
			String message = "subscribe - port:" + localPort + "\n";

			Socket theServer = new Socket("localhost", localPort);
			DataOutputStream out = new DataOutputStream(
					theServer.getOutputStream());
			out.writeBytes(message);

			int i = 0;
			while (true) {
				out.writeBytes(message + " " + i++);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Sink();
	}
}
