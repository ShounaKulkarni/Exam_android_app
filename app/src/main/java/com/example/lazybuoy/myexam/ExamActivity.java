package com.example.lazybuoy.myexam;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import javax.security.auth.Subject;

public class ExamActivity extends AppCompatActivity {

    private static final  String EXTRA_EMAILI="extraemaili";
    private static final long COUNTDOWN_IN_MILLIS = 20000;

    private static final  String KEY_SCORE = "keyscore";
    private static final  String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final  String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final  String KEY_ANSWERED = "keyAnswered";
    private static final  String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewCountdown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonconfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList getTextColorDefaultCd;


    private CountDownTimer countDownTimer;
    private long timeLeftMillis;

    private ArrayList<Question> questionList;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewCountdown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonconfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        getTextColorDefaultCd = textViewCountdown.getTextColors();


        Intent myintent = getIntent();
        email=myintent.getStringExtra(OnlineActivity.EXTRA_MAIL);

        TextView text = (TextView) findViewById(R.id.editmail);
        text.setText(" Email:" +email);

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(OnlineActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(OnlineActivity.EXTRA_CATEGORY_NAME);



        textViewCategory.setText("Subject: " + categoryName);



        if (savedInstanceState == null) {


            TestDbHelper dbHelper = TestDbHelper.getInstance(this);

            questionList = dbHelper.getQuestions(categoryID);

            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        }
        else
        {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter -1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);


            if (!answered)
            {
                startCounrDown();
            }
            else
            {
                updateCountDownText();
                showSolution();
            }
        }
        buttonconfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered)
                {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                    {
                        checkAnswer();
                    }
                    else
                    {
                        Toast.makeText(ExamActivity.this, "Select an Answer", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion()
    {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal)
        {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonconfirmNext.setText("Confirm");

            timeLeftMillis = COUNTDOWN_IN_MILLIS;
            startCounrDown();
        }
        else
        {
            finishTest();
        }
    }

    private void startCounrDown()
    {
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftMillis = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                timeLeftMillis = 0;
                updateCountDownText();
                checkAnswer();

            }
        }.start();
    }

    private void updateCountDownText()
    {
        int minutes = (int) (timeLeftMillis / 1000) / 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountdown.setText(timeFormatted);

        if (timeLeftMillis< 10000) {
            textViewCountdown.setTextColor(Color.RED);
        } else {
            textViewCountdown.setTextColor(getTextColorDefaultCd);
        }
    }

    private void checkAnswer()
    {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());

        int answer = rbGroup.indexOfChild(rbSelected) + 1;

        if(answer == currentQuestion.getAnswer())
        {
            score++;
            textViewScore.setText("Score: " + score);

        }

        showSolution();
    }

    private void showSolution()
    {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer())
        {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;

            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;

            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }

        if (questionCounter < questionCountTotal)
        {
            buttonconfirmNext.setText("Next");
        }
        else
        {
            buttonconfirmNext.setText("Finish");
        }
    }

    private void finishTest()
    {
        sendEmail();


        buttonconfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ExamActivity.this,LogoutActivity.class);
                startActivity(i);
            }
        });


    }

    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Online Examination Result");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your student has secured:"+score + " Out of " + questionCounter+  "\nThank you");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ExamActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    public void onBackPressed()
    {
        if (backPressedTime + 2000 > System.currentTimeMillis())
        {
            finishTest();
        }
        else
        {
            Toast.makeText(this, "Press back again to Finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}