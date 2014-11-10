import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

//import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Create a Node on a given port.
 * The Node can also know of another Node in the network if another port is specified.
 */
public class Node {
    private int ownPort;
    private int otherPort;
    private int secondaryPort;
    private HashMap<Integer, String> messages = new HashMap<Integer, String>();

    /**
     * Create a Node that might optionally know about another Node in the network.
     */
    public Node(int ownPort, int otherPort) {
        //Open own port, and optionally know about a neighbour Node.
        this.ownPort = ownPort;

        if (otherPort != 0) {
            this.otherPort = otherPort;
            getSecondaryNode();
        }
        
        new Thread(() -> heartBeat()).start();

        try {
            while (true) {
            	ServerSocket socket = new ServerSocket(ownPort);
                Socket client = socket.accept();
                new Thread(() -> handleMessage(client)).start();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void getSecondaryNode()
    {
    	try{
    		Socket socket = new Socket("localhost", otherPort);
    		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
    		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
    		
    		dataOut.writeBytes("returnPort: " + ownPort + "\n");
    		secondaryPort = Integer.parseInt(dataIn.readLine().trim());
    		System.out.println("Secondary port set to: " + secondaryPort);
    		socket.close();
    	}catch(UnknownHostException ex)
    	{
    		System.err.println("Unknown Host: localhost");
    	}catch(IOException ex)
    	{
    		System.err.println("Failed to retrieve I/O for the connection localhost");
    	}
    }
    
    
    private void heartBeat()
    {
    	do {
    		System.out.println("heartbeat called");
    		try{
    			Thread.sleep(3000);
    		}catch(InterruptedException e)
    		{
    			System.out.println(e);
    		}
    		if(otherPort != 0)
    		{
    			checkPrimary();
    		}
    		if(secondaryPort != 0)
    		{
    			checkSecondary();
    		}
		} while (true);
    }

    private void doHeartbeat(int onPort) throws IOException {
        Socket socket = new Socket("localhost", onPort);
        //DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        dataOut.writeBytes("HeartBeat:" + ownPort);
        //dataIn.readLine();

        socket.close();
    }

    private void checkPrimary()
    {
    	try{
            doHeartbeat(otherPort);
            System.out.println("Heartbeat to primary successfull.");
    	}catch(UnknownHostException ex)
    	{
    		System.err.println("Unknown Host: localhost");
    	}catch(IOException ex)
    	{
    		otherPort = secondaryPort;
    		System.out.println("Heartbeat to primary failed: ownPort set to secondary port:" + otherPort);
    	}
    }
    
    private void checkSecondary()
    {
    	try{
    		doHeartbeat(secondaryPort);
    		System.out.println("Heartbeat to secondary successfull");
    	}catch(UnknownHostException ex)
    	{
    		System.err.println("Unknown Host: localhost");
    	}catch(IOException ex)
    	{
    		getSecondaryNode();
    		System.out.println("Heartbeat to secondary failed: secondary port is set to:" + secondaryPort);
    	}
    }

    private void handleMessage(Socket client){
        try {
            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            String input = (fromClient.readLine());
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
            toClient.writeBytes(otherPort + "\n");
            parseInput(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the message of this node.
     */
    public void setMessage(int messageKey, String message) {
    	messages.put(messageKey, message);
    }

    /**
     * Try to get the message of this node. If this node doesn't have the message then it forwards the request to the
     * other Nodes in the network.
     */
    public void getMessage(int messageKey, int getterPort) {
        if (messages.containsKey(messageKey)) {
            Put.put(messageKey, messages.get(messageKey),getterPort);
        }
        else {
            forward(messageKey, getterPort);
        }
    }

    /**
     * Forward the GET-request to the neighbour Node if this Node knows about it.
     */
    private void forward(int messageKey, int originalPort) {
        try{
        	if (otherPort != 0) {
                Get.get(messageKey, otherPort, originalPort);
                return; // everything fine
            }
        }catch(IOException ex)
    	{
        	System.err.println("Failed to retrieve I/O for the connection localhost");
    	}
        
        try
        {
        	if (secondaryPort != 0) {
        		Get.get(messageKey, secondaryPort, originalPort);
        		return; // everything fine
        	}
        }catch(IOException ex)
    	{
        	System.err.println("Failed to retrieve I/O for the connection localhost");
    	}
    }

    /**
     * Finished the Parser.
	 *	Get: messageKey : getterPort
	 *	Put: messageKey : Message
     * @param getMessage
     */
    private void parseInput(String getMessage)
    {
    	String[] input = getMessage.toLowerCase().split(":");
    	if(input[0].equals("get"))
    	{
    		getMessage(Integer.parseInt(input[1].trim()),Integer.parseInt(input[2].trim()));
    	}
    	else if(input[0].equals("put"))
    	{
    		setMessage(Integer.parseInt(input[1].trim()), input[2].trim());
    	}
    	else if(input[0].equals("returnport"))
    	{
    		return;
    	}
    	else if(input[0].equals("heartbeat"))
    	{
    		return;
    	}
    	else{
    		System.out.println("Parser no comprehendi la cody");
    	}
    }

    /**
     * Instantiate a Node with a given port and optionally a neighbour Node.
     * @param args Port(s) for a Node and optionally the port of its neighbour.
     * @throws IllegalArgumentException If no port for the Node is given.
     */
    public static void main(String[] args) throws IllegalArgumentException {
        if (args[0] == null) {
            throw new IllegalArgumentException();
        }

        else if (args.length < 2) {
            new Node(Integer.parseInt(args[0]), 0);
            return;
        }

        new Node(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
