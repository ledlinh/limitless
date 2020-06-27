package de.htw.limitless.controller;

import android.os.UserManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.htw.limitless.R;
import de.htw.limitless.model.DeviceMotionQuestion;
import de.htw.limitless.model.InputQuestion;
import de.htw.limitless.model.MultipleChoicesQuestion;
import de.htw.limitless.model.Player;
import de.htw.limitless.model.Question;

public class GameLogic {

    public static GameLogic game;
    private static Player mPlayer;
    private String[] mTitleList = new String[]{"Not There", "Baby", "Smart Kid", "Clever Teenager", "Genius Youngster", "Overachiever", "Scholar", "Pioneer", "Inventor", "Walking Intelligence", "Einstein"};

    private static final int WIN_LEVEL = 3;
    private static final int TO_LEVEL_UP = 2;
    private static final int COOKIE_TO_USE = 1;
    private static final int COOKIE_TO_ADD = 1;
    private static final int CUPCAKE_TO_USE = 1;
    private static final int CUPCAKE_TO_ADD = 1;

    private QuestionDatabase mQuestionDatabase = new QuestionDatabase();
    private Question currentQuestion;
    private HashSet<String> answeredQuestionsList;

    private static PreferenceManager preferenceManager;
    private AchievementListener listener;

    public static GameLogic getGame() {
        if (game == null) {
            game = new GameLogic();
        }
        return game;
    }

    private GameLogic() {
        mQuestionDatabase.generateQuestions();
        preferenceManager = PreferenceManager.getInstance();
    }

    public interface AchievementListener {
        public void leveledUp();
        public void ended();
    }

    public void setAchievementListener(AchievementListener listener) {
        this.listener = listener;
    }

    public void setUpNewGame(String playerName) {
        if (canStart()) {
            preferenceManager.write("started", true);
            mPlayer = Player.getPlayer(playerName);
            mPlayer.setTitle(mTitleList[mPlayer.getLevel()]);
            answeredQuestionsList = new HashSet<>();
            updateSharedPreferences();
        } else {
            throw new RuntimeException("Not enough question in the database");
        }

    }

    private boolean canStart() {
        int questionsSize = mQuestionDatabase.getSize();
        int requirement = ((WIN_LEVEL + 1) * TO_LEVEL_UP) + ((WIN_LEVEL + 1) * CUPCAKE_TO_USE);
        if (questionsSize < requirement) return false;
        else return true;
    }

    public void nextQuestion() {
        currentQuestion = mQuestionDatabase.getNextQuestion();
        if (answeredQuestionsList.contains(currentQuestion)) {
            nextQuestion();
        }
    }

    public Boolean started() {
        if (preferenceManager.readBoolean("started")) {
            return true;
        } else {
            return false;
        }
    }

    public void setUpPreviousGame() {
        if (canStart()) {
            String playerName = preferenceManager.readString("playerName");
            mPlayer = Player.getPlayer(playerName);
            updateGame();
        } else {
            throw new RuntimeException("Not enough questions in the database");
        }
    }

    private void updateGame() {
        mPlayer = Player.getPlayer(preferenceManager.readString("playerName"));

        mPlayer.setLevel(preferenceManager.readInt("level"));
        mPlayer.setTitle(preferenceManager.readString("title"));
        mPlayer.setCookies(preferenceManager.readInt("cookies"));
        mPlayer.setQuestionsAnswered(preferenceManager.readInt("questionAnswered"));
        mPlayer.setCupcakes(preferenceManager.readInt("cupcakes"));

        String answeredQuestions = preferenceManager.readString("answeredQuestions");
        answeredQuestionsList = new HashSet<>(Arrays.asList(answeredQuestions.split(",")));
    }

    public String getPlayerName() {
        return mPlayer.getName();
    }
    public int getPlayerLevel() { return mPlayer.getLevel(); }
    public String getPlayerTitle() { return mTitleList[mPlayer.getLevel()]; }
    public int getPlayerCookies() { return mPlayer.getCookies(); }
    public int getPlayerCupcakes() { return mPlayer.getCupcakes(); }


