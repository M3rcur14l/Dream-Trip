package com.example.chai.dreamtrip;

import android.app.Activity;
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

public class StartActivity extends Activity {

    private float scaleX, scaleY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        scaleX = size.x / 1920f;
        scaleY = size.y / 1080f;

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
        translateAnimation.setDuration(4000);
        ship.startAnimation(translateAnimation);

        final ImageView start = (ImageView) findViewById(R.id.start);
        start.setScaleX(scaleX);
        start.setScaleY(scaleY);
        start.setX(700 * scaleX);
        start.setY(200 * scaleY);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start.setImageResource(R.drawable.start_pressed);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    start.setImageResource(R.drawable.start);
                    return true;
                } else
                    return false;
            }
        });
        final ImageView options = (ImageView) findViewById(R.id.options);
        options.setScaleX(scaleX);
        options.setScaleY(scaleY);
        options.setX(560 * scaleX);
        options.setY(450 * scaleY);
        options.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    options.setImageResource(R.drawable.options_pressed);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    options.setImageResource(R.drawable.options);
                    return true;
                } else
                    return false;
            }
        });
        final ImageView credits = (ImageView) findViewById(R.id.credits);
        credits.setScaleX(scaleX);
        credits.setScaleY(scaleY);
        credits.setX(590 * scaleX);
        credits.setY(700 * scaleY);
        credits.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    credits.setImageResource(R.drawable.credits_pressed);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    credits.setImageResource(R.drawable.credits);
                    return true;
                } else
                    return false;
            }
        });



    }
}
