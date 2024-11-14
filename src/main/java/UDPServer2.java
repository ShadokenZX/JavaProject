import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer2 {

    //ncat --udp localhost 12345

    private static final int PORT = 12345;
    private static final int BUFFER_SIZE = 1024;

    // Thread-safe set to keep track of clients
    private Set<SocketAddress> clients = ConcurrentHashMap.newKeySet();

    byte[] buffer = new byte[1024];

    public static void main(String[] args) {
        new UDPServer2().start();
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP Chat Server started on port " + PORT);

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                // Receive a packet from a client
                DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                SocketAddress clientAddress = packet.getSocketAddress();

                System.out.println("Received message from " + clientAddress + ": " + message);

                // Add the client to the set if it's a new client
                clients.add(clientAddress);

                // Broadcast the message to all other clients
                broadcastMessage(message, clientAddress, socket);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void broadcastMessage(String message, SocketAddress senderAddress, DatagramSocket socket) {
        for (SocketAddress clientAddress : clients) {
            // Skip the sender
            if (clientAddress.equals(senderAddress)) {
                continue;
            }

            try {
                buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientAddress);
                socket.send(packet);
            } catch (Exception e) {
                System.err.println("Error sending message to " + clientAddress + ": " + e.getMessage());
            }
        }
    }
}
