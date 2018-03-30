import javafx.util.Pair;

import java.util.ArrayList;

public class IterativeDeepeningSearch {
    private AlphaBetaPruning alphaBetaPruning;

    public IterativeDeepeningSearch() {
        alphaBetaPruning = new AlphaBetaPruning();
    }

    public State iterativeDeepeningSearch(State state, ArrayList<Pair<Integer, Integer>> future) {
        State bestState = null;

        for (int i = 1; i <= Game.DEPTH; ++i) {
            System.out.println(i);
            bestState = alphaBetaPruning.alpha_beta_pruning(state, future, i);
        }

        return bestState;
    }
}