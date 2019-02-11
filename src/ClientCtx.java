import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientCtx {

    private DataInputStream inp;
    private DataOutputStream out;
    private Socket socket;

    public ClientCtx(Socket socket) {
        try {
            this.socket = socket;
            this.inp = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void run() {
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

    }

}