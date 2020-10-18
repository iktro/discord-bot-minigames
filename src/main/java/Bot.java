import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Bot extends ListenerAdapter{

    static TicTacToe ticTacToe;
    static Hangman hangman;

    public static void main(String[] args) throws LoginException {
        ticTacToe = null;
        hangman = null;
        String token = null;
        try {
            File tokenFile = Paths.get("resources/token.txt").toFile();
            if (!tokenFile.exists()) {
                System.out.println("[ERROR] Could not find token.txt file");
                System.out.print("Please paste in your bot token: ");
                Scanner s = new Scanner(System.in);
                token = s.nextLine();
                System.out.println();
                System.out.println("[INFO] Creating token.txt - please wait");
                if (!tokenFile.createNewFile()) {
                    System.out.println(
                            "[ERROR] Could not create token.txt - please create this file and paste in your token"
                                    + ".");
                    s.close();
                    return;
                }
                Files.write(tokenFile.toPath(), token.getBytes());
                s.close();
            }
            token = new String(Files.readAllBytes(tokenFile.toPath()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.addEventListeners(new Bot());
        builder.build();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(event.getMessage().getContentRaw().equals("_help")){
            event.getChannel().
                    sendMessage("Use _play <game> to launch a game. Now available : \n " +
                            "tictactoe or hangman \n" +
                            "Use _ stop <game> to stop the current game \n" +
                            "For tictactoe, use _ttt <x> <y> \n" +
                            "For hangman, use _guess <word> to guess the word \n" +
                            "or _guess <letter> to guess a letter").queue();
        }

        if(event.getMessage().getContentRaw().equals("_play tictactoe")) {
            playTictacToe(event);
        }

        if(event.getMessage().getContentRaw().startsWith("_ttt ")) {
            turnTicTacToe(event);
        }

        if(event.getMessage().getContentRaw().equals("_stop tictactoe")){
            ticTacToe = null;
            event.getChannel().sendMessage("Game stopped").queue();
        }

        if(event.getMessage().getContentRaw().equals("_play hangman")) {
            launchHangman(event);
        }

        if(event.getMessage().getContentRaw().startsWith("_guess ")){
            guessHangman(event);
        }

        if(event.getMessage().getContentRaw().equals("_stop hangman")) {
            hangman = null;
            event.getChannel().sendMessage("Game stopped").queue();
        }
    }

    private void guessHangman(@Nonnull MessageReceivedEvent event) {
        if(hangman != null) {
            String[] parsedSet = event.getMessage().getContentRaw().split(" ");
            if (parsedSet.length == 2) {
                if (parsedSet[1].length() == 1) {
                    hangman.guessLetter(parsedSet[1]);
                    String guessing = hangman.getHangmanString();
                    event.getChannel().sendMessage(guessing).queue();
                } else {
                    boolean result = hangman.guessWord(parsedSet[1]);
                    if(result){
                        String guessing = hangman.getHangmanString();
                        event.getChannel().sendMessage(event.getAuthor() + " WON. The word were : " + guessing).queue();
                        hangman = null;
                    }
                    else{
                        event.getChannel().sendMessage(event.getAuthor().getName() + " wrong guess : " + parsedSet[1]).queue();
                    }
                }
            } else {
                event.getChannel().sendMessage("Use _guess <your letter> or _guess <your guessed word> \n " +
                        "No space in your word").queue();
            }
        }
    }

    private void launchHangman(@Nonnull MessageReceivedEvent event) {
        try {
            hangman = new Hangman();
            hangman.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String guessing = hangman.getHangmanString();
        event.getChannel().sendMessage(guessing).queue();
    }

    private void turnTicTacToe(@Nonnull MessageReceivedEvent event) {
        if(ticTacToe != null){
            String[] parsedSet = event.getMessage().getContentRaw().split(" ");
            if (parsedSet.length == 3) {
                if ((parsedSet[1].chars().allMatch(Character::isDigit) && parsedSet[1].length() == 1) && (parsedSet[2].chars().allMatch(Character::isDigit) && parsedSet[2].length() == 1)) {
                    int x = Integer.parseInt(parsedSet[1]);
                    int y = Integer.parseInt(parsedSet[2]);

                    event.getChannel().sendMessage(ticTacToe.TicTacToeManager(x - 1, y - 1)).queue();
                }
            }
        }
    }

    private void playTictacToe(@Nonnull MessageReceivedEvent event) {
        if (ticTacToe == null) {
            ticTacToe = new TicTacToe();
            event.getChannel().sendMessage(
                    "Use _ttt x y to play \n" + ticTacToe.TicTacToePrinter()).queue();
        } else {
            event.getChannel().sendMessage("Game already started, use _stop tictactoe to cancel the game").queue();
        }
    }
}