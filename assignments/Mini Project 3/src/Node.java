import com.sun.javaws.exceptions.InvalidArgumentException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * WHAT I DO?
 */
public class Node {
    private int ownPort;
    private int otherPort;
    private int messageKey;
    private String message;

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

    public void setMessage(int messageKey, String message) {
        this.messageKey = messageKey;
        this.message = message;
    }

    public void getMessage(int messageKey, int getterPort) {
        if (this.messageKey == messageKey) {
            Put.put(getterPort, messageKey, message);
        }

        else {
            forward(messageKey, getterPort);
        }
    }

    private void forward(int messageKey, int originalPort) {
        if (otherPort != 0) {
            Get.get(messageKey, otherPort, originalPort);
        }
    }

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
