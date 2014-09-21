import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RFC862 {

    public RFC862(String port) {
        echo(Integer.parseInt(port));
    }

    private void echo(int portNumber) {
        try {
            ServerSocket echoSocket = new ServerSocket(portNumber);

            while (true) {
                Socket connection = echoSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                output.writeBytes(input.readLine());
            }
        } catch (Exception e) {
            System.out.println("Shit went wrong." + e.getMessage());
        }
    }
}