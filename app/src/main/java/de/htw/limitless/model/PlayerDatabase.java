package de.htw.limitless.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Player.class}, version = 3)
public abstract class PlayerDatabase extends RoomDatabase {
    private static final String LOG_TAG = PlayerDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "player_db";
    private static PlayerDatabase sInstance; //Singleton instance

    public static PlayerDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new player instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), PlayerDatabase.class, PlayerDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration().build();
            }
        }
        Log.d(LOG_TAG, "Getting the player instance");
        return sInstance;
    }

    public abstract PlayerDAO getPlayerDAO();
}
