package com.example.dizzer.testtaskprojectb.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dizzer.testtaskprojectb.R;
import com.example.dizzer.testtaskprojectb.constants.ConstantsForDB;
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
    private long imageTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grantUriPermission(ConstantsForDB.CONTENT_AUTHORITY,ConstantsForDB.CONTENT_URI_IMAGES,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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

    private void showImage() {
        Picasso.with(MainActivity.this).load(imageLink).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                statusForLink = CustomConstants.STATUS_DOWNLOADED;
                saveLink(statusForLink);
            }

            @Override
            public void onError() {
                statusForLink = CustomConstants.STATUS_ERROR;
                saveLink(statusForLink);
            }
        });
    }

    private void saveLink(int statusForLink) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsForDB.IMAGE_LINK, imageLink);
        contentValues.put(ConstantsForDB.IMAGE_STATUS, statusForLink);
        contentValues.put(ConstantsForDB.IMAGE_TIME, imageTime);
        getContentResolver().insert(ConstantsForDB.CONTENT_URI_IMAGES,contentValues);
        Toast.makeText(MainActivity.this,getString(R.string.link_saved_main_activity),Toast.LENGTH_SHORT).show();
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
