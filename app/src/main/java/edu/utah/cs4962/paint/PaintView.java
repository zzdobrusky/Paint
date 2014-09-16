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
public class PaintView extends View
{
    private int _color;
    private RectF _contentRect;
    private float _radius;
    private OnSplotchTouchListener _onSplotchTouchListener = null;
    private int _pointCount = 50;

    public interface OnSplotchTouchListener
    {
        public void onSplotchTouch(PaintView paintView);
        public void onSplotchTouchOut(PaintView paintView);
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

    public PaintView(Context context)
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
        Path path = new Path();

        _contentRect.left = getPaddingLeft();
        _contentRect.top = getPaddingTop();
        _contentRect.right = getWidth() - getPaddingRight();
        _contentRect.bottom = getHeight() - getPaddingBottom();

        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());


        _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);

        boolean goingUp = false;
        int countDownToChange = (int) (Math.random() * _pointCount/4);// + _pointCount/4);
        float deltaAngle = (float) (2.0f * Math.PI / _pointCount);
        float baseRadius = _radius/2;
        float randRadius = baseRadius;
        for (int pointIndex = 0; pointIndex < _pointCount; pointIndex++)
        {
            if ( goingUp )
            {
                // mostly go up (but allow a few jags down)
                randRadius += (float) (Math.round(Math.random() * 12) - 1);
            }
            else
            {
                // mostly go down (but allow a few jags up)
                randRadius += (float) (Math.round(Math.random() * 12) - 11);
            }


            // now figure out if we need to start moving in the opposite direction.
            // and make sure it doesn't go over the edge
            countDownToChange--;
            if ( countDownToChange <= 0 || randRadius >= _radius || randRadius < _radius/4)
            {
                countDownToChange = (int) (Math.random() * _pointCount/4); //+ _pointCount/4);
                goingUp = !goingUp;
            }


            PointF point = new PointF();
            point.x = center.x + randRadius * (float) Math.cos(pointIndex * deltaAngle);
            point.y = center.y + randRadius * (float) Math.sin(pointIndex * deltaAngle);

            if (pointIndex == 0)
                path.moveTo(point.x, point.y);
            else
                path.lineTo(point.x, point.y);
        }
        path.close();

        canvas.drawPath(path, paint);
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
