import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class QuestionableDatagramSocket extends DatagramSocket {

	public QuestionableDatagramSocket(int port) throws SocketException {
		super(port);
	}
	
	@Override
	public void send(DatagramPacket packet) throws IOException {
		Random r = new Random();
		int randomnizz = r.nextInt(2) % 2;
		if(randomnizz == 0) {
			byte[] data = packet.getData();
			byte[] modifiedData = new byte[packet.getLength()/2];
			for (int i = 0; i < modifiedData.length; i++) {
				modifiedData[i] = data[i];
			}
			packet = new DatagramPacket(modifiedData, modifiedData.length, packet.getAddress(), packet.getPort());
			
		}		
		super.send(packet);
	}

	/* CODE BELOW IS JUST EXECUTION */
	
    public static void server() {
    	DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(8888);
            // create socket at agreed port
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                byte[] data = new byte[request.getLength()];
                System.arraycopy(request.getData(), request.getOffset(), data, 0, request.getLength());
                System.out.println("Recieved message " + new String(data));
            }
        } catch (Exception e){
        	System.out.println("Socket2: " + e.getMessage());
        } finally {
        	if(aSocket != null) aSocket.close();
        }
    }

    public static void funkyClient(String input) {
		DatagramSocket aSocket = null;
		try{
			aSocket = new QuestionableDatagramSocket(9999);

            // create socket at agreed port
			byte[] buffer = input.getBytes();
			DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 8888);

			aSocket.send(request);
		} catch (Exception e){
			System.out.println("SocketClient: " + e.getMessage());
		} finally {
			if(aSocket != null) aSocket.close();
		}
    }
    
    public static void main(String[] args) {
        new Thread(() -> QuestionableDatagramSocket.server()).start();
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur efficitur dolor imperdiet, condimentum ipsum nec.";
        for (int i = 0; i < 10; i++) {
        	QuestionableDatagramSocket.funkyClient(input);
		}
        	
        
		
	}
}