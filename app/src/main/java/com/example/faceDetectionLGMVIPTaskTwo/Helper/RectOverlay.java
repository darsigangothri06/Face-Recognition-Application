package com.example.faceDetectionLGMVIPTaskTwo.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic{
    private int colorForRect = Color.GREEN;
    private Paint paintForRect;
    private Rect rect;
    private float widthStroke = 4.0f;
    private GraphicOverlay overlayGraphics;

    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        paintForRect = new Paint();
        paintForRect.setColor(colorForRect);
        paintForRect.setStyle(Paint.Style.STROKE);
        paintForRect.setStrokeWidth(widthStroke);
        this.overlayGraphics = graphicOverlay;
        this.rect = rect;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectFObj =new RectF(rect);
        rectFObj.left = translateX(rectFObj.left);
        rectFObj.right = translateX(rectFObj.right);
        rectFObj.bottom = translateX(rectFObj.bottom);
        rectFObj.top = translateX(rectFObj.top);
        canvas.drawRect(rectFObj, paintForRect);
    }
}