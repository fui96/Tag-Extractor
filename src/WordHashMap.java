import javax.swing.*;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WordHashMap {
    //Fields
    private Set<String> stopWords;
    private File targetText;

    //Constructor
    WordHashMap() throws IOException {
       this.stopWords = loadStopWords("src/English Stop Words.txt");
       this.targetText = getTargetText();
    }

    //
    public File getTargetText() {
        JFileChooser chooser = new JFileChooser();
        String workingDirectory = System.getProperty("user.dir") + "/src";
        chooser.setCurrentDirectory(new File(workingDirectory));
        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            }
            else{
                System.out.println("No file selected");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Set<String> loadStopWords(String filename) throws IOException {
        Set<String> words = new HashSet<String>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            words.add(line.toLowerCase().trim());
        }
        br.close();
      return words;
    }
}
