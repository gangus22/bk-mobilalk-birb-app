package com.example.birbapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PostUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("REFRESH_POSTS"));
    }
}