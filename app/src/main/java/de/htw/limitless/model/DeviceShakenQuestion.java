package de.htw.limitless.model;

import java.util.ArrayList;
import java.util.List;

public class DeviceShakenQuestion implements Question {

    private String mQuestion;
    private List<String> mChoicesList = new ArrayList<>();
    private String mHint;

    public DeviceShakenQuestion(String question) {
        this.mQuestion = question;
    }

    public void setUp(String choice1, String choice2, String choice3, String choice4, String hint) {
        mChoicesList.add(choice1);
        mChoicesList.add(choice2);
        mChoicesList.add(choice3);
        mChoicesList.add(choice4);
        this.mHint = hint;
    }

    public List<String> getChoicesList() {
        return mChoicesList;
    }

    @Override
    public String getQuestionType() {
        return "shaken";
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }

    @Override
    public String getHint() {
        return mHint;
    }

    public boolean checkAnswer(boolean isShaken) {
        return isShaken;
    }
}
