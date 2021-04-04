import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerThread extends Thread{

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean eof = false;
    boolean firstRequest = true;
    int packetSeq = 1;
    int ackReceived = 1;
    String txtLine = "";

    public ServerThread() throws IOException{
        this("ServerThread");
    }
    public ServerThread(String name) throws IOException{
        super(name);
        socket = new DatagramSocket(4445);
    }

    public void run(){
        while(eof == false){
            try {
            byte[] buf = new byte[500];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            if(firstRequest){
                firstRequest(dataFromBuffer(packet.getData()));
            }
            else{
                String received = dataFromBuffer(packet.getData());
                ackReceived = Integer.parseInt(received);
                if(ackReceived == packetSeq){
                    packetSeq++;
                }
            }

            txtLine = packetSeq + " " + getData();
            buf = txtLine.getBytes();

            InetAddress clientIP = packet.getAddress();
            int port = packet.getPort();

            packet = new DatagramPacket(buf, buf.length, clientIP, port);
            socket.send(packet);
            System.out.println("packet " + packetSeq + " attempting to send");

            sleep(1000);

            } catch (SocketTimeoutException s) {
                System.out.println("Server has timed out");
                System.exit(1);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("An error has occurred");
            }
        }
    }

    public String getData() throws IOException {
        String txtLine = "";
        txtLine = in.readLine();
        if(txtLine == null){
            in.close();
            eof = true;
            txtLine = "End of file has been reached";
        }
        return txtLine;
    }

    public void firstRequest(String filename){
        try {
            in = new BufferedReader(new FileReader(new String(filename)));
            socket.setSoTimeout(5000);
            System.out.println("File requested by client found");
        }catch (Exception e){
            System.err.println("Could not find file");
        }
        firstRequest = false;
    }

    public String dataFromBuffer(byte[] buffer){
        ArrayList<Byte> dataList = new ArrayList<>();
        for(byte b: buffer){
            if(b != 0){
                dataList.add(b);
            }
        }
        byte[] dataArray = new byte[dataList.size()];
        for(int i = 0; i < dataArray.length; i++){
            dataArray[i] = dataList.get(i).byteValue();
        }

        return new String(dataArray, StandardCharsets.UTF_8);
    }

}
