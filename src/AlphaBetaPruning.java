import javafx.util.Pair;

import java.util.HashSet;
import java.util.Iterator;

public class AlphaBetaPruning {
    public State alpha_beta_pruning(State state, HashSet<Pair<Integer, Integer>> future, int depth) {
        Pair<State, Integer> nextStatePair = max_value(state, future, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

        return nextStatePair.getKey();
    }

    private Pair<State, Integer> max_value(State state, HashSet<Pair<Integer, Integer>> future, int depth, int alpha, int beta) {
        if (depth == 0)
            return new Pair(null, estimate_function(state));

        int v = Integer.MIN_VALUE;

        State bestState = null;

        Iterator iterator = future.iterator();

        while (iterator.hasNext()) {
            HashSet<Pair<Integer, Integer>> copyFuture = (HashSet<Pair<Integer, Integer>>) future.clone();
            State nextState = new State(state);

            Pair<Integer, Integer> pos = (Pair<Integer, Integer>) iterator.next();

            int x = pos.getKey();
            int y = pos.getValue();

            if (Game.board.isExist(x, y)) {
                iterator.remove();
                continue;
            }

            nextState.setX(x);
            nextState.setY(y);
            nextState.setBoard(x, y, Game.currentPlayer.getId());

            for (int j = 0; j < Game.pos[0].length; ++j) {
                int newX = x + Game.pos[0][j];
                int newY = y + Game.pos[1][j];


                if (Game.board.isValid(newX, newY)) {
                    if (!Game.board.isExist(newX, newY)) {
                        copyFuture.add(new Pair(newX, newY));
                    }
                }
            }

            int temp = min_value(nextState, copyFuture, depth - 1, alpha, beta).getValue();

            if (v < temp) {
                v = temp;
                bestState = nextState;
            }

            if (v >= beta)
                return new Pair(null, v);


            alpha = Integer.max(alpha, v);
        }

        return new Pair(bestState, v);
    }

    private Pair<State, Integer> min_value(State state, HashSet<Pair<Integer, Integer>> future, int depth, int alpha, int beta) {
        if (depth == 0)
            return new Pair(null, estimate_function(state));

        int v = Integer.MAX_VALUE;

        State bestState = null;

        Iterator iterator = future.iterator();

        while(iterator.hasNext()) {
            HashSet<Pair<Integer, Integer>> copyFuture = (HashSet<Pair<Integer, Integer>>) future.clone();
            State nextState = new State(state);

            Pair<Integer, Integer> pos = (Pair<Integer, Integer>)iterator.next();

            int x = pos.getKey();
            int y = pos.getValue();

            if (Game.board.isExist(x, y)) {
                iterator.remove();
                continue;
            }

            nextState.setX(x);
            nextState.setY(y);
            nextState.setBoard(x, y, (Game.currentPlayer.getId() + 1) % 2);

            for (int j = 0; j < Game.pos[0].length; ++j) {
                int newX = x + Game.pos[0][j];
                int newY = y + Game.pos[1][j];

                if (Game.board.isValid(newX, newY)) {
                    if (!Game.board.isExist(newX, newY)) {
                        copyFuture.add(new Pair(newX, newY));
                    }
                }
            }

            int temp = max_value(nextState, copyFuture, depth - 1, alpha, beta).getValue();

            if (v > temp) {
                v = temp;
                bestState = nextState;
            }

            if (v <= alpha)
                return new Pair(null, v);

            beta = Integer.min(beta, v);
        }

        return new Pair(bestState, v);
    }

    private int estimate_function(State state) {
        int utility = 0;

        int[][] board = state.getBoard();

        int ai = Game.currentPlayer.getId();
        int human = (Game.currentPlayer.getId() + 1) % 2;

        for (int i = 0; i < Game.BOARD_SIZE; ++i) {
            for (int j = 0; j < Game.BOARD_SIZE; ++j) {
                //내가 오목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai && board[i + 4][j] == ai) {
                        utility += 5;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai && board[i][j + 4] == ai) {
                        utility += 5;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai && board[i + 4][j + 4] == ai) {
                        utility += 5;
                    }
                }
                if (i > 3 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai && board[i - 3][j + 3] == ai && board[i - 4][j + 4] == ai) {
                        utility += 5;
                    }
                }

                //상대가 오목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human && board[i + 4][j] == human) {
                        utility -= 5;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human && board[i][j + 4] == human) {
                        utility -= 5;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human && board[i + 4][j + 4] == human) {
                        utility -= 5;
                    }
                }
                if (i > 3 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human && board[i - 3][j + 3] == human && board[i - 4][j + 4] == human) {
                        utility -= 5;
                    }
                }

                //내가 사목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai) {
                        utility += 4;
                    }
                }
                if (j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai) {
                        utility += 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai) {
                        utility += 4;
                    }
                }
                if (i > 2 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai && board[i - 3][j + 3] == ai) {
                        utility += 4;
                    }
                }

                //상대가 사목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human) {
                        utility -= 4;
                    }
                }
                if (j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human) {
                        utility -= 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human) {
                        utility -= 4;
                    }
                }
                if (i > 2 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human && board[i - 3][j + 3] == human) {
                        utility -= 4;
                    }
                }

                //내가 삼목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j] == ai && board[i + 2][j] == ai) {
                        utility += 3;
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i][j + 1] == ai && board[i][j + 2] == ai) {
                        utility += 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai) {
                        utility += 3;
                    }
                }
                if (i > 1 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai) {
                        utility += 3;
                    }
                }

                //상대가 삼목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j] == human && board[i + 2][j] == human) {
                        utility -= 3;
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i][j + 1] == human && board[i][j + 2] == human) {
                        utility -= 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human) {
                        utility -= 3;
                    }
                }
                if (i > 1 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human) {
                        utility -= 3;
                    }
                }

                //내가 이목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i + 1][j] == ai) {
                        utility += 2;
                    }
                }
                if (j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i][j + 1] == ai) {
                        utility += 2;
                    }
                }
                if (i < Game.BOARD_SIZE - 1 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai) {
                        utility += 2;
                    }
                }
                if (i > 0 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == ai) {
                        utility += 2;
                    }
                }

                //상대가 이목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i + 1][j] == human) {
                        utility -= 2;
                    }
                }
                if (j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i][j + 1] == human) {
                        utility -= 2;
                    }
                }
                if (i < Game.BOARD_SIZE - 1 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human) {
                        utility -= 2;
                    }
                }
                if (i > 0 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i - 1][j + 1] == human) {
                        utility -= 2;
                    }
                }

                //내 돌 주위에 상대 돌이 4개가 있는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human && board[i + 4][j] == human) {
                        utility += 4;
                    }
                }
                if (i > 3) {
                    if (board[i][j] == ai && board[i - 1][j] == human && board[i - 2][j] == human && board[i - 3][j] == human && board[i - 4][j] == human) {
                        utility += 4;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human && board[i][j + 4] == human) {
                        utility += 4;
                    }
                }
                if (j > 3) {
                    if (board[i][j] == ai && board[i][j - 1] == human && board[i][j - 2] == human && board[i][j - 3] == human && board[i][j - 4] == human) {
                        utility += 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human && board[i + 4][j + 4] == human) {
                        utility += 4;
                    }
                }
                if (i > 3 && j > 3) {
                    if (board[i][j] == ai && board[i - 1][j - 1] == human && board[i - 2][j - 2] == human && board[i - 3][j - 3] == human && board[i - 4][j - 4] == human) {
                        utility += 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j > 3) {
                    if (board[i][j] == ai && board[i + 1][j - 1] == human && board[i + 2][j - 2] == human && board[i + 3][j - 3] == human && board[i + 4][j - 4] == human) {
                        utility += 4;
                    }
                }
                if (i > 3 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human && board[i - 3][j + 3] == human && board[i - 4][j + 4] == human) {
                        utility += 4;
                    }
                }

                //상대 돌 주위에 내 돌이 4개가 있는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai && board[i + 4][j] == ai) {
                        utility -= 4;
                    }
                }
                if (i > 3) {
                    if (board[i][j] == human && board[i - 1][j] == ai && board[i - 2][j] == ai && board[i - 3][j] == ai && board[i - 4][j] == ai) {
                        utility -= 4;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai && board[i][j + 4] == ai) {
                        utility -= 4;
                    }
                }
                if (j > 3) {
                    if (board[i][j] == human && board[i][j - 1] == ai && board[i][j - 2] == ai && board[i][j - 3] == ai && board[i][j - 4] == ai) {
                        utility -= 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai && board[i + 4][j + 4] == ai) {
                        utility -= 4;
                    }
                }
                if (i > 3 && j > 3) {
                    if (board[i][j] == human && board[i - 1][j - 1] == ai && board[i - 2][j - 2] == ai && board[i - 3][j - 3] == ai && board[i - 4][j - 4] == ai) {
                        utility -= 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j > 3) {
                    if (board[i][j] == human && board[i + 1][j - 1] == ai && board[i + 2][j - 2] == ai && board[i + 3][j - 3] == ai && board[i + 4][j - 4] == ai) {
                        utility -= 4;
                    }
                }
                if (i > 3 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai && board[i - 3][j + 3] == ai && board[i - 4][j + 4] == ai) {
                        utility -= 4;
                    }
                }

                //내 돌 주위에 상대 돌이 3개가 있는 경우
                if (i < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human) {
                        utility += 3;
                    }
                }
                if (i > 2) {
                    if (board[i][j] == ai && board[i - 1][j] == human && board[i - 2][j] == human && board[i - 3][j] == human) {
                        utility += 3;
                    }
                }
                if (j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human) {
                        utility += 3;
                    }
                }
                if (j > 2) {
                    if (board[i][j] == ai && board[i][j - 1] == human && board[i][j - 2] == human && board[i][j - 3] == human) {
                        utility += 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human) {
                        utility += 3;
                    }
                }
                if (i > 2 && j > 2) {
                    if (board[i][j] == ai && board[i - 1][j - 1] == human && board[i - 2][j - 2] == human && board[i - 3][j - 3] == human) {
                        utility += 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j > 2) {
                    if (board[i][j] == ai && board[i + 1][j - 1] == human && board[i + 2][j - 2] == human && board[i + 3][j - 3] == human) {
                        utility += 3;
                    }
                }
                if (i > 2 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human && board[i - 3][j + 3] == human) {
                        utility += 3;
                    }
                }

                //상대 돌 주위에 내 돌이 3개가 있는 경우
                if (i < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai) {
                        utility -= 3;
                    }
                }
                if (i > 2) {
                    if (board[i][j] == human && board[i - 1][j] == ai && board[i - 2][j] == ai && board[i - 3][j] == ai) {
                        utility -= 3;
                    }
                }
                if (j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai) {
                        utility -= 3;
                    }
                }
                if (j > 2) {
                    if (board[i][j] == human && board[i][j - 1] == ai && board[i][j - 2] == ai && board[i][j - 3] == ai) {
                        utility -= 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai) {
                        utility -= 3;
                    }
                }
                if (i > 2 && j > 2) {
                    if (board[i][j] == human && board[i - 1][j - 1] == ai && board[i - 2][j - 2] == ai && board[i - 3][j - 3] == ai) {
                        utility -= 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j > 2) {
                    if (board[i][j] == human && board[i + 1][j - 1] == ai && board[i + 2][j - 2] == ai && board[i + 3][j - 3] == ai) {
                        utility -= 3;
                    }
                }
                if (i > 2 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai && board[i - 3][j + 3] == ai) {
                        utility -= 3;
                    }
                }

                //내 돌 주위에 상대 돌이 2개가 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j] == human && board[i + 2][j] == human) {
                        utility += 2;
                    }
                }
                if (i > 1) {
                    if (board[i][j] == ai && board[i - 1][j] == human && board[i - 2][j] == human) {
                        utility += 2;
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i][j + 1] == human && board[i][j + 2] == human) {
                        utility += 2;
                    }
                }
                if (j > 1) {
                    if (board[i][j] == ai && board[i][j - 1] == human && board[i][j - 2] == human) {
                        utility += 2;
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human) {
                        utility += 2;
                    }
                }
                if (i > 1 && j > 1) {
                    if (board[i][j] == ai && board[i - 1][j - 1] == human && board[i - 2][j - 2] == human) {
                        utility += 2;
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j > 1) {
                    if (board[i][j] == ai && board[i + 1][j - 1] == human && board[i + 2][j - 2] == human) {
                        utility += 2;
                    }
                }
                if (i > 1 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human) {
                        utility += 2;
                    }
                }

                //상대 돌 주위에 내 돌이 2개가 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j] == ai && board[i + 2][j] == ai) {
                        utility -= 2;
                    }
                }
                if (i > 1) {
                    if (board[i][j] == human && board[i - 1][j] == ai && board[i - 2][j] == ai) {
                        utility -= 2;
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i][j + 1] == ai && board[i][j + 2] == ai) {
                        utility -= 2;
                    }
                }
                if (j > 1) {
                    if (board[i][j] == human && board[i][j - 1] == ai && board[i][j - 2] == ai) {
                        utility -= 2;
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai) {
                        utility -= 2;
                    }
                }
                if (i > 1 && j > 1) {
                    if (board[i][j] == human && board[i - 1][j - 1] == ai && board[i - 2][j - 2] == ai) {
                        utility -= 2;
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j > 1) {
                    if (board[i][j] == human && board[i + 1][j - 1] == ai && board[i + 2][j - 2] == ai) {
                        utility -= 2;
                    }
                }
                if (i > 1 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai) {
                        utility -= 2;
                    }
                }

                //내 돌 주위에 상대 돌이 1개가 있는 경우
                if (i < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i + 1][j] == human) {
                        utility += 1;
                    }
                }
                if (i > 0) {
                    if (board[i][j] == ai && board[i - 1][j] == human) {
                        utility += 1;
                    }
                }
                if (j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i][j + 1] == human) {
                        utility += 1;
                    }
                }
                if (j > 0) {
                    if (board[i][j] == ai && board[i][j - 1] == human) {
                        utility += 1;
                    }
                }
                if (i < Game.BOARD_SIZE - 1 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == human) {
                        utility += 1;
                    }
                }
                if (i > 0 && j > 0) {
                    if (board[i][j] == ai && board[i - 1][j - 1] == human) {
                        utility += 1;
                    }
                }
                if (i < Game.BOARD_SIZE - 1 && j > 0) {
                    if (board[i][j] == ai && board[i + 1][j - 1] == human) {
                        utility += 1;
                    }
                }
                if (i > 0 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == human) {
                        utility += 1;
                    }
                }

                //상대 돌 주위에 내 돌이 1개가 있는 경우
                if (i < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i + 1][j] == ai) {
                        utility -= 1;
                    }
                }
                if (i > 0) {
                    if (board[i][j] == human && board[i - 1][j] == ai) {
                        utility -= 1;
                    }
                }
                if (j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i][j + 1] == ai) {
                        utility -= 1;
                    }
                }
                if (j > 0) {
                    if (board[i][j] == human && board[i][j - 1] == ai) {
                        utility -= 1;
                    }
                }
                if (i < Game.BOARD_SIZE - 1 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i + 1][j + 1] == ai) {
                        utility -= 1;
                    }
                }
                if (i > 0 && j > 0) {
                    if (board[i][j] == human && board[i - 1][j - 1] == ai) {
                        utility -= 1;
                    }
                }
                if (i < Game.BOARD_SIZE - 1 && j > 0) {
                    if (board[i][j] == human && board[i + 1][j - 1] == ai) {
                        utility -= 1;
                    }
                }
                if (i > 0 && j < Game.BOARD_SIZE - 1) {
                    if (board[i][j] == human && board[i - 1][j + 1] == ai) {
                        utility -= 1;
                    }
                }


                //내 돌이 상대 돌 4개를 감싸는 경우
                if (i < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == ai && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human && board[i + 4][j] == human && board[i + 5][j] == ai) {
                        utility += 4;
                    }
                }
                if (i > 4) {
                    if (board[i][j] == ai && board[i - 1][j] == human && board[i - 2][j] == human && board[i - 3][j] == human && board[i - 4][j] == human && board[i - 5][j] == ai) {
                        utility += 4;
                    }
                }
                if (j < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == ai && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human && board[i][j + 4] == human && board[i][j + 5] == ai) {
                        utility += 4;
                    }
                }
                if (j > 4) {
                    if (board[i][j] == ai && board[i][j - 1] == human && board[i][j - 2] == human && board[i][j - 3] == human && board[i][j - 4] == human && board[i][j - 5] == ai) {
                        utility += 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 5 && j < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human && board[i + 4][j + 4] == human && board[i + 5][j + 5] == ai) {
                        utility += 4;
                    }
                }
                if (i > 4 && j > 4) {
                    if (board[i][j] == ai && board[i - 1][j - 1] == human && board[i - 2][j - 2] == human && board[i - 3][j - 3] == human && board[i - 4][j - 4] == human && board[i - 5][j - 5] == ai) {
                        utility += 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 5 && j > 4) {
                    if (board[i][j] == ai && board[i + 1][j - 1] == human && board[i + 2][j - 2] == human && board[i + 3][j - 3] == human && board[i + 4][j - 4] == human && board[i + 5][j - 5] == ai) {
                        utility += 4;
                    }
                }
                if (i > 4 && j < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human && board[i - 3][j + 3] == human && board[i - 4][j + 4] == human && board[i - 5][j - 5] == ai) {
                        utility += 4;
                    }
                }

                //상대 돌이 내 돌 4개를 감싸는 경우
                if (i < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == human && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai && board[i + 4][j] == ai && board[i + 5][j] == human) {
                        utility -= 4;
                    }
                }
                if (i > 4) {
                    if (board[i][j] == human && board[i - 1][j] == ai && board[i - 2][j] == ai && board[i - 3][j] == ai && board[i - 4][j] == ai && board[i - 5][j] == human) {
                        utility -= 4;
                    }
                }
                if (j < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == human && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai && board[i][j + 4] == ai && board[i][j + 5] == human) {
                        utility -= 4;
                    }
                }
                if (j > 4) {
                    if (board[i][j] == human && board[i][j - 1] == ai && board[i][j - 2] == ai && board[i][j - 3] == ai && board[i][j - 4] == ai && board[i][j - 5] == human) {
                        utility -= 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 5 && j < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == human && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai && board[i + 4][j + 4] == ai && board[i + 5][j + 5] == human) {
                        utility -= 4;
                    }
                }
                if (i > 4 && j > 4) {
                    if (board[i][j] == human && board[i - 1][j - 1] == ai && board[i - 2][j - 2] == ai && board[i - 3][j - 3] == ai && board[i - 4][j - 4] == ai && board[i - 5][j - 5] == human) {
                        utility -= 4;
                    }
                }
                if (i < Game.BOARD_SIZE - 5 && j > 4) {
                    if (board[i][j] == human && board[i + 1][j - 1] == ai && board[i + 2][j - 2] == ai && board[i + 3][j - 3] == ai && board[i + 4][j - 4] == ai && board[i + 5][j - 5] == human) {
                        utility -= 4;
                    }
                }
                if (i > 4 && j < Game.BOARD_SIZE - 5) {
                    if (board[i][j] == human && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai && board[i - 3][j + 3] == ai && board[i - 4][j + 4] == ai && board[i - 5][j + 5] == human) {
                        utility -= 4;
                    }
                }

                //내 돌이 상대 돌 3개를 감싸는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human && board[i + 4][j] == ai) {
                        utility += 3;
                    }
                }
                if (i > 3) {
                    if (board[i][j] == ai && board[i - 1][j] == human && board[i - 2][j] == human && board[i - 3][j] == human && board[i - 4][j] == ai) {
                        utility += 3;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human && board[i][j + 4] == ai) {
                        utility += 3;
                    }
                }
                if (j > 3) {
                    if (board[i][j] == ai && board[i][j - 1] == human && board[i][j - 2] == human && board[i][j - 3] == human && board[i][j - 4] == ai) {
                        utility += 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human && board[i + 4][j + 4] == ai) {
                        utility += 3;
                    }
                }
                if (i > 3 && j > 3) {
                    if (board[i][j] == ai && board[i - 1][j - 1] == human && board[i - 2][j - 2] == human && board[i - 3][j - 3] == human && board[i - 4][j - 4] == ai) {
                        utility += 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j > 3) {
                    if (board[i][j] == ai && board[i + 1][j - 1] == human && board[i + 2][j - 2] == human && board[i + 3][j - 3] == human && board[i + 4][j - 4] == ai) {
                        utility += 3;
                    }
                }
                if (i > 3 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i - 1][j + 1] == human && board[i - 2][j + 2] == human && board[i - 3][j + 3] == human && board[i - 4][j + 4] == ai) {
                        utility += 3;
                    }
                }

                //상대 돌이 내 돌 3개를 감싸는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai && board[i + 4][j] == human) {
                        utility -= 3;
                    }
                }
                if (i > 3) {
                    if (board[i][j] == human && board[i - 1][j] == ai && board[i - 2][j] == ai && board[i - 3][j] == ai && board[i - 4][j] == human) {
                        utility -= 3;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai && board[i][j + 4] == human) {
                        utility -= 3;
                    }
                }
                if (j > 3) {
                    if (board[i][j] == human && board[i][j - 1] == ai && board[i][j - 2] == ai && board[i][j - 3] == ai && board[i][j - 4] == human) {
                        utility -= 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai && board[i + 4][j + 4] == human) {
                        utility -= 3;
                    }
                }
                if (i > 3 && j > 3) {
                    if (board[i][j] == human && board[i - 1][j - 1] == ai && board[i - 2][j - 2] == ai && board[i - 3][j - 3] == ai && board[i - 4][j - 4] == human) {
                        utility -= 3;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j > 3) {
                    if (board[i][j] == human && board[i + 1][j - 1] == ai && board[i + 2][j - 2] == ai && board[i + 3][j - 3] == ai && board[i + 4][j - 4] == human) {
                        utility -= 3;
                    }
                }
                if (i > 3 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i - 1][j + 1] == ai && board[i - 2][j + 2] == ai && board[i - 3][j + 3] == ai && board[i - 4][j + 4] == human) {
                        utility -= 3;
                    }
                }
            }
        }

        return utility;
    }
}
