package com.example.paymentsdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import com.example.paymentsdk.LandiSDK.api.DeviceService;

import com.example.paymentsdk.LandiSDK.util.data.BytesUtil;
import com.example.paymentsdk.sdk.Common.Constant;
import com.example.paymentsdk.sdk.Common.TerminalSecurity;
import com.usdk.apiservice.aidl.UDeviceService;
import com.vfi.smartpos.deviceservice.aidl.IDeviceService;


public class CTIApplication {

    private static final String USDK_ACTION_NAME = "com.usdk.apiservice";
    private static final String USDK_PACKAGE_NAME = "com.usdk.apiservice";
    private static DeviceService deviceService;


    private static final String VERIFONE_USDK_ACTION_NAME = "com.vfi.smartpos.device_service";
    private static final String VERIFONE_USDK_PACKAGE_NAME = "com.vfi.smartpos.deviceservice";

    private static Context context;
    private static String TAG="service_connect:";

    private static IDeviceService verfioneDeviceService;


    /**
     * Get context.
     */
    public static Context getContext() {
        if (context == null) {
            throw new RuntimeException("Initiate context failed");
        }
        return context;
    }

    /**
     * Set context.
     */
    public static void setContext(Context context) {
        CTIApplication.context = context;
    }

    /**
     * Get device service instance.
     */
    public static DeviceService getDeviceService() {
        if (deviceService == null) {
            throw new RuntimeException("SDK service is still not connected.");
        }
        return deviceService;
    }

    public static void terminateService(Context context)
    {
        try {

            if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi)) {
                if (deviceService != null)
                    deviceService.unregister();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        context.unbindService(serviceConnection);
    }


    public static void bindSdkDeviceService(Context context) {
        Intent intent_L = new Intent();
        Intent intent_V = new Intent();
        intent_L.setAction(USDK_ACTION_NAME);
        intent_L.setPackage(USDK_PACKAGE_NAME);
        intent_V.setAction(VERIFONE_USDK_ACTION_NAME);
        intent_V.setPackage(VERIFONE_USDK_PACKAGE_NAME);

        if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi)) {
            Log.d(TAG, "binding sdk device service...");
            boolean flag = context.bindService(intent_L, serviceConnection, Context.BIND_AUTO_CREATE);
            if (!flag) {
                Log.d(TAG, "SDK service binding failed.");
                return;
            }
            else
                Log.d(TAG, "SDK service binding successfully.");

        } else if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione)) {
            Log.d(TAG, "binding sdk device service...");
            boolean flag = context.bindService(intent_V, serviceConnection, Context.BIND_AUTO_CREATE);
            if (!flag) {
                Log.d(TAG, "SDK service binding failed.");
                return;
            }
            else

                Log.d(TAG, "SDK service binding successfully.");
        }

    }

    public static void unregisterDeviceService() {
        try {
            deviceService.unregister();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void unbindServiceConnection() {
        terminateService(context);

    }

    /**
     * Get device service instance.
     */
    public static IDeviceService getVerifoneDeviceService() {
        if (verfioneDeviceService == null) {
            throw new RuntimeException("Verifone SDK service is still not connected.");
        }

        return verfioneDeviceService;
    }



    /**
     * Service connection.
     */
    private static ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "SDK service disconnected.");
            deviceService = null;
            verfioneDeviceService=null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "SDK service connected.");

            try {
                if(Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi)) {
                    deviceService = new DeviceService(UDeviceService.Stub.asInterface(service));
                    deviceService.register();
                    deviceService.debugLog(true, true);
                    Log.d(TAG, "SDK deviceService initiated version:" + deviceService.getVersion() + ".");
                }
                else if(Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Verfione))
                {
                    verfioneDeviceService = IDeviceService.Stub.asInterface(service);

                    Log.d(TAG, "SDK deviceService initiated");
                }

                loadMasterAndSessionKey();
            } catch (RemoteException e) {
                throw new RuntimeException("SDK deviceService initiating failed.", e);
            }

            try {
                linkToDeath(service);
            } catch (RemoteException e) {
                throw new RuntimeException("SDK service link to death error.", e);
            }
        }
        private void loadMasterAndSessionKey() {
            TerminalSecurity.LoadMasterKey(BytesUtil.hexString2ByteArray(
                    Constant.PINBMasterKey), null);
            TerminalSecurity.LoadSessionKey(BytesUtil.hexString2ByteArray(Constant.PINBSessionKey));
        }

        private void linkToDeath(IBinder service) throws RemoteException {
            service.linkToDeath(() -> {
                Log.d(TAG, "SDK service is dead. Reconnecting...");
                bindSdkDeviceService(context);
            }, 0);
        }
    };
}
