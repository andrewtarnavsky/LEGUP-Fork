package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class YinYang extends Puzzle {

    public YinYang() {
        super();
        this.name = "Yin Yang";
        this.importer = new YinYangImporter(this);
        this.exporter = new YinYangExporter(this);
        this.factory = new YinYangCellFactory();
    }

    @Override
    public void initializeView() {
        boardView = new YinYangView((YinYangBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        YinYangBoard board = new YinYangBoard(difficulty, difficulty);

        board.setCell(0, 0, new YinYangCell(YinYangType.WHITE, 0, 0));
        board.setCell(difficulty - 1, difficulty - 1, new YinYangCell(YinYangType.BLACK, difficulty - 1, difficulty - 1));

        return board;
    }

    @Override
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 2 && columns >= 2 && rows == columns;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        YinYangBoard yinYangBoard = (YinYangBoard) board;

        if (yinYangBoard.getPuzzleElements().stream()
                .map(e -> (YinYangCell) e)
                .anyMatch(cell -> cell.getType() == YinYangType.UNKNOWN)) {
            return false;
        }

        return YinYangUtilities.validateNo2x2Blocks(yinYangBoard) &&
                YinYangUtilities.validateConnectivity(yinYangBoard);
    }

    @Override
    public void onBoardChange(Board board) {
        YinYangBoard yinYangBoard = (YinYangBoard) board;

        if (!YinYangUtilities.validateNo2x2Blocks(yinYangBoard)) {
            System.out.println("Warning: Board contains invalid 2x2 blocks.");
        }

        if (!YinYangUtilities.validateConnectivity(yinYangBoard)) {
            System.out.println("Warning: Board contains disconnected groups.");
        }
    }
}
