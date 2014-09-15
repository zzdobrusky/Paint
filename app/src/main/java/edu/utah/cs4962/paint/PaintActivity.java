package edu.utah.cs4962.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class PaintActivity extends Activity
{
    private PaintView _paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        PaletteView rootLayout = new PaletteView(this);

        for(int splotchIndex=0; splotchIndex<10; splotchIndex++)
        {
            _paintView = new PaintView(this);
            _paintView.setColor(Color.DKGRAY);
            //_paintView.setPadding(40, 40, 40, 40);

            rootLayout.addView(_paintView);

            _paintView.setOnSplotchTouchListener(new PaintView.OnSplotchTouchListener()
            {
                @Override
                public void onSplotchTouch(PaintView view)
                {
                    _paintView.setColor(Color.GREEN);
                }

                @Override
                public void onSplotchTouchOut(PaintView view)
                {
                    _paintView.setColor(Color.RED);
                }
            });
        }

        setContentView(rootLayout);

    }

    protected void onResume()
    {
        super.onResume();
    }

}
