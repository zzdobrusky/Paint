package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by zbynek on 9/5/2014.
 */
public class PaintView extends View
{
    public PaintView(Context context)
    {
        super(context);
        setBackgroundColor(0xFF228844);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        Path path = new Path();

        RectF contentRect = new RectF();
        contentRect.left = getPaddingLeft();
        contentRect.top = getPaddingTop();
        contentRect.right = getWidth() - getPaddingRight();
        contentRect.bottom = getHeight() - getPaddingBottom();

        PointF center = new PointF(contentRect.centerX(), contentRect.centerY());
//        center.x = (float) getWidth() * 0.5f;
//        center.y = (float) getHeight() * 0.5f;

        float radius = Math.min(contentRect.width() * 0.5f, contentRect.height() * 0.5f);

        int pointCount = 50;
        float deltaAngle = (float) (2.0f * Math.PI/pointCount);
        for (int pointIndex=0; pointIndex < pointCount; pointIndex++)
        {
            float randRadius = (float) (radius + (Math.random() - 0.5) * 2.0 * 0.05 * contentRect.width());

            PointF point = new PointF();
            point.x = center.x + randRadius * (float)Math.cos(pointIndex * deltaAngle);
            point.y = center.y + randRadius * (float)Math.sin(pointIndex * deltaAngle);

            if (pointIndex == 0)
                path.moveTo(point.x, point.y);
            else
                path.lineTo(point.x, point.y);
        }

        canvas.drawPath(path, paint);

//        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        linePaint.setStrokeWidth(5.0f);
//
//        canvas.clipRect(new Rect(50, 20, 350, 500));
//
//        canvas.drawLine(10.0f, 10.0f, 100.0f, 100.0f, linePaint);
//
//        float[] pts = {35.4f, 435.4f, 232.4f, 97.3f, 87.5f, 243.4f, 93.4f, 974.4f};
//        canvas.drawLines(pts, linePaint);
//
//        linePaint.setColor(0x7F0000FF);
//        canvas.drawOval(new RectF(40.5f, 86.4f, 440.5f, 186.4f), linePaint);
//
//        Path path = new Path();
//        linePaint.setColor(Color.RED);
//        linePaint.setStyle(Paint.Style.STROKE);
//        path.moveTo(40.0f, 200.0f);
//        path.lineTo(250.0f, 600.0f);
//        path.lineTo(600.0f, 50.0f);
//        path.close();
//        canvas.drawPath(path, linePaint);
    }
}
