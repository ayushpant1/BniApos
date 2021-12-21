package com.example.paymentsdk.LandiSDK.api;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.usdk.apiservice.aidl.ethernet.UEthernet;

/**
 * Ethernet API.
 */

public class Ethernet {

    public static final String CONFIG_LOCAL_IP = "localIp";
    public static final String CONFIG_GATEWAY = "gateway";
    public static final String CONFIG_MASK = "mask";
    public static final String CONFIG_DNS1 = "dns1";
    public static final String CONFIG_DNS2 = "dns2";
    public static final String CONFIG_IS_DHCP = "isDhcp";
    public static final String CONFIG_MAC = "mac";
    public static final int STATE_DISABLING = 0;
    public static final int STATE_DISABLED = 1;
    public static final int STATE_ENABLING = 2;
    public static final int STATE_ENABLED = 3;
    public static final int STATE_UNKNOWN = 4;

    /**
     * Ethernet object.
     */
    private UEthernet ethernet = CTIApplication.getDeviceService().getEthernet();

    /**
     * Open.
     */
    public boolean open() throws RemoteException {
        return ethernet.open();
    }

    /**
     * Close.
     */
    public boolean close() throws RemoteException {
        return ethernet.close();
    }

    /**
     * Get real info.
     */
    public Bundle getInfo() throws RemoteException {
        return ethernet.getInfo();
    }

    /**
     * Get config info.
     */
    public Bundle getConfigInfo() throws RemoteException {
        return ethernet.getConfigInfo();
    }

    /**
     * Set config info.
     */
    public boolean config(Bundle bundle) throws RemoteException {
        return ethernet.config(bundle);
    }

    /**
     * Get ethernet state.
     */
    public int getEthernetState() throws RemoteException {
        return ethernet.getEthernetState();
    }

    /**
     * Is ethernet enabled.
     */
    public boolean isEthernetEnabled() throws RemoteException {
        return ethernet.isEthernetEnabled();
    }

    /**
     * Get ethernet enabled state.
     */
    public boolean getEthernetEnabledState() throws RemoteException {
        return ethernet.getEthernetEnabledState();
    }

    /**
     * Is ethernet exist.
     */
    public boolean isEthernetExist(String iface) throws RemoteException {
        return ethernet.isEthernetExist(iface);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final Ethernet INSTANCE = new Ethernet();
    }

    /**
     * Get ethernet instance.
     */
    public static Ethernet getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private Ethernet() {

    }
}
