import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

public class TCPForwarder implements Runnable {
    private String h = "";
    private int p1, p2;

    public static void main(String[] args) {
        new Thread(new TCPForwarder(4820));
    }

    public TCPForwarder(int p1) {
        this.p1 = p1;
        run();
    }

    @Override
    public void run() {
    	ServerSocket socket = null;
    	Socket connection = null;
    	
        try{
            socket = new ServerSocket(p1);
            while(true){
            	connection = socket.accept();
            	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            	DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
            	String clientSentence = "", result = "";
            	
            	while((clientSentence = inFromClient.readLine()).length() != 0) {
            		result += clientSentence + "\n";
            	}
            	System.out.println("Client data retrieved.");
             	
            	// retrieve data from ITU
            	InetAddress address = InetAddress.getByName("itu.dk");
                Socket forward = new Socket(address, 80);    
                DataOutputStream toItu = new DataOutputStream(forward.getOutputStream());
                DataInputStream  fromItu = new DataInputStream(forward.getInputStream());
                toItu.writeBytes(result + "\n"); // forward client to itu.
                
                // get size of returning message from itu 
                short size = fromItu.readShort();
                byte[] payload = new byte[size];
                fromItu.readFully(payload);
                forward.close();
                outToClient.write(payload);
                outToClient.flush();
                connection.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}