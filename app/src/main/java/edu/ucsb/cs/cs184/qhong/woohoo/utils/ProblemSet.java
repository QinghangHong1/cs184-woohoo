package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class ProblemSet {
    private ArrayList<Problem> problems;
    private String name;
//    private int numProblems;

    public ProblemSet() { problems = new ArrayList<Problem>();
//    numProblems = 0;
    }

    public ProblemSet(ArrayList<Problem> problems) {
        this.problems = problems;
//        this.numProblems = problems.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProblem(Problem p){
        problems.add(p);
    }

    public void setProblems(ArrayList<Problem> problems) { this.problems = problems; }

    public ArrayList<Problem> getProblems() { return problems; }

    public int getNumProblems() {
        return problems.size();
    }

//    public void setNumProblems(Integer numOfProbs) {
//        this.numProblems = numOfProbs;
//    }

    @Override
    public String toString() {
        return "ProblemSet{" +
                "problems=" + problems +
                ", name='" + name + '\'' +
                '}';
    }
}

