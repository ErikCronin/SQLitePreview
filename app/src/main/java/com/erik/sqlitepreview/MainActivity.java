package com.erik.sqlitepreview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private static final int REQUEST_CODE_QUIZ = 1;
    private TextView textViewHighscore;
    private Spinner spinnerDifficulty;

    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);

        String[] difficultyLevels = Question.getAllDifficultyLevels();

        //Creates the layout for the difficulty spinner and displays it
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        //adapterDifficulty for aesthetics
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);

        loadHighscore();
    }

    public void startQuiz(View view) {
        //This string will pass the chosen difficulty
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}
