package com.example.dizzer.testtaskprojectb.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.dizzer.testtaskprojectb.R;
import com.example.dizzer.testtaskprojectb.constants.CustomConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Intent incomeIntent;
    private int followedIntent;

    private ImageView imageView;
    private String imageLink;
    private int statusForLink = CustomConstants.STATUS_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.iv_main_activity);

        incomeIntent = getIntent();
        followedIntent = incomeIntent.getIntExtra(CustomConstants.INTENT_CODE, CustomConstants.LAUNCHER);
        imageLink = incomeIntent.getStringExtra(CustomConstants.INTENT_LINK);

        switch (followedIntent) {
            case CustomConstants.LAUNCHER:
                closeApp();
                break;
            case CustomConstants.TEST_FRAGMENT:
                showImage();
                break;
            case CustomConstants.HISTORY_FRAGMENT:
                break;
        }
    }

    private void showImage(){
        Picasso.with(MainActivity.this).load(imageLink).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                statusForLink = CustomConstants.STATUS_DOWNLOADED;
                saveLink();
            }

            @Override
            public void onError() {
                statusForLink = CustomConstants.STATUS_ERROR;
                saveLink();
            }
        });
    }

    private void saveLink() {
        //TODO Realize save to DB
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
