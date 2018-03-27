/*
Player
- 돌을 둔다
*/

public class Player {
    private int id;

    public Player(int id) {
        this.id = id;
    }

    public boolean putStone(int x, int y) {
        if(Game.board.isExist(x, y))
            return false;

        if (Game.board.isValid(x, y)) {
            Game.board.setBoard(x, y, id);

            return true;
        } else
            return false;
    }

    public void win() {
        System.out.println("Player" + id + "가 승리했습니다.");
    }
}
