package com.example.dizzer.testtaskprojectb.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dizzer.testtaskprojectb.R;
import com.example.dizzer.testtaskprojectb.constants.ConstantsForDB;
import com.example.dizzer.testtaskprojectb.constants.CustomConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Intent incomeIntent;
    private int followedIntent;

    private ImageView imageView;
    private String imageLink;
    private long id;
    private int statusForLink = CustomConstants.STATUS_UNKNOWN;
    private int oldStatusForLink;
    private long imageTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grantUriPermission(ConstantsForDB.CONTENT_AUTHORITY, ConstantsForDB.CONTENT_URI_IMAGES, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        imageView = (ImageView) findViewById(R.id.iv_main_activity);

        incomeIntent = getIntent();
        followedIntent = incomeIntent.getIntExtra(CustomConstants.INTENT_CODE, CustomConstants.LAUNCHER);
        imageLink = incomeIntent.getStringExtra(CustomConstants.INTENT_LINK);
        id = incomeIntent.getLongExtra(CustomConstants.INTENT_ID, 0);
        oldStatusForLink = incomeIntent.getIntExtra(CustomConstants.INTENT_STATUS, 0);

        showImage();
    }

    private void showImage() {

        switch (followedIntent) {
            case CustomConstants.LAUNCHER:
                closeApp();
                break;
            case CustomConstants.TEST_FRAGMENT:
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
                break;
            case CustomConstants.HISTORY_FRAGMENT:
                Picasso.with(MainActivity.this).load(imageLink).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        statusForLink = CustomConstants.STATUS_DOWNLOADED;
                        if (oldStatusForLink != statusForLink){
                            updateLink();
                        }else {
                            saveImage();
                            deleteLink(id);
                        }
                    }

                    @Override
                    public void onError() {
                        statusForLink = CustomConstants.STATUS_ERROR;
                        updateLink();
                    }
                });
                break;
        }

    }

    private void saveLink(int statusForLink) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsForDB.IMAGE_LINK, imageLink);
        contentValues.put(ConstantsForDB.IMAGE_STATUS, statusForLink);
        contentValues.put(ConstantsForDB.IMAGE_TIME, imageTime);
        getContentResolver().insert(ConstantsForDB.CONTENT_URI_IMAGES, contentValues);
        Toast.makeText(MainActivity.this, getString(R.string.link_saved_main_activity), Toast.LENGTH_SHORT).show();
    }

    private void saveImage() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String filename = Uri.parse(imageLink).getLastPathSegment();

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            File path = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "BIGDIG/test/B");
            path.mkdirs();
            File sdFile = new File(path, filename);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(sdFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();

                if (sdFile.exists()) {
                    Toast.makeText(this, getString(R.string.save_image_txt) + sdFile.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(MainActivity.this, getString(R.string.sd_card_error), Toast.LENGTH_LONG).show();
    }

    private void updateLink() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsForDB.IMAGE_STATUS, statusForLink);
        contentValues.put(ConstantsForDB.IMAGE_TIME, imageTime);
        getContentResolver().update(ConstantsForDB.CONTENT_URI_IMAGES, contentValues, "_id = " + id, null);
    }

    private void deleteLink(long id) {
        getContentResolver().delete(ConstantsForDB.CONTENT_URI_IMAGES, "_id = " + id, null);
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
