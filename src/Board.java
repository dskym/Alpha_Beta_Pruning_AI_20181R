/*
Board
- 판 상태를 체크
- 오목인지 체크
*/

public class Board {
    private int[][] board = new int[Game.BOARD_SIZE][Game.BOARD_SIZE];

    public Board() {
        for (int i = 0; i < Game.BOARD_SIZE; ++i)
            for (int j = 0; j < Game.BOARD_SIZE; ++j)
                this.board[i][j] = 0;
    }

    public void setBoard(int x, int y, int player) {
        this.board[x][y] = player;
    }

    public boolean isValid(int x, int y) {
        if (x < 0 || x >= Game.BOARD_SIZE || y < 0 || y >= Game.BOARD_SIZE)
            return false;
        else
            return true;
    }

    public void printBoard() {
        for (int i = 0; i < Game.BOARD_SIZE; ++i) {
            for (int j = 0; j < Game.BOARD_SIZE; ++j) {
                if(this.board[i][j] == Game.PLAYER_A)
                    System.out.print('○');
                else if(this.board[i][j] == Game.PLAYER_B)
                    System.out.print('●');
                else
                    System.out.print('□');
            }
            System.out.println();
        }
    }
}
