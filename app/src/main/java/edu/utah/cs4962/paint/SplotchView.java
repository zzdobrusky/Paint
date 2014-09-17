package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zbynek on 9/5/2014.
 */
public class SplotchView extends View
{
    private int _color;
    private RectF _contentRect;
    private float _radius;
    private OnSplotchTouchListener _onSplotchTouchListener = null;
    private int _pointCount = 30; // dividable by 3

    public interface OnSplotchTouchListener
    {
        public void onSplotchTouch(SplotchView splotchView);
        public void onSplotchTouchOut(SplotchView splotchView);
    }

    public void setColor(int _color)
    {
        this._color = _color;
        invalidate();
    }

    public int getColor()
    {
        return _color;
    }

    public OnSplotchTouchListener getOnSplotchTouchListener()
    {
        return _onSplotchTouchListener;
    }

    public void setOnSplotchTouchListener(OnSplotchTouchListener onSplotchTouchListener)
    {
        _onSplotchTouchListener = onSplotchTouchListener;
    }

    public SplotchView(Context context)
    {
        super(context);
        setBackgroundColor(Color.LTGRAY);
        _color = Color.CYAN;
        _contentRect = new RectF();
        setMinimumWidth(50);
        setMinimumHeight(50);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        // get location of touch
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        float circleCenterX = _contentRect.centerX();
        float circleCenterY = _contentRect.centerY();

        float distance = (float)Math.sqrt(
                (circleCenterX - x)*(circleCenterX - x) +
                (circleCenterY - y)*(circleCenterY - y));

        if(distance < _radius)
        {
            Log.i("paint_view", "Touch inside the circle!");
            // raise event
            if(_onSplotchTouchListener != null)
                _onSplotchTouchListener.onSplotchTouch(this);
        }
        else
        {
            Log.i("paint_view", "Touch outside the circle!");
            if(_onSplotchTouchListener != null)
                _onSplotchTouchListener.onSplotchTouchOut(this);
        }

        return super.onTouchEvent(motionEvent);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(_color);
        //paint.setStyle(Paint.Style.STROKE); // for testing only
        //paint.setStrokeWidth(4.0f);
        //paint.setColor(Color.BLUE);
        Path path = new Path();

        _contentRect.left = getPaddingLeft();
        _contentRect.top = getPaddingTop();
        _contentRect.right = getWidth() - getPaddingRight();
        _contentRect.bottom = getHeight() - getPaddingBottom();

        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());

        _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);

        float deltaAngle = (float) (2.0f * Math.PI / _pointCount);
        // remember the first point to close the path after
        PointF point0 = getRandomPoint(deltaAngle, center.x, center.y, 0);
        path.moveTo(point0.x, point0.y);

        for (int pointIndex = 1; pointIndex < _pointCount; pointIndex += 3)
        {
            PointF point1 = getRandomPoint(deltaAngle, center.x, center.y, pointIndex);
            PointF point2 = getRandomPoint(deltaAngle, center.x, center.y, pointIndex);
            PointF point3;
            // make sure the path closes
            if(pointIndex == _pointCount - 2)
                point3 = point0;
            else
                point3 = getRandomPoint(deltaAngle, center.x, center.y, pointIndex);

            path.cubicTo(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y);
        }
        canvas.drawPath(path, paint);
    }

    private PointF getRandomPoint(float angle, float centerX, float centerY, int pointIndex)
    {
        float halfRadius = _radius/2;
        float randRadius = halfRadius + (float) (Math.random() - 0.5) * halfRadius;
        PointF point = new PointF();
        point.x = centerX + randRadius * (float) Math.cos(pointIndex * angle);
        point.y = centerY + randRadius * (float) Math.sin(pointIndex * angle);

        return point;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        // start small and get bigger
        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if(widthMode == MeasureSpec.AT_MOST)
            width = widthSpec;
        if(heightMode == MeasureSpec.AT_MOST)
            height = heightSpec;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
            height = width;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        // TODO: respect padding
        // make sure that it is squared
        if (width > height && widthMode != MeasureSpec.EXACTLY)
            width = height;
        if (height > width && heightMode != MeasureSpec.EXACTLY)
            height = width;

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec,
                        width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL : 0),
                resolveSizeAndState(height, heightMeasureSpec,
                        height < getSuggestedMinimumHeight() ? MEASURED_STATE_TOO_SMALL : 0)
        );
    }
}
