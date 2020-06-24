package de.htw.limitless.controller;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferenceManager {

    private static PreferenceManager instance;
    private static SharedPreferences sharedPreferences;
    private static Context appContext;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private PreferenceManager() {
        appContext = Game.getAppContext();
        String packageName = appContext.getPackageName();
        sharedPreferences = appContext.getSharedPreferences(packageName, appContext.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public static PreferenceManager getInstance() {
        if (instance == null) {
            instance = new PreferenceManager();
        }
        return instance;
    }

    public void write(String key, String value) {
        sharedPreferencesEditor.putString(key, value);
        sharedPreferencesEditor.apply();
    }

    public void write(String key, Integer value) {
        sharedPreferencesEditor.putInt(key, value);
        sharedPreferencesEditor.apply();
    }

    public void write(String key, Boolean value) {
        sharedPreferencesEditor.putBoolean(key, value);
        sharedPreferencesEditor.apply();
    }

    public String readString(String key) {
        return sharedPreferences.getString(key,"");
    }

    public Integer readInt(String key) {
        return sharedPreferences.getInt(key,0);
    }

    public Boolean readBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void clear() {
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.apply();
    }

}
