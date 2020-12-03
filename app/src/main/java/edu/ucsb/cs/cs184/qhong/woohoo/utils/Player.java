package edu.ucsb.cs.cs184.qhong.woohoo.utils;

public class Player {
    private String username;
    private int userId;
    private int score;

    public Player(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int score) {
        this.score += score;
    }
//    public void joinGame(Game game) {
//
//    }
}
