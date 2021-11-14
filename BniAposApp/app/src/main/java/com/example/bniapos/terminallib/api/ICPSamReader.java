package com.example.bniapos.terminallib.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.bniapos.CTIApplication;
import com.example.bniapos.R;
import com.usdk.apiservice.aidl.data.ApduResponse;
import com.usdk.apiservice.aidl.icreader.APDUComm;
import com.usdk.apiservice.aidl.icreader.APDUResp;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.icreader.ICError;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.icreader.UPSamReader;

import java.util.Hashtable;
import java.util.Map;

/**
 * IC PSamReader API.
 */

public class ICPSamReader {

    /**
     * IC PSam reader object.
     */
    private UPSamReader pSamReader = CTIApplication.getDeviceService().getPSamReader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Init module.
     */
    public void initModule(int voltage, int mode) throws RemoteException {
        int ret = pSamReader.initModule(voltage, mode);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power up.
     */
    public void powerUp(BytesValue atr, IntValue protocol) throws RemoteException {
        int ret = pSamReader.powerUp(atr, protocol);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Power down.
     */
    public void powerDown() throws RemoteException {
        int ret = pSamReader.powerDown();
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Exchange APDU.
     */
    public ApduResponse exchangeApdu(byte[] apdu) throws RemoteException {
        ApduResponse apduResponse = pSamReader.exchangeApdu(apdu);
        if (apduResponse == null) {
            throw new RemoteException(context.getString(R.string.exchange_apdu_error));
        }
        return apduResponse;
    }

    /**
     * Incoming APDU.
     */
    public void incomingApdu(APDUComm cmd, byte[] inData, APDUResp response) throws RemoteException {
        int ret = pSamReader.incomingApdu(cmd, inData, response);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Outgoing APDU.
     */
    public void outgoingApdu(APDUComm cmd, APDUResp response, BytesValue outData) throws RemoteException {
        int ret = pSamReader.outgoingApdu(cmd, response, outData);
        if (ret != ICError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ICPSamReader INSTANCE = new ICPSamReader();
    }

    /**
     * Get IC PSam reader instance.
     */
    public static ICPSamReader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ICPSamReader() {

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
    public static int getErrorId(int errorCode) {
        if (errorCodes.containsKey(errorCode)) {
            return errorCodes.get(errorCode);
        }

        return R.string.other_error;
    }
}
