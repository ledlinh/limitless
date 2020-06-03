package de.htw.limitless.controller;

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
import de.htw.limitless.model.AppExecutors;
import de.htw.limitless.model.Player;
import de.htw.limitless.model.PlayerDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mStartButton;
    private Player mPlayer;
    private PlayerDatabase playerDB;
    private static final int MAIN_ACTIVITY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGreetingText = findViewById(R.id.greetingText);
        mNameInput = findViewById(R.id.nameInput);
        mStartButton = findViewById(R.id.startBtn);

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
                mPlayer = new Player(mNameInput.getText().toString());
                playerDB = PlayerDatabase.getInstance(MainActivity.this);

                AppExecutors.getInstance().diskIO().execute(new Runnable() { //Run database on a different thread

                    @Override
                    public void run() {
                        playerDB.getPlayerDAO().insert(mPlayer);
                    }
                });
                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                startActivity(gameActivity);
            }
        });
    }

}
