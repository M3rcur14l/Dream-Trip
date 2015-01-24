package com.example.chai.dreamtrip;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.chai.dreamtrip.utils.BitmapUtils;

public class StartActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_start);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float scaleX = size.x / 1920f;
        float scaleY = size.y / 1080f;
        final Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap bm;

        ImageView blinkingStars1 = (ImageView) findViewById(R.id.blinking_stars_1);
        Animation blinkAnimation1 = AnimationUtils.loadAnimation(this, R.anim.blinking1);
        blinkingStars1.startAnimation(blinkAnimation1);
        ImageView blinkingStars2 = (ImageView) findViewById(R.id.blinking_stars_2);
        Animation blinkAnimation2 = AnimationUtils.loadAnimation(this, R.anim.blinking2);
        blinkingStars2.startAnimation(blinkAnimation2);
        ImageView ship = (ImageView) findViewById(R.id.ship);
        ship.setScaleX(scaleX);
        ship.setScaleY(scaleY);
        ship.setBackgroundResource(R.drawable.ship_animation);
        AnimationDrawable shipAnimation = (AnimationDrawable) ship.getBackground();
        shipAnimation.start();
        Animation translateAnimation = new TranslateAnimation(-500, size.x + 500, 0, 0);
        translateAnimation.setRepeatMode(Animation.RESTART);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setDuration(6000);
        ship.startAnimation(translateAnimation);

        final ImageView start = (ImageView) findViewById(R.id.start);
        bm = BitmapUtils.getBitmap("img/start.png", this);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        start.setImageBitmap(bm);
        start.setX(600 * scaleX);
        start.setY(180 * scaleY);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Bitmap bm = BitmapUtils.getBitmap("img/start_pressed.png", StartActivity.this);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                    start.setImageBitmap(bm);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Bitmap bm = BitmapUtils.getBitmap("img/start.png", StartActivity.this);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                    start.setImageBitmap(bm);
                    return true;
                } else
                    return false;
            }
        });
        final ImageView options = (ImageView) findViewById(R.id.options);
        bm = BitmapUtils.getBitmap("img/options.png", this);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        options.setImageBitmap(bm);
        options.setX(360 * scaleX);
        options.setY(430 * scaleY);
        options.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Bitmap bm = BitmapUtils.getBitmap("img/options_pressed.png", StartActivity.this);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                    options.setImageBitmap(bm);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Bitmap bm = BitmapUtils.getBitmap("img/options.png", StartActivity.this);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                    options.setImageBitmap(bm);
                    return true;
                } else
                    return false;
            }
        });
        final ImageView credits = (ImageView) findViewById(R.id.credits);
        bm = BitmapUtils.getBitmap("img/credits.png", this);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        credits.setImageBitmap(bm);
        credits.setX(400 * scaleX);
        credits.setY(680 * scaleY);
        credits.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Bitmap bm = BitmapUtils.getBitmap("img/credits_pressed.png", StartActivity.this);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                    credits.setImageBitmap(bm);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Bitmap bm = BitmapUtils.getBitmap("img/credits.png", StartActivity.this);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                    credits.setImageBitmap(bm);
                    return true;
                } else
                    return false;
            }
        });

    }
}
