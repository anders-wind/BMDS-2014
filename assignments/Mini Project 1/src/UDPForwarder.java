
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.lang.*;

public class UDPForwarder {
	
	private static int client;
	private static int port1;
	private static int port2; 
	
    public static void main(String args[]) throws Exception {
    	
    	client = 8888; //Integer.parseInt(args[0]);
    	port1 = 9999; //Integer.parseInt(args[1]);
    	port2 = 777; //Integer.parseInt(args[2]);
        new Thread(() -> p1()).start();
        new Thread(() -> p2()).start();
        
        while(true) {
        	System.in.read();
        	client();
        }
    }
    
    public static void p1() {
    	DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(port1);
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), InetAddress.getLocalHost(), port2);
                aSocket.send(reply);
                System.out.println("P1 forwarded message to p2");
            }
        } catch (Exception e){
        	System.out.println("Socket1: " + e.getMessage());
        	
        } finally{
        	if(aSocket != null) aSocket.close();
        }
    }
    
    public static void p2() {
    	DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(port2);
            // create socket at agreed port
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                byte[] data = new byte[request.getLength()];
                System.arraycopy(request.getData(), request.getOffset(), data, 0, request.getLength());
                System.out.println("P2 - Dropped the package message " + new String(data));
            }
        } catch (Exception e){
        	System.out.println("Socket2: " + e.getMessage());
        } finally {
        	if(aSocket != null) aSocket.close();
        }
    }
    public static void client() {
		DatagramSocket aSocket = null;
		try{
			aSocket = new DatagramSocket(client);

            // create socket at agreed port
			byte[] buffer = "lolo".getBytes();
			DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port1);

			aSocket.send(request);
		} catch (Exception e){
			System.out.println("SocketClient: " + e.getMessage());
		} finally {
			if(aSocket != null) aSocket.close();
		}
    }
}