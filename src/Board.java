/*
Board
- 유효 값 체크
- 오목 체크
- 현재 판 출력
*/

public class Board {
    private int[][] board;

    public Board() {
        board = new int[Game.BOARD_SIZE][Game.BOARD_SIZE];

        for (int i = 0; i < Game.BOARD_SIZE; ++i)
            for (int j = 0; j < Game.BOARD_SIZE; ++j)
                this.board[i][j] = 0;
    }

    public void setBoard(int x, int y, int player) {
        this.board[x][y] = player;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isValid(int x, int y) {
        if (x < 0 || x >= Game.BOARD_SIZE || y < 0 || y >= Game.BOARD_SIZE)
            return false;
        else
            return true;
    }

    public boolean isExist(int x, int y) {
        if (board[x][y] != 0)
            return true;
        else
            return false;
    }

    public boolean checkBoard(int x, int y) {
        if (!isValid(x, y))
            return false;

        int color = this.board[x][y];

        //가로 체크
        int _x = x;
        int _y = y;
        int count = 0;

        while (true) {
            --_y;

            if (_y < 0) {
                _y = 0;
                break;
            }

            if (isValid(_x, _y)) {
                if (board[_x][_y] != color) {
                    ++_y;
                    break;
                }
            } else
                break;
        }

        for (int i = 0; i < 5; ++i) {
            if (isValid(_x, _y + i)) {
                if (board[_x][_y + i] == color)
                    count++;
                else
                    break;
            } else
                break;
        }

        if (count == 5)
            return true;

        //세로 체크
        _x = x;
        _y = y;
        count = 0;

        while (true) {
            --_x;

            if (_x < 0) {
                _x = 0;
                break;
            }

            if (isValid(_x, _y)) {
                if (board[_x][_y] != color) {
                    ++_x;
                    break;
                }
            } else
                break;
        }

        for (int i = 0; i < 5; ++i) {
            if (isValid(_x + i, _y)) {
                if (board[_x + i][_y] == color)
                    count++;
                else
                    break;
            } else
                break;
        }

        if (count == 5)
            return true;

        //우하향 대각선 체크
        _x = x;
        _y = y;
        count = 0;

        while (true) {
            --_x;

            if (_x < 0) {
                _x = 0;
                break;
            }

            --_y;

            if (_y < 0) {
                ++_x;
                _y = 0;
                break;
            }

            if (isValid(_x, _y)) {
                if (board[_x][_y] != color) {
                    ++_x;
                    ++_y;
                    break;
                }
            } else
                break;
        }

        for (int i = 0; i < 5; ++i) {
            if (isValid(_x + i, _y + i)) {
                if (board[_x + i][_y + i] == color)
                    count++;
                else
                    break;
            } else
                break;
        }

        if (count == 5)
            return true;

        //우상향 대각선 체크
        _x = x;
        _y = y;
        count = 0;

        while (true) {
            ++_x;

            if (_x >= Game.BOARD_SIZE) {
                _x = Game.BOARD_SIZE - 1;
                break;
            }

            --_y;

            if (_y < 0) {
                --_x;
                _y = 0;
                break;
            }

            if (isValid(_x, _y)) {
                if (board[_x][_y] != color) {
                    --_x;
                    ++_y;
                    break;
                }
            } else
                break;
        }

        for (int i = 0; i < 5; ++i) {
            if (isValid(_x - i, _y + i)) {
                if (board[_x - i][_y + i] == color)
                    count++;
                else
                    break;
            } else
                break;
        }

        if (count == 5)
            return true;

        return false;
    }

    public void printBoard() {
        System.out.println("   0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18");
        for (int i = 0; i < Game.BOARD_SIZE; ++i) {
            System.out.printf("%2d", i);
            for (int j = 0; j < Game.BOARD_SIZE; ++j) {
                if (this.board[i][j] == Game.WHITE_PLAYER)
                    System.out.printf("%2c", '○');
                else if (this.board[i][j] == Game.BLACK_PLAYER)
                    System.out.printf("%2c", '●');
                else
                    System.out.printf("%2c", '※');
            }
            System.out.println();
        }
    }
}
