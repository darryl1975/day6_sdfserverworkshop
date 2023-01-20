package sg.edu.nus.iss;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        String dirPath = "data2";

        // instantiate a file/directory object
        File newDir = new File(dirPath);

        // if directory exists, print to console 'directory exists' message
        // else create the directory
        if (newDir.exists())
            System.out.println("Directory already exists");
        else
            newDir.mkdir();

        ServerSocket ss;
        try {
            ss = new ServerSocket(12345);
            Socket s = ss.accept(); // establish connection and wait for client to connect

            Thread t = new Thread(new Cookie(s));
            t.start();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
