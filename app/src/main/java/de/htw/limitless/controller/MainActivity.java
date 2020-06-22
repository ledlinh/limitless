package de.htw.limitless.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.htw.limitless.R;
import de.htw.limitless.model.Player;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mStartButton;
    private GameLogic game;
    private SharedPreferences sharedPreferences;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGreetingText = findViewById(R.id.greetingText);
        mNameInput = findViewById(R.id.nameInput);
        mStartButton = findViewById(R.id.startBtn);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences.contains("playerName")) {
            retrievePlayer();
        } else {
            setUpNewPlayer();
        }
    }

    private void setUpNewPlayer() {
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
                game = GameLogic.getGame(mNameInput.getText().toString(), sharedPreferences);
                startGameActivity();
            }
        });
    }

    private void retrievePlayer() {
        mNameInput.setVisibility(View.GONE);

        String playerName = sharedPreferences.getString("playerName", "");
        game = GameLogic.getGame(playerName, sharedPreferences);
        mGreetingText.setText("Welcome back to Limitless, " + playerName);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity();
            }
        });
    }

    private void startGameActivity() {
        Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
        startActivity(gameActivity);
    }
}
