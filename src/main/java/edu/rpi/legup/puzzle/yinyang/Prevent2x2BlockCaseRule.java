package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.ArrayList;
import java.util.List;

public class Prevent2x2BlockCaseRule extends CaseRule {

    public Prevent2x2BlockCaseRule() {
        super(
                "prevent2x2",
                "Prevent 2x2 Block",
                "If coloring a cell could create a 2x2 block of one color, split the possibilities.",
                "edu/rpi/legup/images/yinyang/cases/Prevent2x2.png"
        );
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> children = transition.getParents().get(0).getChildren();
        if (children.size() != 2) {
            return getInvalidUseOfRuleMessage() + ": This case rule must have 2 children.";
        }

        YinYangCell mod1 = (YinYangCell) children.get(0).getBoard().getModifiedData().iterator().next();
        YinYangCell mod2 = (YinYangCell) children.get(1).getBoard().getModifiedData().iterator().next();

        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return getInvalidUseOfRuleMessage() + ": Both cases must modify the same cell.";
        }

        YinYangType type1 = mod1.getType();
        YinYangType type2 = mod2.getType();

        if (!((type1 == YinYangType.WHITE && type2 == YinYangType.BLACK) ||
                (type1 == YinYangType.BLACK && type2 == YinYangType.WHITE))) {
            return getInvalidUseOfRuleMessage() + ": The cell must be black in one case and white in the other.";
        }

        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        YinYangBoard yinYangBoard = (YinYangBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(yinYangBoard, this);
        yinYangBoard.setModifiable(false);

        for (PuzzleElement element : yinYangBoard.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getType() == YinYangType.UNKNOWN &&
                    couldComplete2x2(cell, yinYangBoard)) {
                caseBoard.addPickableElement(cell);
            }
        }

        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) return cases;

        // Case 1: WHITE
        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(YinYangType.WHITE.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        // Case 2: BLACK
        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(YinYangType.BLACK.toValue());
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    /**
     * Checks whether assigning a color to this cell could potentially complete a 2x2 block.
     */
    private boolean couldComplete2x2(YinYangCell cell, YinYangBoard board) {
        int x = cell.getX();
        int y = cell.getY();

        YinYangCell[][] neighbors = new YinYangCell[][]{
                {board.getCell(x - 1, y), board.getCell(x - 1, y - 1), board.getCell(x, y - 1)},
                {board.getCell(x + 1, y), board.getCell(x + 1, y - 1), board.getCell(x, y - 1)},
                {board.getCell(x + 1, y), board.getCell(x + 1, y + 1), board.getCell(x, y + 1)},
                {board.getCell(x - 1, y), board.getCell(x - 1, y + 1), board.getCell(x, y + 1)},
        };

        for (YinYangCell[] trio : neighbors) {
            if (trio[0] != null && trio[1] != null && trio[2] != null) {
                YinYangType t1 = trio[0].getType();
                YinYangType t2 = trio[1].getType();
                YinYangType t3 = trio[2].getType();

                if (t1 == t2 && t2 == t3 && t1 != YinYangType.UNKNOWN) {
                    return true;
                }
            }
        }

        return false;
    }
}
