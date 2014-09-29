import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class AwesomeServer {
	
	private ArrayList<Client> subscribers; 
	
	public AwesomeServer() {
		subscribers = new ArrayList<>();
		 
		try {
			ServerSocket s = new ServerSocket(7777);
			Socket client;
			while(true) {
				client = s.accept();
				DataOutputStream toServer = new DataOutputStream(client.getOutputStream());
				DataInputStream fromServer = new DataInputStream(client.getInputStream());
				
				String line = fromServer.readLine();
				if(line.contains("subscribe")) {
					// new subscriber, add to list
					String[] split = client.getRemoteSocketAddress().toString().split(":");
					String caddress = split[0].substring(1, split[0].length());
					int cport = Integer.parseInt(split[1]);
					subscribers.add(new Client(caddress, cport));
				} else {
					// not a subscribe. publish
					if(subscribers.isEmpty()) {
						System.out.println("No subscribers available");
					} else {
						subscribers.stream().forEach(c -> broadcast(c, line));
						// This might throw an exception
						Client[] x = (Client[]) subscribers.stream().filter(c -> !c.dead).toArray();
						subscribers = new ArrayList<>(Arrays.asList(x));
					}
				}

				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void broadcast(Client c, String message) {
		try {
			Socket testSender = new Socket(c.address, c.port);
			DataOutputStream toServer = new DataOutputStream(testSender.getOutputStream());
			toServer.writeBytes(message);
		} catch (IOException e) {
			// The socket doesn't work.
			c.dead = true;
		}
	}
	
	
	private class Client {
		private String address;
		private int port;
		private boolean dead;

		public Client(String a, int p) {
			this.dead = false;
			this.address = a;
			this.port = p;
		}
	}
	
	public static void main(String[] args) {
		new Thread(AwesomeServer::new).start();
		try {
			Socket testSender = new Socket("127.0.0.1", 7777);
			DataOutputStream toServer = new DataOutputStream(testSender.getOutputStream());
			toServer.writeBytes("subssdscribe \n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
