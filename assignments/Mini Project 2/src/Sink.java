import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Sink {

	private int localPort;
	private String h;

	public Sink(String address) {
		h = address;
		//test();
		makeConnection();
		getMessage();
	}

	private void makeConnection() {
		try {
			String message = "subscribe \n"; // port:" + localPort + "\n";

			Socket theServer = new Socket(h, 7777);
			DataOutputStream out = new DataOutputStream(
					theServer.getOutputStream());
			out.writeBytes(message);

			DataInputStream in = new DataInputStream(theServer.getInputStream());
			in.readLine();
			
			System.out.println("---------\nConnection to AwesomeServer was successfull\n---------\n");
			
			localPort = theServer.getLocalPort();
			theServer.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Connection to AwesomeServer failed...\nTrying again in 1 second");
			waitForOneSec();
			makeConnection();
		}
	}
	
	

	private void getMessage() {
		ServerSocket socket;
		Socket connection;
		try {
			socket = new ServerSocket(localPort);
			while (true) {
				connection = socket.accept();
				DataInputStream in = new DataInputStream(
						connection.getInputStream());
				String messageFromSource = in.readLine();

                if (messageFromSource == null) {
                    messageFromSource = "";
                }

                System.out.println(messageFromSource);
				connection.close();
			}
		} catch (Exception e) {
			//tStackTrace();
			System.out.println("Connection to server failed.!\nTrying again...");
			makeConnection();
		}
	}

	
	private void waitForOneSec()
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			//e1.printStackTrace();
			System.out.println("Terminates");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		new Sink("localhost");
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
}
