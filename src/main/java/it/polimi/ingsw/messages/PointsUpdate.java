package it.polimi.ingsw.messages;

@Deprecated
public class PointsUpdate extends Message {
    private int points;
    private String player;
    private int commonId;

    public PointsUpdate(int points, String player, int commonId) {
        super(MessageType.POINTS);
        this.points = points;
        this.player = player;
        this.commonId = commonId;
    }

    public int getPoints() {
        return points;
    }

    public int getCommonId() {
        return commonId;
    }

    public String getPlayer() {
        return player;
    }
}
