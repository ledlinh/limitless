package de.htw.limitless.model;

public class Player {

    public static Player player;
    private String mName;
    private int mLevel;
    private String mTitle;
    private int mCookies;
    private int mQuestionsAnswered;
    private int mCupcakes;

    private Player(String name) {
        this.mName = name;
        this.mLevel = 0;
        this.mCookies = 0;
        this.mQuestionsAnswered = 0;
        this.mCupcakes = 0;
    }

    public static Player getPlayer(String name) {
        if (player == null & name != null) {
            player = new Player(name);
        }
        return player;
    }

    public String getName() {
        return mName;
    }

    public int getLevel() {
        return mLevel;
    }

    public void levelUp() {
        this.mLevel++;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getQuestionsAnswered() {
        return mQuestionsAnswered;
    }

    public void answeredOneQuestion() {
        this.mQuestionsAnswered++;
    }

    public void setQuestionsAnswered(int questions) {
        this.mQuestionsAnswered = questions;
    }

    public int getCookies() {
        return mCookies;
    }

    public void addCookie() {
        this.mCookies++;
    }

    public void useCookie() { this.mCookies--; }

    public void setCookies(int cookies) {
        this.mCookies = cookies;
    }

    public int getCupcakes() {
        return mCupcakes;
    }

    public void addCupcake() {
        this.mCupcakes++;
    }

    public void useCupcake() { this.mCupcakes--; }

    public void setCupcakes(int cupcakes) {
        this.mCupcakes = cupcakes;
    }

    public void delete() {
        player = null;
    }

}
