package com.example.paymentsdk.api;

import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;


/**
 * Pinpad for DUKPT API.
 */

public class PinpadForDUKPT extends Pinpad {

    /**
     * Init DUKPT IK KSN.
     */
    public void initDUKPTIkKSN(int keyId, byte[] ksnData) throws RemoteException {
        if (!this.pinpad.initDukptIkKsn(keyId, ksnData)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Increase current KSN.
     */
    public void increaseCurrentKSN(int keyId) throws RemoteException {
        if (!this.pinpad.increaseDukptKsn(keyId)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Get current KSN.
     */
    public byte[] getCurrentKSN(int keyId) throws RemoteException {
        byte[] ksnData = pinpad.getDukptCurKsn(keyId);
        if (ksnData == null || ksnData.length <= 0) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return ksnData;
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final PinpadForDUKPT INSTANCE = new PinpadForDUKPT();
    }

    /**
     * Get pinpad for DUKPT instance.
     */
    public static PinpadForDUKPT getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private PinpadForDUKPT() {
        super(CTIApplication.getContext(), CTIApplication.getDeviceService().getPinpadForDUKPT());
    }
}
