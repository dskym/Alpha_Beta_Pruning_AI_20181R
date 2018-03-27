/*
Game
- 게임 상태 관리(시간, 턴)
*/

import java.util.Scanner;

public class Game {
    public static int BOARD_SIZE = 19;

    public static int BLACK_PLAYER = 1;
    public static int WHITE_PLAYER = 2;

    public static Board board = new Board();

    private Scanner scanner;

    private int timeLimit;

    private int x;
    private int y;

    private boolean success;

    private Player currentPlayer;
    private Player blackPlayer;
    private Player whitePlayer;

    public Game() {
        scanner = new Scanner(System.in);

        blackPlayer = new Player(Game.BLACK_PLAYER);
        whitePlayer = new Player(Game.WHITE_PLAYER);
    }

    public void inputTimeLimit() {
        System.out.print("시간 제한 : ");
        timeLimit = scanner.nextInt();
    }

    public void inputFirstPlayer() {
        int player;

        while(true) {
            System.out.print("먼저 시작할 플레이어(BLACK = 1, WHITE = 2) : ");
            player = scanner.nextInt();

            if (player == Game.BLACK_PLAYER) {
                currentPlayer = blackPlayer;
                break;
            }
            else if (player == Game.WHITE_PLAYER) {
                currentPlayer = whitePlayer;
                break;
            }
            else
                continue;
        }
    }

    public void inputStonePosition() {
        System.out.print("x : ");
        x = scanner.nextInt();

        System.out.print("y : ");
        y = scanner.nextInt();
    }

    public void changeTurn() {
        System.out.println("Turn Change");

        if(currentPlayer == blackPlayer)
            currentPlayer = whitePlayer;
        else if(currentPlayer == whitePlayer)
            currentPlayer = blackPlayer;
    }

    public void play() {
        inputFirstPlayer();
        inputTimeLimit();

        while(true) {
            inputStonePosition();

            success = currentPlayer.putStone(x, y);

            if(!success) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            Game.board.printBoard();

            success = Game.board.checkBoard(x, y);

            if(success) {
                currentPlayer.win();
                break;
            }

            changeTurn();
        }
    }
}
