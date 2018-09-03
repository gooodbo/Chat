import com.network.com.TCPConnection;
import com.network.com.TCPConnectionListner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListner {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    public static void main(String[] args) {

        new ChatServer();
    }

    private ChatServer() {

        System.out.println("Server running... ");

        try (ServerSocket serverSocket = new ServerSocket(8888)) {

            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exeption " + e);
                }
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sentToAll("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sentToAll(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sentToAll("Client disconnected: " + tcpConnection);

    }

    @Override
    public synchronized void onExeption(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exeption " + e);
    }

    private void sentToAll(String value) {
        System.out.println(value);
        final int count = connections.size();

        for (int i = 0; i < count; i++) connections.get(i).sentString(value);


    }
}
