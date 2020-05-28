package com.example.mynote.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static boolean checkFileExists(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean createFile(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
