import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerThread extends Thread{

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean eof = false;
    boolean firstRequest = true;
    int packetSeq = 0;

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
            if(firstRequest){ firstRequest(packet);}

            String txtLine = packetSeq + " " + getData();
            buf = txtLine.getBytes();

            InetAddress clientIP = packet.getAddress();
            int port = packet.getPort();

            packet = new DatagramPacket(buf, buf.length, clientIP, port);
            socket.send(packet);
            System.out.println("packet " + packetSeq++ + " attempting to send");

            } catch (Exception e) {
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

    public void firstRequest(DatagramPacket packet){
        try {
            in = new BufferedReader(new FileReader(new String(packet.getData(), 0, packet.getLength())));
            System.out.println("File requested by client found");
        }catch (Exception e){
            System.err.println("Could not find file");
        }
        firstRequest = false;
    }

}
