import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket = null;

    private InputStreamReader inputStreamReader = null;
    private OutputStreamWriter outputStreamWriter = null;

    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;

    public void StartClient()
    {

        BufferedReader userReader = null;

        try{
            socket = new Socket("localhost",8080);

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            while (true)
            {
                //This is a blocking operation so for you to be able to send a message and keep reading
                //it needs to be multi threaded
                userReader = new BufferedReader(new InputStreamReader(System.in));
                String msgToSend = userReader.readLine();

                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                System.out.println(bufferedReader.readLine());

                if(msgToSend.equals("Exit"))
                {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //might need to implemnt this
    public void listenForMessages()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String msgFromServer;

                while (true) {
                    try{
                        msgFromServer = bufferedReader.readLine();
                        System.out.println(msgFromServer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }

    public static void main(String[] args)
    {
        Client client = new Client();
        client.StartClient();

        //Need to set it up so that the user input reader is moved somehwere else
        //Setup client variabless first then start listen thread
    }


}
