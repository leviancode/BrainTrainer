package com.leviancode.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button mGoButton;
    private Button mPlayAgainButton;
    private Button mButton0;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private TextView mTimerTextView;
    private TextView mScoreTextView;
    private TextView mResultTextView;
    private TextView mQuestionTextView;
    private TableLayout mTableLayout;

    private final int MAX_ADDEND = 30;
    private final int MAX_TIME = 30000;
    private Random mRandom = new Random();
    private List<Integer> mAnswers = new ArrayList<>();
    private CountDownTimer mCountDownTimer;

    private int mScore;
    private int mQuestionCount;
    private int mCorrectAnswerPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoButton = findViewById(R.id.goButton);
        mPlayAgainButton = findViewById(R.id.playAgainButton);
        mButton0 = findViewById(R.id.button0);
        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);

        mTimerTextView = findViewById(R.id.timerTextView);
        mScoreTextView = findViewById(R.id.scoreTextView);
        mResultTextView = findViewById(R.id.resultTextView);
        mQuestionTextView = findViewById(R.id.questionTextView);

        mTableLayout = findViewById(R.id.tableLayout);
    }

    public void onGoButtonClicked(View view) {
        updateUI();
        createCountDownTimer();
        startGame();
    }

    private void startGame() {
        mScore = 0;
        mQuestionCount = 0;
        updateScore();

        mCountDownTimer.start();
        generateNewQuestion();
    }

    private void generateNewQuestion() {
        int a = mRandom.nextInt(MAX_ADDEND + 1);
        int b = mRandom.nextInt(MAX_ADDEND + 1);
        int correctAnswer = a + b;
        String question = a + " + " + b;
        mQuestionTextView.setText(question);

        generateAnswers(correctAnswer);

        mButton0.setText(String.valueOf(mAnswers.get(0)));
        mButton1.setText(String.valueOf(mAnswers.get(1)));
        mButton2.setText(String.valueOf(mAnswers.get(2)));
        mButton3.setText(String.valueOf(mAnswers.get(3)));
    }

    private void generateAnswers(int correctAnswer){
        mAnswers.clear();
        mCorrectAnswerPosition = mRandom.nextInt(4);

        for (int i = 0; i < 4; i++) {
            if (i == mCorrectAnswerPosition){
                mAnswers.add(correctAnswer);
            } else {
                int wrongAnswer = mRandom.nextInt(MAX_ADDEND * 2 + 1);

                while (wrongAnswer == correctAnswer){
                    wrongAnswer = mRandom.nextInt(MAX_ADDEND * 2 + 1);
                }
                mAnswers.add(wrongAnswer);
            }
        }
    }

    private void createCountDownTimer(){
        mCountDownTimer = new CountDownTimer(MAX_TIME, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int timeInSec = (int) (millisUntilFinished / 1000);
                StringBuilder timeBuilder = new StringBuilder();

                if (timeInSec < 10){
                    timeBuilder.append(0);
                }
                timeBuilder.append(timeInSec).append("s");

                mTimerTextView.setText(timeBuilder.toString());
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        };
    }

    private void gameOver() {
        mResultTextView.setText(R.string.gameOver);
        mPlayAgainButton.setVisibility(View.VISIBLE);

        mButton0.setEnabled(false);
        mButton1.setEnabled(false);
        mButton2.setEnabled(false);
        mButton3.setEnabled(false);
    }

    private void updateUI(){
        mGoButton.setVisibility(View.INVISIBLE);
        mQuestionTextView.setVisibility(View.VISIBLE);
        mTableLayout.setVisibility(View.VISIBLE);
        mResultTextView.setVisibility(View.VISIBLE);
    }

    public void chooseAnswer(View view) {
        String tag = view.getTag().toString();

        if (Integer.parseInt(tag) == mCorrectAnswerPosition){
            mResultTextView.setText(R.string.correctAnswer);
            mScore++;
        } else {
            mResultTextView.setText(R.string.wrongAnswer);
        }
        mQuestionCount++;
        updateScore();
        generateNewQuestion();
    }

    private void updateScore() {
        StringBuilder scoreBuilder = new StringBuilder();
        if (mScore < 10){
            scoreBuilder.append(0);
        }
        scoreBuilder.append(mScore).append("/");
        if (mQuestionCount < 10){
            scoreBuilder.append(0);
        }
        scoreBuilder.append(mQuestionCount);
        mScoreTextView.setText(scoreBuilder);
    }

    public void playAgain(View view) {
        mPlayAgainButton.setVisibility(View.INVISIBLE);
        mButton0.setEnabled(true);
        mButton1.setEnabled(true);
        mButton2.setEnabled(true);
        mButton3.setEnabled(true);

        startGame();
    }
}