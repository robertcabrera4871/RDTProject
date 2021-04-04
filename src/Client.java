import Utils.extractNumbers;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

static int bufferSize = 500;

    public static void main(String[] args) throws IOException{
        boolean end = false;
        boolean firstRequest = true;
        int ackNo = 1;
        int seqNoReceived;

        File requestedFile = new File("requestedFile.txt");
        if(!requestedFile.exists()){
            requestedFile.createNewFile();
        }
        FileWriter fw = new FileWriter("requestedFile.txt", false);


        if(args.length != 1){
            System.out.println("Please provide server IP Address (Consult report)");
            return;
        }

        while(!end) {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[bufferSize];
            InetAddress serverIP = InetAddress.getByName(args[0]);

            if(firstRequest) {
                Scanner scanner = new Scanner(new InputStreamReader(System.in));
                System.out.println("Enter the file you are requesting");
                String fileRequested = scanner.nextLine();
                buffer = fillBuffer(fileRequested.getBytes());
                firstRequest = false;
            }
            else {
                String ackToByte = String.valueOf(ackNo);
                buffer = fillBuffer(ackToByte.getBytes());
            }

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverIP, 4445);
            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            seqNoReceived = extractNumbers.extract(received);
            ackNo = seqNoReceived;
            // +1 for whitespace
            int seqNoLength = String.valueOf(seqNoReceived).length() + 1;
            received = received.substring(seqNoLength);


            if (received.contains("End of file has been reached")) {
                fw.close();
                end = true;
                socket.close();
            }else{
                fw.write(received + "\n");
            }

           System.out.println("Sending ack " + ackNo);


        }

    }

    public static byte[] fillBuffer(byte[] data){
        int i = 0;
        byte[] buffer = new byte[bufferSize];
        while( i < data.length){
            buffer[i] = data[i];
            i++;
        }
        return buffer;
    }



}
