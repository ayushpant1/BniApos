package com.example.paymentsdk.LandiSDK.api;

import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;

import com.usdk.apiservice.aidl.UDeviceService;
import com.usdk.apiservice.aidl.beeper.UBeeper;
import com.usdk.apiservice.aidl.constants.RFDeviceName;
import com.usdk.apiservice.aidl.device.UDeviceManager;
import com.usdk.apiservice.aidl.emv.UEMV;
import com.usdk.apiservice.aidl.ethernet.UEthernet;
import com.usdk.apiservice.aidl.icreader.AT24CxxType;
import com.usdk.apiservice.aidl.icreader.DriverID;
import com.usdk.apiservice.aidl.icreader.UAT1604Reader;
import com.usdk.apiservice.aidl.icreader.UAT1608Reader;
import com.usdk.apiservice.aidl.icreader.UAT24CxxReader;
import com.usdk.apiservice.aidl.icreader.UICCpuReader;
import com.usdk.apiservice.aidl.icreader.UPSamReader;
import com.usdk.apiservice.aidl.icreader.USIM4428Reader;
import com.usdk.apiservice.aidl.icreader.USIM4442Reader;
import com.usdk.apiservice.aidl.innerscanner.UInnerScanner;
import com.usdk.apiservice.aidl.led.ULed;
import com.usdk.apiservice.aidl.lki.ULKITool;
import com.usdk.apiservice.aidl.magreader.UMagReader;
import com.usdk.apiservice.aidl.paramfile.UParamFile;
import com.usdk.apiservice.aidl.pinpad.DeviceName;
import com.usdk.apiservice.aidl.pinpad.KAPId;
import com.usdk.apiservice.aidl.pinpad.KeySystem;
import com.usdk.apiservice.aidl.pinpad.UPinpad;
import com.usdk.apiservice.aidl.printer.UPrinter;
import com.usdk.apiservice.aidl.rfreader.URFReader;
import com.usdk.apiservice.aidl.scanner.CameraId;
import com.usdk.apiservice.aidl.scanner.UScanner;
import com.usdk.apiservice.aidl.serialport.USerialPort;
import com.usdk.apiservice.aidl.systemstatistics.USystemStatistics;
import com.usdk.apiservice.aidl.tms.UTMS;

/**
 * Initialize the objects of all test module, provide the calling interface.
 */

public class DeviceService {

    private static final String CARD_TYPE = "cardType";
    private static final String SLOT = "slot";
    private static final String EMV_LOG = "emvLog";
    private static final String COMMON_LOG = "commonLog";

    /**
     * Device service object.
     */
    private UDeviceService deviceService;

