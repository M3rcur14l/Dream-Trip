package com.example.chai.watchmotionchai;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks {

    private static final String DATA_ITEM = "/mydata";

    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    private GoogleApiClient mApiClient;
    int counter = 0;

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
                mTextView = (TextView) stub.findViewById(R.id.text);
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
        mTextView.setText("Wear connected");
        //Toast.makeText(this, "Wearconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mTextView.setText("Wear suspended");
        // Toast.makeText(this,"Wear connection suspended", Toast.LENGTH_SHORT).show();
    }

    public void startToMeasure(View v) {
        Button startStopButton = (Button) v;

        String text = "counter: ";
        if (linear_acceleration != null) {
            if (linear_acceleration.length != 0) {
                for (int i = 0; i < linear_acceleration.length; i++) {
                    text = text + linear_acceleration[i];
                }
                sendMessage(DATA_ITEM, text);
            }
        } else {
            sendMessage(DATA_ITEM, text + counter++);
        }


    }

    private void sendMessage(final String path, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mApiClient == null) {
                    initGoogleApiClient();
                    return;
                }
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mApiClient, node.getId(), path, text.getBytes()).await();

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("message sent");
                    }
                });
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = 0.8f;
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
