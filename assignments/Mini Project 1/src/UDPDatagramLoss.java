import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Anders on 14-09-2014.
 */
public class UDPDatagramLoss {

	private static int sender;
	private static int reciever;

	static int dgAmt;
	static int dgSize;
	static double interval;
	static int[] returnedPacketsArray;

	static int numberOfLostPackets;
	static int numberOfDuplicates;
	static int numberOfPacketsReceivedOnlyOnce;

	public static void main(String[] args)
	{
		sender = 2405;
		reciever = 2909;
		dgSize = Integer.parseInt("10000");
		dgAmt = Integer.parseInt("100000");
		interval = Double.parseDouble("0.005");

		returnedPacketsArray = new int[dgAmt];


		Thread senderThread = new Thread(){
			@Override
			public void run() {
				try {
					sender(dgSize,dgAmt,interval);
				} catch (IOException e) {
					System.out.println("tesat");
					e.printStackTrace();
				}
			}
		};
		Thread recieverThread = new Thread(){
			@Override
			public void run() {
				reciever();
			}
		};
		recieverThread.start();
		senderThread.start();

		try
		{
			Thread.sleep(8000);

		}
		catch(InterruptedException e)
		{
			System.out.println("Problem sleeping: " + e.getMessage());
		}

		evaluateArray();
		printLossToUser();

		System.exit(0);
	}

	static void evaluateArray()
	{
		for(int i = 0; i < returnedPacketsArray.length; i++)
		{
			if(returnedPacketsArray[i] == 0)
			{
				numberOfLostPackets++;
			}
			else if(returnedPacketsArray[i] == 1)
			{
				numberOfPacketsReceivedOnlyOnce++;
			}
			else if (numberOfPacketsReceivedOnlyOnce >= 2)
			{
				numberOfDuplicates++;
			}
		}
	}



	static double determinePercentage(int a)
	{
		return (double) a / (double) dgAmt*100.0;
	}


	static void printLossToUser()
	{
		System.out.println("Number of lost packets: " + numberOfLostPackets);
		System.out.println("Percentage lost packets: " + determinePercentage(numberOfLostPackets));

		System.out.println("Number of duplicates: " + numberOfDuplicates);
		System.out.println("Percentage duplicates: " + determinePercentage(numberOfDuplicates));

	}

	static void sender(int dgSize, int dgAmt, double interval) throws IOException
	{
		DatagramSocket aSocket = null;
		try
		{
			aSocket = new DatagramSocket(sender);
		}
		catch(IOException e) {
			return;
		}

		for(int i = 0; i < dgAmt; i++)
		{
			String dataInStringForm = "" + i;

			byte[] dataInByteArrayForm = dataInStringForm.getBytes();

			DatagramPacket request = new DatagramPacket(dataInByteArrayForm,dataInByteArrayForm.length,InetAddress.getLocalHost(),reciever);
			//DatagramPacket request = new DatagramPacket(dataInByteArrayForm,dataInByteArrayForm.length,InetAddress.getByName("106.185.40.123"),reciever);
			try
			{
				aSocket.send(request);
			}
			catch(IOException e)
			{
				System.out.println("Error sending packet: " + e.getMessage());
			}
		}
	}

	static void reciever()
	{
		DatagramSocket bSocket = null;
		try
		{
			bSocket = new DatagramSocket(reciever);
		}
		catch(IOException e) {
			return;
		}

		while(true)
		{
			byte[] receivingBuffer = new byte[dgSize];

			DatagramPacket receivingPacket = new DatagramPacket(receivingBuffer, receivingBuffer.length);
			try
			{
				bSocket.receive(receivingPacket);
				//System.out.println("recieve - got the package message " + new String(receivingPacket.getData()));
			}
			catch(IOException e)
			{
				System.out.println("Error receiving packet: " + e.getMessage());
			}


			// Extract contained information in receivingPacket
			byte[] extractedData = receivingPacket.getData();

			String extractedString = new String(extractedData,receivingPacket.getOffset(),receivingPacket.getLength());
			int packetNumber = Integer.parseInt(extractedString);


			returnedPacketsArray[packetNumber]++;
		}

	}
}
