import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
            	System.out.println("Client request retrieved.");
             	
            	// retrieve data from ITU
            	InetAddress address = InetAddress.getByName("itu.dk");
                Socket forward = new Socket(address, 80);    
                DataOutputStream toItu = new DataOutputStream(forward.getOutputStream());
                DataInputStream  fromItu = new DataInputStream(forward.getInputStream());
                toItu.writeBytes(result); // forward client to itu.
                                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte buffer[] = new byte[1024];
                for(int s; (s=fromItu.read(buffer)) != -1; )
                {
                  baos.write(buffer, 0, s);
                }
                byte[] yo = baos.toByteArray();
                forward.close();
                outToClient.write(yo);
                System.out.println("Client request forwarded and response returned.");
                connection.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}