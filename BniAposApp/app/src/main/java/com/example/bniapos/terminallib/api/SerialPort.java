package com.example.bniapos.terminallib.api;

import android.content.Context;
import android.os.RemoteException;

import com.example.bniapos.CTIApplication;
import com.example.bniapos.R;
import com.usdk.apiservice.aidl.serialport.SerialPortError;
import com.usdk.apiservice.aidl.serialport.USerialPort;

import java.util.Hashtable;
import java.util.Map;

/**
 * SerialPort API.
 */

public class SerialPort {

    /**
     * Serial port object.
     */
    private USerialPort serialPort;

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Open.
     */
    public void open(String deviceName) throws RemoteException {
        serialPort = CTIApplication.getDeviceService().getSerialPort(deviceName);
        int ret = serialPort.open();
        if (ret != SerialPortError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Init.
     */
    public void init(int baudRate, int parityBit, int dataBit) throws RemoteException {
        int ret = serialPort.init(baudRate, parityBit, dataBit);
        if (ret != SerialPortError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Write.
     */
    public void write(byte[] data, int timeout) throws RemoteException {
        int ret = serialPort.write(data, timeout);
        if (ret == -1) {
            throw new RemoteException(context.getString(R.string.write_fail));
        }
    }

    /**
     * Read.
     */
    public void read(byte[] data, int timeout) throws RemoteException {
        int ret = serialPort.read(data, timeout);
        if (ret == -1) {
            throw new RemoteException(context.getString(R.string.read_fail));
        }
    }

    /**
     * Is buffer empty.
     */
    public boolean isBufferEmpty(boolean input) throws RemoteException {
        return serialPort.isBufferEmpty(input);
    }

    /**
     * Clear input buffer.
     */
    public void clearInputBuffer() throws RemoteException {
        int ret = serialPort.clearInputBuffer();
        if (ret != SerialPortError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Close.
     */
    public void close() throws RemoteException {
        int ret = serialPort.close();
        if (ret != SerialPortError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final SerialPort INSTANCE = new SerialPort();
    }

    /**
     * Get serial port instance.
     */
    public static SerialPort getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private SerialPort() {

    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(SerialPortError.SUCCESS, R.string.succeed);
        errorCodes.put(SerialPortError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(SerialPortError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(SerialPortError.ERROR_DEVICE_DISABLE, R.string.device_disable);
//        errorCodes.put(SerialPortError.DEVICE_USED, R.string.device_used);
        errorCodes.put(SerialPortError.ERROR_OTHERERR, R.string.other_error);
        errorCodes.put(SerialPortError.ERROR_PARAMERR, R.string.param_error);
        errorCodes.put(SerialPortError.ERROR_TIMEOUT, R.string.timeout);
    }

    /**
     * Get error id.
     */
    private static int getErrorId(int errorCode) {
        if (errorCodes.containsKey(errorCode)) {
            return errorCodes.get(errorCode);
        }

        return R.string.other_error;
    }
}
