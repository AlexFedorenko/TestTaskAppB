package com.example.dizzer.testtaskprojectb.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dizzer.testtaskprojectb.R;
import com.example.dizzer.testtaskprojectb.constants.CustomConstants;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Intent incomeIntent;
    private int followedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incomeIntent = getIntent();
        followedIntent = incomeIntent.getIntExtra(CustomConstants.INTENT_CODE, CustomConstants.LAUNCHER);

        if (followedIntent == CustomConstants.LAUNCHER){
            closeApp();
        }
    }

    private void closeApp() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getString(R.string.title_for_dialog));
        new CountDownTimer(11000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (!isFinishing()) {
                    progressDialog.setMessage(getString(R.string.main_activity_massage) + " " +
                            millisUntilFinished / 1000 + " " + getString(R.string.main_activity_seconds_text));
                    progressDialog.show();
                }
            }

            public void onFinish() {
                progressDialog.dismiss();
                finish();
            }
        }.start();
    }
}
