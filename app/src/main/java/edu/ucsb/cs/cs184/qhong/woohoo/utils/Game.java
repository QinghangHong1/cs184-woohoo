package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int roomId;
    private ProblemSet problemSet;
    private ArrayList<Player> players;
    private int timePerQuestion;

    public Game(){
        this.players = new ArrayList<Player>();
        this.timePerQuestion = 0;
        generateGameID();
    }

    public Game(ProblemSet problemSet) {
        this.problemSet = problemSet;
        this.players = new ArrayList<Player>();
        this.timePerQuestion = 0;
        generateGameID();
    }
    public Game(int roomId){
        this.players = new ArrayList<Player>();
        this.timePerQuestion = 0;
        this.roomId = roomId;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    private int currentProblemIndex;
    public void start(){

//        return problemSet.getProblems().get(currentProblemIndex);
    }
    public Problem getNextProblem(){
        if(currentProblemIndex < (problemSet.getNumProblems() - 1)){
            currentProblemIndex++;
            return problemSet.getProblems().get(currentProblemIndex);
        }
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

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public int getRoomId(){return roomId;}
}
