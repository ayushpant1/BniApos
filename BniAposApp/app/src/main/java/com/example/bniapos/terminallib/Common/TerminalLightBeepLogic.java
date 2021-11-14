package com.example.bniapos.terminallib.Common;

import android.os.RemoteException;

import com.example.bniapos.terminallib.api.Beeper;
import com.example.bniapos.terminallib.api.LED;
import com.usdk.apiservice.aidl.led.Light;

public class TerminalLightBeepLogic {
    public static void startBeep(int milliseconds) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {

                Beeper.getInstance().startBeep(milliseconds);
            } catch (Exception ex) {

            }
        }
    }

    public static void transactionSuccessLightsBeep(int milliseconds)
    {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        startBeep(milliseconds);
                        LED.getInstance().operateGreenLight();
                        sleep(300);
                        LED.getInstance().operateGreenLight();
                        sleep(300);
                        LED.getInstance().operateGreenLight();
                        sleep(300);

                    } catch (RemoteException | InterruptedException e) {
                        //


                    }
                }
            }.start();
        }
    }
    public static void transactionFailLightsBeep(int milliseconds)
    {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        startBeep(milliseconds);
                        LED.getInstance().operateYellowLight();
                        sleep(300);
                        LED.getInstance().operateRedLight();
                        sleep(300);
                        LED.getInstance().operateRedLight();
                        sleep(300);

                    } catch (RemoteException | InterruptedException e) {
                        //


                    }
                }
            }.start();
        }
    }

    public static void transactionPrintLight()
    {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {
                LED.getInstance().turnOn(Light.BLUE);

            } catch (Exception ex) {

            }
        }
    }
    public static void closePrintLights()
    {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {

                LED.getInstance().turnOff(Light.BLUE);

            } catch (Exception ex) {

            }
        }
    }
    public static void closeAllLights()
    {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {

                LED.getInstance().turnOffAll();

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }
}
