package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class YinYangController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        if (!(data instanceof YinYangCell cell)) {
            throw new IllegalArgumentException("Invalid cell type");
        }

        YinYangBoard board = (YinYangBoard) this.boardView.getBoard();
        int button = e.getButton();
        boolean isControlPressed = e.isControlDown();

        if (button == MouseEvent.BUTTON1) { // Left click
            if (isControlPressed) {
                showSelectionPopup(e);
            } else {
                cycleCellType(board, cell, true);
            }
        } else if (button == MouseEvent.BUTTON3) { // Right click
            cycleCellType(board, cell, false);
        }
        this.boardView.repaint();
    }

    /**
     * Displays the selection popup menu at the mouse event location.
     */
    private void showSelectionPopup(MouseEvent e) {
        this.boardView.getSelectionPopupMenu().show(
                boardView,
                this.boardView.getCanvas().getX() + e.getX(),
                this.boardView.getCanvas().getY() + e.getY()
        );
    }

    /**
     * Cycles the cell type forward or backward, ensuring rule compliance.
     *
     * @param board    The YinYang board.
     * @param cell     The cell to be changed.
     * @param forward  If true, cycles forward; if false, cycles backward.
     */
    private void cycleCellType(YinYangBoard board, YinYangCell cell, boolean forward) {
        YinYangType nextType = switch (cell.getType()) {
            case UNKNOWN -> forward ? YinYangType.WHITE : YinYangType.BLACK;
            case WHITE -> forward ? YinYangType.BLACK : YinYangType.UNKNOWN;
            case BLACK -> forward ? YinYangType.UNKNOWN : YinYangType.WHITE;
        };

        if (canSetType(board, cell, nextType)) {
            cell.setType(nextType);
        }
    }

    /**
     * Checks if setting a cell to a specific type is valid according to the game rules.
     *
     * @param board The game board.
     * @param cell  The cell being modified.
     * @param type  The proposed type change.
     * @return True if the change is valid, false otherwise.
     */
    private boolean canSetType(YinYangBoard board, YinYangCell cell, YinYangType type) {
        YinYangType originalType = cell.getType();
        cell.setType(type);
        boolean isValid = YinYangUtilities.validateNo2x2Blocks(board) &&
                YinYangUtilities.validateConnectivity(board);
        cell.setType(originalType);
        return isValid;
    }
}
