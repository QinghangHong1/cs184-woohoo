package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class ProblemSet {
    private ArrayList<Problem> problems;
    private int numProblems;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public ProblemSet(ArrayList<Problem> problems) {
        this.problems = problems;
        this.numProblems = problems.size();
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public int getNumProblems() {
        return numProblems;
    }
}

