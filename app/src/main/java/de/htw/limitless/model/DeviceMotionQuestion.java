package de.htw.limitless.model;

import java.util.ArrayList;
import java.util.List;

public class DeviceMotionQuestion implements Question {

    private String id;
    private String mQuestion;
    private List<String> mChoicesList = new ArrayList<>();
    private String mHint;
    private String mAnswer;

    private static final String TILTED_ONCE = "tilted once vertically";
    private static final String TILT_HORIZONTALLY = "tilting horizontally";
    private static final String ROTATE_180 = "rotated 180 degree";

    public DeviceMotionQuestion(String id, String question) {
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

    public void setTiltingHorizontallyAnswer() {
        this.mAnswer = TILT_HORIZONTALLY;
    }

    public void setTiltedOnceAnswer() {
        this.mAnswer = TILTED_ONCE;
    }

    public void setRotatedAnswer() {
        this.mAnswer = ROTATE_180;
    }

    public List<String> getChoicesList() {
        return mChoicesList;
    }

    @Override
    public String getQuestionType() {
        return "motion";
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
