import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class EchoServer {
    private static List<ClientCtx> clients = new ArrayList<>();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        final int PORT = 7777;
        ServerSocket serverSocket = null;
        try {
            // создаем серверный порт
            serverSocket = new ServerSocket(PORT);
            // в цикле создаем контекты для подключающихся клиентов
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected! ");
                ClientCtx clientCtx = new ClientCtx(socket, clients);
                clientCtx.run(clientCtx);
                clients.add(clientCtx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


