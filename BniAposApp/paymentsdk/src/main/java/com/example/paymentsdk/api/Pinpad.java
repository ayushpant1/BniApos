package com.example.paymentsdk.api;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.pinpad.DESMode;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.pinpad.KAPId;
import com.usdk.apiservice.aidl.pinpad.OfflinePinVerify;
import com.usdk.apiservice.aidl.pinpad.OnPinEntryListener;
import com.usdk.apiservice.aidl.pinpad.PinPublicKey;
import com.usdk.apiservice.aidl.pinpad.PinVerifyResult;
import com.usdk.apiservice.aidl.pinpad.PinpadError;
import com.usdk.apiservice.aidl.pinpad.UPinpad;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Pinpad common API.
 */

abstract class Pinpad {

    /**
     * CBC encrypt mode.
     */
    public static int ENCRYPT_MODE_CBC = 1;

    /**
     * ECB encrypt mode.
     */
    public static int ENCRYPT_MODE_ECB = 0;

    /**
     * Verify cancel.
     */
    public static byte VERIFY_STATUS_CANCEL = 0x00;

    /**
     * Verify success.
     */
    public static byte VERIFY_STATUS_SUCCESS = 0x01;

    /**
     * Verify fail.
     */
    public static byte VERIFY_STATUS_FAIL = 0x02;

    /**
     * Verify offline retry limit executed.
     */
    public static byte VERIFY_STATUS_OFFLINE_RETRY_LIMIT_EXEC = 0x03;

    /**
     * Verify by pass pin.
     */
    public static byte VERIFY_STATUS_BY_PASS_PIN = 0x04;

    /**
     * Pinpad object.
     */
    protected UPinpad pinpad;

    /**
     * Context.
     */
    protected Context context;

    /**
     * Constructor.
     */
    protected Pinpad(Context context, UPinpad pinpad) {
        this.context = context;
        this.pinpad = pinpad;
    }

    /**
     * Start pin entry.
     */
    public void startPinEntry(int keyId, Bundle param, OnPinEntryListener onPinEntryListener) throws RemoteException {
        this.pinpad.startPinEntry(keyId, param, onPinEntryListener);
    }

    /**
     * Start offline pin entry.
     */
    public void startOfflinePinEntry(Bundle param, OnPinEntryListener listener) throws RemoteException {
        this.pinpad.startOfflinePinEntry(param, listener);
    }

    /**
     * Get existent key IDs in key system.
     */
    public List<IntValue> getExistentKeyIdsInKeySystem(byte keyUsage) throws RemoteException {
        List<IntValue> keyIdList = this.pinpad.getExistentKeyIdsInKeySystem(keyUsage);
        if (keyIdList == null) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return keyIdList;
    }

    /**
     * Get existent KAP IDs.
     */
    public List<KAPId> getExistentKapIds() throws RemoteException {
        List<KAPId> kapIdList = this.pinpad.getExistentKapIds();
        if (kapIdList == null) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return kapIdList;
    }

