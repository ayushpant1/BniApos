package com.example.paymentsdk.VerifoneSDK.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.example.paymentsdk.sdk.Common.ISuccessResponse;
import com.example.paymentsdk.sdk.Common.TerminalApiHelper;
import com.vfi.smartpos.deviceservice.aidl.IScanner;
import com.vfi.smartpos.deviceservice.aidl.ScannerListener;


public class VFTerminalApiHelper extends TerminalApiHelper {

    public ISuccessResponse successResponse = null;

    public VFTerminalApiHelper(ISuccessResponse _successResponse) {
        super(_successResponse);
        this.successResponse = _successResponse;
    }

    ScannerListener scannerListener;

    private void initateScanner(Context context) {
        Toast toast = new Toast(context);


        scannerListener = new ScannerListener.Stub() {
            @Override
            public void onSuccess(String barcode) throws RemoteException {
                successResponse.processFinish(barcode);
            }

            @Override
            public void onError(int error, String message) throws RemoteException {
                toast.setText("Error : " + message);
                toast.show();
            }

            @Override
            public void onTimeout() throws RemoteException {
                toast.setText(R.string.timeout);
                toast.show();
            }

            @Override
            public void onCancel() throws RemoteException {

                toast.setText(R.string.timeout);
                toast.show();
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        };
    }

    //QRCode Scanning From Server
    public String StartQRCodeScan(Context context, String ControlID) throws RemoteException {
        try {

            IScanner scanner = CTIApplication.getVerifoneDeviceService().getScanner(0); //back camera ... 1/front camera

            initateScanner(context);

            Bundle paramScanner = new Bundle();
            paramScanner.putString("topTitleString", "CTIPayment Scanner");
            paramScanner.putString("upPromptString", "Scan QR/Barcode");
            paramScanner.putString("downPromptString", "Scan QR Code align to this box");


            scanner.startScan(paramScanner, 30000, scannerListener);

        } catch (Exception ex) {
            ex.printStackTrace();
            successResponse.processFailed(ex.getLocalizedMessage());
        }
        return null;
    }


}
