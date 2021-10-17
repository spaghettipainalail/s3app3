import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Application extends Couche {
    Application(boolean isServer, String FileName) {

        InputStream inStream = null;
        BufferedInputStream bis = null;

        try {
            // inStream = new FileInputStream("c:/test.txt");
            inStream = new FileInputStream(FileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        // input stream is converted to buffered input stream
        bis = new BufferedInputStream(inStream);

        // read number of bytes available
        try {
            int numByte = bis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // this will write or read the file complety and will pass a pointer to the
        // layer transport.
    }
}