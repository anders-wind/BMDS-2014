import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * WHAT I DO?
 */
public class Node {
    private int ownPort;
    private int otherPort;

    /**
     * Optionally create a Node that knows of another Node.
     */
    public Node(int ownPort, int otherPort) {
        //Open own port, and optionally know about a neighbour Node.
        this.ownPort = ownPort;

        if (otherPort != 0) {
            this.otherPort = otherPort;
        }
    }

    private void forward(int messageKey, int originalPort) {
        if (otherPort != 0) {
            Get.get(messageKey, otherPort, originalPort);
        }
    }
    
    
    private void parseInput(String getMessage)
    {
    	String[] input = getMessage.split(":");
    	if(input[0].equals("Get"))
    	{
    		
    	}
    	else if(input[0].equals("Put"))
    	{
    		
    	}
    }
}
