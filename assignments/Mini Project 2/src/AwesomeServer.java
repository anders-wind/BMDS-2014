import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class AwesomeServer {
	
	private ArrayList<Client> subscribers; 
	
	public AwesomeServer() {
		subscribers = new ArrayList<Client>();
		 
		try {
			ServerSocket s = new ServerSocket(7777);
			while(true) {
				Socket c = s.accept();
				new Thread(() -> listen(c)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void listen(Socket client) {
		try {
			DataOutputStream toServer = new DataOutputStream(client.getOutputStream());
			DataInputStream fromServer = new DataInputStream(client.getInputStream());
			
			String line = fromServer.readLine();
			toServer.writeBytes("ok \n");
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
					subscribers = subscribers.stream()
							.filter(c -> !c.dead)
							.collect(Collectors.toCollection(ArrayList::new));
				}
			}
	
			client.close();
		} catch(IOException e) {
			System.out.println("and error happened in the listen method");
		}
	}
	
	public void broadcast(Client c, String message) {
		try {
			Socket testSender = new Socket(c.address, c.port);
			DataOutputStream toServer = new DataOutputStream(testSender.getOutputStream());
			toServer.writeBytes(message);
			testSender.close();
		} catch (IOException e) {
			// The socket doesn't work.
			System.out.println("the endpoint doesn't exist");
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
	}
}
