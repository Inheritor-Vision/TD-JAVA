import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.PrintWriter;

public class IPBOX_SendConfig {
    public static void main(String[] args) throws IOException{
        System.out.println("SendConfig: Start");
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("SendConfig: address " + address);
        Socket client = new Socket(address, 6000);
        System.out.println("SendConfig: Connection successful to TestBox");
        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
        out.println("ready-to-test");
        System.out.println("SendConfig: ready-to-test command sent");
        System.out.println("SendConfig: Wainting for next command");
        boolean fin = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while(!fin){
            String temp = in.readLine();
            if(!temp.isEmpty()){
                System.out.println("SendConfig: reception of TCP packet " + temp);
                fin = true;
            }
                
        }
        System.out.println("SendConfig: Sending back list-of-stats command to Testbox");
        out.println("list-of-stat");
        System.out.println("SendConfig: command sent");
        client.close();
    }
    
}