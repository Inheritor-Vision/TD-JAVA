import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
/**
 * IPBOX_App
 */
public class IPBOX_App {

    public static void main(String[] args) throws IOException, SocketException{
        System.out.println("App: Starting");
        int port = 6002;
        DatagramSocket dgramSocket= new DatagramSocket(port);
        System.out.println("App: Server Created on port " + port );
        byte[] buffer = new byte[256];
        DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
        dgramSocket.receive(inPacket);
        InetAddress clientAddress= inPacket.getAddress();
        int clientPort= inPacket.getPort();
        String message = new String(inPacket.getData(), 0, inPacket.getLength());
        System.out.println("App: Message received \"" + message + "\"");
        DatagramPacket outPacket= new DatagramPacket(message.getBytes(), message.length(),clientAddress, clientPort);
        dgramSocket.send(outPacket);
        System.out.println("App: Message echoed");
        dgramSocket.close();
    }
}