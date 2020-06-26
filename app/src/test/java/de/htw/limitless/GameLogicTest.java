package de.htw.limitless;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.htw.limitless.controller.GameLogic;
import de.htw.limitless.controller.PreferenceManager;
import de.htw.limitless.view.MainActivity;

public class GameLogicTest {

    private GameLogic game;

    @Test
    public void testIfGameCanStart() {
        PreferenceManager prefs = Mockito.mock(PreferenceManager.class);
        MainActivity mainActivity = Mockito.mock(MainActivity.class);


        String playerName = "name";
        if (game.started()) {
            game.setUpPreviousGame();
        } else {
            game.setUpNewGame(playerName);
        }

        Assert.assertEquals("true", game.canStart());

    }
}
