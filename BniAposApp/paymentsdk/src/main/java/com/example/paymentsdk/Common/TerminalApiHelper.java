package com.example.paymentsdk.Common;

import android.app.AlertDialog;
import android.content.Context;

import android.os.RemoteException;

import android.widget.Toast;


import com.example.paymentsdk.R;
import com.example.paymentsdk.api.ScannerForBack;
import com.usdk.apiservice.aidl.scanner.OnScanListener;

public class TerminalApiHelper {

    public ISuccessResponse successResponse = null;
    public TerminalApiHelper(ISuccessResponse _successResponse)
    {
        this.successResponse = _successResponse;
    }

    //QRCode Scanning From Server
    public String StartQRCodeScan(Context context, String ControlID) throws RemoteException {
        try {
            AlertDialog.Builder alertPrintBuilder = new AlertDialog.Builder(context);
            AlertDialog alertPrint = alertPrintBuilder.create();
            Toast toast = new Toast(context);
            ScannerForBack.getInstance().startScan(30, new OnScanListener.Stub() {

                @Override
                public void onSuccess(String code) throws RemoteException {
                    successResponse.processFinish(code);
                }

                @Override
                public void onError(int error) throws RemoteException {
                    toast.setText(ScannerForBack.getErrorId(error));
                    toast.show();


                }

                @Override
                public void onTimeout() throws RemoteException {
                    toast.setText(R.string.timeout);
                    toast.show();

                }

                @Override
                public void onCancel() throws RemoteException {
                    toast.setText(R.string.cancel);
                    toast.show();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
            successResponse.processFailed(ex.getLocalizedMessage());
        }
        return null;
    }


}
