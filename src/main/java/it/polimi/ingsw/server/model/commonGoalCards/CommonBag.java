package it.polimi.ingsw.server.model.commonGoalCards;

import java.util.ArrayList;
import java.util.Random;

public class CommonBag {
    private final ArrayList<CommonGoalCard> commonGoals;


    /**
     * {@code CommonBag} constructor.
     */
    public CommonBag(int nPlayers) {
        commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoalCard_1(nPlayers));
        commonGoals.add(new CommonGoalCard_2(nPlayers));
        commonGoals.add(new CommonGoalCard_3(nPlayers));
        commonGoals.add(new CommonGoalCard_4(nPlayers));
        commonGoals.add(new CommonGoalCard_5(nPlayers));
        commonGoals.add(new CommonGoalCard_6(nPlayers));
        commonGoals.add(new CommonGoalCard_7(nPlayers));
        commonGoals.add(new CommonGoalCard_8(nPlayers));
        commonGoals.add(new CommonGoalCard_9(nPlayers));
        commonGoals.add(new CommonGoalCard_10(nPlayers));
        commonGoals.add(new CommonGoalCard_11(nPlayers));
        commonGoals.add(new CommonGoalCard_12(nPlayers));
    }


    /**
     * Draws two {@code CommonGoalCard} for this game.
     *
     * @return an array with the two {@code CommonGoalCard} drawn.
     */
    public CommonGoalCard[] draw() {
        Random indexGen = new Random();
        CommonGoalCard[] result = new CommonGoalCard[2];

        for (int i = 0; i < 2; i++) {
            int index = indexGen.nextInt(commonGoals.size());
            CommonGoalCard c = commonGoals.get(index);
            commonGoals.remove(index);
            result[i] = c;
        }

        return result;
    }
}
