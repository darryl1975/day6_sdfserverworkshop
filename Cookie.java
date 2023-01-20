package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.OutputStream;

public class Cookie implements Runnable {
    String dirPath = "data2";
    String fileName = "cookie.txt";

    List<String> cookieItems = null;

    final Socket s;

    public Cookie(Socket s) {
        this.s = s;
    }

    public void readCookieFile() throws FileNotFoundException, IOException {
        cookieItems = new ArrayList<String>();

        File file = new File(dirPath + File.separator + fileName);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String readString;

        try {
            while ((readString = br.readLine()) != null) {
                cookieItems.add(readString);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            br.close();
            fr.close();
        }
    }

    public String returnCookie() {
        Random rand = new Random();

        if (cookieItems != null) {
            return cookieItems.get(rand.nextInt(cookieItems.size()));
        } else {
            return "There is no cookie found.";
        }
    }

    public void showCookies() {
        if (cookieItems != null) {
            // cookieItems.forEach(ci -> System.out.println(ci));

            for (String s : cookieItems) {
                System.out.println(s);
            }
        }
    }

    @Override
    public void run() {
        try {

            this.readCookieFile();
            // this.showCookies();

            InputStream is;
            try {
                is = s.getInputStream();

                BufferedInputStream bis = new BufferedInputStream(is);
                DataInputStream dis = new DataInputStream(bis);
                String msgReceived = "";

                try (OutputStream os = s.getOutputStream()) {
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    DataOutputStream dos = new DataOutputStream(bos);

                    while (!msgReceived.equals("close")) {
                        msgReceived = dis.readUTF();

                        if (msgReceived.equalsIgnoreCase("get-cookie")) {
                            String cookieValue = this.returnCookie();
                            System.out.println(cookieValue);

                            dos.writeUTF(cookieValue);
                            dos.flush();
                        }
                    }

                    dos.close();
                    bos.close();
                    os.close();
                } catch (EOFException ex) {
                    ex.printStackTrace();
                }

                bis.close();
                dis.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
