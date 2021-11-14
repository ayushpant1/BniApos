package com.example.bniapos.terminallib.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.bniapos.CTIApplication;
import com.example.bniapos.R;
import com.usdk.apiservice.aidl.lki.LKIError;
import com.usdk.apiservice.aidl.lki.OnLKIResultListener;
import com.usdk.apiservice.aidl.lki.ULKITool;

import java.util.Hashtable;
import java.util.Map;

/**
 * LKI tool common API.
 */

public class LKITool {

    /**
     * LKI tool object.
     */
    private ULKITool lkiTool = CTIApplication.getDeviceService().getLKITool();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Cancel download master key from master pos.
     */
    public void cancelDownloadMkeyFromMasterPos() throws RemoteException {
        int ret = lkiTool.cancelInjection();
        if (ret != LKIError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Download master key from master pos.
     */
    public void downloadMkeyFromMasterPos(OnLKIResultListener onLKIResultListener) throws RemoteException {
        lkiTool.injectLKey(onLKIResultListener);
    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(LKIError.SUCCESS, R.string.succeed);
        errorCodes.put(LKIError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(LKIError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(LKIError.ERROR_OPEN_DEVICE, R.string.lki_error_open_device);
        errorCodes.put(LKIError.ERROR_STATUS, R.string.lki_error_status);
        errorCodes.put(LKIError.ERROR_CANCEL, R.string.lki_error_cancel);
        errorCodes.put(LKIError.ERROR_CONNECTING, R.string.lki_error_connecting);
        errorCodes.put(LKIError.ERROR_END, R.string.lki_error_end);
        errorCodes.put(LKIError.ERROR_PROCESS, R.string.lki_error_process);
        errorCodes.put(LKIError.ERROR_GETTK, R.string.lki_error_get_tk);
        errorCodes.put(LKIError.ERROR_GETTK_END, R.string.lki_error_get_tk_end);
        errorCodes.put(LKIError.ERROR_INJECTKEY, R.string.lki_error_inject_key);
        errorCodes.put(LKIError.ERROR_INJECTKEY_END, R.string.lki_error_inject_key_end);
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