import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockClient extends JFrame {
    private JLabel clockLabel;

    public ClockClient() {
        setTitle("Clock");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clockLabel = new JLabel("", SwingConstants.CENTER);
        add(clockLabel, BorderLayout.CENTER);

        setVisible(true);

        connectToServerAndUpdateTime();
    }

    private void connectToServerAndUpdateTime() {
        try {
            Socket socket = new Socket("127.0.0.1", 6789);

            while (true) {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes("time\n");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = bufferedReader.readLine();

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = formatter.parse(response);
                clockLabel.setText(formatter.format(date));

                Thread.sleep(1000);
            }
        } catch (IOException | ParseException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClockClient();
    }
}
