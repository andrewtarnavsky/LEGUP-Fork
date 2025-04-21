package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class YinYangExporter extends PuzzleExporter {

    public YinYangExporter(YinYang puzzle) {
        super(puzzle);
    }

    @Override
    protected Element createBoardElement(Document document) {
        YinYangBoard board;
        if (puzzle.getTree() != null) {
            board = (YinYangBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (YinYangBoard) puzzle.getBoardView().getBoard();
        }

        // Validation: ensure puzzle has at least one white and one black clue
        if (board.getCellsByType(YinYangType.WHITE).isEmpty()
                || board.getCellsByType(YinYangType.BLACK).isEmpty()) {
            throw new IllegalStateException("Cannot save YinYang puzzle: each color must have at least one clue.");
        }
        // Validation: ensure no 2x2 area is filled with the same color
        if (!YinYangUtilities.validateNo2x2Blocks(board)) {
            throw new IllegalStateException("Cannot save YinYang puzzle: invalid 2x2 same-color block detected.");
        }

        Element boardElement = document.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        Element cellsElement = document.createElement("cells");
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                YinYangCell cell = board.getCell(x, y);
                if (cell != null && cell.getType() != YinYangType.UNKNOWN) {
                    Element cellElement = puzzle.getFactory().exportCell(document, cell);
                    cellsElement.appendChild(cellElement);
                }
            }
        }
        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
