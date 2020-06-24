package de.htw.limitless.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.htw.limitless.R;
import de.htw.limitless.controller.GameLogic;

public class LevelUpActivity extends AppCompatActivity {

    private TextView mLevelView;
    private TextView mTitleView;
    private Button mContinueBtn;

    private GameLogic game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_up);

        mLevelView = findViewById(R.id.levelAchievementView);
        mTitleView = findViewById(R.id.titleAchievementView);
        mContinueBtn = findViewById(R.id.continueBtn);

        game = GameLogic.getGame();
        game.setUpPreviousGame();

        mLevelView.setText("Your level: " + String.valueOf(game.getPlayerLevel()));
        mTitleView.setText("Your title " + game.getPlayerTitle());

        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivity = new Intent(LevelUpActivity.this, GameActivity.class);
                startActivity(gameActivity);
            }
        });
    }
}