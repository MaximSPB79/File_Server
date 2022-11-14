package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private final ServerSocket serverSocket;

    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }


    public void start() {
        System.out.println("Server started!");
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                Handler handler = new Handler(this, socket);
                handler.handle();
                handler.commandProcessing();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
