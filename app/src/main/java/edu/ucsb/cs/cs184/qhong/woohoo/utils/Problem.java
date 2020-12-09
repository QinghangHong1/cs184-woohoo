package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class Problem {
    private String question;
    private ArrayList<String> answer_choices;
    private int true_answer_index;

    public Problem() {
        answer_choices = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "question='" + question + '\'' +
                ", answer_choices=" + answer_choices +
                ", true_answer_index=" + true_answer_index +
                '}';
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public ArrayList<String> getAnswer_choices() {
        return answer_choices;
    }

    public void setAnswer_choices(ArrayList<String> answer_choices) {
        this.answer_choices = answer_choices;
    }

    public int getTrue_answer_index() {
        return true_answer_index;
    }

    public void setTrue_answer_index(int true_answer_index) { this.true_answer_index = true_answer_index; }

    public Problem(String question, ArrayList<String> answer_choices, int true_answer_index) {
        this.question = question;
        this.answer_choices = answer_choices;
        this.true_answer_index = true_answer_index;
    }
}
