package com.example.chai.watchmotionchai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity implements  GoogleApiClient.ConnectionCallbacks {

    private TextView xText;
    private TextView yText;
    private TextView zText;

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
            }
        };


    }

    @Override
    public void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter("REQUEST_PROCESSED"));
    }



    public void initGoogleApiClient(){
    mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
    mApiClient.connect();
}

    @Override
    public void onConnected(Bundle bundle) {

        Toast.makeText(this,"Wearconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Wear connection suspended", Toast.LENGTH_SHORT).show();
    }



public void loadUiElement(){

    xText = (TextView)findViewById(R.id.x_value);
    yText = (TextView)findViewById(R.id.y_value);
    zText = (TextView)findViewById(R.id.z_value);
}



    //start service when click start button
    public void startMyService(View v){

        Toast.makeText(this,"service started", Toast.LENGTH_SHORT).show();
    }


    //stopservice when click start button
    public void stopMyService(View v){

        Toast.makeText(this,"service stopped", Toast.LENGTH_SHORT).show();
    }



@Override
public void onDestroy(){
    super.onDestroy();
    mApiClient.disconnect();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
}






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
