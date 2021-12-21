package com.example.paymentsdk.LandiSDK.Common;

import android.content.Context;

import com.example.paymentsdk.LandiSDK.api.SystemStatistics;
import com.example.paymentsdk.sdk.Common.HeartbeatHelper;
import com.usdk.apiservice.aidl.systemstatistics.StatisticInfo;

public class LandiHeartbeatHelper extends HeartbeatHelper {


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

    public LandiHeartbeatHelper(Context context) {
        super(context);
        //do the device specific processing here to get the heartbeat data.
        DeviceName = "Ingenico APOS";
        ModelName = "A8";

        try {
            SystemStatistics systemStatistics = SystemStatistics.getInstance();
            if (systemStatistics != null) {
                for (StatisticInfo eachStat : systemStatistics.getAllStatisticsAndStatus()) {
                    switch (eachStat.getName().toLowerCase()) {
                        case "net_type": {
                            NetType = eachStat.getValue();
                            break;
                        }
                        case "power_level": {
                            String PowerLevel = eachStat.getValue();
                            if (PowerLevel.length() > 0) {
                                int batteryLevel = Integer.parseInt(PowerLevel);
                                batteryLevel = batteryLevel / 20;
                                BatteryLevel = String.valueOf(batteryLevel);
                            }
                            break;
                        }
                        case "frontcamera_status.": {
                            String _FrontCameraStatus = "0";
                            if (eachStat.getValue().toLowerCase().contains("success"))
                                _FrontCameraStatus = "1";

                            FrontCameraStatus = _FrontCameraStatus;
                            break;
                        }
                        case "backcamera_status.s": {
                            String _BackCameraStatus = "0";
                            if (eachStat.getValue().toLowerCase().contains("success"))
                                _BackCameraStatus = "1";
                            BackCameraStatus = _BackCameraStatus;
                            break;
                        }
                        case "android_version": {
                            //  response.setAndroidVersion(eachStat.getValue());
                            break;
                        }
                        case "prn_status": {
                            String _PrintStatus = "0";
                            if (eachStat.getValue().toLowerCase().contains("success"))
                                _PrintStatus = "1";

                            PrinterStatus = _PrintStatus;
                            break;
                        }
                        case "lcd_status.status": {

                            String _LCDStatus = "0";
                            if (eachStat.getValue().toLowerCase().contains("success"))
                                _LCDStatus = "1";

                            LCDStatus = _LCDStatus;
                        }
                        case "board_temperature": {

                            break;

                        }
                        case "battery_temperature": {
                            BatteryTemperature = eachStat.getValue();
                            break;
                        }
                        case "sd_storage": {
                            SDStorage = eachStat.getValue();
                            break;
                        }
                        case "sd_storage_percent": {
                            SDStoragePercent = eachStat.getValue();
                            break;
                        }
                        case "totalmemory_info": {
                            break;
                        }
                        case "availmemory_info": {
                            AvailableMemoryInfo = eachStat.getValue();
                            break;
                        }
                        case "iccard_status": {
                            String _ICCardStatus = "0";
                            if (eachStat.getValue().toLowerCase().contains("success"))
                                _ICCardStatus = "1";
                            ICCCardStatus = _ICCardStatus;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }

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
