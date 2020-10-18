public class TicTacToe {

    public Grid grid;
    public Box[][] boxes;
    public int turn = 0;

    /**
     * Constructor for TicTacToe game
     */
    public TicTacToe(){
        grid = new Grid(3,3);
        boxes = grid.boxes;
        turn = 0;
    }

    /**
     * @param x position
     * @param y position
     */
    public String TicTacToeManager(int x, int y){
        String player;
        TicTacToe ticTacToe = this;
        if(ticTacToe.turn == 9){
            Bot.ticTacToe = null;
            return "EVEN";
        }
        if(ticTacToe.boxes[x][y].content  == "-"){
            if (this.turn % 2 == 0) {
                player = "X";
            } else {
                player = "O";
            }
            ticTacToe.boxes[x][y].content = player;
            ticTacToe.turn++;
            String win = TicTacToeReferee(this);
            if(!win.equals("")){
                TicTacToe ttt = this;
                Bot.ticTacToe = null;
                return ttt + "\t" + win +" PLAYER WON";
            }
            return this.TicTacToePrinter();
        } else{
            return "Box (" + (x+1) +"," + (y+1) + ") already taken";
        }
    }

    public String TicTacToePrinter(){
        String ticTacToeString = " \n 1  |  2  |  3 \n";
        for(int i=0; i< 3; i++){
            ticTacToeString += " " + this.boxes[0][i].content + "  |  " + this.boxes[1][i].content + "  |  " + this.boxes[2][i].content + " " + (i+1) +"\n";
        }
        return ticTacToeString;
    }

    /**
     *
     * @param ticTacToe
     * @return The player who won or empty string if no one won
     */
    public String TicTacToeReferee(TicTacToe ticTacToe){
        String player = "";
        for(int x=0;x<3;x++) {
            if ( !(ticTacToe.boxes[x][0].content.equals("-")) && ticTacToe.boxes[x][0].content.equals(ticTacToe.boxes[x][1].content) && ticTacToe.boxes[x][0].content.equals(ticTacToe.boxes[x][2].content)) {
                return ticTacToe.boxes[x][0].content;
            }
        }
        for(int y=0;y<3;y++) {
            if (!(ticTacToe.boxes[0][y].content.equals("-")) && ticTacToe.boxes[0][y].content.equals(ticTacToe.boxes[1][y].content) && ticTacToe.boxes[2][y].content.equals(ticTacToe.boxes[0][y].content)) {
                return ticTacToe.boxes[0][y].content;
            }
        }

        if (!(ticTacToe.boxes[1][1].content.equals("-")) && (ticTacToe.boxes[0][2].content.equals(ticTacToe.boxes[1][1].content)) && ticTacToe.boxes[2][0].content.equals(ticTacToe.boxes[1][1].content)) {
            return ticTacToe.boxes[1][1].content;
        }
        if (!(ticTacToe.boxes[1][1].content.equals("-")) && ticTacToe.boxes[0][0].content.equals(ticTacToe.boxes[1][1].content) && ticTacToe.boxes[2][2].content.equals(ticTacToe.boxes[0][0].content)) {
            return ticTacToe.boxes[1][1].content;
        }

        return player;
    }
}


class Grid {

    public Box[][] boxes;

    public Grid(int height, int length){
        boxes = new Box[3][3];
        for(int i = 0; i<length; i++) {
            for (int j = 0; j < height; j++) {
                boxes[i][j] = new Box(i, j);
            }
        }
    }
}

/**
 * Constructor for box (x,y) position and content
 */
class Box{

    public String content;
    public int x;
    public int y;

    /**
     * @param xPos is x position
     * @param yPos is y position
     * Create a box with empty content which is "-"
     */
    public Box(int xPos, int yPos){
        x = xPos;
        y = yPos;
        content = "-";
    }
}