    /**
     * Get KAP mode.
     */
    public void getKapMode(IntValue kapMode) throws RemoteException {
        if (!this.pinpad.getKapMode(kapMode)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Get accessible KAP IDs.
     */
    public List<KAPId> getAccessibleKapIds(int expectedKapsNum) throws RemoteException {
        return this.pinpad.getAccessibleKapIds(expectedKapsNum);
    }

    /**
     * Display.
     */
    public void display(int line, String msg) throws RemoteException {
        // Only for external
        if (!this.pinpad.display(line, msg)) {
            throw new RemoteException(context.getString(R.string.display_error));
        }
    }

    /**
     * Beep.
     */
    public void beep(int beepMs) throws RemoteException {
        // Only for external
        if (!this.pinpad.beep(beepMs)) {
            throw new RemoteException(context.getString(R.string.pinpad_beep_error));
        }
    }

    /**
     * Reset.
     */
    public void reset() throws RemoteException {
        // Only for external
        if (!this.pinpad.reset()) {
            throw new RemoteException(context.getString(R.string.pinpad_reset_error));
        }
    }

    /**
     * Verify offline pin.
     */
    public void verifyOfflinePin(OfflinePinVerify pinVerify, PinPublicKey publicKey, PinVerifyResult verifyResult) throws RemoteException {
        if (!this.pinpad.verifyOfflinePin(pinVerify, publicKey, verifyResult)) {
            throw new RemoteException(context.getString(R.string.verify_offline_pin_error));
        }
    }

    /**
     * Load plain text key.
     */
    public void loadPlainTextKey(int keyType, int keyId, byte[] key) throws RemoteException {
        if (!this.pinpad.loadPlainTextKey(keyType, keyId, key)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Load enc key.
     */
    public void loadEncKey(int keyType, int srcKeyId, int dstKeyId, byte[] encKey, byte[] checkValue) throws RemoteException {
        if (!this.pinpad.loadEncKey(keyType, srcKeyId, dstKeyId, encKey, checkValue)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Format.
     */
    public void format() throws RemoteException {
        if (!this.pinpad.format()) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Set key algorithm.
     */
    public void setKeyAlgorithm(char keyAlgorithm) throws RemoteException {
        this.pinpad.setKeyAlgorithm(keyAlgorithm);
    }

    /**
     * Set encrypted key format.
     */
    public void setEncKeyFormat(int keyFormat) throws RemoteException {
        this.pinpad.setEncKeyFormat(keyFormat);
    }

    /**
     * Switch to work mode.
     */
    public void switchToWorkMode() throws RemoteException {
        if (!this.pinpad.switchToWorkMode()) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Is key exist.
     */
    public boolean isKeyExist(int keyId) throws RemoteException {
        return this.pinpad.isKeyExist(keyId);
    }

    /**
     * Set pinpad serial number.
     */
    public void setPinpadSerialNum(byte[] serialNum) throws RemoteException {
        if (!this.pinpad.setPinpadSerialNum(serialNum)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Delete key.
     */
    public void deleteKey(int keyId) throws RemoteException {
        if (!this.pinpad.deleteKey(keyId)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Cancel pin entry.
     */
    public void cancelPinEntry(int reason) throws RemoteException {
        if (!this.pinpad.cancelPinEntry(reason)) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Calculate KCV.
     */
    public byte[] calcKCV(int keyId) throws RemoteException {
        byte[] kcvData = this.pinpad.calcKCV(keyId);
        if (kcvData == null || kcvData.length <= 0) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return kcvData;
    }

    /**
     * Calculate MAC.
     */
    public byte[] calcMAC(int keyId, int mode, int algorithm, byte[] icvData, byte[] data) throws RemoteException {
        byte[] macData = this.pinpad.calcMAC(keyId, mode, algorithm, icvData, data);
        if (macData == null || macData.length <= 0) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return macData;
    }

    /**
     * Calculate DES.
     */
    public byte[] calculateDes(int keyId, DESMode mode, byte[] icvData, byte[] data) throws RemoteException {
        byte[] desData = this.pinpad.calculateDes(keyId, mode, icvData, data);
        if (desData == null || desData.length <= 0) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return desData;
    }

    /**
     * Encrypt mag track.
     */
    public byte[] encryptMagTrack(int mode, int keyId, byte[] data) throws RemoteException {
        byte[] trkData = this.pinpad.encryptMagTrack(mode, keyId, data);
        if (trkData == null || trkData.length <= 0) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return trkData;
    }

    /**
     * Get random.
     */
    public byte[] getRandom(int length) throws RemoteException {
        byte[] random = this.pinpad.getRandom(length);
        if (random == null || random.length <= 0) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
        return random;
    }

    /**
     * Open device.
     */
    public void open() throws RemoteException {
        if (!this.pinpad.open()) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Close device.
     */
    public void close() throws RemoteException {
        if (!this.pinpad.close()) {
            throw new RemoteException(context.getString(getErrorId(this.pinpad.getLastError())));
        }
    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(PinpadError.SUCCESS, R.string.succeed);
        errorCodes.put(PinpadError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(PinpadError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(PinpadError.ERROR_OTHERERR, R.string.pinpad_error_error);
        errorCodes.put(PinpadError.ERROR_ABOLISH, R.string.pinpad_error_abolish);
        errorCodes.put(PinpadError.ERROR_NO_SUCH_PINPAD, R.string.pinpad_error_no_such_pinpad);
        errorCodes.put(PinpadError.ERROR_INVALID_ARGUMENT, R.string.pinpad_error_invalid_argument);
        errorCodes.put(PinpadError.ERROR_NO_ENOUGH_SPACE, R.string.pinpad_error_no_enough_space);
        errorCodes.put(PinpadError.ERROR_TIMEOUT, R.string.pinpad_error_time_out);
        errorCodes.put(PinpadError.ERROR_COMM_ERR, R.string.pinpad_error_comm_err);
        errorCodes.put(PinpadError.ERROR_UNSUPPORTED_FUNC, R.string.pinpad_error_unsupported_func);
        errorCodes.put(PinpadError.ERROR_BUSY, R.string.pinpad_error_busy);
        errorCodes.put(PinpadError.ERROR_NO_SUCH_KEY, R.string.pinpad_error_no_such_key);
        errorCodes.put(PinpadError.ERROR_FAIL_TO_AUTH, R.string.pinpad_error_fail_to_auth);
        errorCodes.put(PinpadError.ERROR_CANCELLED_BY_USER, R.string.pinpad_error_cancelled_by_user);
        errorCodes.put(PinpadError.ERROR_NO_PIN_ENTERED, R.string.pinpad_error_no_pin_entered);
        errorCodes.put(PinpadError.ERROR_DUKPT_COUNTER_OVERFLOW, R.string.pinpad_error_dukpt_counter_overflow);
        errorCodes.put(PinpadError.ERROR_REOPEN_PINPAD, R.string.pinpad_error_reopen_pinpad);
        errorCodes.put(PinpadError.ERROR_BAD_STATUS, R.string.pinpad_error_bad_status);
        errorCodes.put(PinpadError.ERROR_BAD_KEY_USAGE, R.string.pinpad_error_bad_key_usage);
        errorCodes.put(PinpadError.ERROR_BAD_MODE_OF_KEY_USE, R.string.pinpad_error_bad_mode_of_key_use);
        errorCodes.put(PinpadError.ERROR_INVALID_KEY_HANDLE, R.string.pinpad_error_invalid_key_handle);
        errorCodes.put(PinpadError.ERROR_NO_SUCH_KAP, R.string.pinpad_error_no_such_kap);
        errorCodes.put(PinpadError.ERROR_KAP_ALREADY_EXIST, R.string.pinpad_error_kap_already_exist);
        errorCodes.put(PinpadError.ERROR_KEY_USAGE_AND_MODE_OF_USE_NOT_MATCH, R.string.pinpad_error_key_usage_and_mode_of_use_not_match);
        errorCodes.put(PinpadError.ERROR_REFER_TO_KEY_OUTSIDE_KAP, R.string.pinpad_error_refer_to_key_outside_kap);
        errorCodes.put(PinpadError.ERROR_PERMISSION_DENY, R.string.pinpad_error_permission_deny);
        errorCodes.put(PinpadError.ERROR_ACCESSING_KAP_DENY, R.string.pinpad_error_accessing_kap_deny);
        errorCodes.put(PinpadError.ERROR_WRONG_KAP_MODE, R.string.pinpad_error_wrong_kap_mode);
        errorCodes.put(PinpadError.ERROR_PIN_ENTRY_TOO_FREQUENTLY, R.string.pinpad_error_pin_entry_too_frequently);
        errorCodes.put(PinpadError.ERROR_DUKPT_NOT_INITED, R.string.pinpad_error_dukpt_not_inited);
        errorCodes.put(PinpadError.ERROR_INCOMPATIBLE_KEY_SYSTEM, R.string.pinpad_error_incompatible_key_system);
        errorCodes.put(PinpadError.ERROR_ENC_KEY_FMT_TOO_SIMPLE, R.string.pinpad_error_enc_key_fmt_too_simple);
        errorCodes.put(PinpadError.ERROR_SAME_KEY_VALUE_DETECTED, R.string.pinpad_error_same_key_value_detected);
        errorCodes.put(PinpadError.ERROR_KEYBUNDLE_ERR, R.string.pinpad_error_key_bundle_err);
        errorCodes.put(PinpadError.ERROR_ENCRYPT_MAG_TRACK_TOO_FREQUENTLY, R.string.pinpad_error_encrypt_mag_track_too_frequently);
        errorCodes.put(PinpadError.ERROR_ARGUMENT_CONFLICT, R.string.pinpad_error_argument_conflict);
        errorCodes.put(PinpadError.ERROR_SERVICE_DIED, R.string.pinpad_error_service_died);
        errorCodes.put(PinpadError.ERROR_INPUT_TIMEOUT, R.string.pinpad_error_input_timeout);
        errorCodes.put(PinpadError.ERROR_INPUT_COMM_ERR, R.string.pinpad_error_input_comm_err);
        errorCodes.put(PinpadError.ERROR_INPUT_UNKNOWN, R.string.pinpad_error_input_unknown);
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