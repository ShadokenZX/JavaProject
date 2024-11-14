import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {


        private ServerSocket serverSocket;

        public MultiThreadServer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        public void Start() {
            try {
                //Run the loop as long as its open
                //waiting for a client
                while (!serverSocket.isClosed()) {

                    Socket socket = serverSocket.accept();
                    System.out.println("Client has connected on socket" + socket.getLocalAddress().toString());

                    //Have a handler that can run on diff thread
                    //this is so we can have multiple clients connect here
                    // we need it to run on a different thread because
                    // each client needs to listen to a message
                    //that is a blocking operation
                    //That means that the program is stuck waiting for the message,
                    // so we need it to run on a different thread
                    ClientHandler clientHandler = new ClientHandler(socket);

                    Thread thread = new Thread(clientHandler);

                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void safeCloseServerSocket() {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        MultiThreadServer server = new MultiThreadServer(serverSocket);
        server.Start();
    }
}
