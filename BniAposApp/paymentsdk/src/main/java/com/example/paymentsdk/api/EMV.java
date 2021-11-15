package com.example.paymentsdk.api;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.emv.CAPublicKey;
import com.usdk.apiservice.aidl.emv.ECCardLog;
import com.usdk.apiservice.aidl.emv.EMVError;
import com.usdk.apiservice.aidl.emv.EMVEventHandler;
import com.usdk.apiservice.aidl.emv.ICCardLog;
import com.usdk.apiservice.aidl.emv.RecoverCert;
import com.usdk.apiservice.aidl.emv.SearchCardListener;
import com.usdk.apiservice.aidl.emv.UEMV;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * EMV API.
 */

public class EMV {

    /**
     * Application selection type: PSE(Payment System Environment) and Aid list. PSE first, then aid list.
     * <p>
     * - PSE describes the applications supported by the card. It is written to card when the card is published.
     * <p>
     * - AID list describes the applications supported by the terminal. It is downloaded to terminal from host.
     * <p>
     * Choose this application selection type, EMV kernel shall read PSE from the card first, to get the applications
     * supported by the card. Then compare each application of the card with the AID list, finally send out the
     * applications that both the card and the terminal support to user to choose.
     * <p>
     * If there is no PSE in the card, EMV kernel shall send each AID of the AID list to card, to check if the card also
     * support the application, finally send out the applications that both the card and the terminal support
     * to user to choose.
     */
    public static final byte PSE_AID_LIST = 0;

    /**
     * Application selection type: Only PSE.
     */
    public static final byte ONLY_PSE = 1;

    /**
     * Application selection type: Only aid list.
     */
    public static final byte ONLY_AID_LIST = 2;

    /**
     * Disable contactless card select app.
     */
    public static final byte DISABLE_CONTACTLESS_CARD_SELECT_APP = 0;

    /**
     * Enable contactless card select app.
     */
    public static final byte ENABLE_CONTACTLESS_CARD_SELECT_APP = 1;

    /**
     * Verify offline pin success.
     */
    public static final byte VERIFY_OFFLINE_PIN_SUCCESS = 0;

    /**
     * Verify offline pin error.
     */
    public static final byte VERIFY_OFFLINE_PIN_ERROR = 1;

    /**
     * Goods.
     */
    public final static byte SERVICE_TYPE_GOODS_SERVICE = 0x00;

    /**
     * CashBack.
     */
    public final static byte SERVICE_TYPE_CASH_BACK = 0x09;

    /**
     * Cash.
     */
    public final static byte SERVICE_TYPE_CASH = 0x01;

    /**
     * Refund.
     */
    public final static byte SERVICE_TYPE_REFUND = 0x20;

    /**
     * Normal
     */
    public final static byte GAC_NORMAL = 0x00;

    /**
     * Offline
     */
    public final static byte GAC_OFFLINE = 0x01;

    /**
     * Online
     */
    public final static byte GAC_ONLINE = 0x02;

    /**
     * Decline
     */
    public final static byte GAC_DECLINE = 0x03;

    /**
     * EMV object.
     */
    private UEMV emv = CTIApplication.getDeviceService().getEmv();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Start process.
     */
    public void startProcess(Bundle bundle, EMVEventHandler emvEventHandler) throws RemoteException {
        emv.startProcess(bundle, emvEventHandler);
    }

    /**
     * Stop process.
     */
    public void stopProcess() throws RemoteException {
        emv.stopProcess();
    }

    /**
     * Search card.
     */
    public void searchCard(Bundle bundle, int timeout, SearchCardListener searchCardListener) throws RemoteException {
        emv.searchCard(bundle, timeout, searchCardListener);
    }

    /**
     * Stop search.
     */
    public void stopSearch() throws RemoteException {
        emv.stopSearch();
    }

    /**
     * Response card.
     */
    public void responseCard() throws RemoteException {
        emv.respondCard();
    }