    public String getQuestion() {
        return currentQuestion.getQuestion();
    }
    public String getQuestionType() {
        return currentQuestion.getQuestionType();
    }

    public List<String> getChoicesList() {
        String questionType = getQuestionType();
        List<String> choicesList = new ArrayList<>();
        if (questionType.equals("multiple")) {
            choicesList = ((MultipleChoicesQuestion) currentQuestion).getChoicesList();
        } else if (questionType.equals("motion")) {
            choicesList = ((DeviceMotionQuestion) currentQuestion).getChoicesList();
        }
        return choicesList;
    }

    public boolean checkMultipleChoicesAnswer(List<Integer> input) {
        boolean answered = true;
        Collections.sort(input);
        List<Integer> answersIndices = ((MultipleChoicesQuestion) currentQuestion).getAnswerIndices();

        if ((input.size() != 0) && (input.size() == answersIndices.size())) {
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i) != answersIndices.get(i)) {
                    answered = false;
                }
            }
        } else {
            answered = false;
        }
        if (answered) process();
        return answered;
    }

    public boolean checkStringAnswer(String input) {
        boolean answered = ((InputQuestion) currentQuestion).getAnswer().equals(input);
        if (answered) process();
        return answered;
    }

    public boolean checkMotion(String motion) {
        boolean answered = ((DeviceMotionQuestion) currentQuestion).getAnswer().equals(motion);
        if (answered) process();
        return answered;
    }

    private void process() {
        storeAnsweredQuestion();
        mPlayer.answeredOneQuestion();
        mPlayer.setCookies(mPlayer.getCookies() + COOKIE_TO_ADD);
        updateSharedPreferences();
    }

    public boolean canProceed() {
        if (listener != null) {
            if (answeredQuestionsList.size() < mQuestionDatabase.getSize()) {
                if (mPlayer.getQuestionsAnswered() < TO_LEVEL_UP) {
                    return true;
                } else {
                    levelUpPlayer();
                    return false;
                }
            } else {
                listener.ended();
                return false;
            }
        } else {
            throw new RuntimeException("No listener for GameLogic");
        }
    }

    private void levelUpPlayer() {
        mPlayer.levelUp();
        mPlayer.setCupcakes(mPlayer.getCupcakes() + CUPCAKE_TO_ADD);
        mPlayer.setTitle(mTitleList[mPlayer.getLevel()]);
        mPlayer.setQuestionsAnswered(0);
        updateSharedPreferences();

        if (mPlayer.getLevel() == WIN_LEVEL) {
            listener.ended();
        } else {
            listener.leveledUp();
        }
    }

    public boolean canGetHint() {
        return (mPlayer.getCookies() > 0);
    }

    public String getQuestionHint() {
        mPlayer.setCookies(mPlayer.getCookies() - COOKIE_TO_USE);
        updateSharedPreferences();
        return currentQuestion.getHint();
    }

    public boolean canSkipQuestion() {
        return (mPlayer.getCupcakes() > 0);
    }

    public void skipQuestion() {
        mPlayer.setCupcakes(mPlayer.getCupcakes() - CUPCAKE_TO_USE);
        Question skippedQuestion = currentQuestion;
        mQuestionDatabase.removeCurrentQuestion(currentQuestion);
        mQuestionDatabase.addQuestion(skippedQuestion); //push the question to the end of the list
        updateSharedPreferences();
    }

    private void storeAnsweredQuestion() {
        answeredQuestionsList.add(currentQuestion.getId());
    }

    public void reset() {
        preferenceManager.clear();
        mPlayer.delete();
        game = null;
    }

    private void updateSharedPreferences() {
        preferenceManager.write("playerName", mPlayer.getName());
        preferenceManager.write("level", mPlayer.getLevel());
        preferenceManager.write("title", mTitleList[mPlayer.getLevel()]);
        preferenceManager.write("cookies", mPlayer.getCookies());
        preferenceManager.write("questionAnswered", mPlayer.getQuestionsAnswered());
        preferenceManager.write("cupcakes", mPlayer.getCupcakes());
        String answeredQuestions = TextUtils.join(",", answeredQuestionsList);
        preferenceManager.write("answeredQuestions", answeredQuestions);
    }
}
