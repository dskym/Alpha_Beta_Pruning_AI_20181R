public class State {
    private int board[][];
    private int x;
    private int y;

    public State() {
        board = new int[Game.BOARD_SIZE][Game.BOARD_SIZE];

        for (int i = 0; i < Game.BOARD_SIZE; ++i) {
            for (int j = 0; j < Game.BOARD_SIZE; ++j) {
                board[i][j] = Game.board.getBoard()[i][j];
            }
        }
    }

    public State(State currentState) {
        board = new int[Game.BOARD_SIZE][Game.BOARD_SIZE];

        for (int i = 0; i < Game.BOARD_SIZE; ++i) {
            for (int j = 0; j < Game.BOARD_SIZE; ++j) {
                board[i][j] = currentState.getBoard()[i][j];
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int x, int y, int player) {
        this.board[x][y] = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}