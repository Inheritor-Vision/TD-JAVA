import java.net.Socket;
import java.io.IOException;
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
        System.out.println("SendConfig: ready-to-test command sent, closing . . . .");
        client.close();
    }
    
}