import javafx.util.Pair;

import java.util.ArrayList;

public class AlphaBetaPruning {
    public State alpha_beta_pruning(State state, ArrayList<Pair<Integer, Integer>> future, int depth) {
        ArrayList<Pair<Integer, Integer>> copyFuture = (ArrayList<Pair<Integer, Integer>>) future.clone();

        Pair<State, Integer> nextStatePair = max_value(state, copyFuture, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

        return nextStatePair.getKey();
    }

    private Pair<State, Integer> max_value(State state, ArrayList<Pair<Integer, Integer>> future, int depth, int alpha, int beta) {
        if (depth == 0)
            return new Pair(null, estimate_function(state));

        int v = Integer.MIN_VALUE;

        State bestState = null;

        for (int i = 0; i < future.size(); ++i) {
            //새로운 돌을 둔 상태

            ArrayList<Pair<Integer, Integer>> copyFuture = (ArrayList<Pair<Integer, Integer>>) future.clone();
            State nextState = new State(state);

            int x = future.get(i).getKey();
            int y = future.get(i).getValue();

            if (Game.board.isExist(x, y)) {
                copyFuture.remove(new Pair(x, y));
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

    private Pair<State, Integer> min_value(State state, ArrayList<Pair<Integer, Integer>> future, int depth, int alpha, int beta) {
        if (depth == 0)
            return new Pair(null, estimate_function(state));

        int v = Integer.MAX_VALUE;

        State bestState = null;

        for (int i = 0; i < future.size(); ++i) {
            //새로운 돌을 둔 상태

            ArrayList<Pair<Integer, Integer>> copyFuture = (ArrayList<Pair<Integer, Integer>>) future.clone();
            State nextState = new State(state);

            int x = future.get(i).getKey();
            int y = future.get(i).getValue();

            if (Game.board.isExist(x, y)) {
                copyFuture.remove(new Pair(x, y));
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
                        return Integer.MAX_VALUE;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai && board[i][j + 4] == ai) {
                        return Integer.MAX_VALUE;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai && board[i + 4][j + 4] == ai) {
                        return Integer.MAX_VALUE;
                    }
                }

                //상대가 오목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human && board[i + 4][j] == human) {
                        return Integer.MIN_VALUE;
                    }
                }
                if (j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human && board[i][j + 4] == human) {
                        return Integer.MIN_VALUE;
                    }
                }
                if (i < Game.BOARD_SIZE - 4 && j < Game.BOARD_SIZE - 4) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human && board[i + 4][j + 4] == human) {
                        return Integer.MIN_VALUE;
                    }
                }

                //내가 사목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i + 1][j] == ai && board[i + 2][j] == ai && board[i + 3][j] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.9);
                    }
                }
                if (j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i][j + 1] == ai && board[i][j + 2] == ai && board[i][j + 3] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.9);
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai && board[i + 3][j + 3] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.9);
                    }
                }

                //상대가 사목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i + 1][j] == human && board[i + 2][j] == human && board[i + 3][j] == human) {
                        return (int) (Integer.MIN_VALUE * 0.9);
                    }
                }
                if (j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i][j + 1] == human && board[i][j + 2] == human && board[i][j + 3] == human) {
                        return (int) (Integer.MIN_VALUE * 0.9);
                    }
                }
                if (i < Game.BOARD_SIZE - 3 && j < Game.BOARD_SIZE - 3) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human && board[i + 3][j + 3] == human) {
                        return (int) (Integer.MIN_VALUE * 0.9);
                    }
                }

                //내가 삼목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j] == ai && board[i + 2][j] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.8);
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i][j + 1] == ai && board[i][j + 2] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.8);
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai && board[i + 2][j + 2] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.8);
                    }
                }

                //상대가 삼목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j] == human && board[i + 2][j] == human) {
                        return (int) (Integer.MIN_VALUE * 0.8);
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i][j + 1] == human && board[i][j + 2] == human) {
                        return (int) (Integer.MIN_VALUE * 0.8);
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human && board[i + 2][j + 2] == human) {
                        return (int) (Integer.MIN_VALUE * 0.8);
                    }
                }

                //내가 이목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.1);
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i][j + 1] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.1);
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == ai && board[i + 1][j + 1] == ai) {
                        return (int) (Integer.MAX_VALUE * 0.1);
                    }
                }

                //상대가 이목을 만들 수 있는 경우
                if (i < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j] == human) {
                        return (int) (Integer.MIN_VALUE * 0.1);
                    }
                }
                if (j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i][j + 1] == human) {
                        return (int) (Integer.MIN_VALUE * 0.1);
                    }
                }
                if (i < Game.BOARD_SIZE - 2 && j < Game.BOARD_SIZE - 2) {
                    if (board[i][j] == human && board[i + 1][j + 1] == human) {
                        return (int) (Integer.MIN_VALUE * 0.1);
                    }
                }
            }
        }

        return utility;
    }
}
