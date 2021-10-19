import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private void log(String s, String path) {
        // creer le fichier au besoin
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        // write
        try {
            FileWriter myWriter = new FileWriter(path, true);
            myWriter.append(s).append("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void logClient(String s){
        log(s, "liasonDeDonnesServeur.log");
    }

    public void writeCode(String s, String file){
        log(s, file);
    }
}
