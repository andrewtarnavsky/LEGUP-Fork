package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.event.MouseEvent;

public class YinYangController extends ElementController {
    private YinYang puzzle;

    public YinYangController(YinYang puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        if (!(data instanceof YinYangCell cell)) {
            throw new IllegalArgumentException("Invalid cell type");
        }
        YinYangBoard board = (YinYangBoard) this.boardView.getBoard();
        int button = e.getButton();
        boolean ctrl = e.isControlDown();

        if (button == MouseEvent.BUTTON1) {  // Left click
            if (ctrl) {
                puzzle.pushUndoState();
                showSelectionPopup(e);
            } else {
                puzzle.pushUndoState();
                cycleCellType(board, cell, true);
            }
        } else if (button == MouseEvent.BUTTON3) {  // Right click
            puzzle.pushUndoState();
            cycleCellType(board, cell, false);
        }
        this.boardView.repaint();
    }

    /** Displays the selection popup menu at the mouse event location. */
    private void showSelectionPopup(MouseEvent e) {
        this.boardView.getSelectionPopupMenu().show(
                boardView,
                this.boardView.getCanvas().getX() + e.getX(),
                this.boardView.getCanvas().getY() + e.getY()
        );
    }

    /** Cycles the cell type forward or backward, ensuring rule compliance. */
    private void cycleCellType(YinYangBoard board, YinYangCell cell, boolean forward) {
        YinYangType nextType = switch (cell.getType()) {
            case UNKNOWN -> forward ? YinYangType.WHITE : YinYangType.BLACK;
            case WHITE   -> forward ? YinYangType.BLACK : YinYangType.UNKNOWN;
            case BLACK   -> forward ? YinYangType.UNKNOWN : YinYangType.WHITE;
        };
        if (canSetType(board, cell, nextType)) {
            cell.setType(nextType);
        }
    }

    /** Checks if setting a cell to a specific type is valid according to game rules. */
    private boolean canSetType(YinYangBoard board, YinYangCell cell, YinYangType type) {
        YinYangType originalType = cell.getType();
        cell.setType(type);
        boolean isValid = YinYangUtilities.validateNo2x2Blocks(board) &&
                YinYangUtilities.validateConnectivity(board);
        cell.setType(originalType);
        return isValid;
    }
}
