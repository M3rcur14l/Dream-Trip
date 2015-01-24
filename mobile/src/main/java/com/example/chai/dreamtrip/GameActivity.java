package com.example.chai.dreamtrip;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;


public class GameActivity extends Activity {


    private GLSurfaceView mySurfaceView;
    private GameRenderer myRenderer;

    private TextView fps_counter;




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
    }

    public void loadUIElement(){
        mySurfaceView = (GLSurfaceView)findViewById(R.id.gameView);
        mySurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mySurfaceView.setEGLContextClientVersion(2);

        fps_counter = (TextView)findViewById(R.id.fps_counter);
    }


    //method to call to send position to the renderer
    public void requestUpdateToRenderer(final int x, final int y ){

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
