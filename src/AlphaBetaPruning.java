public class AlphaBetaPruning {
    public int alpha_beta_pruning(Node state, int depth, int alpha, int beta, int player) {
        state.setUtility(0);

        if(depth == 0 || state.getChildList() == null) {
            return estimate_function(state);
        }

        if(player == Game.BLACK_PLAYER) {
            state.setUtility(Integer.MIN_VALUE);

            for (int i = 0; i < state.getChildNum(); ++i) {
                state.setUtility(Integer.max(state.getUtility(), alpha_beta_pruning(state.getChildList()[i], depth - 1, alpha, beta, Game.WHITE_PLAYER)));
                alpha = Integer.max(alpha, state.getUtility());

                if (beta <= alpha)
                    break;
            }

            return state.getUtility();
        }
        else if(player == Game.WHITE_PLAYER) {
            state.setUtility(Integer.MAX_VALUE);

            for (int i = 0; i < state.getChildNum(); ++i) {
                state.setUtility(Integer.min(state.getUtility(), alpha_beta_pruning(state.getChildList()[i], depth - 1, alpha, beta, Game.WHITE_PLAYER)));
                alpha = Integer.min(beta, state.getUtility());

                if (beta <= alpha)
                    break;
            }

            return state.getUtility();
        }
        else
            return 0;
    }

    public int estimate_function(Node state) {
        return 0;
    }
}
