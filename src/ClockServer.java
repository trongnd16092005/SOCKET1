import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket=new ServerSocket(6789);
            Socket socket=serverSocket.accept();

            while (true){
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                String request=bufferedReader.readLine();
                if(request.equals("time")){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                    String time=simpleDateFormat.format(new Date());
                    dataOutputStream.writeBytes(time+'\n');
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
