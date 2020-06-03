package de.htw.limitless;

import org.junit.Assert;
import org.junit.Test;

import de.htw.limitless.controller.MainActivity;
import de.htw.limitless.model.Player;

public class PlayerTest {

    @Test
    public void playerTest() {
        Player player = new Player("Jane");
        player.levelUp();

        Assert.assertEquals(1, player.getLevel());

    }

    @Test
    public void highLevelPlayerTest() {
        Player player = new Player("Tim");
        player.setLevel(10);

        Assert.assertEquals("Einstein", player.getTitle());
    }
}
