package it.polimi.ingsw.messages;

public class ColumnResponse extends Message {
    public final int selectedColumn;

    public ColumnResponse(int column) {
        super(MessageType.COLUMN_RESPONSE);
        this.selectedColumn = column;
    }
}
