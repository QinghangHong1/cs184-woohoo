package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class ProblemSet {
    private ArrayList<Problem> problems;

    private String name;

    public ProblemSet() {
        problems = new ArrayList<Problem>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ProblemSet(ArrayList<Problem> problems) {
        this.problems = problems;
//        this.numProblems = problems.size();
    }
    public void addProblem(Problem p){
        problems.add(p);
    }
    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public int getNumProblems() {
        return problems.size();
    }

    @Override
    public String toString() {
        return "ProblemSet{" +
                "problems=" + problems +
                ", name='" + name + '\'' +
                '}';
    }
}

