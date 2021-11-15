package com.example.paymentsdk.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.icreader.ICError;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.icreader.USIM4428Reader;

import java.util.Hashtable;
import java.util.Map;

/**
 * SIM4428Reader API.
 */

public class ICSIM4428Reader {

    /**
     * IC SIM4428 reader object.
     */
    private USIM4428Reader sim4428Reader = CTIApplication.getDeviceService().getSIM4428Reader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Power up.
     */
    public void powerUp(int voltage, BytesValue atr) throws RemoteException {
        int ret = sim4428Reader.powerUp(voltage, atr);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power down.
     */
    public void powerDown() throws RemoteException {
        int ret = sim4428Reader.powerDown();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Lock card.
     */
    public void lockCard() throws RemoteException {
        int ret = sim4428Reader.lockCard();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Verify.
     */
    public void verify(byte[] password, IntValue error) throws RemoteException {
        int ret = sim4428Reader.verify(password, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Change key.
     */
    public void changeKey(byte[] password) throws RemoteException {
        int ret = sim4428Reader.changeKey(password);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read error.
     */
    public void readError(IntValue error) throws RemoteException {
        int ret = sim4428Reader.readError(error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read.
     */
    public void read(int addr, int len, BytesValue data) throws RemoteException {
        int ret = sim4428Reader.read(addr, len, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read status.
     */
    public void readStatus(int addr, IntValue status) throws RemoteException {
        int ret = sim4428Reader.readStatus(addr, status);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Check data.
     */
    public void checkData(int addr, byte data) throws RemoteException {
        int ret = sim4428Reader.checkData(addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write.
     */
    public void write(int mode, int addr, byte[] data) throws RemoteException {
        int ret = sim4428Reader.write(mode, addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ICSIM4428Reader INSTANCE = new ICSIM4428Reader();
    }

    /**
     * Get IC SIM4428 reader instance.
     */
    public static ICSIM4428Reader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ICSIM4428Reader() {

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
        errorCodes.put(ICError.SIM4428Card.ERROR_SIM4428_CHGDISABLE, R.string.change_disable);
        errorCodes.put(ICError.SIM4428Card.ERROR_SIM4428_NOVERIFY, R.string.ic_no_verify);
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
