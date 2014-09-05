import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 */
public class TestingUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket testSocketOne = new DatagramSocket();

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
