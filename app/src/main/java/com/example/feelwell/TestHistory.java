package com.example.feelwell;

public class TestHistory {
    private String date;
    private int score;
    private int totalScore;

    public TestHistory(String date, int score, int totalScore) {
        this.date = date;
        this.score = score;
        this.totalScore = totalScore;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public int getTotalScore() {
        return totalScore;
    }
}