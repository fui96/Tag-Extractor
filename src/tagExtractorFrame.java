import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class tagExtractorFrame extends javax.swing.JFrame {

    //Fields
    private WordHashMap tagMap;
    private Map<String, Integer> tagSet  = new HashMap<>();
    //Panels
    JPanel MainPanel, BottomPanel, CenterPanel;
    //Buttons
    JButton GetTag,SaveTag;
    //Text Areas
    JTextArea TagArea;
    //Scroll Panes
    JScrollPane TagScrollPane;
    //Labels
    JLabel FileLabel;

    //Constructor
    public tagExtractorFrame(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tag Extractor");
        setSize(800, 600);
        setLocationRelativeTo(null);

        CreateMainPanel();
        add(MainPanel);

        CreateTextPanel();
        MainPanel.add(CenterPanel, BorderLayout.CENTER);

        CreateBottomPanel();
        MainPanel.add(BottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    //Methods

    public void CreateMainPanel(){
        MainPanel = new JPanel();
        MainPanel.setLayout(new BorderLayout());
    }

    public void CreateTextPanel(){
        CenterPanel = new JPanel();
        CenterPanel.setLayout(new BorderLayout());

        FileLabel = new JLabel("FileName");
        FileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        FileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        CenterPanel.add(FileLabel, BorderLayout.NORTH);

        TagArea = new JTextArea(10,10);
        TagArea.setEditable(false);
        TagScrollPane = new JScrollPane(TagArea);

        CenterPanel.add(TagScrollPane, BorderLayout.CENTER);

    }

    public void CreateBottomPanel(){
        BottomPanel = new JPanel();
        BottomPanel.setLayout(new BorderLayout());

        GetTag = new JButton("Get Tags");
        GetTag.addActionListener((ActionEvent ae) -> {
            try {
               getTagSet();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        SaveTag = new JButton("Save Tags");
        SaveTag.addActionListener((ActionEvent ae) -> {
            try {
                saveTags();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });


        BottomPanel.add(GetTag, BorderLayout.CENTER);
        BottomPanel.add(SaveTag, BorderLayout.SOUTH);
    }

    public void getTagSet() throws IOException {
        this.tagMap = new WordHashMap();
        tagMap.compileContent(tagMap.returnTargetText());

        ArrayList<String> words = tagMap.getContent();
        tagSet.clear();

        FileLabel.setText(tagMap.returnTargetText().toString());

        for(String word : words){
            tagSet.put(word, tagSet.getOrDefault(word, 0) + 1);
        }

        StringBuilder tagText = new StringBuilder();
        tagSet.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .forEach(entry -> {
                        System.out.println(entry.getKey() + " " + entry.getValue());
                        tagText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                });

        TagArea.setText(tagText.toString());

    }

    public File getExportFile(){
        String[] options = {"Open Existing File","Create New File"};
        int choice = JOptionPane.showOptionDialog(null,
                "Would you like to open an existing file or create a new one?",
                "File Selection",
                JOptionPane.DEFAULT_OPTION
                ,JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if(choice == 0){
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File selectedFile;
                return selectedFile  = chooser.getSelectedFile();
            }
        }
        else if(choice == 1){
            String fileName = JOptionPane.showInputDialog("Please enter a file name");

            if(fileName != null && !fileName.trim().isEmpty()){
               File file = new File(fileName.trim());

               try {
                   if(file.createNewFile()){
                       JOptionPane.showMessageDialog(null, "File Created");
                       return file;
                   }
                   else{
                       JOptionPane.showMessageDialog(null, "File already exists");
                   }
               }catch(IOException e){
                   JOptionPane.showMessageDialog(null, e + " Issue Creating File");
                   throw new RuntimeException(e);
               }
            }


        }

        return null;
    }


    public void saveTags() throws IOException{
        File exportFile = getExportFile();
        if(exportFile != null){
           StringBuilder sb  = new StringBuilder();
            tagSet.entrySet().stream().sorted((a,b) -> b.getValue().compareTo(a.getValue())).limit(10).forEach(entry -> {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            });
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(exportFile));
                bw.write(sb.toString());
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "No File Selected");
        }
    }



    public static void main(String[] args) {
        tagExtractorFrame frame = new tagExtractorFrame();

    }
}
