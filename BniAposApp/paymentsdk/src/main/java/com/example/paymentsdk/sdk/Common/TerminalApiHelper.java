package com.example.paymentsdk.sdk.Common;

import android.content.Context;
import android.os.RemoteException;


public abstract class TerminalApiHelper {

    public TerminalApiHelper( ISuccessResponse _successResponse) {
    }
    public abstract String StartQRCodeScan(Context context, String ControlID) throws RemoteException;

}
