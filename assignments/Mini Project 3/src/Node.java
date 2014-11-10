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
    private int primaryPort;
    private int secondaryPort;
    private HashMap<Integer, String> messages = new HashMap<>();

    /**
     * Create a Node that might optionally know about another Node in the network.
     */
    public Node(int ownPort, int primaryPort) {
        //Open own port, and optionally know about a neighbour Node.
        this.ownPort = ownPort;

        //Check if no neighbour port has been provided.
        //If there, then we chain this node to the neighbours neighbour.
        if (primaryPort != 0) {
            this.primaryPort = primaryPort;
            getSecondaryNode();
        }

        //Start heartbeating (heartbeats fix errors in the system.
        new Thread(() -> heartBeat()).start();

        try {
            while (true) {
                //Receive messages infinitely.
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
    		Socket socket = new Socket("localhost", primaryPort);
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
    		System.out.println("(( <3 ))");
    		try{
    			Thread.sleep(3000);
    		}catch(InterruptedException e)
    		{
    			System.out.println(e);
    		}
    		if(primaryPort != 0)
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
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        dataOut.writeBytes("(( <3 )) : " + ownPort + "\n");
        dataIn.readLine();

        socket.close();
    }

    private void checkPrimary()
    {
    	try{
            doHeartbeat(primaryPort);
            System.out.println("(( <3 )) to primary successful.");
    	}catch(UnknownHostException ex)
    	{
    		System.err.println("Unknown Host: localhost");
    	}catch(IOException ex)
    	{
    		primaryPort = secondaryPort;
    		System.out.println("(( <3 )) to primary failed: ownPort set to secondary port:" + primaryPort);
    	}
    }
    
    private void checkSecondary()
    {
    	try{
    		doHeartbeat(secondaryPort);
    		System.out.println("(( <3 )) to secondary successful.");
    	}catch(UnknownHostException ex)
    	{
    		System.err.println("Unknown Host: localhost");
    	}catch(IOException ex)
    	{
    		getSecondaryNode();
    		System.out.println("(( <3 )) to secondary failed: secondary port is set to:" + secondaryPort);
    	}
    }

    private void handleMessage(Socket client){
        try {
            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            String input = (fromClient.readLine());
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
            toClient.writeBytes(primaryPort + "\n");
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
        	if (primaryPort != 0) {
                Get.get(messageKey, primaryPort, originalPort);
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
    	else if(input[0].equals("(( <3 ))"))
    	{
    		return;
    	}
    	else{
    		System.out.println("Parser no comprehendi la cody.");
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
