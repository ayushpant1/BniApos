package com.example.bniapos;

import android.app.Application;
import android.content.Context;

import com.example.paymentsdk.CTIApplication;


public class BniApplication extends Application {

    /**
     * Create.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        CTIApplication.setContext(getApplicationContext());
        bindSdkDeviceService(getApplicationContext());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        CTIApplication.unbindServiceConnection();
        System.exit(0);
    }


    private static void bindSdkDeviceService(Context context) {
        CTIApplication.bindSdkDeviceService(context);
    }


}
