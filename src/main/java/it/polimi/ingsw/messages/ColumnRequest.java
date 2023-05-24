package it.polimi.ingsw.messages;

public class ColumnRequest extends Message {
    private final int[] columnsAvailable;

    /**
     * Constructs a {@code ColumnRequest} containing the available columns where the {@code Tile}s can be placed.
     * Is used to ask the client which columns of the {@code Shelf} he wants to place his {@code Tile}s.
     *
     * @param columns the available columns.
     */
    public ColumnRequest(int[] columns) {
        super(MessageType.COLUMN_REQUEST);
        this.columnsAvailable = columns;
    }

    public int[] getAvailableColumns() {
        return columnsAvailable;
    }
}
