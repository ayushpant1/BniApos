package com.example.paymentsdk.LandiSDK.api;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.scanner.BackError;
import com.usdk.apiservice.aidl.scanner.OnScanListener;
import com.usdk.apiservice.aidl.scanner.UScanner;

import java.util.Hashtable;
import java.util.Map;

/**
 * Scanner for back API.
 */

public class ScannerForBack {

    private static final String TIMEOUT = "timeout";

    /**
     * Back scanner object.
     */
    private UScanner backScanner = CTIApplication.getDeviceService().getBackScanner();

    /**
     * Start scan.
     */
    public void startScan(int timeout, OnScanListener onScanListener) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putInt(TIMEOUT, timeout);
        backScanner.startScan(bundle, onScanListener);
    }

    /**
     * Stop scan.
     */
    public void stopScan() throws RemoteException {
        backScanner.stopScan();
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ScannerForBack INSTANCE = new ScannerForBack();
    }

    /**
     * Get scanner instance.
     */
    public static ScannerForBack getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ScannerForBack() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(BackError.SUCCESS, R.string.succeed);
        errorCodes.put(BackError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(BackError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(BackError.ERROR_ALREADY_INIT, R.string.already_init);
        errorCodes.put(BackError.ERROR_AUTH_LICENSE, R.string.auth_license_error);
        errorCodes.put(BackError.ERROR_INIT_FAIL, R.string.init_decoder_library_fail);
        errorCodes.put(BackError.ERROR_OPEN_CAMERA, R.string.open_camera_error);
    }

    /**
     * Get error id.
     */
    public static int getErrorId(int errorCode) {
        if (errorCodes.containsKey(errorCode)) {
            return errorCodes.get(errorCode);
        }

        return R.string.other_error;
    }
}
