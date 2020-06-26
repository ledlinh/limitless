package de.htw.limitless.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.htw.limitless.R;
import de.htw.limitless.controller.GameLogic;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mStartButton;
    private GameLogic game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGreetingText = findViewById(R.id.greetingText);
        mNameInput = findViewById(R.id.nameInput);
        mStartButton = findViewById(R.id.startBtn);

        game = GameLogic.getGame();
        if (game.started()) {
            retrieveGame();
        } else {
            setUpNewGame();
        }
    }

    private void setUpNewGame() {
        mGreetingText.setText("Welcome to Limitless");
        mNameInput.setVisibility(View.VISIBLE);

        mStartButton.setEnabled(false);

        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStartButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.setUpNewGame(mNameInput.getText().toString());
                startGameActivity();
            }
        });
    }

    private void retrieveGame() {
        mNameInput.setVisibility(View.GONE);
        game.setUpPreviousGame();
        mGreetingText.setText("Welcome back to Limitless, " + game.getPlayerName());

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity();
            }
        });
    }

    private void startGameActivity() {
        if (game.canStart()) {
            Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
            startActivity(gameActivity);
        } else {
            Intent maintenanceActivity = new Intent(MainActivity.this, MaintenanceActivity.class);
            startActivity(maintenanceActivity);
        }
    }
}
