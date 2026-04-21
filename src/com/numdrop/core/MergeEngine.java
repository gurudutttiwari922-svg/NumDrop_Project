package com.numdrop.core;

import com.numdrop.model.Board;
import com.numdrop.model.GameState;

public class MergeEngine {

    private final GameState gameState;

    public MergeEngine(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean verticalMerge(Board board) {

        boolean changed = false;

        int rows = board.getRows();
        int cols = board.getCols();

        for (int col = 0; col < cols; col++) {
            for (int row = rows - 1; row > 0; row--) {

                int current = board.getCell(row, col);
                int above = board.getCell(row - 1, col);

                if (current != 0 && current == above) {
                    int merged = current * 2;
                    board.setCell(row, col, merged);
                    board.setCell(row - 1, col, 0);
                    gameState.addScore(merged);
                    changed = true;
                }
            }
        }

        return changed;
    }

    public boolean horizontalMerge(Board board, int fallingCol) {

        boolean changed = false;

        int rows = board.getRows();
        int cols = board.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols - 1; col++) {

                int current = board.getCell(row, col);
                if (current == 0) {
                    continue;
                }

                if (board.getCell(row, col + 1) == current) {
                    int merged = current * 2;
                    int targetCol = (fallingCol == col || fallingCol == col + 1) ? fallingCol : col + 1;

                    board.setCell(row, col, 0);
                    board.setCell(row, col + 1, 0);
                    board.setCell(row, targetCol, merged);

                    gameState.addScore(merged);
                    changed = true;
                    continue;
                }

                if (col < cols - 2 &&
                    board.getCell(row, col + 1) == 0 &&
                    board.getCell(row, col + 2) == current) {

                    int merged = current * 2;
                    int targetCol = (fallingCol == col || fallingCol == col + 1 || fallingCol == col + 2)
                        ? fallingCol
                        : col + 1;

                    board.setCell(row, col, 0);
                    board.setCell(row, col + 2, 0);
                    board.setCell(row, targetCol, merged);

                    gameState.addScore(merged);
                    changed = true;
                }
            }
        }

        return changed;
    }

    public boolean horizontalMerge(Board board) {
        return horizontalMerge(board, -1);
    }
}
