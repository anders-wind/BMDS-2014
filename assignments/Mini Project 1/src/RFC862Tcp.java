import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RFC862Tcp {
	public static void TCPEchoServer() {
    	ServerSocket socket = null;
    	Socket connection = null;
    	
        try{
            socket = new ServerSocket(7007);
            while(true){
            	connection = socket.accept();
            	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            	DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
            	String clientSentence = "", result = "";
            	
            	while((clientSentence = inFromClient.readLine()).length() != 0) {
            		result += clientSentence + "\n";
            	}

                outToClient.write(result.getBytes());
                connection.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
	}
	
	public static void Client() throws IOException {
		//console
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // retrieve data from console
		System.out.print("Enter String\n");
	    String userinput = br.readLine();
	    
		//setup TCP
		InetAddress address = InetAddress.getLocalHost();
        Socket forward = new Socket(address, 7007);    
        DataOutputStream toServer = new DataOutputStream(forward.getOutputStream());
        DataInputStream  fromServer = new DataInputStream(forward.getInputStream());
	    
	    // write to stream
        toServer.writeBytes(userinput);
        toServer.close();
        
        System.out.println("CLIENT: Sent message (" + userinput + ")");
        
        // read from stream
        while(fromServer.available() == 0){
        	try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
        
        System.out.println("CLIENT: Recieved data. Converting..");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];

        for(int s; (s = fromServer.read(buffer)) != -1; ) {
          baos.write(buffer, 0, s);
        }
        
        // print to console
        byte[] yo = baos.toByteArray();
        System.out.println("CLIENT: Recieved the following: " + new String(yo));
	}
	
	public static void main(String[] args) {
		//start server		
		new Thread(() -> RFC862Tcp.TCPEchoServer()).start();;

		try {
			RFC862Tcp.Client(); // call server
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
		
}