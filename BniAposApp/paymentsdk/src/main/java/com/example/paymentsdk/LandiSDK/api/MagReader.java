package com.example.paymentsdk.LandiSDK.api;

import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.magreader.MagError;
import com.usdk.apiservice.aidl.magreader.OnSwipeListener;
import com.usdk.apiservice.aidl.magreader.TrackType;
import com.usdk.apiservice.aidl.magreader.UMagReader;

import java.util.Hashtable;
import java.util.Map;

/**
 * MagReader API.
 */

public class MagReader {

    public static final String PAN = "PAN";
    public static final String TRACK1 = "TRACK1";
    public static final String TRACK2 = "TRACK2";
    public static final String TRACK3 = "TRACK3";
    public static final String SERVICE_CODE = "SERVICE_CODE";
    public static final String EXPIRED_DATE = "EXPIRED_DATE";

    /**
     * Mag reader object.
     */
    private UMagReader magReader = CTIApplication.getDeviceService().getMagReader();

    /**
     * Search card.
     */
    public void searchCard(int timeout, OnSwipeListener onSwipeListener) throws RemoteException {
        // industry_card does not check format;
        // bank_card check track2 format.
        magReader.setTrackType(TrackType.INDUSTRY_CARD);
        magReader.searchCard(timeout, onSwipeListener);
    }

    /**
     * Stop search.
     */
    public void stopSearch() throws RemoteException {
        magReader.stopSearch();
    }

    /**
     * Enable track.
     */
    public void enableTrack(int id) throws RemoteException {
        magReader.enableTrack(id);
    }

    /**
     * Disable track.
     */
    public void disableTrack(int id) throws RemoteException {
        magReader.disableTrack(id);
    }

    /**
     * Set LRC check enabled.
     */
    public void setLRCCheckEnabled(boolean isLRCEnabled) throws RemoteException {
        magReader.setLRCCheckEnabled(isLRCEnabled);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final MagReader INSTANCE = new MagReader();
    }

    /**
     * Get mag reader instance.
     */
    public static MagReader getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private MagReader() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(MagError.SUCCESS, R.string.succeed);
        errorCodes.put(MagError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(MagError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(MagError.ERROR_INVALID, R.string.verify_track_or_data_error);
        errorCodes.put(MagError.ERROR_NEEDSTART, R.string.need_restart);
        errorCodes.put(MagError.ERROR_NODATA, R.string.empty_card);
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
