package com.example.paymentsdk.LandiSDK.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;

import com.example.paymentsdk.LandiSDK.api.ScannerForBack;
import com.example.paymentsdk.R;
import com.example.paymentsdk.sdk.Common.ISuccessResponse;
import com.example.paymentsdk.sdk.Common.TerminalApiHelper;
import com.usdk.apiservice.aidl.scanner.OnScanListener;

public class LandiTerminalApiHelper extends TerminalApiHelper {

    public ISuccessResponse successResponse = null;

    public LandiTerminalApiHelper(ISuccessResponse _successResponse) {
        super(_successResponse);
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
