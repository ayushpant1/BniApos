package com.example.paymentsdk.LandiSDK.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.icreader.ICError;
import com.usdk.apiservice.aidl.icreader.UAT24CxxReader;

import java.util.Hashtable;
import java.util.Map;

/**
 * AT24CxxReader API.
 */

public class ICAT24CxxReader {

    /**
     * IC AT24Cxx reader object.
     */
    private UAT24CxxReader at24CxxReader = CTIApplication.getDeviceService().getAT24CxxReader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Power up.
     */
    public void powerUp(int voltage) throws RemoteException {
        int ret = at24CxxReader.powerUp(voltage);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power down.
     */
    public void powerDown() throws RemoteException {
        int ret = at24CxxReader.powerDown();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read.
     */
    public void read(int addr, int len, BytesValue data) throws RemoteException {
        int ret = at24CxxReader.read(addr, len, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write.
     */
    public void write(int addr, byte[] data) throws RemoteException {
        int ret = at24CxxReader.write(addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ICAT24CxxReader INSTANCE = new ICAT24CxxReader();
    }

    /**
     * Get IC AT24Cxx reader instance.
     */
    public static ICAT24CxxReader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ICAT24CxxReader() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(ICError.SUCCESS, R.string.succeed);
        errorCodes.put(ICError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(ICError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(ICError.ERROR_INSERT, R.string.insert_error);
        errorCodes.put(ICError.ERROR_POWERUP, R.string.power_up_error);
        errorCodes.put(ICError.ERROR_OTHER, R.string.other_error);
        errorCodes.put(ICError.ERROR_DEVICE_USED, R.string.device_used);
        errorCodes.put(ICError.ERROR_TIMEOUT, R.string.timeout);
        errorCodes.put(ICError.ERROR_ERRPARAM, R.string.param_error);
        errorCodes.put(ICError.ERROR_DEVICE_DISABLE, R.string.device_disable);
        errorCodes.put(ICError.ERROR_FAILED, R.string.failed);
        errorCodes.put(ICError.ERROR_IC_ATRERR, R.string.ic_atr_error);
        errorCodes.put(ICError.ERROR_IC_TIMEOUT, R.string.ic_timeout);
        errorCodes.put(ICError.ERROR_IC_NEEDRESET, R.string.ic_need_reset);
        errorCodes.put(ICError.ERROR_IC_ERRTYPE, R.string.ic_type_error);
        errorCodes.put(ICError.ERROR_IC_DATAERR, R.string.ic_data_error);
        errorCodes.put(ICError.ERROR_IC_NOPOWER, R.string.ic_no_power);
        errorCodes.put(ICError.ERROR_IC_FORRESP, R.string.ic_for_response);
        errorCodes.put(ICError.ERROR_IC_SWDIFF, R.string.ic_sw_error);
        errorCodes.put(ICError.ERROR_IC_NOCARD, R.string.ic_no_card);
    }

    /**
     * Get error id.
     */
    private static int getErrorId(int errorCode) {
        if (errorCodes.containsKey(errorCode)) {
            return errorCodes.get(errorCode);
        }

        return R.string.other_error;
    }
}
