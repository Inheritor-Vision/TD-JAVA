import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class IPBOX_Testbox {
    public static void main(String[] args) throws SocketException, IOException{
        try{
            
            ServerSocket servSocket = new ServerSocket(6000);
            System.out.println("Testbox: Server created, waiting for connection");
            Socket link = servSocket.accept();
            
            System.out.println("Testbox: Client accepted, waiting for data");
            boolean fin =false;
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            while(!fin){
                String temp = in.readLine();
                if(!temp.isEmpty()){
                    System.out.println("Testbox: reception of TCP packet \"" + temp + "\"");
                    fin = true;
                    
                }
                
            }
            link.close();
            System.out.println("Testbox: Start connection to receive-config");
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Testbox: address " + address);
            link = new Socket(address, 6001);
            System.out.println("Testbox: Connection successful to receive-config");
            PrintWriter out = new PrintWriter(link.getOutputStream(),true);
            out.println("random-command");
            System.out.println("Testbox: random command command sent, waiting echo. . . .");
            fin = false;
            in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            while(!fin){ 
                String temp = in.readLine();
                if(!temp.isEmpty()){
                    System.out.println("Testbox: reception of TCP packet \"" + temp + "\"");
                    fin = true;
                    
                }
                
            }
            link.close();
            servSocket.close();

            System.out.println("Testbox: Init UDP connection");
            DatagramSocket dgramSocketIface= new DatagramSocket();
            DatagramSocket dgramSocketApp= new DatagramSocket();
            dgramSocketApp.setSoTimeout(2000);
            dgramSocketIface.setSoTimeout(2000);
            String message = "random UDP message";
            InetAddress host = InetAddress.getLocalHost();
            int portApp = 6002;
            int portIface = 6003;
            DatagramPacket outPacketApp= new DatagramPacket(message.getBytes(), message.length(),host, portApp);
            DatagramPacket outPacketIface= new DatagramPacket(message.getBytes(), message.length(),host, portIface);
            dgramSocketApp.send(outPacketApp);
            dgramSocketIface.send(outPacketIface);
            System.out.println("Testbox: Data sent to both UDP socket");
            byte[] bufferApp = new byte[256];
            byte[] bufferIface = new byte[256];
            DatagramPacket inPacketApp= new DatagramPacket(bufferApp, bufferApp.length);
            DatagramPacket inPacketIface= new DatagramPacket(bufferIface, bufferIface.length);
            boolean finApp = false, finIface = false;
            while (!finApp || !finIface){
                if(!finApp){
                    try{
                        dgramSocketApp.receive(inPacketApp);
                        String responseApp = new String(inPacketApp.getData(), 0, inPacketApp.getLength());
                        System.out.println("Testbox: data received from App \"" + responseApp + "\"");
                        finApp = true;
                    }catch(SocketTimeoutException e){
                        System.out.println("Testbox: Resending message to App");
                        dgramSocketApp.send(outPacketApp);
                        continue;
                    } 
                }
               
            if(!finIface){
                try{
                    dgramSocketIface.receive(inPacketIface);
                    String responseIface = new String(inPacketIface.getData(), 0, inPacketIface.getLength());
                    System.out.println("Testbox: data received from Iface \"" + responseIface + "\"");
                    finIface = true;
                }catch(SocketTimeoutException e){
                    System.out.println("Testbox: Resending message to Iface");
                    dgramSocketIface.send(outPacketIface);
                    continue;
                }
                
            }
        }
            
            
        dgramSocketApp.close();
        dgramSocketIface.close();

        }catch (IOException e){
            System.out.print("Testbox: Error instanciation Socket ");
        }
    }
    
}