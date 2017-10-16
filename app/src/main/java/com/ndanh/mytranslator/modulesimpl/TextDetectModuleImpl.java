package com.ndanh.mytranslator.modulesimpl;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.ndanh.mytranslator.model.DetectResult;
import com.ndanh.mytranslator.services.ITextDetect;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndanh on 4/18/2017.
 */

public class TextDetectModuleImpl implements ITextDetect {
    private final static String TAG = "TextDetectModuleImpl";
    //private TessBaseAPI mTess;
    private  String datapath = "";
    private Context mContext;
    private DetectBitmapCallback callback;

    public TextDetectModuleImpl(Context context) {
        this.mContext = context;
        datapath = context.getFilesDir()+ "/tesseract/";

    }

    private TextDetectModuleImpl() {

    }

    @Override
    public void release() {
        callback = null;
       // mTess = null;
    }

    @Override
    public void setLanguage(String lang) {
        //checkFile(new File(datapath + "tessdata/"), lang);
        //mTess = new TessBaseAPI();
        //mTess.init(datapath, lang);
    }

    @Override
    public void detectBitmap(Bitmap bitmap) {
//        mTess.setImage ( bitmap );
//        mTess.getUTF8Text();
//        ResultIterator iterator = mTess.getResultIterator ();
//        int level = TessBaseAPI.PageIteratorLevel.RIL_WORD;
//        List<DetectResult> result = new ArrayList<DetectResult> ();
//        DetectResult item = new DetectResult ();
//        while (iterator.next(level)){
//            if(iterator.confidence ( level ) < 80) continue;
//            if(iterator.getUTF8Text ( level ) == "1")continue;
//            if(checkPosition ( item ,iterator.getBoundingRect ( level ))){
//                item.merge (iterator.getBoundingRect ( level ) ,iterator.getUTF8Text ( level ));
//            } else {
//                item = new DetectResult ();
//                item.setSrcText(iterator.getUTF8Text ( level ));
//                item.setPosition (iterator.getBoundingRect ( level ));
//                result.add ( item );
//            }
//        }
//        if(callback != null){
//            callback.onSuccess ( result);
//        }
    }

    @Override
    public String detectMat(Mat mat) {
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap ( mat, bm );
        //mTess.setImage ( bm );
        //String temp = mTess.getUTF8Text();
        bm.recycle ();
        return "";
    }

    @Override
    public String detectbyte(byte[] dataArr, int w, int h, int bpp, int bpl) {
      //  mTess.setImage ( dataArr , w, h, bpp ,bpl );
      return "";
    }

    @Override
    public void setMat(Mat mat) {
        byte[] dataArr = new byte[(int) (mat.total () * mat.channels ())];
        mat.get ( 0,0,dataArr );
       // mTess.setImage ( dataArr , mat.width (), mat.height (), mat.channels () , mat.cols () * mat.channels () );
    }

    @Override
    public String getData(org.opencv.core.Rect rect) {
       // mTess.setRectangle ( rect.x, rect.y, rect.x + rect.width , rect.y + rect.height );
      return "";
    }

    private void checkFile(File dir, String lang) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles(lang);
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/"+ lang +".traineddata";
            File datafile = new File(datafilepath);


            if (!datafile.exists()) {
                copyFiles(lang);
            }
        }

    }

    private void copyFiles(String lang) {
        try {
            String filepath = datapath +"/tessdata/"+ lang +".traineddata";
            AssetManager assetManager = mContext.getAssets();

            InputStream instream = assetManager.open("tessdata/"+ lang +".traineddata");
            OutputStream outstream = new FileOutputStream (filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File (filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDetectBitmapCallback(DetectBitmapCallback callback){
        this.callback = callback;
    }

    private boolean checkPosition(DetectResult item, Rect rect2){
        if(item.getPosition () == null ||item.getSrcText()== null || rect2 == null)
            return false;

        if(item.getSrcText().contains(".")) return false;
        Rect rect1 = item.getPosition ();
        if(rect1.bottom < rect2.top ){
            return false;
        }

        int spaceSize = rect1.height () / 2;
        int distance = rect2.left - rect1.right;
        return distance <= spaceSize;
    }
}
