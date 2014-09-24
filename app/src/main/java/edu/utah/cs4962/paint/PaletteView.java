package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zbynek on 9/15/2014.
 */
public class PaletteView extends ViewGroup implements SplotchView.OnSplotchTouchListener
{
    public interface OnActiveColorChangedListener
    {
        public void onActiveColorChanged(PaletteView v);
    }

    OnActiveColorChangedListener _onActiveColorChangedListener = null;

    public OnActiveColorChangedListener getOnActiveColorChangedListener()
    {
        return _onActiveColorChangedListener;
    }

    public void setOnActiveColorChangedListener(OnActiveColorChangedListener onActiveColorChangedListener)
    {
        _onActiveColorChangedListener = onActiveColorChangedListener;
    }

    private Rect _layoutRect;

    public PaletteView(Context context)
    {
        super(context);

        addColor(Color.RED);
        addColor(Color.GREEN);
        addColor(Color.BLUE);
        addColor(Color.BLACK);
        addColor(Color.WHITE);
        addColor(Color.LTGRAY);
        addColor(Color.DKGRAY);
        addColor(Color.CYAN);
        addColor(Color.MAGENTA);
        addColor(Color.GRAY);
    }

    @Override
    public void addView(View child)
    {
        if (!(child instanceof SplotchView))
            Log.e("PaletteView", "Can't add view to palette view");
        else
            super.addView(child);
    }

    public int getActiveColor()
    {
        for (int splotchViewIndex=0; splotchViewIndex<getChildCount(); splotchViewIndex++)
        {
            SplotchView splotchView = (SplotchView)getChildAt(splotchViewIndex);
            if(splotchView.isActive())
                return splotchView.getColor();
        }

        return Color.BLACK;
    }

    public void setActiveColor(int color)
    {
        for (int splotchViewIndex=0; splotchViewIndex<getChildCount(); splotchViewIndex++)
        {
            SplotchView splotchView = (SplotchView)getChildAt(splotchViewIndex);
            if(splotchView.getColor() == color)
                splotchView.setActive(true);
            else
                splotchView.setActive(false);
        }

        if (_onActiveColorChangedListener != null)
            _onActiveColorChangedListener.onActiveColorChanged(this);
    }

    public int[] getColors()
    {
        int[] colors = new int[getChildCount()];
        for (int splotchViewIndex=0; splotchViewIndex<getChildCount(); splotchViewIndex++)
        {
            SplotchView splotchView = (SplotchView)getChildAt(splotchViewIndex);
            colors[splotchViewIndex] = splotchView.getColor();
        }
        return colors;
    }

    public void addColor(int color)
    {
        for (int matchingColor : getColors())
        {
            if (matchingColor == color)
                return;
        }

        SplotchView splotchView = new SplotchView(getContext());
        splotchView.setColor(color);
        addView(splotchView);
        splotchView.setOnSplotchTouchListener(this);
    }

    public void removeColor(int color)
    {
        for (int splotchViewIndex= getChildCount()-1; splotchViewIndex>=0; splotchViewIndex--)
        {
            SplotchView splotchView = (SplotchView)getChildAt(splotchViewIndex);
            if (splotchView.getColor() == color)
                removeView(splotchView);
        }

        // ????
        if(color == getActiveColor())
        {
            if (_onActiveColorChangedListener != null)
                _onActiveColorChangedListener.onActiveColorChanged(this);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle instanceState = new Bundle();

        instanceState.putInt("activeColor", getActiveColor());
        instanceState.putParcelable("superState", super.onSaveInstanceState());

        return instanceState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(state.getClass() != Bundle.class)
            return;

        Bundle instanceState = (Bundle)state;

        if(instanceState.containsKey("superState"))
        {
            super.onRestoreInstanceState(instanceState.getParcelable("superState"));
        }

        if(instanceState.containsKey("activeColor"))
        {
            int activeColor = instanceState.getInt("activeColor");
            setActiveColor(activeColor);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        _layoutRect = new Rect();
        _layoutRect.left = getPaddingLeft();
        _layoutRect.top = getPaddingTop();
        _layoutRect.right = getWidth() - getPaddingRight();
        _layoutRect.bottom = getHeight() - getPaddingBottom();

        // my suggested child size
        int mySuggestedChildWidth = 2 * _layoutRect.width() / getChildCount();
        int mySuggestedChildHeight = 2 * _layoutRect.height() / getChildCount();

        // be as big as you can be and then get smaller when needed
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        int childState = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
            child.measure(
                    MeasureSpec.AT_MOST | mySuggestedChildWidth,
                    MeasureSpec.AT_MOST | mySuggestedChildHeight
            );
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState)
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        //Log.i("palette_layout", "r: " + r + " getRight(): " + getRight());

        int childWidthMax = 0;
        int childHeightMax = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
            childWidthMax = Math.max(childWidthMax, child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax, child.getMeasuredHeight());
        }

        Log.i("palette_layout", "childWidthMax: " + childWidthMax + " childHeightMax: " + childHeightMax);


        float deltaAngle = (float) (2 * Math.PI / getChildCount());

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
            double angle = (double) childIndex * deltaAngle;

            // assuming splotches are square
            int childWidth = childWidthMax;
            int childHeight = childHeightMax;

            int childSize = Math.min(childWidth, childHeight);
            Log.i("palette_layout", "childWidth: " + childWidth + " childHeight: " + childHeight + " childSize: " + childSize);
            // Ellipse
            int childCenterX = (int) (_layoutRect.centerX() + (_layoutRect.width() - childSize) / 2 * Math.cos(angle));
            int childCenterY = (int) (_layoutRect.centerY() + (_layoutRect.height() - childSize) / 2 * Math.sin(angle));
            // testing Cardioid  x = a(2cos(t) - cos(2t)), y = a(2sin(t) - sin(2t))
            //int childCenterX = (int) (_layoutRect.centerX() + childWidth/2 + (_layoutRect.width() - childWidth)/5 * (2 * Math.cos(angle) - Math.cos(2 * angle)));
            //int childCenterY = (int) (_layoutRect.centerY() + (_layoutRect.height() - 2 * childHeight)/5 * (2 * Math.sin(angle) - Math.sin(2 * angle)));

            child.layout(
                    childCenterX - childSize / 2,
                    childCenterY - childSize / 2,
                    childCenterX + childSize / 2,
                    childCenterY + childSize / 2);
        }
    }

    @Override
    public void onSplotchTouch(SplotchView splotchView)
    {
        Log.i("splotch_touched", "touched");
        setActiveColor(splotchView.getColor());
    }

    @Override
    public void onSplotchTouchOut(SplotchView splotchView)
    {

    }
}
