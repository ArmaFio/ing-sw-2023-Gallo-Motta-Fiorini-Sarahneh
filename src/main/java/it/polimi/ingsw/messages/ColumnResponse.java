package it.polimi.ingsw.messages;

public class ColumnResponse extends Message {
    public final int selectedColumn;

    /**
     * Constructs a {@code ColumnResponse} containing the column selected.
     * Is used to send the server which column the client chose to put his {@code Tile}s.
     *
     * @param column the available selected.
     */
    public ColumnResponse(int column) {
        super(MessageType.COLUMN_RESPONSE);
        this.selectedColumn = column;
    }
}
