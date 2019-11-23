
import java.io.IOException;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 

class ServeurSocket{
    public static void main (String[] args){ 
        try{
            ServerSocket servSocket = new ServerSocket(6666);
            System.out.println(" Waiting for connection");
            Socket link = servSocket.accept();
            
            System.out.println("Client accepted, waiting for data");
            PrintWriter out = new PrintWriter(link.getOutputStream(),true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            out.println(dtf.format(now));
            out.println("stop");
            link.close();
            servSocket.close();

        }catch (IOException e){
            System.out.print("Error instanciation Socket Client");
        }

        

        


    }

}