package de.htw.limitless.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleChoicesQuestion implements Question {

    private String id;
    private String mQuestion;
    private List<String> mChoicesList = new ArrayList<>();
    private List<Integer> mAnswerIndices = new ArrayList<>();
    private String mHint;

    public MultipleChoicesQuestion(String id, String question) {
        this.id = id;
        this.mQuestion = question;
    }

    public String getId() {
        return this.id;
    }

    public void setUp(String choice1, String choice2, String choice3, String choice4, String hint) {
        mChoicesList.add(choice1);
        mChoicesList.add(choice2);
        mChoicesList.add(choice3);
        mChoicesList.add(choice4);
        this.mHint = hint;
    }

    public void setAnswerIndices(List<Integer> indices) {
        Collections.sort(indices);
        for (int index : indices) {
            mAnswerIndices.add(index);
        }
    }

    public List<String> getChoicesList() {
        return mChoicesList;
    }

    public List<Integer> getAnswerIndices() {
        return mAnswerIndices;
    }

    @Override
    public String getQuestionType() {
        return "multiple";
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }

    @Override
    public String getHint() {
        return mHint;
    }
}
