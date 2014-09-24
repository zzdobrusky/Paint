package edu.utah.cs4962.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PaintActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        PaletteView paletteView = new PaletteView(this);
        paletteView.setId(10);
        paletteView.setOnActiveColorChangedListener(new PaletteView.OnActiveColorChangedListener()
        {
            @Override
            public void onActiveColorChanged(PaletteView v)
            {
                //paintAreaView.setDrawingColor(v.getActiveColor());
            }
        });

        setContentView(paletteView);
    }

    protected void onResume()
    {
        super.onResume();
    }

}
