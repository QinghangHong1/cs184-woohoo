package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class ProblemSet {
    private ArrayList<Problem> problems;
    private int numProblems;

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

