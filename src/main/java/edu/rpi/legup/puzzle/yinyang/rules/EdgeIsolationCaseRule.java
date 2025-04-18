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

public class EdgeIsolationCaseRule extends CaseRule {

    public EdgeIsolationCaseRule() {
        super(
                "edgeisolation",
                "Edge Isolation",
                "An edge cell may isolate a group if set to the wrong color.",
                "edu/rpi/legup/images/yinyang/cases/EdgeIsolation.png"
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

        YinYangType t1 = mod1.getType();
        YinYangType t2 = mod2.getType();

        if (!((t1 == YinYangType.BLACK && t2 == YinYangType.WHITE) ||
                (t1 == YinYangType.WHITE && t2 == YinYangType.BLACK))) {
            return getInvalidUseOfRuleMessage() + ": Cell must be black in one case and white in the other.";
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

            if (cell.getType() == YinYangType.UNKNOWN && isEdgeOrCorner(cell, yinYangBoard)) {
                if (hasColorNeighbor(cell, yinYangBoard)) {
                    caseBoard.addPickableElement(cell);
                }
            }
        }

        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) return cases;

        // Case 1: BLACK
        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(YinYangType.BLACK.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        // Case 2: WHITE
        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(YinYangType.WHITE.toValue());
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    private boolean isEdgeOrCorner(YinYangCell cell, YinYangBoard board) {
        int x = cell.getX();
        int y = cell.getY();
        int width = board.getWidth();
        int height = board.getHeight();
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }

    private boolean hasColorNeighbor(YinYangCell cell, YinYangBoard board) {
        int x = cell.getX();
        int y = cell.getY();

        YinYangCell[] neighbors = new YinYangCell[]{
                board.getCell(x - 1, y),
                board.getCell(x + 1, y),
                board.getCell(x, y - 1),
                board.getCell(x, y + 1)
        };

        int colored = 0;
        for (YinYangCell neighbor : neighbors) {
            if (neighbor != null && neighbor.getType() != YinYangType.UNKNOWN) {
                colored++;
            }
        }

        return colored <= 2 && colored >= 1;
    }
}
