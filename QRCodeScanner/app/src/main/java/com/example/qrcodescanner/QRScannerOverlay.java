package com.example.qrcodescanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class QRScannerOverlay extends View {

    private Paint overlayPaint, cornerPaint, linePaint;
    private int cornerWidth = 50;
    private RectF scanRect;
    private boolean showScanLine = true;
    private int cornerLength = 100;
    private float scanLinePosition = 0.5f;

    public QRScannerOverlay(Context context) {
        super(context);
        init();
    }

    public QRScannerOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        overlayPaint = new Paint();
        overlayPaint.setColor(Color.parseColor("#80000000"));
        overlayPaint.setStyle(Paint.Style.FILL);

        cornerPaint = new Paint();
        cornerPaint.setColor(Color.parseColor("#FFA500"));
        cornerPaint.setStyle(Paint.Style.STROKE);
        cornerPaint.setStrokeWidth(cornerWidth);
        cornerPaint.setStrokeCap(Paint.Cap.ROUND);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#FFA500"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = (int) (getHeight() * 1.5);

        // Calculate scan area (square in center)
        int scanSize = Math.min(width, height) * 2 / 3;
        int left = (width - scanSize) / 2;
        int top = (height - scanSize) / 4;
        int right = left + scanSize;
        int bottom = top + scanSize;

        scanRect = new RectF(left, top, right, bottom);

        // Draw overlay (darken everything except scan area)
        Path overlayPath = new Path();
        overlayPath.addRect(0, 0, width, height, Path.Direction.CW);
        overlayPath.addRect(scanRect, Path.Direction.CCW);
        canvas.drawPath(overlayPath, overlayPaint);

        // Draw corner brackets
        drawCorners(canvas);

        // Draw scanning line
        if (showScanLine) {
            drawScanLine(canvas);
        }
    }

    private void drawCorners(Canvas canvas) {
        float left = scanRect.left;
        float top = scanRect.top;
        float right = scanRect.right;
        float bottom = scanRect.bottom;

        // Top-left corner
        canvas.drawLine(left, top + cornerLength, left, top, cornerPaint);
        canvas.drawLine(left, top, left + cornerLength, top, cornerPaint);

        // Top-right corner
        canvas.drawLine(right - cornerLength, top, right, top, cornerPaint);
        canvas.drawLine(right, top, right, top + cornerLength, cornerPaint);

        // Bottom-left corner
        canvas.drawLine(left, bottom - cornerLength, left, bottom, cornerPaint);
        canvas.drawLine(left, bottom, left + cornerLength, bottom, cornerPaint);

        // Bottom-right corner
        canvas.drawLine(right - cornerLength, bottom, right, bottom, cornerPaint);
        canvas.drawLine(right, bottom, right, bottom - cornerLength, cornerPaint);
    }

    private void drawScanLine(Canvas canvas) {
        float lineY = scanRect.top + (scanRect.height() * scanLinePosition);
        canvas.drawLine(scanRect.left - 20, lineY, scanRect.right + 20, lineY, linePaint);

        // Animate scan line
        scanLinePosition += 0.01f;
        if (scanLinePosition > 1.0f) {
            scanLinePosition = 0;
        }
        invalidate();
    }

    public RectF getScanRect() {
        return scanRect;
    }

}
