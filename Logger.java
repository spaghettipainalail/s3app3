import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private void log(String s, String path) {
        // creer le fichier au besoin
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
            } else {
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        // write
        try {
            FileWriter myWriter = new FileWriter(path, true);
            myWriter.append(s).append("\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logClient(String s){
    }

    public void logServer(String s){
        // log(s, "liasonDeDonnesClient.log");
    }

}
