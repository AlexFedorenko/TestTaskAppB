package com.example.dizzer.testtaskprojectb.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.dizzer.testtaskprojectb.R;
import com.example.dizzer.testtaskprojectb.constants.ConstantsForDB;
import com.example.dizzer.testtaskprojectb.constants.CustomConstants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dizzer on 2/26/2018.
 */

public class DeleteService extends IntentService{

    private long id;
    private Handler handler;

    public DeleteService() {
        super("Delete Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        handler = new Handler();
        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        id = intent.getLongExtra(CustomConstants.INTENT_ID, 0);
        getContentResolver().delete(ConstantsForDB.CONTENT_URI_IMAGES, "_id = " + id, null);
        showMsg();
    }

    private void showMsg(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.image_delete_msg),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
