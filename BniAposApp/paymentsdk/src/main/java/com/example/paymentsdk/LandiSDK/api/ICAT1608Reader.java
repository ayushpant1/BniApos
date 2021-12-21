package com.example.paymentsdk.LandiSDK.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.icreader.GcCalculator;
import com.usdk.apiservice.aidl.icreader.ICError;
import com.usdk.apiservice.aidl.icreader.UAT1608Reader;

import java.util.Hashtable;
import java.util.Map;

/**
 * AT1608Reader API.
 */

public class ICAT1608Reader {

    /**
     * IC AT1608 reader object.
     */
    private UAT1608Reader at1608Reader = CTIApplication.getDeviceService().getAT1608Reader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Power up.
     */
    public void powerUp(int voltage, BytesValue atr) throws RemoteException {
        int ret = at1608Reader.powerUp(voltage, atr);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power down.
     */
    public void powerDown() throws RemoteException {
        int ret = at1608Reader.powerDown();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Verify.
     */
    public void verify(int keyType, byte[] password, IntValue error) throws RemoteException {
        int ret = at1608Reader.verify(keyType, password, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Change key.
     */
    public void changeKey(int keyType, byte[] password) throws RemoteException {
        int ret = at1608Reader.changeKey(keyType, password);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read error.
     */
    public void readError(int keyType, IntValue error) throws RemoteException {
        int ret = at1608Reader.readError(keyType, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read.
     */
    public void read(int addr, int len, BytesValue data) throws RemoteException {
        int ret = at1608Reader.read(addr, len, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write.
     */
    public void write(int addr, byte[] data) throws RemoteException {
        int ret = at1608Reader.write(addr, data);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read access status.
     */
    public void readAccessStatus(IntValue status) throws RemoteException {
        int ret = at1608Reader.readAccessStatus(status);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Change access status.
     */
    public void changeAccessStatus(int status) throws RemoteException {
        int ret = at1608Reader.changeAccessStatus(status);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Set user zone.
     */
    public void setUserZone(int zone) throws RemoteException {
        int ret = at1608Reader.setUserZone(zone);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read fuse.
     */
    public void readFuse(IntValue fuse) throws RemoteException {
        int ret = at1608Reader.readFuse(fuse);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write fuse.
     */
    public void writeFuse() throws RemoteException {
        int ret = at1608Reader.writeFuse();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Authentication.
     */
    public void authentication(byte[] key, GcCalculator calculator, IntValue error) throws RemoteException {
        int ret = at1608Reader.authentication(key, calculator, error);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read auth info.
     */
    public void readAuthInfo(IntValue aac, BytesValue nc) throws RemoteException {
        int ret = at1608Reader.readAuthInfo(aac, nc);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write auth info.
     */
    public void writeAuthInfo(int aac, byte[] nc, byte[] gc) throws RemoteException {
        int ret = at1608Reader.writeAuthInfo(aac, nc, gc);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * IO test.
     */
    public void ioTest() throws RemoteException {
        int ret = at1608Reader.ioTest();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ICAT1608Reader INSTANCE = new ICAT1608Reader();
    }

    /**
     * Get IC AT1608 reader instance.
     */
    public static ICAT1608Reader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ICAT1608Reader() {

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
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_ACKERR, R.string.ack_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_AUTHERR, R.string.auth_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_FUSEDONE, R.string.fuse_done);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_INITAUTHERR, R.string.init_auth_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_NOAUTH, R.string.ic_no_auth);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_NOVERIFY, R.string.ic_no_verify);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_OPERAFORBID, R.string.operation_forbid);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_OPERATIONFAIL, R.string.operation_fail);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READAACERR, R.string.read_aac_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READARERR, R.string.read_ar_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READCIERR, R.string.read_ci_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READFAIL, R.string.read_fail);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READFUSEFAIL, R.string.read_fuse_fail);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READNCERR, R.string.read_nc_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READONLY, R.string.read_only);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_READPACERR, R.string.read_pac_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_RSTERR, R.string.return_power_up_information_error);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_SECCODE_UNVERI, R.string.save_password_without_verify);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_TIMEOUT, R.string.timeout);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_USERZONENOTSET, R.string.user_zone_not_set);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_VERCOUNTOVL, R.string.verify_count_over_limit);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_VERIFYFAIL, R.string.verify_fail);
        errorCodes.put(ICError.AT1608Card.ERROR_AT1608_WRITEFAIL, R.string.write_fail);
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
