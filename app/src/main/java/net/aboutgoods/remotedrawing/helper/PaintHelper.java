package net.aboutgoods.remotedrawing.helper;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * The type Paint helper.
 */
public class PaintHelper {

    /**
     * Create paint from rgb paint.
     *
     * @param rgb the rgb
     * @return the paint
     */
    public static Paint createPaintFromRGB(String rgb) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor(rgb));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
        return paint;
    }

    /**
     * Gets circle paint.
     *
     * @return the circle paint
     */
    public static Paint getCirclePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeWidth(16);
        return paint;
    }

    /**
     * Gets blueprint paint.
     *
     * @return the blueprint paint
     */
    public static Paint getBluePrintPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeWidth(8);
        return paint;
    }
}
