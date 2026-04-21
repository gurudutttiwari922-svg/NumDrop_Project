package com.numdrop.core;

import com.numdrop.model.Board;

public class GravityEngine {

    public void applyGravity(Board board) {

        for (int col = 0; col < board.getCols(); col++) {

            int[] temp = new int[board.getRows()];
            int index = board.getRows() - 1;

            for (int row = board.getRows() - 1; row >= 0; row--) {
                if (board.getCell(row, col) != 0) {
                    temp[index--] = board.getCell(row, col);
                }
            }

            for (int row = 0; row < board.getRows(); row++) {
                board.setCell(row, col, temp[row]);
            }
        }
    }
}