import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Anders on 03/11/14.
 */
public class Get {
	private static boolean gotResponse = false;
	
    public static void get(int messageKey, int portToGetFrom, int portToReceiveTo) throws IOException{
    	String message = "GET:" + messageKey + ":" + portToReceiveTo + " \n";

	    	Socket nodeSocket 			= new Socket("localhost", portToGetFrom);
	    	BufferedReader fromNode		= new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
	    	DataOutputStream toNode		= new DataOutputStream(nodeSocket.getOutputStream());
	    	toNode.writeBytes(message);
	    	fromNode.readLine(); // blocking till the receiver has read my stuff
	    	nodeSocket.close();
    }
    
    public static void newNode(int nodePort, int nodeID, int nextNode)throws IOException
    {
    	String message = "NewNode: " + nodePort + " : " + nodeID + " \n";

    	Socket nodeSocket 			= new Socket("localhost", nextNode);
    	BufferedReader fromNode		= new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
    	DataOutputStream toNode		= new DataOutputStream(nodeSocket.getOutputStream());
    	toNode.writeBytes(message);
    	fromNode.readLine(); // blocking till the receiver has read my stuff
    	nodeSocket.close();
    }
    
    public static void handleResponse(ServerSocket socket) {
    	try {
	    	//waiting for answer
	    	Socket reply = socket.accept();    	
	    	
	    	BufferedReader fromAnswerNode = new BufferedReader(new InputStreamReader(reply.getInputStream()));
	    	DataOutputStream toAnswerNode = new DataOutputStream(reply.getOutputStream());
	    	
	    	String output = fromAnswerNode.readLine();
	    	toAnswerNode.writeBytes("Tjek \n");
	    	
	    	// All its done. Peace out - Hasta la vista baby.
	    	gotResponse = true;
	    	reply.close();
	    	System.out.println(output + " \n");
    	} catch(Exception e) {
    		System.out.println(">> Something whent wrong when trying to handleResponse");
    	}

    }

    public static void main(String[] args) throws Exception {
    	int key 			= Integer.parseInt(args[0]);
    	int nodePort	 	= Integer.parseInt(args[1]);

    	int myPort = 6666; // hardcoded yes
    	ServerSocket replySocket = new ServerSocket(myPort);
    	Thread t = new Thread(() -> handleResponse(replySocket)); // Open socket to get replied to
    	t.start();
    	get(key, nodePort, myPort);
    	int timeoutCounter = 0;
    	do {
    		Thread.sleep(100);
    		timeoutCounter++;
		} while (!gotResponse && timeoutCounter < 30);
    	if(gotResponse == false)
    	{
    		t.stop(); // horrible way to do this.
    		System.out.println(">> Timeout on response from peer/node.");
    	}
    	gotResponse = false;
    }
}
