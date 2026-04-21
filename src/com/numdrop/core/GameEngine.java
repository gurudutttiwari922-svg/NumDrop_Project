package com.numdrop.core;

import com.numdrop.model.Board;
import com.numdrop.model.Block;
import com.numdrop.model.GameState;

import java.util.Random;
import java.util.Scanner;

public class GameEngine {

    private Board board;
    private GameState gameState;
    private Block fallingBlock;

    private MergeEngine mergeEngine;
    private GravityEngine gravityEngine;
    private ChainController chainController;

    private int nextSpawnCol;
    private Random rand = new Random();
    private Scanner scanner = new Scanner(System.in);

    public GameEngine() {
        board = new Board();
        gameState = new GameState();

        mergeEngine = new MergeEngine(gameState);
        gravityEngine = new GravityEngine();
        chainController = new ChainController(mergeEngine, gravityEngine);
        nextSpawnCol = board.getCols() / 2;
    }

    public void startGame() {

        System.out.println("Game Started");

        while (!gameState.gameOver) {

            spawnBlock();

            while (true) {

                printBoard();

                String input = handleInput();
                boolean moved = !input.equals("invalid");

                if (isSpawnBlocked()) {
                    if (input.equals("") || input.equals("s")) {
                        gameState.gameOver = true;
                        break;
                    }
                    continue;
                }

                if (isCollision()) {
                    int landedCol = fallingBlock.col;
                    nextSpawnCol = landedCol;
                    int scoreBefore = gameState.getScore();
                    lockBlock();
                    int comboCount = chainController.resolve(board, landedCol);
                    int turnScore = gameState.getScore() - scoreBefore;
                    printTurnFeedback(turnScore, comboCount);
                    if (isTopRowOccupied()) {
                        gameState.gameOver = true;
                    }
                    printBoard();
                    break;
                }

                if (!moved && !isCollision()) {
                    moveDown();
                }
            }
        }

        System.out.println("Game Over");
        System.out.println("Final Score: " + gameState.getScore());
    }

    private void spawnBlock() {
        int col = nextSpawnCol;

        int max = board.getMaxValue();
        if (max < 2) {
            max = 2;
        }

        int maxPower = (int) (Math.log(max) / Math.log(2));
        int totalWeight = 0;
        int middle = (maxPower + 1) / 2;

        for (int p = 1; p <= maxPower; p++) {
            totalWeight += middle - Math.abs(p - middle) + 1;
        }

        int roll = rand.nextInt(totalWeight);
        int power = 1;

        for (int p = 1; p <= maxPower; p++) {
            int weight = middle - Math.abs(p - middle) + 1;
            if (roll < weight) {
                power = p;
                break;
            }
            roll -= weight;
        }

        int value = (int) Math.pow(2, power);

        fallingBlock = new Block(0, col, value);
    }

    private String handleInput() {

        System.out.print("Move (A=left, D=right, S=down, ENTER=drop): ");

        String input = scanner.nextLine().toLowerCase();

        if (input.equals("")) {
            hardDrop();
            return input;
        } else if (input.equals("a")) {
            moveLeft();
            return input;
        } else if (input.equals("d")) {
            moveRight();
            return input;
        } else if (input.equals("s")) {
            moveDown();
            return input;
        }

        return "invalid";
    }

    private void moveLeft() {
        if (fallingBlock.col > 0 &&
            board.isCellEmpty(fallingBlock.row, fallingBlock.col - 1)) {
            fallingBlock.col--;
        }
    }

    private void moveRight() {
        if (fallingBlock.col < board.getCols() - 1 &&
            board.isCellEmpty(fallingBlock.row, fallingBlock.col + 1)) {
            fallingBlock.col++;
        }
    }

    private void hardDrop() {
        while (!isCollision()) {
            fallingBlock.row++;
        }
    }

    private void moveDown() {
        fallingBlock.row++;
    }

    private boolean isCollision() {
        if (fallingBlock.row == board.getRows() - 1) return true;
        return !board.isCellEmpty(fallingBlock.row + 1, fallingBlock.col);
    }

    private void lockBlock() {
        board.setCell(fallingBlock.row, fallingBlock.col, fallingBlock.value);
    }

    private boolean isSpawnBlocked() {
        return !board.isCellEmpty(fallingBlock.row, fallingBlock.col);
    }

    private boolean isTopRowOccupied() {
        for (int col = 0; col < board.getCols(); col++) {
            if (!board.isCellEmpty(0, col)) {
                return true;
            }
        }

        return false;
    }

    private void printTurnFeedback(int turnScore, int comboCount) {
        if (turnScore <= 0) {
            return;
        }

        if (comboCount > 1) {
            System.out.println("COMBO x" + comboCount + " -> +" + turnScore);
        } else {
            System.out.println("+" + turnScore);
        }

        if (turnScore >= 128) {
            System.out.println("BIG MOVE!");
        }

        if (comboCount >= 3) {
            System.out.println("INSANE COMBO!");
        }
    }

    private void printBoard() {

        System.out.println("\nScore: " + gameState.getScore());

        for (int i = 0; i < board.getCols(); i++) {
            System.out.print("--------");
        }
        System.out.println();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {

                int value;

                if (fallingBlock != null &&
                    i == fallingBlock.row &&
                    j == fallingBlock.col) {
                    value = fallingBlock.value;
                } else {
                    value = board.getCell(i, j);
                }

                if (value == 0) {
                    System.out.print("[      ]");
                } else {
                    System.out.printf("[   %3d]", value);
                }
            }
            System.out.println();
        }

        for (int i = 0; i < board.getCols(); i++) {
            System.out.print("--------");
        }
        System.out.println();
    }
}
