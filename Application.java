import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class Application(boolean isServer) {
    InputStream inStream = null;
    BufferedInputStream bis = null;

    inStream = new FileInputStream("c:/test.txt");
         
    // input stream is converted to buffered input stream
    bis = new BufferedInputStream(inStream);
    
    // read number of bytes available
    int numByte = bis.available();
    
    //this will write or read the file complety and will pass a pointer to the layer transport.
}
