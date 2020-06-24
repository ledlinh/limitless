package de.htw.limitless.controller;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.htw.limitless.model.DeviceMotionQuestion;
import de.htw.limitless.model.InputQuestion;
import de.htw.limitless.model.MultipleChoicesQuestion;
import de.htw.limitless.model.Player;
import de.htw.limitless.model.Question;
import de.htw.limitless.model.QuestionDatabase;

public class GameLogic {

    public static GameLogic game;
    private static Player mPlayer;
    private String[] mTitleList = new String[]{"Not There", "Baby", "Smart Kid", "Clever Teenager", "Genius Youngster", "Overachiever", "Scholar", "Pioneer", "Inventor", "Walking Intelligence", "Einstein"};

    private static PreferenceManager preferenceManager;

    private QuestionDatabase mQuestionDatabase = new QuestionDatabase();
    private Question currentQuestion;
    private List<String> answeredQuestionsList;

    private AchievementListener listener;

    private GameLogic() {
        preferenceManager = PreferenceManager.getInstance();
    }

    public static GameLogic getGame() {
        if (game == null) {
            game = new GameLogic();
        }
        return game;
    }

    public interface AchievementListener {
        public void leveledUp();
        public void ended();
    }

    public void setAchievementListener(AchievementListener listener) {
        this.listener = listener;
    }

    public void setUpNewGame(String playerName) {
        preferenceManager.write("started", true);
        mPlayer = Player.getPlayer(playerName);
        mPlayer.setTitle(mTitleList[mPlayer.getLevel()]);
        answeredQuestionsList = new ArrayList<>();
        updateSharedPreferences();
    }

    public Boolean started() {
        boolean started = false;
        if (preferenceManager.readBoolean("started")) {
            started = true;
        }
        return started;
    }

    public void setUpPreviousGame() {
        if (started()) {
            String playerName = preferenceManager.readString("playerName");
            mPlayer = Player.getPlayer(playerName);
            updateGame();
        } else {
            System.out.println("No game found.");
        }
    }

    public String getPlayerName() {
        return mPlayer.getName();
    }
    public int getPlayerLevel() { return mPlayer.getLevel(); }
    public String getPlayerTitle() { return mTitleList[mPlayer.getLevel()]; }
    public int getPlayerCookies() { return mPlayer.getCookies(); }
    public int getPlayerCupcakes() { return mPlayer.getCupcakes(); }

    public void start() {
        System.out.println("Game started.");
        mQuestionDatabase.generateQuestions();
        nextQuestion();
    }

    public void nextQuestion() {
        if (answeredQuestionsList.size() == mQuestionDatabase.getSize()) {
            listener.ended();
        } else {
            currentQuestion = mQuestionDatabase.getNextQuestion();
            if (questionAnswered(currentQuestion)) {
                nextQuestion();
            }
        }
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

    public String getQuestion() {
        return currentQuestion.getQuestion();
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

    public void process() {
        storeAnsweredQuestion();
        mPlayer.answeredOneQuestion();
        mPlayer.addCookie();
        if (mPlayer.getQuestionsAnswered() == 3) {
            levelUpPlayer();
        }
        updateSharedPreferences();
    }

    public void levelUpPlayer() {
        mPlayer.levelUp();
        mPlayer.addCupcake();
        mPlayer.setTitle(mTitleList[mPlayer.getLevel()]);

        if (mPlayer.getLevel() == 10) {
            listener.ended();
        } else {
            listener.leveledUp();
        }

        mPlayer.setQuestionsAnswered(0);
        updateSharedPreferences();
    }

    public boolean canGetHint() {
        return (mPlayer.getCookies() > 0);
    }

    public String getQuestionHint() {
        mPlayer.useCookie();
        updateSharedPreferences();
        return currentQuestion.getHint();
    }

    public boolean canSkipQuestion() {
        return (mPlayer.getCupcakes() > 0);
    }

    public void skipQuestion() {
        mPlayer.useCupcake();
        updateSharedPreferences();
        nextQuestion();
    }

    public void storeAnsweredQuestion() {
        answeredQuestionsList.add(currentQuestion.getId());
    }

    private boolean questionAnswered(Question currQuestion) {
        String currentId = currQuestion.getId();
        boolean answered = false;
        for (String id : answeredQuestionsList) {
            if (id.equals(currentId)) {
                answered = true;
            }
        }
        return answered;
    }

    public void reset() {
        preferenceManager.clear();
        game = null;
        mPlayer.delete();
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

    public void updateGame() {
        mPlayer = Player.getPlayer(preferenceManager.readString("playerName"));

        mPlayer.setLevel(preferenceManager.readInt("level"));
        mPlayer.setTitle(preferenceManager.readString("title"));
        mPlayer.setCookies(preferenceManager.readInt("cookies"));
        mPlayer.setQuestionsAnswered(preferenceManager.readInt("questionAnswered"));
        mPlayer.setCupcakes(preferenceManager.readInt("cupcakes"));

        String answeredQuestions = preferenceManager.readString("answeredQuestions");
        answeredQuestionsList = new LinkedList<String>(Arrays.asList(answeredQuestions.split(",")));
    }
}
