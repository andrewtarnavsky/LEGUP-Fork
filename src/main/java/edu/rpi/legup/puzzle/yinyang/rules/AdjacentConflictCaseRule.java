package edu.rpi.legup.puzzle.yinyang.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.yinyang.YinYangBoard;
import edu.rpi.legup.puzzle.yinyang.YinYangCell;
import edu.rpi.legup.puzzle.yinyang.YinYangType;

import java.util.ArrayList;

public class AdjacentConflictCaseRule extends CaseRule {

    public AdjacentConflictCaseRule() {
        super("adjconflict",
                "Adjacent Color Conflict",
                "Split when an unknown lies between a black and white cell.",
                "edu/rpi/legup/images/yinyang/cases/AdjacentConflict.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        YinYangBoard yyBoard = (YinYangBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(yyBoard, this);
        for (PuzzleElement e : yyBoard.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) e;
            if (cell.getType() == YinYangType.UNKNOWN && isBetweenOpposites(cell, yyBoard)) {
                caseBoard.addPickableElement(cell);
            }
        }
        return caseBoard;
    }

    private boolean isBetweenOpposites(YinYangCell cell, YinYangBoard board) {
        int x = cell.getX(), y = cell.getY();
        YinYangCell up = board.getCell(x, y - 1);
        YinYangCell down = board.getCell(x, y + 1);
        YinYangCell left = board.getCell(x - 1, y);
        YinYangCell right = board.getCell(x + 1, y);
        return (conflict(up, down) || conflict(left, right));
    }

    private boolean conflict(YinYangCell a, YinYangCell b) {
        return a != null && b != null &&
                ((a.getType() == YinYangType.WHITE && b.getType() == YinYangType.BLACK) ||
                        (a.getType() == YinYangType.BLACK && b.getType() == YinYangType.WHITE));
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement e) {
        ArrayList<Board> cases = new ArrayList<>();
        if (e == null) return cases;

        Board whiteCase = board.copy();
        PuzzleElement white = whiteCase.getPuzzleElement(e);
        white.setData(YinYangType.WHITE.toValue());
        whiteCase.addModifiedData(white);
        cases.add(whiteCase);

        Board blackCase = board.copy();
        PuzzleElement black = blackCase.getPuzzleElement(e);
        black.setData(YinYangType.BLACK.toValue());
        blackCase.addModifiedData(black);
        cases.add(blackCase);

        return cases;
    }

    @Override
    public String checkRuleRaw(TreeTransition t) {
        return null; // no extra checks
    }

    @Override
    public String checkRuleRawAt(TreeTransition t, PuzzleElement e) {
        return null;
    }
}
