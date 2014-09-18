import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class RFC862Udp {
	public static void Server() {
    	DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(7007);
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), InetAddress.getLocalHost(), request.getPort());
                aSocket.send(reply);
            }
        } catch (Exception e){
        	System.out.println("Socket1: " + e.getMessage());
        	
        } finally{
        	if(aSocket != null) aSocket.close();
        }
	}
	public static void Client(String data) {
    	DatagramSocket aSocket = null;
        try{
        	// data
        	byte[] myData = data.getBytes();
        	
            aSocket = new DatagramSocket(6532);
            byte[] buffer = new byte[myData.length];
        	//create socket to receive answer
            DatagramPacket answer = new DatagramPacket(buffer, buffer.length);
            
            // send
            DatagramPacket msg = new DatagramPacket(myData, myData.length, InetAddress.getLocalHost(), 7007);
            aSocket.send(msg);

            // receive
            aSocket.receive(answer);
            System.out.print("Echoed: " + new String(answer.getData()) + "\n");
            
        } catch (IOException e){
        	e.printStackTrace();
        	
        } finally{
        	if(aSocket != null) aSocket.close();
        }
	}
	
	public static void main(String[] args) throws IOException {
		new Thread(() -> Server()).start();
		System.out.println("> UDP echo server started!");
		//console
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
	        // retrieve data from console
			System.out.print("\n> Enter String\n");
		    String userinput = br.readLine();
		    Client(userinput);
		}
	}
}
