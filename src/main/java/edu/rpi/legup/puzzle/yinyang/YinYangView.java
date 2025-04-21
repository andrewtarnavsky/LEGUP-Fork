package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.Point;

public class YinYangView extends GridBoardView {

    public YinYangView(YinYangBoard board, YinYang puzzle) {
        super(new BoardController(), new YinYangController(puzzle), board.getDimension());
        // Instantiate a view for each cell in the board
        for (PuzzleElement element : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            Point loc = cell.getLocation();
            YinYangElementView elementView = new YinYangElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            this.elementViews.add(elementView);
        }
    }
}
