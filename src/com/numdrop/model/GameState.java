package com.numdrop.model;

public class GameState {

    private int score;
    public boolean gameOver;

    public GameState() {
        score = 0;
        gameOver = false;
    }

    public void addScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }
}