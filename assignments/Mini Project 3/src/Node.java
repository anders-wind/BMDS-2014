import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Create a Node on a given port.
 * The Node can also know of another Node in the network if another port is specified.
 */
public class Node {
    private PortWithID ownPort = new PortWithID();
    private PortWithID primaryPort = new PortWithID();
    private PortWithID secondaryPort = new PortWithID();
    private HashMap<Integer, String> messages = new HashMap<>();

    /**
     * Create a Node that might optionally know about another Node in the network.
     */
    public Node(int ownPort, int primaryPort) {
        //Open own port, and optionally know about a neighbour Node.
        this.ownPort.setPort(ownPort);
        this.ownPort.setID(1);

        //Check if no neighbour port has been provided.
        //If there, then we chain this node to the neighbours neighbour.
        if (primaryPort != 0) {
            this.primaryPort.setPort(primaryPort);
            fixPrimAndSecNodes();
            this.ownPort.setID(this.primaryPort.getID() + 1);
            newNode(this.ownPort.getPort(), this.ownPort.getID());
            fixPrimAndSecNodes();
        }

        //Start heartbeating (heartbeats fix errors in the system).
        new Thread(() -> heartbeat()).start();

        //Start listening for incoming messages.
        listenForMessages();
    }

    /**
     * Check and receive messages infinitely.
     */
    private void listenForMessages() {
        try {
            while (true) {

                ServerSocket socket = new ServerSocket(ownPort.getPort());
                Socket client = socket.accept();
                new Thread(() -> handleMessage(client)).start();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to the primary neighbour of this node, and get its primary node.
     * Assign that node as this node's secondary node.
     */
    private void fixPrimAndSecNodes() {
        try {
            Socket socket = new Socket("localhost", primaryPort.getPort());
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            dataOut.writeBytes("ReturnPort: " + ownPort + "\n");
            parseInput(dataIn.readLine());
            System.out.println("Primary port set to: " + primaryPort);
            System.out.println("Secondary port set to: " + secondaryPort);
            socket.close();
        } catch (UnknownHostException ex) {
            System.err.println("Unknown Host: localhost");
        } catch (IOException ex) {
            System.err.println("Failed to retrieve I/O for the connection localhost");
        }
    }


    /**
     * Heartbeat by checking the neighbouring nodes every three seconds.
     * This finds and fixes errors in the system.
     */
    private void heartbeat() {
        while (true) {
            System.out.println("(( <3 ))");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (primaryPort.getPort() != 0) {
                checkPrimary();
            }
            if (secondaryPort.getPort() != 0) {
                checkSecondary();
            }
        }
    }

    /**
     * Open a socket to the given port and send a hearbeat to it.
     * @param onPort The port to open the socket at.
     * @throws IOException Thrown if the socket couldn't be opened/written to.
     */
    private void doHeartbeat(int onPort) throws IOException {
        Socket socket = new Socket("localhost", onPort);
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        dataOut.writeBytes("(( <3 )):" + ownPort + "\n");
        dataIn.readLine();

        socket.close();
    }

    /**
     * Check for errors by sending a heartbeat to the primary node.
     * If errors occur, the node is considered dead and the node will reassign its primary port.
     */
    private void checkPrimary() {
        try {
            doHeartbeat(primaryPort.getPort());
            System.out.println("(( <3 )) to primary successful.");
        } catch (UnknownHostException ex) {
            System.err.println("Unknown Host: localhost");
        } catch (IOException ex) {
            primaryPort = secondaryPort;
            System.out.println("(( <3 )) to primary failed. primaryPort set to:" + primaryPort);
        }
    }

    /**
     * Check for errors by sending a heartbeat to the secondary node.
     * If errors occur, the node is considered dead and the node will reassign its secondary port.
     */
    private void checkSecondary() {
        try {
            doHeartbeat(secondaryPort.getPort());
            System.out.println("(( <3 )) to secondary successful.");
        } catch (UnknownHostException ex) {
            System.err.println("Unknown Host: localhost");
        } catch (IOException ex) {
            fixPrimAndSecNodes();
            System.out.println("(( <3 )) to secondary failed. secondaryPort is set to:" + secondaryPort);
        }
    }

    /**
     * Handle the incoming message by parsing it.
     * @param client The client the message is coming from.
     */
    private void handleMessage(Socket client) {
        try {
            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            String input = (fromClient.readLine());
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
            toClient.writeBytes("MyData:" + ownPort.getID() + ":" + primaryPort.getPort() + ":" + primaryPort.getID() + "\n");
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
            Put.put(messageKey, messages.get(messageKey), getterPort);
        } else {
            forward(messageKey, getterPort);
        }
    }

    /**
     * Forward the GET-request to the neighbour Node if this Node knows about it.
     */
    private void forward(int messageKey, int originalPort) {
        try {
            if (primaryPort.getPort() != 0) {
                Get.get(messageKey, primaryPort.getPort(), originalPort);
                return; // everything fine
            }
        } catch (IOException ex) {
            System.err.println("Failed to retrieve I/O for the connection localhost");
        }

        try {
            if (secondaryPort.getPort() != 0) {
                Get.get(messageKey, secondaryPort.getPort(), originalPort);
                return; // everything fine
            }
        } catch (IOException ex) {
            System.err.println("Failed to retrieve I/O for the connection localhost");
        }
    }

    /**
     * Parse the input by splitting the incoming message on ":".
     * Get: messageKey : getterPort
     * Put: messageKey : Message
     * NewNode: newNodePort
     * MyData: TheNodesID : ThePrimaryPort : ThePrimaryID
     */
    private void parseInput(String message) {
        String[] input = message.toLowerCase().split(":");
        if (input[0].equals("get")) {
            getMessage(Integer.parseInt(input[1].trim()), Integer.parseInt(input[2].trim()));
        } else if (input[0].equals("put")) {
            setMessage(Integer.parseInt(input[1].trim()), input[2].trim());
        } else if (input[0].equals("mydata")) {
           ownPort.setID(Integer.parseInt(input[1].trim()));
           primaryPort = new PortWithID(Integer.parseInt(input[2].trim()),Integer.parseInt(input[3].trim()));
        } else if (input[0].equals("newnode")) {
        	newNode(Integer.parseInt(input[1].trim()),Integer.parseInt(input[2].trim()));
        } else if (input[0].equals("(( <3 ))") || input[0].equals("returnport")) {
            return;
        } else {
            System.out.println("Failed parsing message. \nThe message received was: " + message);

        }
    }
    
    private void newNode(int newNodePort, int newNodeID)
    {
    	if(primaryPort.getID() < ownPort.getPort() && primaryPort.getPort() != 0)
    	{
    		try {
                if (primaryPort.getPort() != 0) {
                    Get.newNode(newNodePort, newNodeID, primaryPort.getPort());
                    return; // everything fine
                }
            } catch (IOException ex) {
                System.err.println("Failed to retrieve I/O for the connection localhost");
            }

            try {
                if (secondaryPort.getPort() != 0) {
                	Get.newNode(newNodePort, newNodeID, secondaryPort.getPort());
                    return; // everything fine
                }
            } catch (IOException ex) {
                System.err.println("Failed to retrieve I/O for the connection localhost");
            }
    	}
    	else
    	{
    		secondaryPort = primaryPort;
    		primaryPort = new PortWithID(newNodePort, newNodeID);
    	}
    }

    /**
     * Instantiate a Node with a given port and optionally neighbouring Nodes.
     *
     * @param args Port(s) for a Node and optionally the port of its neighbour.
     * @throws IllegalArgumentException If no port for the Node is given.
     */
    public static void main(String[] args) throws IllegalArgumentException {
        if (args[0] == null) {
            throw new IllegalArgumentException();
        } else if (args.length < 2) {
            new Node(Integer.parseInt(args[0]), 0);
            return;
        }

        new Node(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
    
    
    private class PortWithID
    {
    	private int port;
    	private int id;
    	
    	public PortWithID()
    	{
    	}
    	
    	public PortWithID(int port, int id)
    	{
    		this.port = port;
    		this.id = id;
    	}
    	
    	public void setPort(int port)
    	{
    		this.port = port;
    	}
    	public int getPort()
    	{
    		return port;
    	}
    	
    	public void setID(int id)
    	{
    		this.id = id;
    	}
    	
    	public int getID()
    	{
    		return id;
    	}
    	
    	@Override
    	public String toString()
    	{
    		return "Port: " + port + " ID: " + id;
    	}
    }
}
