package de.htw.limitless.controller;

import android.content.SharedPreferences;

import de.htw.limitless.model.Player;

public class GameLogic {

    public static GameLogic game;
    private static Player mPlayer;
    private String[] mTitleList = new String[]{"Not There", "Baby", "Smart Kid", "Clever Teenager", "Genius Youngster", "Overachiever", "Scholar", "Pioneer", "Inventor", "Walking Intelligence", "Einstein"};
    private static SharedPreferences sharedPreferences;

    public static GameLogic getGame(String playerName, SharedPreferences sharedPreferences) {
        if (game == null) {
            game = new GameLogic();
            mPlayer = Player.getPlayer(playerName);
            updateSharedPreferences(sharedPreferences);
        } else {
            updatePlayer(sharedPreferences);
        }
        return game;
    }

    public void answeredOneQuestion() {
        mPlayer.addCookie();
        mPlayer.answeredOneQuestion();
        updateSharedPreferences(sharedPreferences);

        if (mPlayer.getQuestionsAnswered() == 5) {
            levelUp();
        }
    }

    public void levelUp() {
        mPlayer.levelUp();
        mPlayer.addCupcake();
        mPlayer.setTitle(mTitleList[mPlayer.getLevel()]);
        mPlayer.setQuestionsAnswered(0);
        updateSharedPreferences(sharedPreferences);
    }

    private static void updateSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("level", mPlayer.getLevel());
        editor.putString("title", mPlayer.getTitle());
        editor.putInt("cookies", mPlayer.getCookies());
        editor.putInt("questionAnswered", mPlayer.getQuestionsAnswered());
        editor.putInt("cupcakes", mPlayer.getCupcakes());

        editor.apply();
    }

    public static void updatePlayer(SharedPreferences sharedPreferences) {
        mPlayer = Player.getPlayer(sharedPreferences.getString("playerName", ""));
        mPlayer.setLevel(sharedPreferences.getInt("level", 0));
        mPlayer.setCookies(sharedPreferences.getInt("cookies", 0));
        mPlayer.setQuestionsAnswered(sharedPreferences.getInt("questionAnswered", 0));
        mPlayer.setCupcakes(sharedPreferences.getInt("cupcakes", 0));
    }
}
