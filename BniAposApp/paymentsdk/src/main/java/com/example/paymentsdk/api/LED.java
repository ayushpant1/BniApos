package com.example.paymentsdk.api;

import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.usdk.apiservice.aidl.led.Light;
import com.usdk.apiservice.aidl.led.ULed;

/**
 * LED API.
 */

public class LED {

    /**
     * LED object.
     */
    private ULed led = CTIApplication.getDeviceService().getLed();

    /**
     * Red light state.
     */
    private boolean redIsOpen = false;

    /**
     * Green light state.
     */
    private boolean greenIsOpen = false;

    /**
     * Yellow light state.
     */
    private boolean yellowIsOpen = false;

    /**
     * Blue light state.
     */
    private boolean blueIsOpen = false;

    /**
     * Turn on all lights.
     */
    public void turnOnAll() throws RemoteException {
        turnOn(Light.RED, Light.GREEN, Light.YELLOW, Light.BLUE);
    }

    /**
     * Turn off all lights.
     */
    public void turnOffAll() throws RemoteException {
        turnOff(Light.RED, Light.GREEN, Light.YELLOW, Light.BLUE);
    }

    /**
     * Operate red light.
     */
    public void operateRedLight() throws RemoteException {
        if (redIsOpen) {
            turnOff(Light.RED);
        } else {
            turnOn(Light.RED);
        }
    }

    /**
     * Operate green light.
     */
    public void operateGreenLight() throws RemoteException {
        if (greenIsOpen) {
            turnOff(Light.GREEN);
        } else {
            turnOn(Light.GREEN);
        }
    }

    /**
     * Operate yellow light.
     */
    public void operateYellowLight() throws RemoteException {
        if (yellowIsOpen) {
            turnOff(Light.YELLOW);
        } else {
            turnOn(Light.YELLOW);
        }
    }

    /**
     * Operate blue light.
     */
    public void operateBlueLight() throws RemoteException {
        if (blueIsOpen) {
            turnOff(Light.BLUE);
        } else {
            turnOn(Light.BLUE);
        }
    }

    /**
     * Turn on lights.
     */
    public void turnOn(int... lights) throws RemoteException {
        for (int light : lights) {
            switch (light) {
                case Light.BLUE:
                    led.turnOn(light);
                    blueIsOpen = true;
                    break;
                case Light.YELLOW:
                    led.turnOn(light);
                    yellowIsOpen = true;
                    break;
                case Light.GREEN:
                    led.turnOn(light);
                    greenIsOpen = true;
                    break;
                case Light.RED:
                    led.turnOn(light);
                    redIsOpen = true;
                    break;
            }
        }
    }

    /**
     * Turn off lights.
     */
    public void turnOff(int... lights) throws RemoteException {
        for (int light : lights) {
            switch (light) {
                case Light.BLUE:
                    led.turnOff(light);
                    blueIsOpen = false;
                    break;
                case Light.YELLOW:
                    led.turnOff(light);
                    yellowIsOpen = false;
                    break;
                case Light.GREEN:
                    led.turnOff(light);
                    greenIsOpen = false;
                    break;
                case Light.RED:
                    led.turnOff(light);
                    redIsOpen = false;
                    break;
            }
        }
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final LED INSTANCE = new LED();
    }

    /**
     * Get LED instance.
     */
    public static LED getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private LED() {

    }
}
