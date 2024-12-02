package com.capstone.bankit.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Slice> slices = new ArrayList<>();
    private final RectF pieBounds = new RectF();
    private final Paint tooltipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float totalValue = 0;
    private String tooltipText = null;
    private float tooltipX = 0;
    private float tooltipY = 0;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);

        tooltipPaint.setColor(Color.BLACK);
        tooltipPaint.setStyle(Paint.Style.FILL);
        tooltipPaint.setAlpha(200);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Slice> slices) {
        this.slices.clear();
        this.slices.addAll(slices);
        totalValue = 0;
        for (Slice slice : slices) {
            totalValue += slice.value;
        }
        tooltipText = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (slices.isEmpty()) return;

        float startAngle = 0;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) * 0.8f;
        pieBounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        for (Slice slice : slices) {
            float sweepAngle = (slice.value / totalValue) * 360f;

            paint.setColor(slice.color);
            canvas.drawArc(pieBounds, startAngle, sweepAngle, true, paint);

            slice.startAngle = startAngle;
            slice.sweepAngle = sweepAngle;
            startAngle += sweepAngle;
        }

        // Draw the tooltip if available
        if (tooltipText != null) {
            float tooltipWidth = textPaint.measureText(tooltipText) + 40;
            float tooltipHeight = 80;

            RectF tooltipRect = new RectF(
                    tooltipX - tooltipWidth / 2,
                    tooltipY - tooltipHeight - 20,
                    tooltipX + tooltipWidth / 2,
                    tooltipY - 20
            );

            canvas.drawRoundRect(tooltipRect, 20, 20, tooltipPaint);
            canvas.drawText(tooltipText, tooltipX, tooltipY - 50, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float radius = Math.min(centerX, centerY) * 0.8f;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (distance <= radius) {
                    float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
                    angle = angle < 0 ? angle + 360 : angle;

                    for (Slice slice : slices) {
                        if (angle >= slice.startAngle && angle <= (slice.startAngle + slice.sweepAngle)) {
                            tooltipText = slice.title + ": " + Math.round((slice.value / totalValue) * 100) + "%";
                            tooltipX = x;
                            tooltipY = y;
                            invalidate();
                            return true;
                        }
                    }
                }
                // If touch is outside the pie chart, hide the tooltip
                tooltipText = null;
                invalidate();
                return false;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Hide the tooltip when the user lifts their finger
                tooltipText = null;
                invalidate();
                return false;

            default:
                return super.onTouchEvent(event);
        }
    }

    public static class Slice {
        String title;
        float value;
        int color;
        float startAngle;
        float sweepAngle;

        public Slice(String title, float value, int color) {
            this.title = title;
            this.value = value;
            this.color = color;
        }
    }
}
