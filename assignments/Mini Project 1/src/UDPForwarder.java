import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 */
public class UDPForwarder implements Runnable
{
    private String h = "";
    private int p1, p2;

    public static void main(String[] args) {
        UDPForwarder first = new UDPForwarder("TESTING!", 1234, 4321);
        UDPForwarder second = new UDPForwarder("TESTING!", 4321, 8888);
        new Thread(first);
        new Thread(second);
    }

    public UDPForwarder(String h, int p1, int p2) {
        this.h = h;
        this.p1 = p1;
        this.p2 = p2;
        run();
    }

    @Override
    public void run() {
        try{
            DatagramSocket serverSocketP1 = new DatagramSocket(p1);
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                System.out.println("Listening . . .");
                serverSocketP1.receive(datagramPacket);
                System.out.println("Received " +
                        datagramPacket.getLength() + " bytes" +
                        " from " +
                        datagramPacket.getAddress().toString() +
                        ":" + datagramPacket.getPort());
                byte[] lol = datagramPacket.getData();
                DatagramPacket forward = new DatagramPacket(lol, buffer.length, datagramPacket.getAddress(), p2);
                serverSocketP1.send(forward);

            }
        } catch(Exception e){

        }
    }
}