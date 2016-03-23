package com.jiggie.android.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import com.jiggie.android.App;

/**
 * Created by rangg on 14/01/2016.
 */
public class BitmapUtility {
    private static final RenderScript renderScript
            = RenderScript.create(App.getInstance());
    public static final float BLUR_RADIUS_DEFAULT = 15F;

    public static Bitmap blur(Bitmap image) {
        if (null == image) return null;

        final Bitmap outputBitmap = Bitmap.createBitmap(image);
        final Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        final Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        final ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS_DEFAULT);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public static Bitmap getBitmapResource(int resourceId) { return BitmapFactory.decodeResource(App.getInstance().getResources(), resourceId); }
}
