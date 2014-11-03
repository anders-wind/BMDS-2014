import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * "A "Node" process takes as arguments a local port and optionally the IP/port of another Node process.
 * The Node then listens for PUT and GET requests on its local port.
 *
 * A "Put-client" process takes as arguments the IP/port of a Node, an integer key, and a string value.
 * The client then submits a PUT(key, value) message to the indicated node and terminates.
 *
 * A "Get-client" process takes arguments the IP/port of a Node, and an integer key.
 * It submits a GET(key, ip2, port2) message to the indicated Node,
 * then listens on ip2/port2 for a PUT(key, value) message, which, if it arrives,
 * indicates that the Node network has stored the association (key, value), that is,
 * that some Put-client previously issued that PUT.
 *
 * If the network of Nodes receives inconsistent PUTs, (e.g., PUT(1, A) then later PUT(1, B)),
 * the value of subsequent GETs is undefined (i.e., no answer, PUT(1, A) and PUT(1, B) are all valid results.)."
 */
public class Node {
    private int ownPort;
    private int otherPort;

    /**
     * Create a node that will listen on a given port.
     */
    public Node(int ownPort) {
        this.ownPort = ownPort;
        //Git works?
    }

    /**
     * Optionally create a Node that knows of another Node.
     */
    public Node(int ownPort, int otherPort) {
        this.ownPort = ownPort;
        this.otherPort = otherPort;
    }

    public String get(int messageKey, int portToGetFrom, int portToReceiveTo) {
        throw new NotImplementedException();
    }

    public void put(int portToSendTo, int messageKey, String messageToPut) {

    }
}
