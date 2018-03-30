import javafx.util.Pair;

import java.util.ArrayList;

public class AlphaBetaPruning {
    public State alpha_beta_pruning(State state, ArrayList<Pair<Integer, Integer>> future, int depth) {
        ArrayList<Pair<Integer, Integer>> copyFuture = (ArrayList<Pair<Integer, Integer>>)future.clone();

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

            ArrayList<Pair<Integer, Integer>> copyFuture = (ArrayList<Pair<Integer, Integer>>)future.clone();
            State nextState = new State(state);

            int x = future.get(i).getKey();
            int y = future.get(i).getValue();

            if(Game.board.isExist(x, y)) {
                copyFuture.remove(new Pair(x, y));
                continue;
            }

            nextState.setX(x);
            nextState.setY(y);
            nextState.setBoard(x, y, Game.currentPlayer.getId());

            for (int j = 0; j < Game.pos[0].length; ++j) {
                int newX = x + Game.pos[0][j];
                int newY = y + Game.pos[1][j];


                if(Game.board.isValid(newX, newY)) {
                    if (!Game.board.isExist(newX, newY)) {
                        copyFuture.add(new Pair(newX, newY));
                    }
                }
            }

            int temp = min_value(nextState, copyFuture, depth - 1, alpha, beta).getValue();

            if(v < temp) {
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

            ArrayList<Pair<Integer, Integer>> copyFuture = (ArrayList<Pair<Integer, Integer>>)future.clone();
            State nextState = new State(state);

            int x = future.get(i).getKey();
            int y = future.get(i).getValue();

            if(Game.board.isExist(x, y)) {
                copyFuture.remove(new Pair(x, y));
                continue;
            }

            nextState.setX(x);
            nextState.setY(y);
            nextState.setBoard(x, y, Game.currentPlayer.getId());

            for (int j = 0; j < Game.pos[0].length; ++j) {
                int newX = x + Game.pos[0][j];
                int newY = y + Game.pos[1][j];

                if(Game.board.isValid(newX, newY)) {
                    if (!Game.board.isExist(newX, newY)) {
                        copyFuture.add(new Pair(newX, newY));
                    }
                }
            }

            int temp = max_value(nextState, copyFuture, depth - 1, alpha, beta).getValue();

            if(v > temp) {
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



        return utility;
    }
}
