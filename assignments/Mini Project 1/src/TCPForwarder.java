import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPForwarder implements Runnable {
    private String h = "";
    private int p1, p2;

    public static void main(String[] args) {
        UDPForwarder first = new UDPForwarder("TESTING!", 1234, 4321);
        UDPForwarder second = new UDPForwarder("TESTING!", 4321, 8888);
        new Thread(first);
        new Thread(second);
    }

    public TCPForwarder(String h, int p1, int p2) {
        this.h = h;
        this.p1 = p1;
        this.p2 = p2;
        run();
    }

    @Override
    public void run() {
        try{
            ServerSocket socket = new ServerSocket(p1);
            Socket forwarder = new Socket("localhost", p2);
            byte[] buffer = new byte[1000];

            while(true){
                Socket connection = socket.accept();
                BufferedReader received = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream output = new DataOutputStream(forwarder.getOutputStream());
                System.out.println("Listening . . .");
                output.writeBytes(received.readLine());
            }
        } catch(Exception e){
            System.out.println("You suck!");
        }
    }
}