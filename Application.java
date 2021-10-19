import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Application extends Couche {
    private static Application _instance;

    private Application() {
    }

    public static Application getInstance() {
        if (_instance == null)
           _instance = new Application();
        return _instance;
    }

    public void starter(boolean isServer, String Filename) {

        InputStream inStream = null;
        byte[] stream = null;
        try {
            inStream = new FileInputStream(Filename);
            BufferedInputStream in = new BufferedInputStream(inStream);
            stream = in.readAllBytes();
            in.close();

        } catch (FileNotFoundException e1) {
            System.out.println("file not found !");
        } catch (IOException e) {
            System.out.println("file not readable !");
        }

        // this.handle(new Dataframe(stream, Filename));
        // this will write or read the file complety and will pass a pointer to the
        // layer transport.
    }
}