import java.net.*;
import java.io.BufferedReader ; 
import java.io.InputStreamReader;
import java.io.*; 
import java.time.LocalDate ;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class UDPThread extends Thread
{

    private int port ; 

    public UDPThread(int port)
    {
        super(); 
        this.port = port ; 

        this.start();
    }

    public void run()
    {

        try
        {   

            DatagramSocket UDPSocket = new DatagramSocket(port); 

            boolean bRcvd = false; 
            byte[] buffer = new byte[512] ; 
            DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);

            UDPSocket.receive(inPacket) ; 

            DatagramPacket outPacket = new DatagramPacket(inPacket.getData(), inPacket.getLength(),InetAddress.getLocalHost(),inPacket.getPort()); 

            UDPSocket.send(outPacket) ; 

        }
        catch(IOException ea)
        {
            System.out.println("failed echoing UDP packet");
        }
    }
}

class RcvInfoThread extends Thread
{

    private final int RCV_CONFIG_PORT = 1200; 

    public RcvInfoThread()
    {
        super(); 
        this.start();
    }

    public void run()
    {

        try
        {   
            ServerSocket rcvConfigSocket = new ServerSocket(RCV_CONFIG_PORT) ; 
            boolean bExit = false ;  
        
            do
            {
                // Wait for connection & \n mgs from TestBox 

                Socket clientSocket = rcvConfigSocket.accept() ; 

                BufferedReader inSendConfig = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                boolean bIsValid = false ; 
                do
                {
                    try
                    {
                        String input = inSendConfig.readLine();

                        bIsValid = input.equals("\\n") ; 
                    }
                    catch(IOException e)
                    {

                    }
                   
                }while(!bIsValid);

                // Echoes msg 

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

                out.println("\\n");

            }while(!bExit);

            rcvConfigSocket.close();

        }
        catch(IOException ea)
        {
            System.out.println("failed echoing \\n packet");
        }
    }
}


public class IPBox
{


    public static void main(String[] args) {

        final int BOX_RCV_PORT = 1100 ; 
        final int APP_UDP_PORT = 1600 ; 
        final int IFACE_UDP_PORT = 1601 ;
        final int SEND_INFO_PORT = 1050;  
                   
        try 
        {
            // Start Rcv_Config, App & Iface thread 
            System.out.println("IPBox : Starting rcvConfig, App & iface");
            RcvInfoThread rcvInfoThread = new RcvInfoThread() ; 
            UDPThread appThread = new UDPThread(APP_UDP_PORT); 
            UDPThread ifaceThread = new UDPThread(IFACE_UDP_PORT);  

            // send ready msg to TestBox 

            Socket boxSocket = new Socket(InetAddress.getLocalHost(),BOX_RCV_PORT) ; 

            BufferedReader inBox = new BufferedReader(new InputStreamReader(boxSocket.getInputStream()));
            PrintWriter outBox = new PrintWriter(boxSocket.getOutputStream(),true) ; 

            outBox.println("ready-to-test");

            System.out.println("IPBox : ready to test sent ");

            // Wait mgs from TestBox 

            boolean bExit = false ; 
            do
            {
                try
                {
                    System.out.println("IPBox : loop in sendConfig ");

                    String inputReady = inBox.readLine();

                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e)
                    {
                        System.out.println("IPBox : Sleep failed ");
                    }

                    

                    if(inputReady.equals("send-stat")) // Sends list of stats if msg == send stat 
                    {
                        System.out.println("IPBox : send-stat received ");

                        outBox.println("list-of-stats") ; 

                        System.out.println("IPBox : list-of-stat sent ");
                    }
                    else if(inputReady.equals("exit")) // close connection if msg == exit 
                    {
                        bExit = true ; 
                        System.out.println("IPBox : Exit received ");
                    }

                    
                     
                }
                catch(IOException e)
                {
                    System.out.println("IPBox : main loop failed ");
                }
               
            }while(!bExit);

            boxSocket.close();

            System.out.println("finished");
            return ; 

        }
        catch(IOException e)
        {
            System.out.println("failed global IPBox");
        }
        

    }
}   