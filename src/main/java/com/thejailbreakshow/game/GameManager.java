package com.thejailbreakshow.game;

public class GameManager {

    private static GameState currentState = GameState.WAITING;

    public static void init() {
        currentState = GameState.WAITING;
    }

    public static void startGame() {
        currentState = GameState.IN_PROGRESS;
    }

    public static void endGame() {
        currentState = GameState.ENDED;
    }

    public static GameState getCurrentState() {
        return currentState;
    }
}
