package com.hamstechonline.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Toast.makeText(context, ""+bundle.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }
}
