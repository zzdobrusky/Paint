package edu.utah.cs4962.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;

public class PaintActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        PaintView paintView = new PaintView(this);
        paintView.setColor(Color.RED);
        paintView.setPadding(40, 40, 40, 40);
        setContentView(paintView);

        //new Timer()
//        paintView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                ((PaintView)view).setColor(Color.GREEN);
//            }
//        });
    }

    protected void onResume()
    {
        super.onResume();
    }

}
