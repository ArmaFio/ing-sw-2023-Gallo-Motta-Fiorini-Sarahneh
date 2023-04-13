package it.polimi.ingsw.server.model.commonGoalCards;

import java.util.ArrayList;
import java.util.Random;

public class CommonBag {
    private final ArrayList<CommonGoalCard> commonGoals = new ArrayList<CommonGoalCard>();
    private final Random indexGen = new Random();
    private int remainingTiles;


    /**
     * {@code CommonBag} constructor.
     */
    public CommonBag() {
        commonGoals.add(new CommonGoalCard_1());
        commonGoals.add(new CommonGoalCard_2());
        commonGoals.add(new CommonGoalCard_3());
        commonGoals.add(new CommonGoalCard_4());
        commonGoals.add(new CommonGoalCard_5());
        commonGoals.add(new CommonGoalCard_6());
        commonGoals.add(new CommonGoalCard_7());
        commonGoals.add(new CommonGoalCard_8());
        commonGoals.add(new CommonGoalCard_9());
        commonGoals.add(new CommonGoalCard_10());
        commonGoals.add(new CommonGoalCard_11());
        commonGoals.add(new CommonGoalCard_12());
    }


    /**
     * Draws two {@code CommonGoalCard} for this game.
     *
     * @return an {@code ArrayList} with the two {@code CommonGoalCard} drawn.
     */
    public ArrayList<CommonGoalCard> draw() {
        ArrayList<CommonGoalCard> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int index = indexGen.nextInt(commonGoals.size());
            CommonGoalCard c = commonGoals.get(index);
            commonGoals.remove(index);
            result.add(c);
        }
        return result;
    }
}
