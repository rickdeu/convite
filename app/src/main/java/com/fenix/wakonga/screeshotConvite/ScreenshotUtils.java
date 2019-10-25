package com.fenix.wakonga.screeshotConvite;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenshotUtils {
    public static Bitmap getScreenshot(View view){
        View screenView=view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap=Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public static File getMainDirectoryName(Context context){
      /*  File mainDir=new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "convidados");*/

        File mainDir = new File(context.getFilesDir(), "images");
        //File newFile = new File(imagePath, "default_image.jpg");





        if (!mainDir.exists()){
            if (mainDir.mkdir()){
                Log.e("Create Directory", "Main Directory Created: "+mainDir);

            }
        }
        return mainDir;
    }
    public static File store(Bitmap bm, String fileName, File saveFilePath){
        File dir=new File(saveFilePath.getAbsolutePath());
        if (!dir.exists()){
            dir.mkdir();
        }



        File file=new File(saveFilePath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }
}
