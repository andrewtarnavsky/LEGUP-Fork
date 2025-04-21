package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.model.elements.PlaceableElement;
import java.awt.event.MouseEvent;

public class YinYangCell extends GridCell<YinYangType> {

    public YinYangCell(YinYangType type, int x, int y) {
        super(type, x, y);
    }

    /** Gets the type of the cell. */
    public YinYangType getType() {
        return getData();
    }

    /** Sets the type of the cell. */
    public void setType(YinYangType type) {
        setData(type);
    }

    /**
     * Sets the cell's type based on a selected puzzle editor element.
     * Allows placement of black/white circles or clearing to unknown.
     */
    public void setType(PlaceableElement element, MouseEvent e) {
        String name = element.getElementName();
        if ("Black Circle".equals(name)) {
            this.setType(YinYangType.BLACK);
        } else if ("White Circle".equals(name)) {
            this.setType(YinYangType.WHITE);
        } else {
            // Any other selection clears the cell to unknown
            this.setType(YinYangType.UNKNOWN);
        }
    }

    /** Gets the X coordinate of the cell. */
    public int getX() {
        return location.x;
    }

    /** Gets the Y coordinate of the cell. */
    public int getY() {
        return location.y;
    }

    /** Performs a deep copy of the YinYangCell. */
    @Override
    public YinYangCell copy() {
        return new YinYangCell(getType(), location.x, location.y);
    }
}
