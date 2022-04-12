package com.example.faceDetectionLGMVIPTaskTwo.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.vision.CameraSource;

import java.util.HashSet;
import java.util.Set;

public class GraphicOverlay extends View {
    private final Object lockObject = new Object();
    private int previewHeight;
    private int previewWidth;
    private float scalarWidthFactr = 1.0f;
    private float scalarHeightFactor = 1.0f;
    private int FaceCam = CameraSource.CAMERA_FACING_BACK;
    private Set<Graphic> overallGraphics = new HashSet<>();


    public static abstract class Graphic {
        private GraphicOverlay overlayGraphical;

        public Graphic(GraphicOverlay overlay) {
            overlayGraphical = overlay;
        }
        public float scaleX(float horizontal) {
            return horizontal * overlayGraphical.scalarWidthFactr;
        }
        public abstract void draw(Canvas canvas);
        public float translateX(float x) {
            if (overlayGraphical.FaceCam == CameraSource.CAMERA_FACING_FRONT) {
                return overlayGraphical.getWidth() - scaleX(x);
            } else {
                return scaleX(x);
            }
        }
        public float scaleY(float vertical) {
            return vertical * overlayGraphical.scalarHeightFactor;
        }
        public float translateY(float y) {
            return scaleY(y);
        }
        public void postInvalidate() {
            overlayGraphical.postInvalidate();
        }
    }

    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void add(Graphic graphic) {
        synchronized (lockObject) {
            overallGraphics.add(graphic);
        }
        postInvalidate();
    }
    public void clear() {
        synchronized (lockObject) {
            overallGraphics.clear();
        }
        postInvalidate();
    }
    public void remove(Graphic graphic) {
        synchronized (lockObject) {
            overallGraphics.remove(graphic);
        }
        postInvalidate();
    }

    public void setCameraInfo(int previewWidth, int previewHeight, int facing) {
        synchronized (lockObject) {
            this.previewWidth = previewWidth;
            this.previewHeight = previewHeight;
            FaceCam = facing;
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        synchronized (lockObject) {
            if ((previewWidth != 0) && (previewHeight != 0)) {
                scalarWidthFactr = (float) canvas.getWidth() / (float) previewWidth;
                scalarHeightFactor = (float) canvas.getHeight() / (float) previewHeight;
            }

            for (Graphic graphic : overallGraphics) {
                graphic.draw(canvas);
            }
        }
    }
}