import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Sink {

	private int localPort;
	private String h;

	public Sink() {
		localPort = 2000;
		h = "";

		test();
		//makeConnection();
		//getMessage();
	}
	
	private void test()
	{
		Thread testThread = new Thread()
		{
			public void run() 
			{
				testInput();
			}
		};
		Thread testThread2 = new Thread()
		{
			public void run() 
			{
				getMessage();
			}
		};
		testThread2.start();
		testThread.start();
	}

	private void makeConnection() {
		try {
			String message = "subscribe - port:" + localPort;

			Socket theServer = new Socket("localhost", 7777);
			DataOutputStream out = new DataOutputStream(
					theServer.getOutputStream());
			out.writeBytes(message);

			DataInputStream in = new DataInputStream(theServer.getInputStream());
			String okMessage = in.readLine();
			theServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getMessage() {
		ServerSocket socket = null;
		Socket connection = null;
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
			while(true)
			{
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
