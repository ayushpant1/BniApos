package com.example.paymentsdk.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.icreader.ICError;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.icreader.UAT1604Reader;

import java.util.Hashtable;
import java.util.Map;

/**
 * AT1604Reader API.
 */

public class ICAT1604Reader {

    /**
     * IC AT1604 reader object.
     */
    private UAT1604Reader at1604Reader = CTIApplication.getDeviceService().getAT1604Reader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Power up.
     */
    public void powerUp(int voltage, BytesValue atr) throws RemoteException {
        int ret = at1604Reader.powerUp(voltage, atr);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power down.
     */
    public void powerDown() throws RemoteException {
        int ret = at1604Reader.powerDown();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Verify.
     */
    public void verify(int keyType, byte[] password, IntValue error) throws RemoteException {
        int ret = at1604Reader.verify(keyType, password, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Change key.
     */
    public void changeKey(int keyType, byte[] password) throws RemoteException {
        int ret = at1604Reader.changeKey(keyType, password);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read error.
     */
    public void readError(int keyType, IntValue error) throws RemoteException {
        int ret = at1604Reader.readError(keyType, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read.
     */
    public void read(int addr, int len, BytesValue data) throws RemoteException {
        int ret = at1604Reader.read(addr, len, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write.
     */
    public void write(int addr, byte[] data) throws RemoteException {
        int ret = at1604Reader.write(addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ICAT1604Reader INSTANCE = new ICAT1604Reader();
    }

    /**
     * Get IC AT1604 reader instance.
     */
    public static ICAT1604Reader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ICAT1604Reader() {

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
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_ERRPARAM, R.string.param_error);
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_ERRTYPE, R.string.ic_type_error);
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_FAILED, R.string.failed);
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_NOCARD, R.string.ic_no_card);
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_NOPOWER, R.string.ic_no_power);
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_NOVERIFY, R.string.ic_no_verify);
        errorCodes.put(ICError.AT1604Card.ERROR_AT1604_OTHERERR, R.string.error);
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
