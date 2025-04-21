package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import java.util.ArrayDeque;
import java.util.Deque;

public class YinYang extends Puzzle {
    private Deque<Board> undoStack = new ArrayDeque<>();
    private Deque<Board> redoStack = new ArrayDeque<>();

    public YinYang() {
        super();
        this.name = "Yin Yang";
        this.importer = new YinYangImporter(this);
        this.exporter = new YinYangExporter(this);
        this.factory = new YinYangCellFactory();
    }

    @Override
    public void initializeView() {
        boardView = new YinYangView((YinYangBoard) currentBoard, this);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    /** Records the current board state for undo history. */
    public void pushUndoState() {
        // Make a deep copy of the current board and push to undo stack
        undoStack.push(currentBoard.copy());
        // Once a new action is taken, previous redo history is cleared
        redoStack.clear();
    }

    /** Reverts the last edit, if possible. */
    public void undo() {
        if (!undoStack.isEmpty()) {
            // Save current state to redo stack, then restore last undo state
            redoStack.push(currentBoard.copy());
            Board prevBoard = undoStack.pop();
            this.currentBoard = prevBoard;
            initializeView();  // refresh the view with the restored board
        }
    }

    /** Reapplies an undone edit, if possible. */
    public void redo() {
        if (!redoStack.isEmpty()) {
            // Save current state to undo stack, then restore last redo state
            undoStack.push(currentBoard.copy());
            Board nextBoard = redoStack.pop();
            this.currentBoard = nextBoard;
            initializeView();  // refresh the view with the redone board
        }
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        YinYangBoard board = new YinYangBoard(difficulty, difficulty);
        board.setCell(0, 0, new YinYangCell(YinYangType.WHITE, 0, 0));
        board.setCell(difficulty - 1, difficulty - 1,
                new YinYangCell(YinYangType.BLACK, difficulty - 1, difficulty - 1));
        return board;
    }

    @Override
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 2 && columns >= 2 && rows == columns;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        YinYangBoard yinYangBoard = (YinYangBoard) board;
        // Puzzle complete only if no cells remain unknown and rules are satisfied
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
