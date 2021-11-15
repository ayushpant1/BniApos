package com.example.bniapos;

import android.app.Application;
import android.content.Context;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.Common.Constant;
import com.example.paymentsdk.Common.TerminalSecurity;
import com.example.paymentsdk.util.data.BytesUtil;


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
