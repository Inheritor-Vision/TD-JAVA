import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
class portScanner {
    public static void main (String[] args){
        for (int x = 0; x < Integer.parseInt(args[0]); x++){
            System.out.print("Port number " +Integer.toString(x) + ": ");
            try{
                InetAddress address = InetAddress.getLocalHost();
                Socket client = new Socket(address, x);
                System.out.print("Server listening of localhost\n");
                client.close();
            }catch(IOException e){
                System.out.print("Server NOT listening of localhost\n");
            }
            
        }


    }

}