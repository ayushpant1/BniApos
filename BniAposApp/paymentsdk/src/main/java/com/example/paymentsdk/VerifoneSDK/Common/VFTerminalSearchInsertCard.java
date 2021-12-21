package com.example.paymentsdk.VerifoneSDK.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.sdk.Common.ISuccessResponse;
import com.example.paymentsdk.sdk.Common.TerminalSearchInsertCard;
import com.vfi.smartpos.deviceservice.aidl.CheckCardListener;
import com.vfi.smartpos.deviceservice.aidl.IPBOC;
import com.vfi.smartpos.deviceservice.constdefine.ConstIPBOC;

public class VFTerminalSearchInsertCard extends TerminalSearchInsertCard {
    private Context context;
    public ISuccessResponse successResponse = null;
    IPBOC ipboc;


    //Context _context,
    public VFTerminalSearchInsertCard(Context _context, ISuccessResponse _successResponse) {

        super(_context, _successResponse);
        this.context = _context;
        this.successResponse = _successResponse;

    }

    public void searchCard(int timeout) throws RemoteException {
        Bundle cardOption = new Bundle();
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_Contactless_boolean, false);
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_SmartCard_boolean, true);
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_MagneticCard_boolean, true);

        try {
            //Set AID / RID File settings Here..
            ipboc = CTIApplication.getVerifoneDeviceService().getPBOC();

            ipboc.checkCard(cardOption, timeout, new CheckCardListener.Stub() {
                        @Override
                        public void onCardSwiped(Bundle track) throws RemoteException {


                        }

                        @Override
                        public void onCardPowerUp() throws RemoteException {
                            successResponse.processFinish(null);
                        }

                        @Override
                        public void onCardActivate() throws RemoteException {


                        }

                        @Override
                        public void onTimeout() throws RemoteException {
                            successResponse.processTimeOut();
                        }

                        @Override
                        public void onError(int error, String message) throws RemoteException {

                            Log.e("searchCard", " : " + message);
                        }
                    }
            );


        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {

        try {
            ipboc.stopCheckCard();
            ipboc.abortPBOC();
        } catch (Exception ex) {
            Log.e("Stop EMV", ex.getLocalizedMessage());
        }


    }
}
