/*
Player
- 돌을 둔다
*/

public class Player {
    private int id;

    public Player(int id) {
        this.id  = id;
    }

    public boolean putStone(int x, int y) {
        if(Game.board.isValid(x, y)) {
            Game.board.setBoard(x, y, id);

            return true;
        }
        else
            return false;
    }
}
