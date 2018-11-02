package com.shalom.itai.myglobalphotos;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.util.List;

/**
 * Proudly written by Itai on 13/10/2018.
 * DB interactor - Interacts with the local db.
 */

class DBInteractor {
    private static final DBInteractor ourInstance = new DBInteractor();

    static DBInteractor getInstance() {
        return ourInstance;
    }

    private DBInteractor() {
    }

    void getAllImages(Context context, final GpsPhotoListPresenter presenter) {
        Uri uri;
        Cursor cursor;
        int column_index_data;

        String absolutePathOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.DATE_MODIFIED};

        cursor = context.getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                presenter.addImageToList(absolutePathOfImage);
            }
            cursor.close();
        }
        presenter.finishedReading();
    }
}
