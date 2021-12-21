package com.example.paymentsdk.LandiSDK.util.signpanel;

import android.content.Context;


import com.example.paymentsdk.R;

import java.util.Hashtable;
import java.util.Map;

/**
 * Sign panel util.
 */

public class SignPanelUtil {

    //private SignPanel signPanel;
    private static SignPanelUtil instance;

    /**
     * Constructor.
     */
    private SignPanelUtil(Context context) {
        //signPanel = new SignPanel(context);
    }

    /**
     * Get instance.
     */
    public static SignPanelUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SignPanelUtil(context);
        }
        return instance;
    }

    /**
     * Show sign panel.
     */
    //public boolean showSignPanel(int timeout, String transCode, int rotation, int redoTimes, boolean resetTimeoutWhenRedo, SignPanel.SignPanelCallback callback) {
    //    return signPanel.showSignPanel(timeout, transCode, rotation, redoTimes, resetTimeoutWhenRedo, callback);
    //}

    /**
     * Cancel sign panel.
     */
    //public boolean cancelSignPanelByForce() {
    //    return signPanel.cancelSignPanelByForce();
    //}

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        //errorCodes.put(SignPanel.RESULT_SUCCESS, R.string.succeed);
        //errorCodes.put(SignPanel.RESULT_CANCELED, R.string.cancel);
        //errorCodes.put(SignPanel.RESULT_TIMEOUT, R.string.timeout);
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