package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zbynek on 9/15/2014.
 */
public class PaletteView extends ViewGroup
{
    public PaletteView(Context context)
    {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        Log.i("layout", "r: " + r + " getRight(): " + getRight());

        Rect layoutRect = new Rect();
        layoutRect.left = getPaddingLeft() + 25;
        layoutRect.top = getPaddingTop() + 25;
        layoutRect.right = getWidth() - getPaddingRight() - 25;
        layoutRect.bottom = getHeight() - getPaddingBottom() - 25;

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            double angle = (double)childIndex / (double)getChildCount() * 2 * Math.PI;
            int childCenterX = (int)(layoutRect.centerX() + layoutRect.width() * Math.cos(angle));
            int childCenterY = (int)(layoutRect.centerY() + layoutRect.height() * Math.sin(angle));

            View child = getChildAt(childIndex);
            child.layout(childCenterX, childCenterY, layoutRect.right, layoutRect.bottom);
        }
    }
}
