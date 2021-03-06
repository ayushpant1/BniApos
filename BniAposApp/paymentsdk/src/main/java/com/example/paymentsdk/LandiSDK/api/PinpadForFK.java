package com.example.paymentsdk.LandiSDK.api;


import com.example.paymentsdk.CTIApplication;

/**
 * Pinpad for FK API.
 */

public class PinpadForFK extends Pinpad {

    /**
     * Creator.
     */
    private static class Creator {
        private static final PinpadForFK INSTANCE = new PinpadForFK();
    }

    /**
     * Get pinpad for FK instance.
     */
    public static PinpadForFK getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private PinpadForFK() {
        super(CTIApplication.getContext(), CTIApplication.getDeviceService().getPinpadForFK());
    }
}
