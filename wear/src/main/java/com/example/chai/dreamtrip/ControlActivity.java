package com.example.chai.dreamtrip;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class ControlActivity extends Activity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks {

    private static final String DATA_ITEM = "/mydata";

    private Button playButton, pauseButton;
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    private GoogleApiClient mApiClient;

    private float gravity[] = {9.81f, 9.81f, 9.81f}; //earth acceleration
    private float linear_acceleration[] = {0f, 0f, 0f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                pauseButton = (Button) stub.findViewById(R.id.pause_btn);
                pauseButton.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            pauseButton.setBackground(getResources().getDrawable(R.drawable.pause_pressed));
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            pauseButton.setBackground(getResources().getDrawable(R.drawable.pause));
                            pauseButton.setVisibility(View.GONE);
                            playButton.setVisibility(View.VISIBLE);
                            return true;
                        } else
                            return false;
                    }
                });

                playButton = (Button) stub.findViewById(R.id.play_btn);
                playButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            playButton.setBackground(getResources().getDrawable(R.drawable.play_pressed));
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            playButton.setBackground(getResources().getDrawable(R.drawable.play));
                            playButton.setVisibility(View.GONE);
                            pauseButton.setVisibility(View.VISIBLE);
                            return true;
                        } else
                            return false;
                    }
                });
            }
        });



        initGoogleApiClient();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStart();
        mSensorManager.unregisterListener(this);
    }

    public void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "Wearconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Toast.makeText(this,"Wear connection suspended", Toast.LENGTH_SHORT).show();
    }


    private void sendMessage(final String path, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(mApiClient, node.getId(), path, text.getBytes()).await();
                }
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = 0.7f;
            String text = "";
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
            for (int i = 0; i < linear_acceleration.length; i++) {
                text = text + linear_acceleration[i] + ',';
            }
            sendMessage(DATA_ITEM, text);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }
}
