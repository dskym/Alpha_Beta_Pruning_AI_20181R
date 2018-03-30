/*
Player
- 돌을 둔다.
- 승리한다.
*/

public class Player {
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean putStone(int x, int y) {
        if (Game.board.isValid(x, y)) {
            if (Game.board.isExist(x, y))
                return false;

            Game.board.setBoard(x, y, id);

            return true;
        } else
            return false;
    }

    public void win() {
        System.out.println("Player" + id + "가 승리했습니다.");
    }
}
