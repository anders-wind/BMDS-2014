import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.lang.*;

public class UDPForwarder {
	
	private static final int port1 = 9999;
	private static final int port2 = 6666; 
	
    public static void main(String args[]) {
        new Thread(() -> p1()).start();
        new Thread(() -> p2()).start();
        
        //System.in.read();
        //client();
    }
    
    public static void p1() {
    	DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(port1);
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), port2);
                aSocket.send(reply);
                System.out.println("Forwarded message to p2");
            }
        } catch (Exception e){
        	System.out.println("Socket: " + e.getMessage());
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
                System.out.println("P2 - Dropped the package.. faggot");
            }
        } catch (Exception e){
        	System.out.println("Socket: " + e.getMessage());
        }
    }
    public static void client() {
		DatagramSocket aSocket = null;
		try{
			aSocket = new DatagramSocket(1241);

            // create socket at agreed port
			byte[] buffer = "lol".getBytes();
			DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 9999);

			aSocket.send(request);
		} catch (Exception e){
			System.out.println("Socket: " + e.getMessage());
		} 
    }
}