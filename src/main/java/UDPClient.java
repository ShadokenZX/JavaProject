import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class UDPClient {

    private static final int PORT = 12345;

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer = new byte[1024];

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public UDPClient(DatagramSocket datagramSocket,InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }


    public void sendMessage()
    {
        //then as long as awe are connected keep seding messages
        while (true)
        {
            try{

                //Get message to send
                BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
                String msgToSend = userReader.readLine();
                buffer = msgToSend.getBytes();

                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length,inetAddress,PORT);
                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void listenForMessages()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromServer;

                while(true)
                {
                    try{
                        DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length);
                        datagramSocket.receive(datagramPacket);

                        msgFromServer = new String(datagramPacket.getData(),0,datagramPacket.getLength());
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

        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        System.out.println("connecting to + " + inetAddress.getHostAddress());

        UDPClient client = new UDPClient(datagramSocket,inetAddress);
        client.listenForMessages();
        client.sendMessage();

    }
}
