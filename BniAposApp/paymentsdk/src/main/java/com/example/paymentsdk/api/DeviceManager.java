package com.example.paymentsdk.api;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.usdk.apiservice.aidl.device.UDeviceManager;

import java.util.List;

/**
 * DeviceManager API.
 */

public class DeviceManager {
    public static final String MODEL_AECR_C10 = "AECR C10";

    /**
     * Device manager object.
     */
    private UDeviceManager deviceManager = CTIApplication.getDeviceService().getDeviceManager();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Get PSam id.
     */
    public String getPSamId() throws RemoteException {
        return deviceManager.getDeviceInfo().getPsamId();
    }

    /**
     * Set PSam id.
     */
    public void setPSamId(String pSamId) throws RemoteException {
        deviceManager.getDeviceInfo().setPsamId(pSamId);
    }

    /**
     * Get serial number.
     */
    public String getSerialNo() throws RemoteException {
        return deviceManager.getDeviceInfo().getSerialNo();
    }

    /**
     * Set serial number.
     */
    public void setSerialNo(String serialNo) throws RemoteException {
        deviceManager.getDeviceInfo().setSerialNo(serialNo);
    }

    /**
     * Get IMSI.
     */
    public String getIMSI() throws RemoteException {
        return deviceManager.getDeviceInfo().getIMSI();
    }

    /**
     * Set IMSI.
     */
    public void setIMSI(String imsi) throws RemoteException {
        deviceManager.getDeviceInfo().setIMSI(imsi);
    }

    /**
     * Get IMEI.
     */
    public String getIMEI() throws RemoteException {
        return deviceManager.getDeviceInfo().getIMEI();
    }

    /**
     * Set IMEI.
     */
    public void setIMEI(String imei) throws RemoteException {
        deviceManager.getDeviceInfo().setIMEI(imei);
    }

    /**
     * Get ICCID.
     */
    public String getICCID() throws RemoteException {
        return deviceManager.getDeviceInfo().getICCID();
    }

    /**
     * Set ICCID.
     */
    public void setICCID(String iccId) throws RemoteException {
        deviceManager.getDeviceInfo().setICCID(iccId);
    }

    /**
     * Get manufacture.
     */
    public String getManufacture() throws RemoteException {
        return deviceManager.getDeviceInfo().getManufacture();
    }

    /**
     * Set manufacture.
     */
    public void setManufacture(String manufacture) throws RemoteException {
        deviceManager.getDeviceInfo().setManufacture(manufacture);
    }

    /**
     * Get model.
     */
    public String getModel() throws RemoteException {
        return deviceManager.getDeviceInfo().getModel();
    }

    /**
     * Set model.
     */
    public void setModel(String model) throws RemoteException {
        deviceManager.getDeviceInfo().setModel(model);
    }

    /**
     * Get android OS version.
     */
    public String getAndroidOSVersion() throws RemoteException {
        return deviceManager.getDeviceInfo().getAndroidOSVersion();
    }

    /**
     * Set android OS version.
     */
    public void setAndroidOSVersion(String androidOSVersion) throws RemoteException {
        deviceManager.getDeviceInfo().setAndroidOSVersion(androidOSVersion);
    }

    /**
     * Get android kernel version.
     */
    public String getAndroidKernelVersion() throws RemoteException {
        return deviceManager.getDeviceInfo().getAndroidKernelVersion();
    }

    /**
     * Set android kernel version.
     */
    public void setAndroidKernelVersion(String androidKernelVersion) throws RemoteException {
        deviceManager.getDeviceInfo().setAndroidKernelVersion(androidKernelVersion);
    }

    /**
     * Get rom version.
     */
    public String getRomVersion() throws RemoteException {
        return deviceManager.getDeviceInfo().getRomVersion();
    }

    /**
     * Set rom version.
     */
    public void setRomVersion(String romVersion) throws RemoteException {
        deviceManager.getDeviceInfo().setRomVersion(romVersion);
    }

    /**
     * Get firmware version.
     */
    public String getFirmwareVersion() throws RemoteException {
        return deviceManager.getDeviceInfo().getFirmwareVersion();
    }

    /**
     * Set firmware version.
     */
    public void setFirmwareVersion(String firmwareVersion) throws RemoteException {
        deviceManager.getDeviceInfo().setFirmwareVersion(firmwareVersion);
    }

    /**
     * Get hardware version.
     */
    public String getHardwareVersion() throws RemoteException {
        return deviceManager.getDeviceInfo().getHardwareVersion();
    }

    /**
     * Set hardware version.
     */
    public void setHardwareVersion(String hardwareVersion) throws RemoteException {
        deviceManager.getDeviceInfo().setHardwareVersion(hardwareVersion);
    }

    /**
     * Update system time, format: yyyyMMddHHmmss.
     */
    public void updateSystemTime(String time) throws RemoteException {
        if (!deviceManager.updateSystemDatetime(time)) {
            throw new RemoteException(context.getString(R.string.update_date_time_error));
        }
    }

    /**
     * Reboot terminal.
     */
    public void reboot() throws RemoteException {
        deviceManager.reboot();
    }

    /**
     * Get system modules version.
     */
    public Bundle getSystemModulesVersion(List<String> moduleNames) throws RemoteException {
        return deviceManager.getSystemModulesVersion(moduleNames);
    }

    /**
     * Get Mac.
     */
    public String getMac(byte[] input) throws RemoteException {
        return deviceManager.getTUSN(0, input).getMac();
    }

    /**
     * Set Mac.
     */
    public void setMac(byte[] input, String mac) throws RemoteException {
        deviceManager.getTUSN(0, input).setMac(mac);
    }

    /**
     * Get terminal type.
     */
    public int getTerminalType(byte[] input) throws RemoteException {
        return deviceManager.getTUSN(0, input).getTerminalType();
    }

    /**
     * Set terminal type.
     */
    public void setTerminalType(byte[] input, int type) throws RemoteException {
        deviceManager.getTUSN(0, input).setTerminalType(type);
    }

    /**
     * Get TUSN.
     */
    public String getTusn(byte[] input) throws RemoteException {
        return deviceManager.getTUSN(0, input).getTusn();
    }

    /**
     * Set TUSN.
     */
    public void setTusn(byte[] input, String tusn) throws RemoteException {
        deviceManager.getTUSN(0, input).setTusn(tusn);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final DeviceManager INSTANCE = new DeviceManager();
    }

    /**
     * Get device manager instance.
     */
    public static DeviceManager getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private DeviceManager() {

    }
}
