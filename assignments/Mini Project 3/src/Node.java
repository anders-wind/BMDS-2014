import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Anders on 03/11/14.
 */
public class Node {
    private String ip;
    private int key;

    public Node(String ownIp, int ownKey) {
        ip = ownIp;
        key = ownKey;
    }

    public String get(int messageKey, int getFromPort, int receiveToPort) {
        throw new NotImplementedException();
    }

    public void put(int messageKey, String valueToPut) {

    }
}
