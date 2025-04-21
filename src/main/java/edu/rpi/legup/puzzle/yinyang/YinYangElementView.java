package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class YinYangElementView extends GridElementView {

    private static final boolean DEBUG_MODE = true; // Toggle this off to hide outlines

    public YinYangElementView(YinYangCell cell) {
        super(cell);
    }

    @Override
    public YinYangCell getPuzzleElement() {
        return (YinYangCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D g2d) {
        YinYangCell cell = (YinYangCell) puzzleElement;
        YinYangType type = cell.getType();

        // Base fill
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.WHITE);
        g2d.fillRect(location.x, location.y, size.width, size.height);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(location.x, location.y, size.width, size.height);

        // Fill circle
        if (type == YinYangType.BLACK || type == YinYangType.WHITE) {
            g2d.setColor(type == YinYangType.BLACK ? Color.BLACK : Color.WHITE);
            int cx = location.x + size.width / 2;
            int cy = location.y + size.height / 2;
            int radius = Math.min(size.width, size.height) / 2 - 4;
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
        }

        // Optional debug overlay — outline cells in distinct colors
        if (DEBUG_MODE) {
            g2d.setColor(type == YinYangType.BLACK ? Color.RED : type == YinYangType.WHITE ? Color.BLUE : Color.GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(location.x + 2, location.y + 2, size.width - 4, size.height - 4);
        }
    }
}