    /**
     * Response event.
     */
    public void responseEvent(String tlvList) throws RemoteException {
        int ret = emv.responseEvent(tlvList);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Manage AID.
     */
    public void manageAID(int action, String aid, boolean isPartSlt) throws RemoteException {
        int ret = emv.manageAID(action, aid, isPartSlt);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Manage recover cert.
     */
    public void manageRecCert(int action, RecoverCert recCert) throws RemoteException {
        int ret = emv.manageRecCert(action, recCert);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Set CA public key.
     */
    public void setCAPubKey(CAPublicKey pubKey) throws RemoteException {
        int ret = emv.setCAPubKey(pubKey);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Set DOL.
     */
    public void setDOL(int type, String dol) throws RemoteException {
        int ret = emv.setDOL(type, dol);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Get DOL.
     */
    public String getDOL(int type) throws RemoteException {
        return emv.getDOL(type);
    }

    /**
     * Get TLV.
     */
    public String getTLV(String tag) throws RemoteException {
        return emv.getTLV(tag);
    }

    /**
     * Set TLV.
     */
    public void setTLV(int kernelId, String tag, String value) throws RemoteException {
        int ret = emv.setTLV(kernelId, tag, value);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Set TLV list.
     */
    public void setTLVList(int kernelId, String tlv) throws RemoteException {
        int ret = emv.setTLVList(kernelId, tlv);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Get data APDU.
     */
    public String getDataAPDU(String tag) throws RemoteException {
        return emv.getDataAPDU(tag);
    }

    /**
     * Get balance.
     */
    public long getBalance() throws RemoteException {
        return emv.getBalance();
    }

    /**
     * Get ECC log.
     */
    public void getECCLog(byte[] aid, List<ECCardLog> ecLogs) throws RemoteException {
        int ret = emv.getECCLog(aid, ecLogs);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Get ICC log.
     */
    public void getICCLog(byte[] aid, List<ICCardLog> icLogs) throws RemoteException {
        int ret = emv.getICCLog(aid, icLogs);
        if (ret != EMVError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Transfer EMV process.
     */
    public void transfer(String transferToApp) throws RemoteException {
        emv.transfer(transferToApp);
    }

    /**
     * Halt.
     */
    public void halt() throws RemoteException {
        emv.halt();
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final EMV INSTANCE = new EMV();
    }

    /**
     * Get EMV instance.
     */
    public static EMV getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private EMV() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(EMVError.SUCCESS, R.string.succeed);
        errorCodes.put(EMVError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(EMVError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(EMVError.ERROR_POWERUP_FAIL, R.string.emv_power_up_card_error);
        errorCodes.put(EMVError.ERROR_ACTIVATE_FAIL, R.string.emv_activate_card_error);
        errorCodes.put(EMVError.ERROR_WAITCARD_TIMEOUT, R.string.wait_card_timeout);
        errorCodes.put(EMVError.ERROR_NOT_START_PROCESS, R.string.not_start_process);
        errorCodes.put(EMVError.ERROR_PARAMERR, R.string.emv_param_error);
        errorCodes.put(EMVError.EMV_RESULT_AMOUNT_EMPTY, R.string.amount_empty);
        errorCodes.put(EMVError.ERROR_MULTIERR, R.string.multi_card_error);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_BUSY, R.string.emv_thread_is_working_please_try_again_later);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_NOAPP, R.string.the_card_no_candidate_applications);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_NOPUBKEY, R.string.public_key_not_set_transaction_is_terminated);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_EXPIRY, R.string.the_application_of_the_card_has_expired_transaction_terminated);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_FLASHCARD, R.string.contactless_quickpass_torn_transaction_transaction_is_terminated_please_take_remedial_measures);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_STOP, R.string.termination_is_required_transaction_exits_automatically);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_REPOWERICC, R.string.power_up_the_card_again_and_start_transaction);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_REFUSESERVICE, R.string.service_is_not_allowed_by_the_card);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_CARDLOCK, R.string.card_is_locked);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_APPLOCK, R.string.application_is_locked);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_EXCEED_CTLMT, R.string.amount_limit_the_number_of_transactions_limit);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_APDU_ERROR, R.string.apdu_error);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_APDU_STATUS_ERROR, R.string.apdu_status_code_error);
        errorCodes.put(EMVError.ERROR_EMV_RESULT_ALL_FLASH_CARD, R.string.return_all_flash_card_process);
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
