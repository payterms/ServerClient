import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientCtx {
    private Scanner consoleInput;
    private DataInputStream inp;
    private DataOutputStream out;
    private Socket socket;
    private static List<ClientCtx> clients;

    public ClientCtx(Socket socket, List<ClientCtx> clntsList) {
        try {
            this.socket = socket;
            this.consoleInput = new Scanner(System.in);
            this.inp = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.clients = clntsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void run(ClientCtx clientCtx) {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    String msg = inp.readUTF();
                    System.out.println("Message from client: " + msg);
                    //Uncomment for ECHO MODE
                    // out.writeUTF(msg);
                    //out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        // отдельный поток для работы с консолью
        new Thread(() -> {
                while (socket.isConnected()) {
                    String msg = consoleInput.nextLine();
                    System.out.println("Message from Console: " + msg);
                    notify_all(msg);
                }
        }).start();
    }

    public static void notify_all(String str) {
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