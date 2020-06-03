package de.htw.limitless.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.htw.limitless.R;
import de.htw.limitless.model.DeviceShakenQuestion;
import de.htw.limitless.model.InputQuestion;
import de.htw.limitless.model.MultipleChoicesQuestion;
import de.htw.limitless.model.Question;
import de.htw.limitless.model.QuestionDatabase;

public class GameActivity extends AppCompatActivity {

    private TextView mQuestionText;
    private Button mAnswer01Btn;
    private Button mAnswer02Btn;
    private Button mAnswer03Btn;
    private Button mAnswer04Btn;
    private TextView mInputChoicesList;
    private Button mClearAnswerBtn;
    private List<Button> mChoicesButtons = new ArrayList<>();

    private EditText mAnswerInput;
    private Button mSubmitAnswerBtn;

    private QuestionDatabase mQuestionDatabase = new QuestionDatabase();
    private Question currentQuestion;

    private List<Integer> inputIndices = new ArrayList<>();

    private Toast mSuccessToast;
    private Toast mWrongToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setUp();
        start();
    }

    private void setUp() {
        mQuestionText = findViewById(R.id.questionText);

        mAnswer01Btn = findViewById(R.id.answer01Btn);
        mAnswer02Btn = findViewById(R.id.answer02Btn);
        mAnswer03Btn = findViewById(R.id.answer03Btn);
        mAnswer04Btn = findViewById(R.id.answer04Btn);
        mInputChoicesList = findViewById(R.id.inputChoicesList);
        mClearAnswerBtn = findViewById(R.id.clearAnswerBtn);

        mAnswerInput = findViewById(R.id.answerTextInput);
        mSubmitAnswerBtn = findViewById(R.id.submitInputAnswer);

        mChoicesButtons.add(mAnswer01Btn);
        mChoicesButtons.add(mAnswer02Btn);
        mChoicesButtons.add(mAnswer03Btn);
        mChoicesButtons.add(mAnswer04Btn);

        mSuccessToast = Toast.makeText(this, "Correct! One Cookie added!", Toast.LENGTH_SHORT);
        mWrongToast = Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT);
    }

    private void start() {
        mQuestionDatabase.generateQuestions();
        nextQuestion();
    }

    private void nextQuestion() {
        currentQuestion = mQuestionDatabase.getNextQuestion();
        switch (currentQuestion.getQuestionType()) {
            case "multiple":
                displayMultipleChoicesAndMotionQuestion();
                solveMultipleChoiceQuestion();
                break;
            case "shaken":
                displayMultipleChoicesAndMotionQuestion();
                solveDeviceShakenQuestion();
                break;
            case "input":
                displayInputQuestion();
                solveInputQuestion();
                break;
        }
    }

    private void displayMultipleChoicesAndMotionQuestion() {
        inputIndices.clear();
        setUpMultipleChoicesQuestionUI();

        List<String> choices;

        if (currentQuestion.getQuestionType().equals("multiple")) {
            choices = ((MultipleChoicesQuestion) currentQuestion).getChoicesList();
        } else {
            choices = ((DeviceShakenQuestion) currentQuestion).getChoicesList();
        }

        mQuestionText.setText(currentQuestion.getQuestion());
        for (int i = 0; i < choices.size(); i++) {
            mChoicesButtons.get(i).setText(choices.get(i));
        }
    }

    private void solveMultipleChoiceQuestion() {
        for (int i = 0; i < mChoicesButtons.size(); i++) {
            final int finalIndex = i;
            mChoicesButtons.get(i).setTag(i);
            mChoicesButtons.get(i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String newestAnswer;
                    if (mInputChoicesList.getText().toString().length() == 0) {
                        newestAnswer = mChoicesButtons.get(finalIndex).getText().toString();
                    } else {
                        newestAnswer = mInputChoicesList.getText().toString() + ", " + mChoicesButtons.get(finalIndex).getText().toString();
                    }

                    int responseIndex = (int) v.getTag();
                    inputIndices.add(responseIndex);
                    mInputChoicesList.setText(newestAnswer);

                    if (((MultipleChoicesQuestion) currentQuestion).checkAnswer(inputIndices)) {
                        mSuccessToast.show();
                        nextQuestion();
                    }
                }
            });
        }

        mClearAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputIndices.clear();
                mInputChoicesList.setText("");
            }
        });
    }

    private void displayInputQuestion() {
        setUpInputQuestionUI();
        mQuestionText.setText(currentQuestion.getQuestion());
    }

    private void solveInputQuestion() {
        mSubmitAnswerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((InputQuestion) currentQuestion).checkAnswer(mAnswerInput.getText().toString())) {
                    mSuccessToast.show();
                    nextQuestion();
                } else {
                    mWrongToast.show();
                }
            }
        });
    }

    private void solveDeviceShakenQuestion() {
        ShakeDetector shakeDetector = new ShakeDetector();
        if (((DeviceShakenQuestion) currentQuestion).checkAnswer(shakeDetector.checkIfShaken())) {
            mSuccessToast.show();
            nextQuestion();
        }
    }

    private void setUpInputQuestionUI() {
        for (Button answerBtn : mChoicesButtons) {
            answerBtn.setVisibility(View.GONE);
        }
        mInputChoicesList.setVisibility(View.GONE);
        mClearAnswerBtn.setVisibility(View.GONE);

        mAnswerInput.setVisibility(View.VISIBLE);
        mAnswerInput.setText("");
        mSubmitAnswerBtn.setVisibility(View.VISIBLE);
    }

    private void setUpMultipleChoicesQuestionUI() {
        mAnswerInput.setVisibility(View.GONE);
        mSubmitAnswerBtn.setVisibility(View.GONE);

        for (Button answerBtn : mChoicesButtons) {
            answerBtn.setVisibility(View.VISIBLE);
        }
        mInputChoicesList.setText("");
        mInputChoicesList.setVisibility(View.VISIBLE);
        mClearAnswerBtn.setVisibility(View.VISIBLE);
    }
}
