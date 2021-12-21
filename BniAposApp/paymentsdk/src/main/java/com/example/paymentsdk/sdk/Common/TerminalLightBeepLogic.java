package com.example.paymentsdk.sdk.Common;

import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.LandiSDK.api.Beeper;
import com.example.paymentsdk.LandiSDK.api.LED;
import com.usdk.apiservice.aidl.led.Light;
import com.vfi.smartpos.deviceservice.constdefine.ConstILed;

public class TerminalLightBeepLogic {
    public static void startBeep(int milliseconds) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {

                if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi))
                    Beeper.getInstance().startBeep(milliseconds);


            } catch (Exception ex) {

            }
        }
    }

    public static void transactionSuccessLightsBeep(int milliseconds) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi)) {
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
            } else if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione)) {

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startBeep(milliseconds);
                            CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.GREEN);
                            sleep(300);
                            CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.GREEN);
                            sleep(300);
                            CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.GREEN);
                            sleep(300);

                        } catch (RemoteException | InterruptedException e) {
                            //


                        }
                    }
                }.start();

            }
        }


    }

    public static void transactionFailLightsBeep(int milliseconds) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi)) {
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
            } else if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione)) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startBeep(milliseconds);
                            CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.RED);
                            sleep(300);
                            CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.RED);
                            sleep(300);
                            CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.RED);
                            sleep(300);

                        } catch (RemoteException | InterruptedException e) {
                            //


                        }
                    }
                }.start();
            }
        }
    }

    public static void transactionPrintLight() {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {
                if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi))
                    LED.getInstance().turnOn(Light.BLUE);
                else if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione)) {
                    CTIApplication.getVerifoneDeviceService().getLed().turnOn(ConstILed.BLUE);
                }

            } catch (Exception ex) {

            }
        }
    }

    public static void closePrintLights() {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {

                if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi))
                    LED.getInstance().turnOff(Light.BLUE);
                else if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione)) {
                    CTIApplication.getVerifoneDeviceService().getLed().turnOff(ConstILed.BLUE);
                }

            } catch (Exception ex) {

            }
        }
    }

    public static void closeAllLights() {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            try {

                if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi))
                    LED.getInstance().turnOffAll();
                else if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione)) {

                    CTIApplication.getVerifoneDeviceService().getLed().turnOff(ConstILed.BLUE);
                    CTIApplication.getVerifoneDeviceService().getLed().turnOff(ConstILed.GREEN);
                    CTIApplication.getVerifoneDeviceService().getLed().turnOff(ConstILed.RED);
                    CTIApplication.getVerifoneDeviceService().getLed().turnOff(ConstILed.YELLOW);
                }

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }
}
