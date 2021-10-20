import java.io.*;
import java.net.*;

public class ServeurThread extends Thread {
    private CouchePhysiqueServeur instance;

    public ServeurThread(CouchePhysiqueServeur instance) throws IOException {
        this("QuoteServerThread");
        this.instance = instance;
    }

    public ServeurThread(String name) throws IOException {
        super(name);
    }

    public void run() {
        try {
            instance.starterEcouter();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}
