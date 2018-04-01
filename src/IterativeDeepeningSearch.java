import javafx.util.Pair;

import java.util.HashSet;

public class IterativeDeepeningSearch {
    private AlphaBetaPruning alphaBetaPruning;

    public IterativeDeepeningSearch() {
        alphaBetaPruning = new AlphaBetaPruning();
    }

    public State iterativeDeepeningSearch(State state, HashSet<Pair<Integer, Integer>> future) {
        State bestState = null;

        for (int i = 1; i <= Game.DEPTH; ++i) {
            bestState = alphaBetaPruning.alpha_beta_pruning(state, future, i);
        }

        return bestState;
    }
}