package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SudokuImporter extends PuzzleImporter {
    public SudokuImporter(@NotNull Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    @Contract(pure = true, value = "-> true")
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    @Override
    @Contract(pure = true, value = "-> false")
    public boolean acceptsTextInput() {
        return false;
    }

    /**
     * Creates an empty board for building
     *
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     * @throws RuntimeException if board can not be created
     */
    @Override
    @Contract (pure = false)
    public void initializeBoard(int rows, int columns) {
        SudokuBoard sudokuBoard;
        int minorSize = (int) Math.sqrt(rows);
        sudokuBoard = new SudokuBoard(rows);

        for (int y = 0; y < columns; y++) {
            for (int x = 0; x < rows; x++) {
                if (sudokuBoard.getCell(x, y) == null) {
                    int groupIndex = x / minorSize + y / minorSize * minorSize;
                    SudokuCell cell = new SudokuCell(0, new Point(x, y), groupIndex, rows);
                    cell.setIndex(y * rows + x);
                    cell.setModifiable(true);
                    sudokuBoard.setCell(x, y, cell);
                }
            }
        }

        puzzle.setCurrentBoard(sudokuBoard);
    }

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    @Contract(pure = false)
    public void initializeBoard(@NotNull Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException(
                        "Sudoku Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "Sudoku Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            SudokuBoard sudokuBoard;
            int size;
            int minorSize;
            if (!boardElement.getAttribute("size").isEmpty()) {
                size = Integer.valueOf(boardElement.getAttribute("size"));
                minorSize = (int) Math.sqrt(size);
                if (minorSize * minorSize != size) {
                    throw new InvalidFileFormatException(
                            "Sudoku Importer: invalid board dimensions");
                }
                sudokuBoard = new SudokuBoard(size);
            } else {
                throw new InvalidFileFormatException("Sudoku Importer: invalid board dimensions");
            }

            for (int i = 0; i < elementDataList.getLength(); i++) {
                SudokuCell cell =
                        (SudokuCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), sudokuBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != 0) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                sudokuBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (sudokuBoard.getCell(x, y) == null) {
                        int groupIndex = x / minorSize + y / minorSize * minorSize;
                        SudokuCell cell = new SudokuCell(0, new Point(x, y), groupIndex, size);
                        cell.setIndex(y * size + x);
                        cell.setModifiable(true);
                        sudokuBoard.setCell(x, y, cell);
                    }
                }
            }
            //
            //            for(int y = 0; y < size; y++)
            //            {
            //                for(int x = 0; x < size; x++)
            //                {
            //                    SudokuCell cell = sudokuBoard.getCell(x, y);
            //                    System.err.println("(" + x + ", " + y + ") - " +
            // cell.getGroupIndex());
            //                }
            //            }

            puzzle.setCurrentBoard(sudokuBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Sudoku Importer: unknown value where integer expected");
        }
    }

    @Override
    @Contract(value = "_ -> fail", pure = false)
    public void initializeBoard(@NotNull String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Sudoku cannot accept text input");
    }
}
