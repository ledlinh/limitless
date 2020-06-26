package de.htw.limitless.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.htw.limitless.R;
import de.htw.limitless.controller.GameLogic;

public class GameEndedActivity extends AppCompatActivity {

    private TextView mLevelView;
    private TextView mTitleView;
    private Button mPlayAgainBtn;
    private GameLogic game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ended);

        mLevelView = findViewById(R.id.levelEndedView);
        mTitleView = findViewById(R.id.titleEndedView);
        mPlayAgainBtn = findViewById(R.id.playAgainWonBtn);

        game = GameLogic.getGame();
        game.setUpPreviousGame();

        mLevelView.setText("Your level: " + String.valueOf(game.getPlayerLevel()));
        mTitleView.setText("Your title " + game.getPlayerTitle());

        mPlayAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.reset();
                Intent mainActivity = new Intent(GameEndedActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });

    }
}