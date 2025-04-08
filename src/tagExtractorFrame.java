import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
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
    JButton GetTag;
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
                        System.out.println(entry.getKey() + " " + entry.getValue());
                        tagText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                });

        TagArea.setText(tagText.toString());

    }

    public static void main(String[] args) {
        tagExtractorFrame frame = new tagExtractorFrame();

    }
}
