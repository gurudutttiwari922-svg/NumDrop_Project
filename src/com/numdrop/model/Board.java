package com.numdrop.model;

public class Board {

    private int[][] grid;

    public Board() {
        grid = new int[7][5];
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, int value) {
        grid[row][col] = value;
    }

    public boolean isCellEmpty(int row, int col) {
        return grid[row][col] == 0;
    }

    public int getRows() {
        return 7;
    }

    public int getCols() {
        return 5;
    }

    public int getMaxValue() {
        int max = 0;

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                max = Math.max(max, getCell(i, j));
            }
        }

        return max;
    }
}
