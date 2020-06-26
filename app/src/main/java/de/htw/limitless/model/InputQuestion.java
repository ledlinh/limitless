package de.htw.limitless.model;

public class InputQuestion implements Question {

    private String id;
    private String mQuestion;
    private String mAnswer;
    private String mHint;

    public InputQuestion(String id, String question) {
        this.id = id;
        this.mQuestion = question;
    }

    public String getId() {
        return this.id;
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

    public String getAnswer() {
        return mAnswer;
    }
}
