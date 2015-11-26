package com.peiban.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.peiban.service.SnsService;

public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOGTAG = "ConnectivityReceiver";

    private SnsService snsService;

    public ConnectivityReceiver(SnsService snsService) {
        this.snsService = snsService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "ConnectivityReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            Log.d(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
            Log.d(LOGTAG, "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected()) {
                Log.i(LOGTAG, "Network connected");
                // 重新连接服务器
                if(snsService != null)
                snsService.getXmppManager().startReconnectionThread();
            }
        } else {
            Log.e(LOGTAG, "Network unavailable");
            // 取消连接
            if(snsService != null)
            snsService.getXmppManager().disconnect();
        }
    }

}
