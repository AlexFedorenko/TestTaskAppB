package com.example.dizzer.testtaskprojectb.constants;

import android.net.Uri;

/**
 * Created by Dizzer on 2/23/2018.
 */

public class ConstantsForDB {
    public static final String CONTENT_AUTHORITY = "com.example.dizzer.testtaskappa.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String IMAGES_PATH = "images";
    public static final Uri CONTENT_URI_IMAGES = BASE_CONTENT_URI.buildUpon().appendPath(IMAGES_PATH).build();

    private static final String IMAGE_TABLE_NAME = "images";

    public static final String IMAGE_ID = "_id";
    public static final String IMAGE_LINK = "link";
    public static final String IMAGE_STATUS = "status";
    public static final String IMAGE_TIME = "time";
}
