package Commons;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AgentAliesSocket {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public AgentAliesSocket(Socket i_socket) {
        socket = i_socket;
        try {
            ois = new ObjectInputStream(i_socket.getInputStream());
            oos = new ObjectOutputStream(i_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getOIS() {
        return ois;
    }

    public ObjectOutputStream getOOS() {
        return oos;
    }


}
