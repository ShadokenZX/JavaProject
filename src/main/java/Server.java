import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        Socket socket = null;

        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        // this waits for requests to come in the network.
        // Basically it waits for clients to connect on
        // the specified port
        ServerSocket serverSocket = new ServerSocket(8080);


        // We need two while loops
        //this one is so the server stays opens and accepts new connections
       while (true)
       {
           try{

               //Waits for client connection
               //once it gets set we can continue with the code
               socket = serverSocket.accept();

               inputStreamReader = new InputStreamReader(socket.getInputStream());
               outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

               bufferedReader = new BufferedReader(inputStreamReader);
               bufferedWriter = new BufferedWriter(outputStreamWriter);

               //This while loop is so it can communicate with each client
               while(true)
               {
                   String msgFromClient = bufferedReader.readLine();

                   System.out.println("Client sent " + msgFromClient);

                   String outputMsg = "Server got your message - " + msgFromClient;

                   bufferedWriter.write(outputMsg);
                   bufferedWriter.newLine();
                   bufferedWriter.flush();

                   if(msgFromClient.equalsIgnoreCase("exit"))
                       break;

               }

           } catch (IOException e) {
               throw new RuntimeException(e);
           }

       }


    }

}
