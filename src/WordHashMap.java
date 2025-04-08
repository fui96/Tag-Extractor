import javax.swing.*;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WordHashMap {
    //Fields
    private Set<String> stopWords;
    private File targetText;
    private ArrayList<String> content;

    //Constructor
    WordHashMap() throws IOException {
       this.stopWords = loadStopWords();
       this.targetText = getTargetText();
       this.content = new ArrayList<>();
    }

    //
    public File returnTargetText() {
        return this.targetText;
    }

    public File getTargetText() {
        JFileChooser chooser = new JFileChooser();
        String workingDirectory = System.getProperty("user.dir") + "/src";
        chooser.setCurrentDirectory(new File(workingDirectory));

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        else{
            JOptionPane.showMessageDialog(null, "No file selected, Program now closing");
            System.exit(0);
            return null;
        }
    }

    public void compileContent(File targetText) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(targetText));
        String line;
        while((line = br.readLine()) != null){
            String[] words = line.toLowerCase().split("\\W+");
            for(String word : words){
                if(!stopWords.contains(word) && !word.isEmpty() && word.length() > 1){
                    word = word.toLowerCase();
                    content.add(word);
                }
            }
        }
        br.close();
    }

    public ArrayList<String> getContent() {
        return content;
    }


    public Set<String> loadStopWords() throws IOException {


        Set<String> words = new HashSet<String>();
        BufferedReader br = new BufferedReader(new FileReader("src/English Stop Words.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            words.add(line.toLowerCase().trim());
        }
        br.close();
      return words;
    }

}
