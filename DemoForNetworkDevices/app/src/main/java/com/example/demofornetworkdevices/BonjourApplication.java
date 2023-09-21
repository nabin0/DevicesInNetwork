package com.example.demofornetworkdevices;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdBindable;
import com.github.druk.rx2dnssd.Rx2DnssdEmbedded;

public class BonjourApplication extends Application {

    private static final String TAG = "BonjourApplication";
    private Rx2Dnssd mRxDnssd;

    @Override
    public void onCreate() {
        super.onCreate();

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .build());

        mRxDnssd = createDnssd();
    }

    public static Rx2Dnssd getRxDnssd(@NonNull Context context) {
        return ((BonjourApplication) context.getApplicationContext()).mRxDnssd;
    }

    private Rx2Dnssd createDnssd() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i(TAG, "Using embedded version of dns sd");
            return new Rx2DnssdEmbedded(this);
        } else {
            Log.i(TAG, "Using bindable version of dns sd");
            return new Rx2DnssdBindable(this);
        }
    }
}
