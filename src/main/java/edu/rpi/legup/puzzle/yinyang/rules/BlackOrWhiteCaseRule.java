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
import java.util.List;

public class BlackOrWhiteCaseRule extends CaseRule {

    public BlackOrWhiteCaseRule() {
        super(
                "blackorwhite",
                "Black or White",
                "Each blank cell is either black or white.",
                "edu/rpi/legup/images/yinyang/cases/BlackOrWhite.png"
        );
    }


    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 2 children.";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1 ||
                case2.getBoard().getModifiedData().size() != 1) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
        }

        YinYangCell mod1 = (YinYangCell) case1.getBoard().getModifiedData().iterator().next();
        YinYangCell mod2 = (YinYangCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getType() == YinYangType.WHITE && mod2.getType() == YinYangType.BLACK) ||
                (mod2.getType() == YinYangType.WHITE && mod1.getType() == YinYangType.BLACK))) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify one cell to white and one to black.";
        }

        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        YinYangBoard yinYangBoard = (YinYangBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(yinYangBoard, this);
        yinYangBoard.setModifiable(false);

        for (PuzzleElement element : yinYangBoard.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getType() == YinYangType.UNKNOWN) {
                caseBoard.addPickableElement(cell);
            }
        }

        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        Board whiteBoard = board.copy();
        PuzzleElement whiteCell = whiteBoard.getPuzzleElement(puzzleElement);
        whiteCell.setData(YinYangType.WHITE.toValue());
        whiteBoard.addModifiedData(whiteCell);
        cases.add(whiteBoard);

        Board blackBoard = board.copy();
        PuzzleElement blackCell = blackBoard.getPuzzleElement(puzzleElement);
        blackCell.setData(YinYangType.BLACK.toValue());
        blackBoard.addModifiedData(blackCell);
        cases.add(blackBoard);

        return cases;
    }
}
