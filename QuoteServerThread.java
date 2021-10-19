import java.io.*;
import java.net.*;
import java.util.*;

public class QuoteServerThread extends Thread {
    private SocketServeur instance;

    public QuoteServerThread(SocketServeur instance) throws IOException {
        this("QuoteServerThread");
        this.instance = instance;
    }

    public QuoteServerThread(String name) throws IOException {
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
