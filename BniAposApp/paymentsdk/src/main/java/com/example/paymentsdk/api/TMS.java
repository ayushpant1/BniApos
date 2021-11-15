package com.example.paymentsdk.api;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.usdk.apiservice.aidl.tms.OnResultListener;
import com.usdk.apiservice.aidl.tms.UTMS;

import java.util.ArrayList;

/**
 * TMS API.
 */

public class TMS {

    /**
     * TMS object.
     */
    private UTMS tms = CTIApplication.getDeviceService().getTms();

    /**
     * Install.
     */
    public void install(String filePath, OnResultListener onResultListener) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        tms.install(bundle, onResultListener);
    }

    /**
     * Uninstall.
     */
    public void uninstall(ArrayList<String> packageNames, OnResultListener onResultListener) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("packageNames", packageNames);
        tms.uninstall(bundle, onResultListener);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final TMS INSTANCE = new TMS();
    }

    /**
     * Get TMS instance.
     */
    public static TMS getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private TMS() {

    }
}
