package com.example.bniapos.terminallib.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.bniapos.CTIApplication;
import com.example.bniapos.R;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.icreader.ICError;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.icreader.USIM4442Reader;

import java.util.Hashtable;
import java.util.Map;

/**
 * SIM4442Reader API.
 */

public class ICSIM4442Reader {

    /**
     * IC SIM4442 reader object.
     */
    private USIM4442Reader sim4442Reader = CTIApplication.getDeviceService().getSIM4442Reader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Power up.
     */
    public void powerUp(int voltage, BytesValue atr) throws RemoteException {
        int ret = sim4442Reader.powerUp(voltage, atr);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power down.
     */
    public void powerDown() throws RemoteException {
        int ret = sim4442Reader.powerDown();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Lock card.
     */
    public void lockCard() throws RemoteException {
        int ret = sim4442Reader.lockCard();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Verify.
     */
    public void verify(byte[] password, IntValue error) throws RemoteException {
        int ret = sim4442Reader.verify(password, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Change key.
     */
    public void changeKey(byte[] password) throws RemoteException {
        int ret = sim4442Reader.changeKey(password);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read error.
     */
    public void readError(IntValue error) throws RemoteException {
        int ret = sim4442Reader.readError(error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read.
     */
    public void read(int addr, int len, BytesValue data) throws RemoteException {
        int ret = sim4442Reader.read(addr, len, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read status.
     */
    public void readStatus(int addr, IntValue status) throws RemoteException {
        int ret = sim4442Reader.readStatus(addr, status);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Check data.
     */
    public void checkData(int addr, byte data) throws RemoteException {
        int ret = sim4442Reader.checkData(addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write.
     */
    public void write(int addr, byte[] data) throws RemoteException {
        int ret = sim4442Reader.write(addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ICSIM4442Reader INSTANCE = new ICSIM4442Reader();
    }

    /**
     * Get IC SIM4442 reader instance.
     */
    public static ICSIM4442Reader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ICSIM4442Reader() {

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
        errorCodes.put(ICError.SIM4442Card.ERROR_SIM4442_NOVERIFY, R.string.ic_no_verify);
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
