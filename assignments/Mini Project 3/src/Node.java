import java.util.HashMap;

import com.sun.javaws.exceptions.InvalidArgumentException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Create a Node on a given port.
 * The Node can also know of another Node in the network if another port is specified.
 */
public class Node {
    private int ownPort;
    private int otherPort;
    private HashMap<Integer,String> resources = new HashMap<Integer, String>();
    private String message;

    /**
     * Create a Node that might optionally know about another Node in the network.
     */
    public Node(int ownPort, int otherPort) {
        //Open own port, and optionally know about a neighbour Node.
        this.ownPort = ownPort;

        if (otherPort != 0) {
            this.otherPort = otherPort;
        }
    }

    /**
     * Set the message of this node.
     */
    public void setMessage(int messageKey, String message) {
    	resources.put(messageKey,message);
    }

    /**
     * Try to get the message of this node. If this node doesn't have the message then it forwards the request to the
     * other Nodes in the network.
     */
    public void getMessage(int messageKey, int getterPort) {
        if (this.messageKey == messageKey) {
            Put.put(getterPort, messageKey, message);
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
