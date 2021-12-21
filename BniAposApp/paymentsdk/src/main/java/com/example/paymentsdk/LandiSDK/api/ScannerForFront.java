package com.example.paymentsdk.LandiSDK.api;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.scanner.FrontError;
import com.usdk.apiservice.aidl.scanner.OnScanListener;
import com.usdk.apiservice.aidl.scanner.UScanner;

import java.util.Hashtable;
import java.util.Map;

/**
 * Scanner for front API.
 */

public class ScannerForFront {

    private static final String TIMEOUT = "timeout";

    /**
     * Front scanner object.
     */
    private UScanner frontScanner = CTIApplication.getDeviceService().getFrontScanner();

    /**
     * Start scan.
     */
    public void startScan(int timeout, OnScanListener onScanListener) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putInt(TIMEOUT, timeout);
        frontScanner.startScan(bundle, onScanListener);
    }

    /**
     * Stop scan.
     */
    public void stopScan() throws RemoteException {
        frontScanner.stopScan();
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ScannerForFront INSTANCE = new ScannerForFront();
    }

    /**
     * Get scanner instance.
     */
    public static ScannerForFront getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ScannerForFront() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(FrontError.SUCCESS, R.string.succeed);
        errorCodes.put(FrontError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(FrontError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(FrontError.ERROR_ALREADY_INIT, R.string.already_init);
        errorCodes.put(FrontError.ERROR_AUTH_LICENSE, R.string.auth_license_error);
//        errorCodes.put(FrontError.ERROR_BASE_STARTSCAN, R.string.base_scan_library_error);
        errorCodes.put(FrontError.ERROR_INIT_ENGINE, R.string.init_engine_error);
        errorCodes.put(FrontError.ERROR_INIT_FAIL, R.string.init_decoder_library_fail);
        errorCodes.put(FrontError.ERROR_NOT_INIT, R.string.no_init);
        errorCodes.put(FrontError.ERROR_START_SCANNER, R.string.start_scanner_fail);
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