    /**
     * Constructor.
     */
    public DeviceService(UDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Get version.
     */
    public String getVersion() throws RemoteException {
        return deviceService.getVersion();
    }

    /**
     * Unregister.
     */
    public void unregister() throws RemoteException {
        deviceService.unregister(null);
    }

    /**
     * Register.
     */
    public void register() throws RemoteException {
        deviceService.register(null, new Binder());
    }

    /**
     * Debug log.
     */
    public void debugLog(boolean outputEMV, boolean outputCommon) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EMV_LOG, outputEMV);
        bundle.putBoolean(COMMON_LOG, outputCommon);
        deviceService.debugLog(bundle);
    }

    /**
     * Get pinpad for MK/SK.
     */
    UPinpad getPinpadForMKSK() {
        try {
            KAPId kapID = new KAPId(0, 0);
            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                return UPinpad.Stub.asInterface(deviceService.getPinpad(kapID, KeySystem.KS_MKSK, DeviceName.COM_EPP));
            }
            return UPinpad.Stub.asInterface(deviceService.getPinpad(kapID, KeySystem.KS_MKSK, DeviceName.IPP));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get pin pad (MK/SK) device service.", e);
        }
    }

    /**
     * Get pinpad for DUKPT.
     */
    UPinpad getPinpadForDUKPT() {
        try {
            KAPId kapID = new KAPId(0, 1);
            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                UPinpad pinpad = UPinpad.Stub.asInterface(deviceService.getPinpad(kapID, KeySystem.KS_DUKPT, DeviceName.COM_EPP));
                return pinpad;
            }
            return UPinpad.Stub.asInterface(deviceService.getPinpad(kapID, KeySystem.KS_DUKPT, DeviceName.IPP));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get pin pad (DUKPT) device service.", e);
        }
    }

    /**
     * Get pinpad for FK.
     */
    UPinpad getPinpadForFK() {
        try {
            KAPId kapID = new KAPId(0, 2);
            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                return UPinpad.Stub.asInterface(deviceService.getPinpad(kapID, KeySystem.KS_FIXED_KEY, DeviceName.COM_EPP));
            }
            return UPinpad.Stub.asInterface(deviceService.getPinpad(kapID, KeySystem.KS_FIXED_KEY, DeviceName.IPP));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get pin pad (FK) device service.", e);
        }
    }

    /**
     * Get system statistics.
     */
    USystemStatistics getSystemStatistics() {
        try {
            return USystemStatistics.Stub.asInterface(deviceService.getSystemStatistics());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get system statistics device service.", e);
        }
    }

    /**
     * Get tms.
     */
    UTMS getTms() {
        try {
            return UTMS.Stub.asInterface(deviceService.getTMS());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get tms device service.", e);
        }
    }

    /**
     * Get emv.
     */
    UEMV getEmv() {
        try {
            return UEMV.Stub.asInterface(deviceService.getEMV());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get led device service.", e);
        }
    }

    /**
     * Get printer device.
     */
    public UPrinter getPrinter() {
        try {
            return UPrinter.Stub.asInterface(deviceService.getPrinter());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get printer device service.", e);
        }
    }

    /**
     * Get beeper device.
     */
    UBeeper getBeeper() {
        try {
            return UBeeper.Stub.asInterface(deviceService.getBeeper());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get beeper device service.", e);
        }
    }

    /**
     * Get led device.
     */
    ULed getLed() {
        try {
            Bundle param = new Bundle();

            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                param.putString("rfDeviceName", RFDeviceName.EXTERNAL);
                return ULed.Stub.asInterface(deviceService.getLed(param));
            }

            param.putString("rfDeviceName", RFDeviceName.INNER);
            return ULed.Stub.asInterface(deviceService.getLed(param));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get led device service.", e);
        }
    }

    /**
     * Get device manager.
     */
    public UDeviceManager getDeviceManager() {
        try {
            return UDeviceManager.Stub.asInterface(deviceService.getDeviceManager());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get device manager device service.", e);
        }
    }

    /**
     * Get RF card reader.
     */
    URFReader getRFReader() {
        try {
            Bundle param = new Bundle();

            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                param.putString("rfDeviceName", RFDeviceName.EXTERNAL);
                return URFReader.Stub.asInterface(deviceService.getRFReader(param));
            }

            param.putString("rfDeviceName", RFDeviceName.INNER);
            return URFReader.Stub.asInterface(deviceService.getRFReader(param));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get RF card reader device service.", e);
        }
    }

    /**
     * Get CPU card reader.
     */
    UICCpuReader getICCpuReader() {
        try {
            return UICCpuReader.Stub.asInterface(deviceService.getICReader(DriverID.ICCPU, null));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get IC CPU reader device service.", e);
        }
    }

    /**
     * Get AT1604 card reader.
     */
    UAT1604Reader getAT1604Reader() {
        try {
            return UAT1604Reader.Stub.asInterface(deviceService.getICReader(DriverID.AT1604, null));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get AT1604 reader device service.", e);
        }
    }

    /**
     * Get AT1608 card reader.
     */
    UAT1608Reader getAT1608Reader() {
        try {
            return UAT1608Reader.Stub.asInterface(deviceService.getICReader(DriverID.AT1608, null));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get AT1608 reader device service.", e);
        }
    }

    /**
     * Get AT24Cxx card reader.
     */
    UAT24CxxReader getAT24CxxReader() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(CARD_TYPE, AT24CxxType.AT24C02);
            return UAT24CxxReader.Stub.asInterface(deviceService.getICReader(DriverID.AT24CXX, bundle));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get AT24Cxx reader device service.", e);
        }
    }

    /**
     * Get PSam card reader.
     */
    UPSamReader getPSamReader() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(SLOT, 1);
            return UPSamReader.Stub.asInterface(deviceService.getICReader(DriverID.PSAMCARD, bundle));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get PSam reader device service.", e);
        }
    }

    /**
     * Get SIM4428 card reader.
     */
    USIM4428Reader getSIM4428Reader() {
        try {
            return USIM4428Reader.Stub.asInterface(deviceService.getICReader(DriverID.SIM4428, null));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get SIM4428 reader device service.", e);
        }
    }

    /**
     * Get SIM4442 card reader.
     */
    USIM4442Reader getSIM4442Reader() {
        try {
            return USIM4442Reader.Stub.asInterface(deviceService.getICReader(DriverID.SIM4442, null));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get SIM4442 reader device service.", e);
        }
    }

    /**
     * Get magnetic card device.
     */
    UMagReader getMagReader() {
        try {
            return UMagReader.Stub.asInterface(deviceService.getMagReader());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get magnetic card reader device service.", e);
        }
    }

    /**
     * Get serial port.
     */
    USerialPort getSerialPort(String deviceName) {
        try {
            return USerialPort.Stub.asInterface(deviceService.getSerialPort(deviceName));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get serial port device service.", e);
        }
    }

    /**
     * Get front scanner device.
     */
    UScanner getFrontScanner() {
        try {
            return UScanner.Stub.asInterface(deviceService.getScanner(CameraId.FRONT));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get front scanner device service.", e);
        }
    }

    /**
     * Get back scanner device.
     */
    UScanner getBackScanner() {
        try {
            return UScanner.Stub.asInterface(deviceService.getScanner(CameraId.BACK));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get back scanner device service.", e);
        }
    }

    /**
     * Get parameter file.
     */
    UParamFile getParamFile() {
        try {
            return UParamFile.Stub.asInterface(deviceService.getParamFile("D086UCCBPAY", "parafile"));
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get parameter file device service.", e);
        }
    }

    /**
     * Get ethernet.
     */
    UEthernet getEthernet() {
        try {
            return UEthernet.Stub.asInterface(deviceService.getEthernet());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get ethernet device service.", e);
        }
    }

    /**
     * Get inner scanner.
     */
    UInnerScanner getInnerScanner() {
        try {
            return UInnerScanner.Stub.asInterface(deviceService.getInnerScanner());
        } catch (RemoteException e) {
            throw new IllegalStateException("Fail to get inner scanner device service.", e);
        }
    }

    /**
     * Get LKI.
     */
    ULKITool getLKITool() {
        try {
           return ULKITool.Stub.asInterface(deviceService.getLKITool());
        }catch (RemoteException e) {
           throw new IllegalStateException("Fail to get LKI tool device service.", e);
        }
    }
}
