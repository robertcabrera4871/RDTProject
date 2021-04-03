import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {



    public static void main(String[] args) throws IOException{
        boolean end = false;
        boolean firstRequest = true;

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        if(args.length != 1){
            System.out.println("Please provide server IP Address (Consult report)");
            return;
        }
        while(!end) {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[150];
            InetAddress serverIP = InetAddress.getByName(args[0]);

            if(firstRequest) {
                System.out.println("Enter the file you are requesting");
                String fileRequested = scanner.nextLine();
                buffer = fileRequested.getBytes();
                firstRequest = false;
            }

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverIP, 4445);
            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            if (received.equals("end")) {
                end = true;
                socket.close();
            }
            System.out.println("Received " + received);
        }
    }

}
