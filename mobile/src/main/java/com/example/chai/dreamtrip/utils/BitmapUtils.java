package com.example.chai.dreamtrip.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * This utility class handles the creation of <tt>Bitmap</tt>s
 *
 * @author Antonello Fodde
 * @author Leonardo Preti
 * @author Ragheed Faik
 */
public class BitmapUtils {

    /**
     * It returns a <tt>Bitmap</tt> using the image retrieved from the specified path
     * @param path the path of the image
     * @param context the <tt>Context</tt> of the application
     * @return the <tt>Bitmap</tt> created for the retrieved image
     */
    public static Bitmap getBitmap(String path, Context context) {
        InputStream file = null;
        Bitmap bitmap = null;
        try {
            file = context.getAssets().open(path);
            bitmap = BitmapFactory.decodeStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return bitmap;
    }
}
