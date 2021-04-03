import java.io.IOException;

public class Main {
    public static void main(String args[]){
        try {
            System.out.println("Starting server...");
            new ServerThread().start();
            System.out.println("Server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
