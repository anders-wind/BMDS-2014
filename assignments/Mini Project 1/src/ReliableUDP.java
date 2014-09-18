import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ReliableUDP {

	private static HashMap<String, String> map = new HashMap<String, String>();

	public static void Server() {
        try{
            DatagramSocket aSocket = new DatagramSocket(7007);
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                // give it to thread and continue receiving.
                new Thread(() -> retriever(aSocket, request)).start();
            }
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	private static void retriever(DatagramSocket aSocket, DatagramPacket packet) {
		String address = packet.getAddress().getHostAddress();
    	if(map.containsKey(address) && map.get(address) != null) {
			// drop it as we've already recieved.
    		System.out.println("dropped the package");
			return; 
    	} else {
			map.put(address, new String(packet.getData()));
			System.out.println(new String(packet.getData()));
			// >>>>>>>>>> OBS <<<<<<<<<<<
			// This implementation WILL if the same message is sent from the same origin drop it. Even if its another message later. (bad)
			// This could be fixed my adding more information to the package, date fx.
    	}       	
	}
	
		public static void Client(String data) {
    	DatagramSocket aSocket = null;
        try{
        	// data
        	byte[] myData = data.getBytes();
        	
            aSocket = new DatagramSocket(6532);
            byte[] buffer = new byte[myData.length];
            
            // send
            DatagramPacket msg = new DatagramPacket(myData, myData.length, InetAddress.getLocalHost(), 7007);
            aSocket.send(msg);            
        } catch (IOException e){
        	e.printStackTrace();
        	
        } finally{
        	if(aSocket != null) aSocket.close();
        }
	}
	
	public static void main(String[] args) {
		new Thread(() -> Server()).start();
		Client("Hey yo, i'm a cool dude man");
	}
}
