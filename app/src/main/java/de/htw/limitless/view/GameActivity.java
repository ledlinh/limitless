package de.htw.limitless.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.htw.limitless.R;
import de.htw.limitless.controller.GameLogic;
import de.htw.limitless.controller.MotionDetector;

public class GameActivity extends AppCompatActivity implements MotionDetector.ChangeListener, GameLogic.AchievementListener {

    private TextView mLevelView;
    private TextView mTitleView;
    private TextView mCookiesView;
    private TextView mCupcakesView;

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

    private Button mShowHintBtn;
    private TextView mHintTextView;
    private Button mSkipBtn;

    private GameLogic game;
    private MotionDetector motionDetector;

    private List<Integer> inputIndices = new ArrayList<>();

    private Toast mSuccessToast;
    private Toast mWrongToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpView();

        game = GameLogic.getGame();
        game.setAchievementListener(this);
        game.setUpPreviousGame();
        game.start();

        displayNextQuestion();
    }

    private void setUpView() {
        mLevelView = findViewById(R.id.levelView);
        mTitleView = findViewById(R.id.titleView);
        mCookiesView = findViewById(R.id.cookiesView);
        mCupcakesView = findViewById(R.id.cupcakesView);

        mQuestionText = findViewById(R.id.questionText);

        mAnswer01Btn = findViewById(R.id.answer01Btn);
        mAnswer02Btn = findViewById(R.id.answer02Btn);
        mAnswer03Btn = findViewById(R.id.answer03Btn);
        mAnswer04Btn = findViewById(R.id.answer04Btn);
        mInputChoicesList = findViewById(R.id.inputChoicesList);
        mClearAnswerBtn = findViewById(R.id.clearAnswerBtn);

        mAnswerInput = findViewById(R.id.answerTextInput);
        mSubmitAnswerBtn = findViewById(R.id.submitInputAnswer);

        mShowHintBtn = findViewById(R.id.showHint);
        mHintTextView = findViewById(R.id.hint);
        mSkipBtn = findViewById(R.id.skipBtn);

        mChoicesButtons.add(mAnswer01Btn);
        mChoicesButtons.add(mAnswer02Btn);
        mChoicesButtons.add(mAnswer03Btn);
        mChoicesButtons.add(mAnswer04Btn);

        mSuccessToast = Toast.makeText(this, "Correct! One Cookie added!", Toast.LENGTH_SHORT);
        mWrongToast = Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT);
    }

    private void displayNextQuestion() {
        game.nextQuestion();
        resetView();
        monitorPlayerRequest();

        switch (game.getQuestionType()) {
            case "multiple":
                displayMultipleChoicesQuestion();
                solveMultipleChoiceQuestion();
                break;
            case "motion":
                displayDeviceMotionQuestion();
                break;
            case "input":
                displayInputQuestion();
                solveInputQuestion();
                break;
        }
    }

    private void resetView() {
        mHintTextView.setText("");
        mShowHintBtn.setEnabled(true);
        updateView();
    }

    private void updateView() {
        // Display player data
        mLevelView.setText("Level: " + game.getPlayerLevel());
        mTitleView.setText("Title: " + game.getPlayerTitle());
        mCookiesView.setText("Cookies: " + game.getPlayerCookies());
        mCupcakesView.setText("Cupcakes: " + game.getPlayerCupcakes());
    }

    // Configure multiple choices question
    private void displayMultipleChoicesQuestion() {
        inputIndices.clear();
        setUpMultipleChoicesQuestionUI();

        List<String> choices = game.getChoicesList();

        mQuestionText.setText(game.getQuestion());
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
                    String newestAnswer = "Your answers: ";
                    if (mInputChoicesList.getText().toString().length() == 0) {
                        newestAnswer += mChoicesButtons.get(finalIndex).getText().toString();
                    } else {
                        newestAnswer = mInputChoicesList.getText().toString() + ", " + mChoicesButtons.get(finalIndex).getText().toString();
                    }

                    int responseIndex = (int) v.getTag();
                    inputIndices.add(responseIndex);
                    mInputChoicesList.setText(newestAnswer);

                    if (game.checkMultipleChoicesAnswer(inputIndices)) {
                        mSuccessToast.show();
                        displayNextQuestion();
                    } else {
                        mWrongToast.show();
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

    // Configure motion questions
    private void displayDeviceMotionQuestion() {
        displayMultipleChoicesQuestion();

        motionDetector = MotionDetector.getInstance();;
        motionDetector.detectStarted();
        motionDetector.setChangeListener(this);

        setUpDeviceMotionAnswer();
    }

    private void setUpDeviceMotionAnswer() {
        for (int i = 0; i < mChoicesButtons.size(); i++) {
            final int finalIndex = i;
            mChoicesButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newestAnswer;
                    if (mInputChoicesList.getText().toString().length() == 0) {
                        newestAnswer = mChoicesButtons.get(finalIndex).getText().toString();
                    } else {
                        newestAnswer = mInputChoicesList.getText().toString() + ", " + mChoicesButtons.get(finalIndex).getText().toString();
                    }

                    mInputChoicesList.setText("Your answers: " + newestAnswer);
                    mWrongToast.show();
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

    //   Detect changes from the motion sensor
    @Override
    public void onChanged(String motion) {
        solveMotionQuestion(motion);
    }

    private void solveMotionQuestion(String motion) {
        if (game.checkMotion(motion)) {
            mInputChoicesList.setText(motion);
            motionDetector.detectPaused();
            mSuccessToast.show();
            displayNextQuestion();
        }
    }

    // Configure input question
    private void displayInputQuestion() {
        setUpInputQuestionUI();
        mQuestionText.setText(game.getQuestion());
    }

    private void solveInputQuestion() {
        mSubmitAnswerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (game.checkStringAnswer(mAnswerInput.getText().toString())) {
                    mSuccessToast.show();
                    displayNextQuestion();
                } else {
                    mWrongToast.show();
                }
            }
        });
    }

    //set up UI
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

    private void monitorPlayerRequest() {
        mShowHintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.canGetHint()) {
                    mHintTextView.setText(game.getQuestionHint());
                } else {
                    mHintTextView.setText("Not enough cookies!");
                }
                updateView();
                mShowHintBtn.setEnabled(false);
            }
        });

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.canSkipQuestion()) {
                    game.skipQuestion();
                    displayNextQuestion();
                } else {
                    mHintTextView.setText("Not enough cupcakes!");
                }
                updateView();
            }
        });
    }

    @Override
    public void leveledUp() {
        Intent levelUpActivity = new Intent(GameActivity.this, LevelUpActivity.class);
        startActivity(levelUpActivity);
    }

    @Override
    public void ended() {
        Intent gameEndedActivity = new Intent(GameActivity.this, GameEndedActivity.class);
        startActivity(gameEndedActivity);
    }
}
