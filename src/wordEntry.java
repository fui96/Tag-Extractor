public class wordEntry{
    private String word;
    private int count;
    public wordEntry(String word, int count){
        this.word = word;
        this.count = 0;
    }
    public String getWord(){
        return word;
    }
    public int getCount(){
        return count;
    }
    public void incrementCount(){
        count++;
    }
    public String toString(){
        return word + ":\t" + count;
    }
}