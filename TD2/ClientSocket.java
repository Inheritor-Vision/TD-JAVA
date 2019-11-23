import java.io.IOException;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientSocket {
    public static void main (String[] args){
        try{
            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address);
            Socket client = new Socket(address, 6666);
            boolean fin =false;
            while(!fin){
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String temp = in.readLine();
                if(!temp.isEmpty()){
                    
                    while(temp != null){
                        if(temp.contains("stop")){
                           fin = true;
                           temp = null;
                        }else{
                            System.out.println(temp);
                            temp = in.readLine();
                        }
                        
                    }
                    
                }
                
            }

            
            client.close();
        }catch(IOException e){
            System.out.println("Error Client Socket");
        }
        
    }

    
}