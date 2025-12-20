package lisval.service2.haproxy;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class HaproxySocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9999;

    private HaproxySocketClient() {
    }

    public static void send(String command) throws Exception {
        try (Socket socket = new Socket(HOST, PORT);
             OutputStream os = socket.getOutputStream()) {

            os.write((command + "\n").getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
