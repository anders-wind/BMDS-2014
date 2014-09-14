import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.concurrent.*;

public class LossEstimator
{
	int datagramSize;
	int numberOfDatagrams;
	double timeInterval;
	InetAddress hostIP;
	String echoIP = "106.185.40.123";
	int echoPort = 7;

	//int port = 7008;
	int port = echoPort;
	int localPort = 1200;


	int[] returnedPacketsArray;
	int numberOfLostPackets;
	int numberOfDuplicates;
	int numberOfPacketsReceivedOnlyOnce;

	DatagramSocket aSocket;


	public LossEstimator(int datagramSize, int numberOfDatagrams, double timeInterval)
	{
		this.datagramSize = datagramSize;
		this.numberOfDatagrams = numberOfDatagrams;
		this.timeInterval = timeInterval;

		returnedPacketsArray = new int[numberOfDatagrams];

		try
		{
			//hostIP = InetAddress.getLocalHost();	
			hostIP = InetAddress.getByName(echoIP);
			aSocket = new DatagramSocket(localPort);
		}
		catch(IOException e)
		{
			System.out.println("Error 1: " + e.getMessage());
		}

	}

	private void execute()
	{
		ReceiverThread receiverThread = new ReceiverThread();
		receiverThread.start();

		SenderThread senderThread = new SenderThread();
		senderThread.start();

		try
		{
			Thread.sleep(11000);

		}
		catch(InterruptedException e)
		{
			System.out.println("Problem sleeping: " + e.getMessage());
		}


		evaluateArray();
		printLossToUser();

		System.exit(0);
	}

	private void evaluateArray()
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



	private double determinePercentage(int a)
	{
		return (double) a / (double) numberOfDatagrams*100.0;
	}


	private void printLossToUser()
	{
		System.out.println("Number of lost packets: " + numberOfLostPackets);
		System.out.println("Percentage lost packets: " + determinePercentage(numberOfLostPackets));

		System.out.println("Number of duplicates: " + numberOfDuplicates);
		System.out.println("Percentage duplicates: " + determinePercentage(numberOfDuplicates));

	}








	private class ReceiverThread extends Thread
	{
		@Override
		public void run()
		{
			//DatagramSocket listeningSocket = null;

			// Opens socket to listen to
			//try
			//{
			/*	listeningSocket = new DatagramSocket(localPort);
			}
			catch(IOException e)
			{
				System.out.println("Error 3: " + e.getMessage());
			}
			*/

			while(true)
			{
				byte[] receivingBuffer = new byte[datagramSize];

				DatagramPacket receivingPacket = new DatagramPacket(receivingBuffer, receivingBuffer.length);
				try
				{
					aSocket.receive(receivingPacket);
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


	private class SenderThread extends Thread
	{
		@Override
		public void run()
		{
			/*DatagramSocket senderSocket = null;

			// Open a datagramSocket
			try
			{
				senderSocket = new DatagramSocket();	
			}
			catch(IOException e)
			{
				System.out.println("Error 1: " + e.getMessage());
			}
			*/

			// Send packets
			for(int i = 0; i < numberOfDatagrams; i++)
			{
				String dataInStringForm = "" + i;
				//byte[] buffer = new byte[datagramSize];

				byte[] dataInByteArrayForm = dataInStringForm.getBytes();

				DatagramPacket dataToBeSent = new DatagramPacket(dataInByteArrayForm,dataInByteArrayForm.length,hostIP,port);

				try
				{
					aSocket.send(dataToBeSent);
				}
				catch(IOException e)
				{
					System.out.println("Error sending packet: " + e.getMessage());
				}

			}
		}
	}


	public static void main(String[] args)
	{
		int datagramSize = 100;
		int numberOfDatagrams = 1000;
		double timeInterval = 0.5;

		LossEstimator lossEstimator = new LossEstimator(datagramSize,numberOfDatagrams,timeInterval);
		lossEstimator.execute();
	}
}