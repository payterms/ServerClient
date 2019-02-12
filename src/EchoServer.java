import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class EchoServer {
    // ! список д.б. синхронизированным во избежание коллизий при добавлении элементов из разных потоков
    private static List<ClientCtx> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        final int PORT = 7777;
        ServerSocket serverSocket = null;
        try {
            // создаем серверный порт
            serverSocket = new ServerSocket(PORT);

            // отдельный поток для работы с консолью
            new Thread(() -> {
                Scanner consoleInput = new Scanner(System.in);
                while (true) {
                    String msg = consoleInput.nextLine();
                    System.out.println("Message from Console: " + msg);
                    notifyAll(msg);
                }
            }).start();

            // в цикле создаем контекты для подключающихся клиентов
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected! ");
                ClientCtx clientCtx = new ClientCtx(socket);
                clientCtx.run();
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

    public static void notifyAll(String str) {
        for (ClientCtx client: clients){
            try {
                client.getOut().writeUTF(str);
                client.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


