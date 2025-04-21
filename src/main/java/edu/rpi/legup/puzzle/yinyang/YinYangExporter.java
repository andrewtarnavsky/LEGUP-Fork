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
        // Determine the source of the current board state
        if (puzzle.getTree() != null) {
            board = (YinYangBoard) puzzle.getTree().getRootNode().getBoard();
        } else {
            board = (YinYangBoard) puzzle.getBoardView().getBoard();
        }

        Element boardElement = document.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        // Export each cell in row-major order for consistency
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
