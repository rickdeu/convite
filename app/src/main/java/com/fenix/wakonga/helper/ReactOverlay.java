package com.fenix.wakonga.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class ReactOverlay extends GraphicOverlay.Graphic {
    private static final int RECT_COLOR=Color.YELLOW;
    private static final float STORE_WIDTH=8.0f;
    private final Paint rectPaint;
    private final Rect bounds;

    public ReactOverlay(GraphicOverlay overlay, Rect bounds) {
        super(overlay);
        this.bounds=bounds;
        rectPaint=new Paint();
        rectPaint.setColor(RECT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STORE_WIDTH);
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        if (bounds==null){
            throw new IllegalStateException("Attempting to draw a null bounds");
        }
        RectF rectF=new RectF(bounds);
        canvas.drawRect(rectF, rectPaint);

    }
}
