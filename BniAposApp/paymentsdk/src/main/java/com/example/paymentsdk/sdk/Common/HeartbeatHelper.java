package com.example.paymentsdk.sdk.Common;

import android.content.Context;

public abstract class HeartbeatHelper {


    public HeartbeatHelper(Context context) {    }

    public abstract String getDeviceName();

    public  abstract void setDeviceName(String deviceName);

    public  abstract String getModelName();

    public  abstract void setModelName(String modelName);

    public abstract String getNetType();

    public  abstract void setNetType(String netType);

    public  abstract String getBatteryLevel();

    public  abstract void setBatteryLevel(String batteryLevel);

    public  abstract String getFrontCameraStatus();

    public  abstract void setFrontCameraStatus(String frontCameraStatus);

    public  abstract String getBackCameraStatus();

    public  abstract void setBackCameraStatus(String backCameraStatus) ;

    public  abstract String getAndroidVersion();

    public  abstract void setAndroidVersion(String androidVersion) ;

    public  abstract String getPrinterStatus();

    public  abstract void setPrinterStatus(String printerStatus);

    public  abstract String getLCDStatus();

    public  abstract void setLCDStatus(String LCDStatus);

    public  abstract String getBoardTemperature();

    public  abstract void setBoardTemperature(String boardTemperature) ;

    public  abstract String getBatteryTemperature();

    public  abstract void setBatteryTemperature(String batteryTemperature) ;

    public  abstract String getSDStorage();

    public  abstract void setSDStorage(String SDStorage);

    public abstract String getSDStoragePercent();

    public  abstract void setSDStoragePercent(String SDStoragePercent);

    public  abstract String getTotalMemoryInfo();

    public  abstract void setTotalMemoryInfo(String totalMemoryInfo) ;

    public  abstract String getAvailableMemoryInfo();

    public  abstract void setAvailableMemoryInfo(String availableMemoryInfo);

    public  abstract String getICCCardStatus();

    public  abstract void setICCCardStatus(String ICCCardStatus);




}
