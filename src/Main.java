import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]){
        System.out.println("Starting server...");
        try {
            new ServerThread().start();
            System.out.println("Server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
