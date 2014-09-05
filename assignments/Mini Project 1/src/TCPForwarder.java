import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
    	ServerSocket socket;
    	Socket connection;
        try{
            socket = new ServerSocket(p1);
            while(true){
            	connection = socket.accept();
            	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            	DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
            	String clientSentence = "";
            	String result = "";
            	
            	while((clientSentence = inFromClient.readLine()).length() != 0) {
            		result += clientSentence + "\n";
            	}
            	System.out.println("Client data retrieved.");
            	//outToClient.writeBytes(result);
            	//outToClient.close();
             	//inFromClient.close();
             	
            	// retrieve data from ITU
                Socket s = new Socket("google.com", 80);    
                Scanner in = new Scanner(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                
                System.out.println("Sended: \n" + result);
                // fuckign send it!

                connection.close();
                s.close();             	
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}