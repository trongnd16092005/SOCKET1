import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            Socket connectionSocket = serverSocket.accept();

            AtomicBoolean shouldStop = new AtomicBoolean(false);

            Thread clientInputThread = new Thread(() -> {
                try {
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    String sentence_from_client;
                    while (!shouldStop.get()) {
                        sentence_from_client = inFromClient.readLine();
                        if (sentence_from_client == null || sentence_from_client.equals("stop")) {
                            shouldStop.set(true);
                            connectionSocket.close();
                            break;
                        }
                        System.out.println("Client: " + sentence_from_client);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            });

            clientInputThread.start();

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String sentence_to_client;
            while (!shouldStop.get()) {
                sentence_to_client = inFromUser.readLine();
                outToClient.writeBytes(sentence_to_client + '\n');
                if (sentence_to_client.equals("stop")) {
                    shouldStop.set(true);
                    connectionSocket.close();
                    break;
                }
            }

            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
