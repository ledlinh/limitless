package de.htw.limitless.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleChoicesQuestion implements Question {

    private String mQuestion;
    private List<String> mChoicesList = new ArrayList<>();
    private List<Integer> mAnswerIndices = new ArrayList<>();
    private String mHint;

    public MultipleChoicesQuestion(String question) {
        this.mQuestion = question;
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

    public boolean checkAnswer(List<Integer> indices) {
        boolean result = true;
        Collections.sort(indices);
        if ((indices.size() != 0) && (indices.size() == mAnswerIndices.size())) {
            for (int i = 0; i < indices.size(); i++) {
                if (indices.get(i) != mAnswerIndices.get(i)) {
                    result = false;
                }
            }
        } else {
            result = false;
        }
        return result;
    }
}
