package com.example.paymentsdk.api;

import android.os.RemoteException;

import com.usdk.apiservice.aidl.serialport.BaudRate;
import com.usdk.apiservice.aidl.serialport.DataBit;
import com.usdk.apiservice.aidl.serialport.DeviceName;
import com.usdk.apiservice.aidl.serialport.ParityBit;

import java.io.File;

public class SerialListener {

    /*
     * Open.
     */
    public void open() throws RemoteException {
        if (isConnectBase()) {
            String deviceName = getDeviceName("ttyUSB", "ttyACM");
            if (deviceName == null) {
                throw new RemoteException("[ERR] Serial Listener : open");
            }
            SerialPort.getInstance().open(deviceName);
        } else {
            SerialPort.getInstance().open(DeviceName.USBD);
        }

        SerialPort.getInstance().init(BaudRate.BPS_9600, ParityBit.NOPAR, DataBit.DBS_8);
    }

    /**
     * Send data.
     */
    public void sendData(byte[] writeData) throws RemoteException {
        SerialPort.getInstance().write(writeData, 0);
    }

    /**
     * Read data.
     */
    public void readData(byte[] readData) throws RemoteException {
        SerialPort.getInstance().read(readData, 0);
    }

    /**
     * Read data with timeout.
     */
    public void readDataWithTimeout(byte[] readData) throws RemoteException {
        SerialPort.getInstance().read(readData, 10000);

    }

    /*
     * Get device name.
     */
    public String getDeviceName(String... prefixes) {
        File dev = new File( "/dev" );
        for (File file : dev.listFiles()) {
            for (String prefix : prefixes) {
                if (file.getAbsolutePath().startsWith("/dev/" + prefix)) {
                    return file.toString().substring(5);
                }
            }
        }
        return null;
    }

    /**
     * Is connect base.
     */
    public boolean isConnectBase() {
        File dev = new File("/sys/bus/usb/devices/1-1.1");
        return dev.exists();
    }
}
