package com.sudosystems.utilities;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils
{
    public Bitmap getScaledImage(Bitmap originalImage, int width, int height)
    {
        int currentWidth    = originalImage.getWidth();
        int currentHeight   = originalImage.getHeight();
        float scaleWidth    = ((float) width) / currentWidth;
        float scaleHeight   = ((float) height) / currentHeight;
        Matrix matrix        = new Matrix();
        
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(originalImage, 0, 0, currentWidth, currentHeight, matrix, true);
    }
}
