package com.example.paymentsdk.LandiSDK.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.paymentsdk.LandiSDK.api.EMV;
import com.example.paymentsdk.sdk.Common.ISuccessResponse;
import com.example.paymentsdk.sdk.Common.TerminalSearchInsertCard;
import com.usdk.apiservice.aidl.emv.SearchCardListener;

/**
 *
 */
public class LandiTerminalSearchInsertCard extends TerminalSearchInsertCard {
    private Context context;
    public ISuccessResponse successResponse = null;

    //Context _context,
    public LandiTerminalSearchInsertCard(Context _context, ISuccessResponse _successResponse) {
        super(_context,_successResponse);
        this.context = _context;
        this.successResponse = _successResponse;

    }

    /**
     *
     * @param timeout
     * @throws RemoteException
     */
    public void searchCard(int timeout) throws RemoteException {

        // Start searching card
        if (EMV.getInstance() != null) {
            EMV.getInstance().searchCard(null, timeout, new SearchCardListener.Stub() {
                @Override
                public void onCardSwiped(Bundle bundle) {

                }

                @Override
                public void onCardInsert() {
                    //Insert Card Checking..
                    successResponse.processFinish(null);


                }

                @Override
                public void onCardPass(int cardType) {

                }

                @Override
                public void onTimeout() throws RemoteException {
                    successResponse.processTimeOut();

                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    // Stop EMV process
                    Log.e("searchCard", " : " + message);

                }
            });
        }
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        try {
            EMV.getInstance().stopSearch();
            EMV.getInstance().halt();
        }
        catch (Exception ex)
        {

        }
    }
}
