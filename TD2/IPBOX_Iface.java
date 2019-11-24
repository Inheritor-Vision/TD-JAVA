import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * IPBOX_Iface
 */
public class IPBOX_Iface {

    public static void main(String[] args) throws SocketException, IOException{
        System.out.println("Iface: Starting");
        int port = 6003;
        DatagramSocket dgramSocket= new DatagramSocket(port);
        System.out.println("Iface: Server Created on port " + port );
        byte[] buffer = new byte[256];
        DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
        dgramSocket.receive(inPacket);
        InetAddress clientAddress= inPacket.getAddress();
        int clientPort= inPacket.getPort();
        String message = new String(inPacket.getData(), 0, inPacket.getLength());
        System.out.println("Iface: Message received \"" + message + "\"");
        DatagramPacket outPacket= new DatagramPacket(message.getBytes(), message.length(),clientAddress, clientPort);
        dgramSocket.send(outPacket);
        System.out.println("Iface: Message echoed");
        dgramSocket.close();
    }
}