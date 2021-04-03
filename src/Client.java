import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {



    public static void main(String[] args) throws IOException{
        boolean end = false;
        boolean firstRequest = true;
        int ackNo = 0;
        int seqNoReceived = 0;

        File requestedFile = new File("requestedFile.txt");
        if(!requestedFile.exists()){
            requestedFile.createNewFile();
        }
        FileWriter fw = new FileWriter("requestedFile.txt", false);


        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        if(args.length != 1){
            System.out.println("Please provide server IP Address (Consult report)");
            return;
        }
        while(!end) {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[500];
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
            seqNoReceived = Character.getNumericValue(received.charAt(0));
            received = received.substring(1);


            if (received.contains("End of file has been reached")) {
                fw.close();
                end = true;
                socket.close();
            }else{
                fw.write(received + "\n");
            }

             System.out.println("Received " + seqNoReceived);

        }

    }

}
