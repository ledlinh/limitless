package de.htw.limitless;

import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.htw.limitless.controller.Game;
import de.htw.limitless.controller.GameLogic;
import de.htw.limitless.controller.PreferenceManager;
import de.htw.limitless.controller.QuestionDatabase;
import de.htw.limitless.model.Player;
import de.htw.limitless.model.Question;
import de.htw.limitless.view.GameActivity;
import de.htw.limitless.view.MainActivity;

public class GameLogicTest {
    private static GameLogic game;
    private static final String PLAYER_NAME = "playerName";

    @Test
    public void previousGameRetrieved() {
        PreferenceManager sharedPreferences = PreferenceManager.getInstance();
        sharedPreferences.write("started", true);
        sharedPreferences.write("playerName", PLAYER_NAME);
        sharedPreferences.write("level", 1);
        sharedPreferences.write("title", "Baby");
        sharedPreferences.write("questionAnswered", 1);
        sharedPreferences.write("cookies", 0);
        sharedPreferences.write("cupcakes", 1);
        sharedPreferences.write("answeredQuestions", "q2,q3,q6");

        game = GameLogic.getGame();

        //before setting up previous game
        Assert.assertEquals(true, game.started());

        game.setUpPreviousGame();
        //after internal storage data retrieved
        Assert.assertEquals(PLAYER_NAME, game.getPlayerName());
        Assert.assertEquals(1, game.getPlayerLevel());
        Assert.assertEquals(true, game.canSkipQuestion());
        Assert.assertEquals(false, game.canGetHint());
    }

    @Test
    public void questionDatabaseIsSufficient() {
        game = GameLogic.getGame();
        Assert.assertEquals(true, game.started()); //test if question database is sufficient
    }

    @Test
    public void newGameCanStart() {
        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.clear();

        game = GameLogic.getGame();
        Assert.assertEquals(false, game.started());
        game.setUpNewGame(PLAYER_NAME);

        Assert.assertEquals(0, game.getPlayerLevel());
        Assert.assertEquals(false, game.canSkipQuestion());
    }

    @Test
    public void playerCanLevelUp() {
        PreferenceManager sharedPreferences = PreferenceManager.getInstance();
        sharedPreferences.write("started", true);
        sharedPreferences.write("playerName", PLAYER_NAME);
        sharedPreferences.write("level", 3);
        sharedPreferences.write("title", "Clever Teenager");
        sharedPreferences.write("questionAnswered", 2);
        sharedPreferences.write("cookies", 10);
        sharedPreferences.write("cupcakes", 3);
        sharedPreferences.write("answeredQuestions", "q2,q3,q6");

        DummyListener listener = new DummyListener();

        game = GameLogic.getGame();
        game.setUpPreviousGame();
        game.setAchievementListener(listener);
        Assert.assertEquals(false, game.canProceed());
        Assert.assertEquals(true, listener.getLeveledUp());
    }

    @Test
    public void allQuestionsAnswered() {
        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.write("started", true);
        preferenceManager.write("playerName", PLAYER_NAME);
        QuestionDatabase questionDatabase = new QuestionDatabase();
        questionDatabase.generateQuestions();

        StringBuilder list = new StringBuilder();
        for (int i = 1; i <= questionDatabase.getSize(); i++) {
            list.append("q" + String.valueOf(i) + ",");
        }
        //store all questions in the player answered list.
        preferenceManager.write("answeredQuestions", list.toString());

        DummyListener listener = new DummyListener();
        game = GameLogic.getGame();
        game.setAchievementListener(listener);
        game.setUpPreviousGame();

        Assert.assertEquals(false, game.canProceed());
        Assert.assertEquals(true, listener.getEnded());

    }

    class DummyListener implements GameLogic.AchievementListener {
        private boolean leveledUp = false;
        private boolean ended = false;

        @Override
        public void leveledUp() {
            this.leveledUp = true;
        }

        @Override
        public void ended() {
            this.ended = true;
        }

        public Boolean getLeveledUp() {
            return leveledUp;
        }

        public Boolean getEnded() {
            return ended;
        }
    }
}
