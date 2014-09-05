package edu.utah.cs4962.paint;

import android.app.Activity;
import android.os.Bundle;

public class PaintActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        PaintView paintView = new PaintView(this);
        paintView.setPadding(40, 40, 40, 40);
        setContentView(paintView);
    }

}
