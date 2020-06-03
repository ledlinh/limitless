package de.htw.limitless.model;

import android.content.Intent;
import android.renderscript.ScriptGroup;

import java.util.List;

public class InputQuestion implements Question {

    private String mQuestion;
    private String mAnswer;
    private String mHint;

    public InputQuestion(String question) {
        this.mQuestion = question;
    }

    public void setUp(String answer, String hint) {
        this.mAnswer = answer;
        this.mHint = hint;
    }

    @Override
    public String getQuestionType() {
        return "input";
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }

    @Override
    public String getHint() {
        return mHint;
    }

    public boolean checkAnswer(String answer) {
        return answer.contains(mAnswer);
    }
}
