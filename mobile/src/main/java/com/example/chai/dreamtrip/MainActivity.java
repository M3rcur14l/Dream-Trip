package com.example.chai.dreamtrip;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
/*
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    private TextView xText, yText, zText, maxXText, maxYText, maxZText;
    private TestView testView;

    private GoogleApiClient mApiClient;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUiElement();
        initGoogleApiClient();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra("MESSAGE");
                String values[] = result.split(",");
                xText.setText(values[0]);
                yText.setText(values[1]);
                zText.setText(values[2]);

                testView.setX(testView.getX() + Float.parseFloat(values[1]) * 20);
                testView.setY(testView.getY() - Float.parseFloat(values[0]) * 10);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("REQUEST_PROCESSED"));
    }

    @Override
    public void onStop() {
        super.onStop();
        mApiClient.disconnect();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Wear connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Wear connection suspended", Toast.LENGTH_SHORT).show();
    }


    public void loadUiElement() {

        xText = (TextView) findViewById(R.id.x_value);
        yText = (TextView) findViewById(R.id.y_value);
        zText = (TextView) findViewById(R.id.z_value);
        maxXText = (TextView) findViewById(R.id.max_x_value);
        maxYText = (TextView) findViewById(R.id.max_y_value);
        maxZText = (TextView) findViewById(R.id.max_z_value);
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        testView = new TestView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        mainLayout.addView(testView, params);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        testView.setX(size.x / 2 - 50);
        testView.setY(size.y / 2 - 50);
    }




}
*/