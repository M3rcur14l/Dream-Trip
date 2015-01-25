package com.example.chai.dreamtrip;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Chai on 23/01/2015.
 */


public class DataListenerService extends WearableListenerService {
    private static final String DATA_ITEM = "/mydata";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(DATA_ITEM)) {
            String result = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            if (result.equals("VIBRATE"))
                vibrate();
        }
        super.onMessageReceived(messageEvent);
    }

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);
    }
}
