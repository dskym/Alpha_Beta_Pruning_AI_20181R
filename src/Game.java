/*
Game
- 게임 상태 관리(시간, 턴)
*/

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public static int BOARD_SIZE = 19;
    public static int DEPTH = 5;
    public static int timeLimit;

    public static int BLACK_PLAYER = 1;
    public static int WHITE_PLAYER = 2;

    public static int HUMAN = 1;
    public static int AI = 2;

    public static Board board = new Board();

    public static int[][] pos = new int[][]{{-1, -1, -1, 0, 0, 1, 1, 1},
                                                {-1, 0, 1, -1, 1, -1, 0, 1}};

    public static Player currentPlayer;

    private Scanner scanner;

    private int x;
    private int y;

    private boolean success;

    private Player humanPlayer;
    private Player aiPlayer;

    private ArrayList<Pair<Integer, Integer>> future;

    private IterativeDeepeningSearch iterativeDeepeningSearch;

    public Game() {
        scanner = new Scanner(System.in);

        humanPlayer = new Player();
        aiPlayer = new Player();

        future = new ArrayList();

        iterativeDeepeningSearch = new IterativeDeepeningSearch();
    }

    public void inputTimeLimit() {
        System.out.print("시간 제한 : ");
        Game.timeLimit = scanner.nextInt();
    }

    public void inputFirstPlayer() {
        int player;

        while (true) {
            System.out.print("먼저 시작할 플레이어(Human = 1, AI = 2) : ");
            player = scanner.nextInt();

            if (player == Game.HUMAN) {
                humanPlayer.setId(Game.BLACK_PLAYER);
                aiPlayer.setId(Game.WHITE_PLAYER);

                Game.currentPlayer = humanPlayer;

                break;
            } else if (player == Game.AI) {
                aiPlayer.setId(Game.BLACK_PLAYER);
                humanPlayer.setId(Game.WHITE_PLAYER);

                Game.currentPlayer = aiPlayer;

                break;
            } else
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

        if (currentPlayer == humanPlayer)
            currentPlayer = aiPlayer;
        else if (currentPlayer == aiPlayer)
            currentPlayer = humanPlayer;
    }

    public void play() {
        inputFirstPlayer();
        inputTimeLimit();

        while (true) {
            if (currentPlayer == humanPlayer)
                inputStonePosition();
            else if (currentPlayer == aiPlayer) {
                //  do alpha beta pruning
                if(Game.board.isExist(9, 9)) {
                    for(int i=0;i<future.size();++i) {
                        int x = future.get(i).getKey();
                        int y = future.get(i).getValue();

                        if(Game.board.isExist(x, y))
                            future.remove(new Pair(x, y));
                    }

                    State nextState = iterativeDeepeningSearch.iterativeDeepeningSearch(new State(), future);

                    if (nextState == null)
                        continue;

                    x = nextState.getX();
                    y = nextState.getY();
                }
                else {
                    x = 9;
                    y = 9;
                }
            }

            success = currentPlayer.putStone(x, y);

            if (!success) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            for (int i = 0; i < Game.pos[0].length; ++i) {
                int newX = x + Game.pos[0][i];
                int newY = y + Game.pos[1][i];

                if(Game.board.isValid(newX, newY)) {
                    if (!Game.board.isExist(newX, newY)) {
                        future.add(new Pair(newX, newY));
                    }
                }
            }

            System.out.println(future.size());

            Game.board.printBoard();

            success = Game.board.checkBoard(x, y);

            if (success) {
                currentPlayer.win();
                break;
            }

            changeTurn();
        }
    }
}