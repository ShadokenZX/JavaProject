import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer {

    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    private DatagramSocket serverSocket;
    private byte[] buffer = new byte[1024];

    public UDPServer(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void receiveAndSend()
    {
        System.out.println("SERVER STARTED");
        while(true)
        {
            try{
                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length);

                //This is a blocking method
                // the server will wait till it receives a message
                //but we dont need multi thread because after we receive we send to all clients
                serverSocket.receive(datagramPacket);

                //when receive a packet
                //we can use this packet to get details about the client
                // and then send to everyone else
                // we need the IP address and also the port number to send it back

                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();

                //casting bytes to string
                // bytes , offset , length
                String messageFromClient = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                System.out.println("Got Message from Client: " + messageFromClient);

                //Send it to everyone else
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length,inetAddress,port);

                serverSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(PORT);
        UDPServer udpServer = new UDPServer(datagramSocket);
        udpServer.receiveAndSend();
    }
}
