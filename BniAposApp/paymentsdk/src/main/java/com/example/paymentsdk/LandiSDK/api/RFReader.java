package com.example.paymentsdk.LandiSDK.api;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.data.ApduResponse;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.rfreader.OnPassAndActiveListener;
import com.usdk.apiservice.aidl.rfreader.OnPassListener;
import com.usdk.apiservice.aidl.rfreader.RFError;
import com.usdk.apiservice.aidl.rfreader.URFReader;

import java.util.Hashtable;
import java.util.Map;

/**
 * RFReader API.
 */

public class RFReader {
    private static final String TAG = RFReader.class.getSimpleName();
    /**
     * RF reader object.
     */
    private URFReader rfReader = CTIApplication.getDeviceService().getRFReader();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Search card and activate.
     */
    public void searchCardAndActivate(OnPassAndActiveListener onPassAndActiveListener) throws RemoteException {
        rfReader.searchCardAndActivate(onPassAndActiveListener);
    }

    /**
     * Search card.
     */
    public void searchCard(OnPassListener onPassListener) throws RemoteException {
        rfReader.searchCard(onPassListener);
    }

    /**
     * Stop search.
     */
    public void stopSearch() throws RemoteException {
        rfReader.stopSearch();
    }

    /**
     * Activate.
     */
    public void activate(int cardType, BytesValue responseData) throws RemoteException {
        int ret = rfReader.activate(cardType, responseData);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Halt.
     */
    public void halt() throws RemoteException {
        rfReader.halt();
    }

    /**
     * Is exist?
     */
    public boolean isExist() throws RemoteException {
        return rfReader.isExist();
    }

    /**
     * Exchange APDU.
     */
    public ApduResponse exchangeApdu(byte[] apdu) throws RemoteException {
        ApduResponse apduResponse = rfReader.exchangeApdu(apdu);
        if (apduResponse == null) {
            throw new RemoteException(context.getString(R.string.exchange_apdu_error));
        }
        return apduResponse;
    }

    /**
     * Auth block.
     */
    public void authBlock(int blockNo, int keyType, byte[] key) throws RemoteException {
        int ret = rfReader.authBlock(blockNo, keyType, key);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Auth sector.
     */
    public void authSector(int sectorNo, int keyType, byte[] key) throws RemoteException {
        int ret = rfReader.authSector(sectorNo, keyType, key);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Read block.
     */
    public void readBlock(int blockNo, BytesValue data) throws RemoteException {
        int ret = rfReader.readBlock(blockNo, data);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write block.
     */
    public void writeBlock(int blockNo, byte[] data) throws RemoteException {
        int ret = rfReader.writeBlock(blockNo, data);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Increase value.
     */
    public void increaseValue(int blockNo, int value) throws RemoteException {
        int ret = rfReader.increaseValue(blockNo, value);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Decrease value.
     */
    public void decreaseValue(int blockNo, int value) throws RemoteException {
        int ret = rfReader.decreaseValue(blockNo, value);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Restore RAM.
     */
    public void restoreRAM(int blockNo) throws RemoteException {
        int ret = rfReader.restoreRAM(blockNo);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Transfer RAM.
     */
    public void transferRAM(int blockNo) throws RemoteException {
        int ret = rfReader.transferRAM(blockNo);
        if (ret != RFError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Get card serial number.
     */
    public byte[] getCardSerialNo(byte[] atr) throws RemoteException {
        return rfReader.getCardSerialNo(atr);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final RFReader INSTANCE = new RFReader();
    }

    /**
     * Get RF reader instance.
     */
    public static RFReader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private RFReader() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(RFError.SUCCESS, R.string.succeed);
        errorCodes.put(RFError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(RFError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(RFError.ERROR_DEFINE_INVALID_CALL, R.string.invalid_call);
        errorCodes.put(RFError.ERROR_DEFINE_NOT_ACTIVATE, R.string.no_activate);
//        errorCodes.put(RFError.ERROR_EXCHANGEAPDU, R.string.exchange_apdu_error);
//        errorCodes.put(RFError.PASS_ERROR, R.string.pass_card_error);
//        errorCodes.put(RFError.ACTIVATE_ERROR, R.string.activate_error);
//        errorCodes.put(RFError.EXCHANGEAPDU_ERROR, R.string.exchange_operation_error);
//        errorCodes.put(RFError.RESETERR, R.string.reset_error);
//        errorCodes.put(RFError.NOTAGERR, R.string.card_no_act);
//        errorCodes.put(RFError.CRCERR, R.string.card_crc_error);
//        errorCodes.put(RFError.AUTHERR, R.string.auth_error);
//        errorCodes.put(RFError.PARITYERR, R.string.verify_parity_error);
//        errorCodes.put(RFError.CODEERR, R.string.ic_data_error);
//        errorCodes.put(RFError.SERNRERR, R.string.prevent_conflict_error);
//        errorCodes.put(RFError.NOTAUTHERR, R.string.ic_no_auth);
//        errorCodes.put(RFError.BITCOUNTERR, R.string.bit_count_error);
//        errorCodes.put(RFError.BYTECOUNTERR, R.string.byte_count_error);
//        errorCodes.put(RFError.OVFLERR, R.string.overflow_error);
//        errorCodes.put(RFError.FRAMINGERR, R.string.frame_error);
//        errorCodes.put(RFError.UNKNOWN_COMMAND, R.string.unknown_command);
//        errorCodes.put(RFError.COLLERR, R.string.card_collide);
//        errorCodes.put(RFError.INTERFACEERR, R.string.interface_error);
//        errorCodes.put(RFError.RECBUF_OVERFLOW, R.string.receive_buffer_overflow);
//        errorCodes.put(RFError.VALERR, R.string.operate_data_value_error);
//        errorCodes.put(RFError.ERRTYPE, R.string.ic_type_error);
        errorCodes.put(RFError.ERROR_ERRPARAM, R.string.param_error);
//        errorCodes.put(RFError.FAILED, R.string.failed);
        errorCodes.put(RFError.ERROR_TRANSERR, R.string.transaction_error);
        errorCodes.put(RFError.ERROR_PROTERR, R.string.data_norm_error);
        errorCodes.put(RFError.ERROR_MULTIERR, R.string.multi_card_error);
//        errorCodes.put(RFError.NOCARD, R.string.ic_no_card);
//        errorCodes.put(RFError.CARDEXIST, R.string.card_exist);
        errorCodes.put(RFError.ERROR_CARDTIMEOUT, R.string.ic_timeout);
        errorCodes.put(RFError.ERROR_CARDNOACT, R.string.card_no_act);
        errorCodes.put(RFError.ERROR_IC_SWDIFF, R.string.ic_sw_error);
    }

    /**
     * Get error id.
     */
    public static int getErrorId(int errorCode) {
        Log.e(TAG, "RFReader errorCode:" + errorCode);
        if (errorCodes.containsKey(errorCode)) {
            return errorCodes.get(errorCode);
        }

        return R.string.other_error;
    }
}
