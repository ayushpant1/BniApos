package com.example.paymentsdk.VerifoneSDK.Common;

import android.content.Context;

import com.example.paymentsdk.sdk.Common.HeartbeatHelper;


public class VFHeartbeatHelper extends HeartbeatHelper {


    public String DeviceName;
    public String ModelName;
    public String NetType;
    public String BatteryLevel;
    public String FrontCameraStatus;
    public String BackCameraStatus;
    public String AndroidVersion;
    public String PrinterStatus;
    public String LCDStatus;
    public String BoardTemperature;
    public String BatteryTemperature;
    public String SDStorage;
    public String SDStoragePercent;
    public String TotalMemoryInfo;
    public String AvailableMemoryInfo;
    public String ICCCardStatus;

    public VFHeartbeatHelper(Context context) {
        super(context);
        //do the device specific processing here to get the heartbeat data.
        DeviceName = "Verifone APOS";
        ModelName = "X990";


    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getNetType() {
        return NetType;
    }

    public void setNetType(String netType) {
        NetType = netType;
    }

    public String getBatteryLevel() {
        return BatteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        BatteryLevel = batteryLevel;
    }

    public String getFrontCameraStatus() {
        return FrontCameraStatus;
    }

    public void setFrontCameraStatus(String frontCameraStatus) {
        FrontCameraStatus = frontCameraStatus;
    }

    public String getBackCameraStatus() {
        return BackCameraStatus;
    }

    public void setBackCameraStatus(String backCameraStatus) {
        BackCameraStatus = backCameraStatus;
    }

    public String getAndroidVersion() {
        return AndroidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        AndroidVersion = androidVersion;
    }

    public String getPrinterStatus() {
        return PrinterStatus;
    }

    public void setPrinterStatus(String printerStatus) {
        PrinterStatus = printerStatus;
    }

    public String getLCDStatus() {
        return LCDStatus;
    }

    public void setLCDStatus(String LCDStatus) {
        this.LCDStatus = LCDStatus;
    }

    public String getBoardTemperature() {
        return BoardTemperature;
    }

    public void setBoardTemperature(String boardTemperature) {
        BoardTemperature = boardTemperature;
    }

    public String getBatteryTemperature() {
        return BatteryTemperature;
    }

    public void setBatteryTemperature(String batteryTemperature) {
        BatteryTemperature = batteryTemperature;
    }

    public String getSDStorage() {
        return SDStorage;
    }

    public void setSDStorage(String SDStorage) {
        this.SDStorage = SDStorage;
    }

    public String getSDStoragePercent() {
        return SDStoragePercent;
    }

    public void setSDStoragePercent(String SDStoragePercent) {
        this.SDStoragePercent = SDStoragePercent;
    }

    public String getTotalMemoryInfo() {
        return TotalMemoryInfo;
    }

    public void setTotalMemoryInfo(String totalMemoryInfo) {
        TotalMemoryInfo = totalMemoryInfo;
    }

    public String getAvailableMemoryInfo() {
        return AvailableMemoryInfo;
    }

    public void setAvailableMemoryInfo(String availableMemoryInfo) {
        AvailableMemoryInfo = availableMemoryInfo;
    }

    public String getICCCardStatus() {
        return ICCCardStatus;
    }

    public void setICCCardStatus(String ICCCardStatus) {
        this.ICCCardStatus = ICCCardStatus;
    }


}
