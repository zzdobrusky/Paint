package edu.utah.cs4962.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PaintActivity extends Activity
{
    private SplotchView _splotchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //PaletteView rootLayout = new PaletteView(this);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        _splotchView = new SplotchView(this);
        _splotchView.setColor(Color.DKGRAY);
        _splotchView.setPadding(40, 40, 40, 40);

        rootLayout.addView(_splotchView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        _splotchView.setOnSplotchTouchListener(new SplotchView.OnSplotchTouchListener()
        {
            @Override
            public void onSplotchTouch(SplotchView view)
            {
                _splotchView.setColor(Color.GREEN);
            }

            @Override
            public void onSplotchTouchOut(SplotchView view)
            {
                _splotchView.setColor(Color.RED);
            }
        });

//        for(int splotchIndex=0; splotchIndex<10; splotchIndex++)
//        {
//            _paintView = new PaintView(this);
//            _paintView.setColor(Color.DKGRAY);
//            //_paintView.setPadding(40, 40, 40, 40);
//
//            rootLayout.addView(_paintView);
//
//            _paintView.setOnSplotchTouchListener(new PaintView.OnSplotchTouchListener()
//            {
//                @Override
//                public void onSplotchTouch(PaintView view)
//                {
//                    _paintView.setColor(Color.GREEN);
//                }
//
//                @Override
//                public void onSplotchTouchOut(PaintView view)
//                {
//                    _paintView.setColor(Color.RED);
//                }
//            });
//        }

        setContentView(rootLayout);
        //setContentView(_paintView);

    }

    protected void onResume()
    {
        super.onResume();
    }

}
