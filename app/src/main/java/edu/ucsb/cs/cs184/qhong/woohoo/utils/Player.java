package edu.ucsb.cs.cs184.qhong.woohoo.utils;

public class Player implements Comparable<Player> {
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //    private String username;
    private String uid;
    private int score;
    public Player(){

    }
    public Player(String uid, int score) {

        this.uid = uid;
        this.score = score;
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

    @Override
    public String toString() {
        return "Player{" +
                "uid='" + uid + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public int compareTo(Player o) {
        return Integer.compare(score, o.getScore());
    }
//    public void joinGame(Game game) {
//
//    }
}
