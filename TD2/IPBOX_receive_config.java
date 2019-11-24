/**
 * IPBOX_receive_config
 */

import java.io.IOException; 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

public class IPBOX_receive_config {

    public static void main(String[] args) {
        try{
            
            ServerSocket servSocket = new ServerSocket(6001);
            System.out.println("receive-config: Server created, waiting for connection");
            Socket link = servSocket.accept();
            
            System.out.println("receive-config: Client accepted, waiting for data");
            boolean fin =false;
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            while(!fin){
                String temp = in.readLine();
                if(!temp.isEmpty()){
                    System.out.println("receive-config: reception of TCP packet " + temp);
                    fin = true;
                    PrintWriter out = new PrintWriter(link.getOutputStream(),true);
                    out.println(temp);
                    System.out.println("receive-config: " + temp +" echoed, closing . . . .");
                }
                
            }
            
            link.close();
            servSocket.close();

        }catch (IOException e){
            System.out.print("Testbox: Error instanciation Socket ");
        }
    }
}