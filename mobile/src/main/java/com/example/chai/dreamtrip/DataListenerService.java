package com.example.chai.dreamtrip;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Chai on 23/01/2015.
 */


public class DataListenerService extends WearableListenerService {
    private static final String DATA_ITEM = "/mydata";
    private LocalBroadcastManager broadCaster = LocalBroadcastManager.getInstance(this);

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        if(messageEvent.getPath().equalsIgnoreCase(DATA_ITEM)){
            StringBuilder builder = new StringBuilder();

                String result = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            sendResult(result);
        }

        super.onMessageReceived(messageEvent);
    }

    public void sendResult(String message){
        Intent intent = new Intent("REQUEST_PROCESSED");
        if(message != null){
            intent.putExtra("MESSAGE",message);
            broadCaster.sendBroadcast(intent);
        }
    }
}
