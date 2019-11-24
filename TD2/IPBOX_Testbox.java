import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class IPBOX_Testbox {
    public static void main(String[] args) {
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

        }catch (IOException e){
            System.out.print("Testbox: Error instanciation Socket ");
        }
    }
    
}