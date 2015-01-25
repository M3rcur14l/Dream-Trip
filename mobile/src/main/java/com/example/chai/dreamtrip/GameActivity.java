package com.example.chai.dreamtrip;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;


public class GameActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {


    private GLSurfaceView mySurfaceView;
    private GameRenderer myRenderer;

    private GoogleApiClient mApiClient;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loadUIElement();

        myRenderer = new GameRenderer(this);
        mySurfaceView.setRenderer(myRenderer);

        // set touch listener
        /*
        mySurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    // Convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y
                    // coordinates are inverted.
                    final float normalizedX = (event.getX() / (float) v
                            .getWidth()) * 2 - 1;
                    final float normalizedY = -((event.getY() / (float) v
                            .getHeight()) * 2 - 1);

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mySurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                myRenderer.handleTouchPress(
                                        normalizedX, normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        mySurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                myRenderer.handleTouchDrag(
                                        normalizedX, normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mySurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                myRenderer.handleTouchUp(
                                        normalizedX, normalizedY);
                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });*/

        initGoogleApiClient();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra("MESSAGE");
                String values[] = result.split(",");
                float x = Float.parseFloat(values[1]);
                float y = Float.parseFloat(values[0]);
                requestUpdateToRenderer(x / 40, y / 25);
            }
        };

    }

    public void loadUIElement() {
        ImageView blinkingStars1 = (ImageView) findViewById(R.id.blinking_stars_1);
        Animation blinkAnimation1 = AnimationUtils.loadAnimation(this, R.anim.blinking1);
        blinkingStars1.startAnimation(blinkAnimation1);
        ImageView blinkingStars2 = (ImageView) findViewById(R.id.blinking_stars_2);
        Animation blinkAnimation2 = AnimationUtils.loadAnimation(this, R.anim.blinking2);
        blinkingStars2.startAnimation(blinkAnimation2);


        mySurfaceView = (GLSurfaceView) findViewById(R.id.gameView);
        mySurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mySurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mySurfaceView.setZOrderOnTop(true);
        mySurfaceView.setEGLContextClientVersion(2);
    }

    public void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mApiClient.connect();
    }


    //method to call to send position to the renderer
    public void requestUpdateToRenderer(final float x, final float y) {
        mySurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                //update the ship position
                myRenderer.updateShipPosition(x, y);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("REQUEST_PROCESSED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    // when closing the current activity, the service will automatically shut down(disconnected).
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Wear connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Wear connection suspended", Toast.LENGTH_SHORT).show();
    }

}
