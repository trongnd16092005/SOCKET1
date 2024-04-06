import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("127.0.0.1", 6789);

            AtomicBoolean shouldStop = new AtomicBoolean(false);

            Thread userInputThread = new Thread(() -> {
                try {
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                    String sentence_to_server;
                    while (!shouldStop.get()) {
                        sentence_to_server = inFromUser.readLine();
                        outToServer.writeBytes(sentence_to_server + '\n');
                        if (sentence_to_server.equals("stop")) {
                            shouldStop.set(true);
                            clientSocket.close();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            userInputThread.start();

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String sentence_from_server;
            while (!shouldStop.get()) {
                sentence_from_server = inFromServer.readLine();
                if (sentence_from_server == null || sentence_from_server.equals("stop")) {
                    shouldStop.set(true);
                    clientSocket.close();
                    break;
                }
                System.out.println("Server: " + sentence_from_server);
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
