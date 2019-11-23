
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
 
 
public class ServeurSocketMultiProcess{
    public static void main (String[] args) throws IOException{
        if (args[0].contains("Fork")){
            System.out.println("Mode Process");
            ForkSocketFils serveur = new ForkSocketFils(true);
            ForkJoinPool piscine = new ForkJoinPool(5);
            piscine.invoke(serveur);
            while (true){
     
            }
        }else if (args[0].contains("Thread")){
            System.out.println("Mode Thread");
            System.out.println("Serveur Created");
            ServerSocket servSocket = new ServerSocket(6666);
            int n = 1;
            while (true){
                System.out.println("Server waiting for connection");
                Socket link = servSocket.accept();
                System.out.println("Client Accepted");
                ThreadSocketFils temp = new ThreadSocketFils(link, n);
                temp.start();
                n++;
                    
            }
        }else if (args[0].contains("TPool")){
            System.out.println("Mode ThreadPool");
            System.out.println("Serveur Created");
            ServerSocket servSocket = new ServerSocket(6666);
            int n = 1;
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            while (true){
                System.out.println("Server waiting for connection");
                Socket link = servSocket.accept();
                System.out.println("Client Accepted");
                ThreadSocketFils temp = new ThreadSocketFils(link, n);
                executor.execute(temp);
                n++;
                    
            }
        }else{
            System.out.println("usage: ServeurSocketMultiProcess [Thread/Fork/TPool]");
        }
       
    }

    
    
}

class ForkSocketFils extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;
    Socket son;
    boolean Pere;
    int numero;
    ForkSocketFils(boolean daron)throws IOException{
        Pere = daron;
        numero = 0;
        son = null;
        
    }

    ForkSocketFils(boolean daron, Socket srv, int num){
        Pere= daron;
        son = srv;
        numero = num;
    }



    @Override
    protected Integer compute(){
        try{
            if (Pere == true){
                System.out.println("Serveur Created");
                ServerSocket servSocket = new ServerSocket(6666);
                int n = 1;
                while (true){
                    System.out.println("Server waiting for connection");
                    son = servSocket.accept();
                    System.out.println("Client Accepted");
                    ForkSocketFils temp = new ForkSocketFils(false, son, n);
                    temp.fork();
                    n++;
                    
                }
                
            }else {
                System.out.println("Forked successful, child socket " + numero);
                PrintWriter out = new PrintWriter(son.getOutputStream(),true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();  
                out.println(dtf.format(now));
                out.println("stop");
                son.close();
                System.out.println("Data sent, end of child socket " + numero);
            }
            
            

        }catch (IOException e){
            System.out.print("Error process fork");
        }
        return 0;
    }
}

class ThreadSocketFils extends Thread{
    Socket son;
    int n;
    ThreadSocketFils(Socket chassot, int a){
        son = chassot;
        n =a;
    }

    @Override
    public void run(){
        try
        {
            System.out.println("thread successful, child socket " + n);
            PrintWriter out = new PrintWriter(son.getOutputStream(),true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            out.println(dtf.format(now));
            out.println("stop");
            son.close();
            System.out.println("Data sent, end of child socket " + n);
        }catch (IOException e){
            System.out.print("Error thread");
        }

    }
} 