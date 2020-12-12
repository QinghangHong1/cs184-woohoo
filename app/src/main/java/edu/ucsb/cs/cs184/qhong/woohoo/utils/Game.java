package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Game{
    private int roomId;
    private ProblemSet problemSet;
    private ArrayList<Player> players;
    private int timePerQuestion;
    private String ProblemSetName;
    private int currentProblemIndex;
    private int currChooseAnswIndex;
    private int score;

    public String getHost_uid() {
        return host_uid;
    }

    public void setHost_uid(String host_uid) {
        this.host_uid = host_uid;
    }

    private String host_uid;

    public Game(){
        this.players = new ArrayList<Player>();
        this.timePerQuestion = 0;
        this.currentProblemIndex = 1;
        this.score = 0;
        generateGameID();
    }

    public Game(ProblemSet problemSet) {
        this.problemSet = problemSet;
        this.players = new ArrayList<Player>();
        this.timePerQuestion = 0;
        this.currentProblemIndex = 1;
        this.score = 0;
        generateGameID();
    }
    public Game(int roomId){
        this.players = new ArrayList<Player>();
        this.timePerQuestion = 0;
        this.roomId = roomId;
        this.currentProblemIndex = 1;
        this.score = 0;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void start(){

//        return problemSet.getProblems().get(currentProblemIndex);
    }

    // if the current index is smaller than size, return true
    // otherwise, return false
    public boolean checkIndex(){
        int size = problemSet.getNumProblems();
        return currentProblemIndex <= size;
    }

    public int getCurrentProblemIndex(){
        return currentProblemIndex;
    }

    public void updateCurrentProblemIndex(){
        currentProblemIndex++;
    }

    // return the problem in specific index
    public Problem getProblem(int index){
        if(index < problemSet.getNumProblems()) return problemSet.getProblems().get(index);

        Log.e("issue", "there is no problem in problem set in getProblem function!");
        return null;
    }

    public void generateGameID(){
        Random rnd = new Random();
        int code = rnd.nextInt(10000);
        roomId = code;
    }

    public void setTimePerQuestion(int timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

    public void setProblemSetName(String setName) {
        this.ProblemSetName = setName;
    }

    public String getProblemSetName() { return ProblemSetName; }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getRoomId(){return roomId;}

    public ProblemSet getProblemSet() {
        return problemSet;
    }

    public void setProblemSet(ProblemSet problemSet) {
        this.problemSet = problemSet;
    }

    public void setCurrChooseAnswIndex(int currChooseAnswIndex) { this.currChooseAnswIndex = currChooseAnswIndex; }

    public int getCurrChooseAnswIndex() { return currChooseAnswIndex; }

    public void updateCurrChooseAnswIndex() { currChooseAnswIndex = -1; }

    public void addScore() { this.score++; }

    public int getScore() { return score; }

    @Override
    public String toString() {
        return "Game{" +
                "roomId=" + roomId +
                ", problemSet=" + problemSet +
                ", players=" + players +
                ", timePerQuestion=" + timePerQuestion +
                ", ProblemSetName='" + ProblemSetName + '\'' +
                ", currentProblemIndex=" + currentProblemIndex +
                ", host_uid='" + host_uid + '\'' +
                '}';
    }
}
