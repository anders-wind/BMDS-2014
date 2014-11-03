import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Create a Node on a given port.
 * The Node can also know of another Node in the network if another port is specified.
 */
public class Node {
    private int ownPort;
    private int otherPort;
    private HashMap<Integer, String> messages = new HashMap<Integer, String>();

    /**
     * Create a Node that might optionally know about another Node in the network.
     */
    public Node(int ownPort, int otherPort) {
        try {
            ServerSocket socket = new ServerSocket(ownPort);
            while (true) {
                Socket client = socket.accept();
                new Thread(() -> handleMessage(client)).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Open own port, and optionally know about a neighbour Node.
        this.ownPort = ownPort;

        if (otherPort != 0) {
            this.otherPort = otherPort;
        }
    }

    private void handleMessage(Socket client){
        try {
            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            parseInput(fromClient.readLine());
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
            Put.put(getterPort, messageKey, messages.get(messageKey));
        }

        else {
            forward(messageKey, getterPort);
        }
    }

    /**
     * Forward the GET-request to the neighbour Node if this Node knows about it.
     */
    private void forward(int messageKey, int originalPort) {
        if (otherPort != 0) {
            Get.get(messageKey, otherPort, originalPort);
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
    	String[] input = getMessage.split(":");
    	if(input[0].equals("Get"))
    	{
    		getMessage(Integer.parseInt(input[1].trim()),Integer.parseInt(input[2].trim()));
    	}
    	else if(input[0].equals("Put"))
    	{
    		setMessage(Integer.parseInt(input[1].trim()), input[2].trim());
    	}
    	else{
    		System.out.println("Parser no comprehendi la cody");
    	}
    }

    /**
     * Instantiate a Node with a given port and optionally a neighbour Node.
     * @param args Port(s) for a Node and optionally the port of its neighbour.
     * @throws InvalidArgumentException If no port for the Node is given.
     */
    public static void main(String[] args) throws InvalidArgumentException {
        if (args[0] == null) {
            throw new InvalidArgumentException(args);
        }

        else if (args[1] == null) {
            new Node(Integer.parseInt(args[0]), 0);
            return;
        }

        new Node(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
