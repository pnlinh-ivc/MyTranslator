package com.ndanh.mytranslator.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class IOUtil {
    /**
     * If file not exist -> mkdirs
     */
    public static boolean copyFile(InputStream is, String to, boolean isOveride) {
        if (!isOveride) {
            if (new File(to).exists())
                return true;
        }
        mkdirIfNeed(to);
        boolean isSuccess = true;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(to);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    public static boolean copyAsset(Context context, String assetFilePath, String filePath, boolean isOveride) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inStream = assetManager.open(assetFilePath);
            return copyFile(inStream, filePath, isOveride);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean copyRaw(Context context, int res, String filePath, boolean isOveride) {
        try {
            InputStream inStream = context.getResources().openRawResource(res);
            return copyFile(inStream, filePath, isOveride);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String copyRawToFileInternal(Context context, int res, String subFolder, String filename, boolean isOveride) {
        try {
            InputStream inStream = context.getResources().openRawResource(res);
            String internalFileDir = context.getFilesDir().getAbsolutePath();
            String filePath = internalFileDir + File.separator + subFolder + File.separator + filename;
            if (new File(filePath).exists())
                return filePath;
            if (copyFile(inStream, filePath, isOveride))
                return filePath;
            else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String copyAssetToFileInternal(Context context, String assetFilepath, String subFolder, String filename, boolean isOveride) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inStream = assetManager.open(assetFilepath);
            String internalFileDir = context.getFilesDir().getAbsolutePath();
            String filePath = internalFileDir + File.separator + subFolder + File.separator + filename;
            if (new File(filePath).exists())
                return filename;
            if (copyFile(inStream, filePath, isOveride))
                return filePath;
            else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static void mkdirIfNeed(String filePath) {
        int i = filePath.lastIndexOf(File.separator);
        if (i > 0) {
            String folder = filePath.substring(0, i);
            File file = new File(folder);
            if (!file.exists())
                file.mkdirs();
        }
    }

}
