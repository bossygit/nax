package com.nasande.nasande;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class FilesHelper {

    public String sendPath(Uri fileUri, Context context){

        // Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(fileUri);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;


    }

    public String sendAudioPath(Uri fileUri, Context context){

        // Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(fileUri);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Audio.Media.DATA };

// where id is equal to
        String sel = MediaStore.Audio.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;


    }
}
