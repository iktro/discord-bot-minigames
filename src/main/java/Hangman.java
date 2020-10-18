import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Random;

public class Hangman {


    Random randomGen = new Random();
    String word;
    String hangmanString;

    public Hangman(){
        word = "";
        hangmanString = "";
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHangmanString() {
        return hangmanString;
    }

    public void setHangmanString(String hangmanString) {
        this.hangmanString = hangmanString;
    }

    public void init() throws IOException {
        Path path = Paths.get("resources/liste_francais.txt");
        File file = new File(path.toString());

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long lineNumber = randomGen.nextInt((int) randomAccessFile.length());
            randomAccessFile.seek(lineNumber);
            randomAccessFile.readLine();

            String word = Normalizer.normalize(randomAccessFile.readLine(), Normalizer.Form.NFD);
            word = word.replaceAll("[^\\p{ASCII}]", "");

            this.setWord(word);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            randomAccessFile.close();
        }
        if(!this.getWord().equals("")){
            hangmanPrinter();
        }
    }

    public void hangmanPrinter(){
        StringBuilder tmp = new StringBuilder();
        char space = ' ';
        char tiret = '-';
        String currentWord = getWord();
        for(int i = 0; i < currentWord.length(); i++){
            if(currentWord.charAt(i) == space || currentWord.charAt(i) == tiret){
                tmp.append(currentWord.charAt(i));
            }
            else{
                tmp.append("#");
            }
        }
        System.out.println(getWord());
        setHangmanString(tmp.toString());
    }

    public void guessLetter(String letter){
        if(getWord().contains(letter)){
            for(int i = 0; i < getWord().length(); i++){
                if(getWord().charAt(i) == letter.charAt(0)){
                    hangmanString = hangmanString.substring(0,i) + letter.charAt(0) + hangmanString.substring(i+1);
                }
            }
        }
    }

    public boolean guessWord(String guessedWord){
        if(guessedWord.equals(word)){
            hangmanString = word;
            return true;
        } else {
            return false;
        }
    }


}
