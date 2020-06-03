package de.htw.limitless.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDAO {

    @Query("SELECT * FROM player")
    List<Player> listAllPlayers();

    @Query("SELECT * FROM player WHERE pid LIKE :playerId LIMIT 1")
    Player findByIds(int playerId);

    @Query("SELECT * FROM player WHERE mName LIKE :name LIMIT 1")
    Player findByName(String name);

    @Insert
    void insert(Player... players);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);
}
