import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * "A "Get-client" process takes arguments the IP/port of a Node, and an integer key.
 * It submits a GET(key, ip2, port2) message to the indicated Node,
 * then listens on ip2/port2 for a PUT(key, value) message, which, if it arrives,
 * indicates that the Node network has stored the association (key, value), that is,
 * that some Put-client previously issued that PUT."
 */
public class Get {
    public Get(String ip, int key) {

    }

    public String get(int key, String ip, int port2) {
        throw new NotImplementedException();
    }
}
