package com.example.chai.dreamtrip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Chai on 23/01/2015.
 */
public class TestView extends View {

    private Paint paint;

    public TestView(Context context) {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 50, paint);
        super.onDraw(canvas);
    }
}
