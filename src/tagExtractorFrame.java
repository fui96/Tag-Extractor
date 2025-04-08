import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.nio.*;
import java.io.*;

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

        TagArea = new JTextArea(10,10);
        TagArea.setEditable(false);
        TagScrollPane = new JScrollPane(TagArea);

        CenterPanel.add(TagScrollPane, BorderLayout.SOUTH);

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

        });

        BottomPanel.add(GetTag, BorderLayout.CENTER);


    }

    public void getTagSet() throws IOException {
        this.tagMap = new WordHashMap();
        tagMap.compileContent(tagMap.returnTargetText());

        ArrayList<String> words = tagMap.getContent();
        tagSet.clear();

        for(String word : words){
            tagSet.put(word, tagSet.getOrDefault(word, 0) + 1);
        }

        StringBuilder tagText = new StringBuilder();
        tagSet.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .forEach(entry -> {
                        tagText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                });

        TagArea.setText(tagText.toString());

    }

    public void saveTags() throws IOException {
        StringBuilder tagText = new StringBuilder();
        tagSet.entrySet()
                .stream()
                .sorted((a,b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .forEach(entry -> {
                    tagText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                });
        String tagExport = tagText.toString();
        //Create Dialog Prompt
        JOptionPane.showMessageDialog(null,"Choose a file to save to","SaveTags",JOptionPane.PLAIN_MESSAGE);
        //Get Output File
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src"));
        String ExportFile = "";
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ExportFile = chooser.getSelectedFile().toString();
        }
        else{
            JOptionPane.showMessageDialog(null,"No file chosen","SaveTags",JOptionPane.ERROR_MESSAGE);
        }

        try {
            BufferedWriter bw  = new BufferedWriter( new FileWriter(ExportFile));
            bw.write(tagExport);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        tagExtractorFrame frame = new tagExtractorFrame();

    }
}
