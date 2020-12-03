package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class Game {
    private int roomId;
    private ProblemSet problemSet;
    private ArrayList<Player> players;
    private GameSetting gameSetting;

    public Game(ProblemSet problemSet, GameSetting gameSetting) {
        this.problemSet = problemSet;
        this.players = new ArrayList<Player>();
        this.gameSetting = gameSetting;
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
}
