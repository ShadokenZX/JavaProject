import java.io.*;
import java.net.Socket;

public class MultiThreadClient {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;


    public MultiThreadClient(Socket socket,String username)
    {
        try
        {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendMessage()
    {
        try{
            //first time we connect we send our username
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();



            //then as long as awe are connected keep seding messages
            while (socket.isConnected())
            {
                BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
                String msgToSend = userReader.readLine();
                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void listenForMessages()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromServer;

                while(socket.isConnected())
                {
                    try{
                        msgFromServer = bufferedReader.readLine();
                        System.out.println(msgFromServer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public void safeClose(Socket socket,BufferedReader bufferedReader,BufferedWriter writer)
    {
        // same as server safe close
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String username = reader.readLine();

        Socket socket = new Socket("localhost",8080);

        MultiThreadClient client = new MultiThreadClient(socket,username);
        client.listenForMessages();
        client.sendMessage();

    }

}
