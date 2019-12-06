import java.net.*;
import java.io.BufferedReader ; 
import java.io.InputStreamReader;
import java.io.*; 
import java.time.LocalDate ;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class UDPCommunicationThread extends Thread
{
    private DatagramSocket socket ; 

    private String msgToSend ; 
    private int rcvPort ; 

    public UDPCommunicationThread(String msg, DatagramSocket socket, int rcvPort)
    {
        super();
        this.socket = socket ;  
        this.msgToSend = msg ; 
        this.rcvPort = rcvPort ; 

        this.start();
    }

    public void run()
    {
        // start communication
        System.out.println("TestBox : UDP Thread started ");
        try
        {
            DatagramPacket outPacket = new DatagramPacket(msgToSend.getBytes(), msgToSend.length(),InetAddress.getLocalHost(),rcvPort); 
            

            boolean bRcvd = false; 
            byte[] buffer = new byte[512] ; 
            DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);

            while(!bRcvd)
            {
                socket.send(outPacket) ; 

                socket.receive(inPacket) ; 

                String rsp = new String(inPacket.getData(),0,inPacket.getLength());

                if(rsp.equals(msgToSend))
                {
                    bRcvd = true ; 
                }
                
            }
            System.out.println("TestBox : UDP echo received ");

        }
        catch(IOException ea)
        {
            System.out.println("failed sending UDP packet");
        }
    }
}


public class TestBox
{


    public static void main(String[] args) {

        Socket LocalSenderSocket ; 
        ServerSocket receiverSocket ; 
        final int LOCAL_RCV_PORT = 1100 ; 
        final int LOCAL_UDP_PORT1 = 1400 ; 
        final int LOCAL_UDP_PORT2 = 1401 ; 
        final int APP_UDP_PORT = 1600 ; 
        final int IFACE_UDP_PORT = 1601 ; 
        final int RCV_CONFIG_PORT = 1200 ; 
                   
        try 
        {
            // Wait for Send-config connection and msg 

            receiverSocket = new ServerSocket(LOCAL_RCV_PORT) ; 
            Socket sendConfigSocket ;
            boolean bIsReady = false ;  
        
            do
            {
                System.out.println("TestBox : Waiting for start ");
                sendConfigSocket = receiverSocket.accept() ; 

                BufferedReader inSendConfig = new BufferedReader(new InputStreamReader(sendConfigSocket.getInputStream()));
                boolean bIsValid = false ; 
                do
                {
                    try
                    {
                        String inputReady = inSendConfig.readLine();

                        bIsValid = inputReady.equals("ready-to-test") ; 
                    }
                    catch(IOException e)
                    {

                    }
                   
                }while(!bIsValid);

                bIsReady = true ; 

            }while(!bIsReady);
            System.out.println("TestBox : ready-to-test received ");

            // Connet to recv_config

            LocalSenderSocket = new Socket(InetAddress.getLocalHost(),RCV_CONFIG_PORT) ; 
            
            // Send "\n" mgs 

            BufferedReader in = new BufferedReader(new InputStreamReader(LocalSenderSocket.getInputStream()));
            PrintWriter out = new PrintWriter(LocalSenderSocket.getOutputStream(),true);

            out.println("\\n");
            System.out.println("TestBox : \\n sent ");

            // Wait for echo 
            String inputEcho = ""; 
            do
            {
                try
                {
                    inputEcho = in.readLine();
                }
                catch(IOException e)
                {

                }
               
            }while(!inputEcho.equals("\\n"));

            System.out.println("TestBox : \\n echo received ");

            // Send UDP Packet to App & iface until echo for both 

            DatagramSocket UDPAppSocket = new DatagramSocket(LOCAL_UDP_PORT1); 
            DatagramSocket UDPIfaceSocket = new DatagramSocket(LOCAL_UDP_PORT2); 

            String msgApp = "Hey App" ; 
            String msgIface = "Hey iface" ; 

            UDPCommunicationThread appThread = new UDPCommunicationThread(msgApp, UDPAppSocket,APP_UDP_PORT) ; 
            UDPCommunicationThread ifaceThread = new UDPCommunicationThread(msgIface, UDPIfaceSocket,IFACE_UDP_PORT) ; 

            try
            {
                appThread.join();
                ifaceThread.join();
            }
            catch(InterruptedException e)
            {
                System.out.println("Failed join");
            }

            System.out.println("TestBox : UDP echoes received  ");


            // Send msg to send-config 
            BufferedReader inSendConfig = new BufferedReader(new InputStreamReader(sendConfigSocket.getInputStream()));
            PrintWriter outSendConfig = new PrintWriter(sendConfigSocket.getOutputStream(),true);

            outSendConfig.println("send-stat"); 

            System.out.println("TestBox : send-stat sent ");

            // wait for IP box msg 

            String input = "" ; 
            do
            {
                
                try
                {
                    input = inSendConfig.readLine();
                }
                catch(IOException e)
                {

                }
               
            }while(!input.equals("list-of-stats"));

            System.out.println("TestBox : list received ");

            // Send exit msg 

            System.out.println("TestBox : exit sent  ");
            outSendConfig.println("exit"); 

            // Close Sockets 

            UDPAppSocket.close();
            UDPIfaceSocket.close();
            receiverSocket.close();

        }
        catch(IOException e)
        {
            System.out.println("failed global TestBox");
        }
        

    }
}   