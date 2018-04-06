import javafx.util.Pair;

import java.util.HashSet;

public class IterativeDeepeningSearch {
    private AlphaBetaPruning alphaBetaPruning;

    public IterativeDeepeningSearch() {
        alphaBetaPruning = new AlphaBetaPruning();
    }

    public State iterativeDeepeningSearch(State state, HashSet<Pair<Integer, Integer>> future) {
        Pair<State, Integer> bestPair = null;

        for(int i=0;;++i) {
            Pair<State, Integer> tempPair = alphaBetaPruning.alpha_beta_pruning(state, future, i);

            if(bestPair == null)
                bestPair = tempPair;
            else {
                if (bestPair.getValue() <= tempPair.getValue())
                    bestPair = tempPair;
            }

            if(System.nanoTime() - Game.startTime >= (long)(Game.timeLimit * 800000000)) {
                System.out.println("Time Over");
                break;
            }
        }

        return bestPair.getKey();
    }
}