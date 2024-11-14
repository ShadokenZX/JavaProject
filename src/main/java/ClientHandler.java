import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    //This array list is to keep track of all clients
    //whenever a client sends a message
    //we can loop through it and send to every client
    //it allows us to broadcast to different server
    //This is static because it needs to belong to the class
    // and not get a new one for each object or instantiation of the class
    private static ArrayList<ClientHandler> handlers = new ArrayList<>();


    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    //Just an extra thing to store the name
    private String clientName;

    public ClientHandler(Socket socket) {
        try {
            //This socket represents the connection sent from the Server to the ClientHandler to connect to the Client
            //So its the connection between all 3
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //We set the first message sent as the name
            this.clientName = bufferedReader.readLine();

            if(clientName == null)
            {
                clientName = " ";
            }

            handlers.add(this);

            sendAll("SERVER :" + clientName + "joined the chat!",true);
        } catch (IOException e) {
            //For safety’s sake
            //lets safely close the socket
            safeCloseSocket(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {

        while(socket.isConnected()) {

            try{
                String messageFromClient = bufferedReader.readLine();

                if(messageFromClient == null || messageFromClient.equalsIgnoreCase("exit"))
                {
                    sendAll(this.clientName + " has left the chat",true);
                    safeCloseSocket(socket,bufferedReader,bufferedWriter);
                    break;
                }

                sendAll(this.clientName + " says " + messageFromClient,false);
            } catch (IOException e) {
                safeCloseSocket(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }


    public void sendAll(String message,boolean exceptThisUser)
    {
        for(ClientHandler handler: handlers)
        {
            try
            {
                if(exceptThisUser || !handler.clientName.equalsIgnoreCase(this.clientName))
                {
                    handler.bufferedWriter.write(message);
                    handler.bufferedWriter.newLine();
                    handler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                safeCloseSocket(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    private void safeCloseSocket(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        handlers.remove(this);
        //sendAll("Server :" + this.clientName + " has left the chat",true);

        try{
            if(bufferedReader != null)
                bufferedReader.close();

            if(bufferedWriter != null)
                bufferedWriter.close();

            if(socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
