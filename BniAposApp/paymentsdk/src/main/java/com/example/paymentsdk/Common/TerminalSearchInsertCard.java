package com.example.paymentsdk.Common;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.paymentsdk.CardReadOutput;
import com.example.paymentsdk.api.DeviceManager;
import com.example.paymentsdk.api.EMV;
import com.example.paymentsdk.util.transaction.Session;
import com.usdk.apiservice.aidl.constants.RFDeviceName;
import com.usdk.apiservice.aidl.emv.SearchCardListener;

public class TerminalSearchInsertCard implements AutoCloseable {
    private Context context;
    public ISuccessResponse_Card successResponse = null;
    /**
     * Session for transaction.
     */
    private Session session;

    private static final String TAG = "EMV_Ingenico";


    //Context _context,
    public TerminalSearchInsertCard(Context _context, ISuccessResponse_Card _successResponse) {

        this.context = _context;
        this.successResponse = _successResponse;

        this.session = null;


        session = new Session();


    }

    public void searchCard(int timeout) throws RemoteException {

        if (DeviceManager.getInstance() != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("supportICCard", true);
            bundle.putBoolean("supportRFCard", false);
            bundle.putBoolean("supportMagCard", true);
            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                bundle.putString("rfDeviceName", RFDeviceName.EXTERNAL);
            } else {
                bundle.putString("rfDeviceName", RFDeviceName.INNER);
            }

            // Start searching card
            if (EMV.getInstance() != null) {
                EMV.getInstance().searchCard(bundle, timeout, new SearchCardListener.Stub() {
                    @Override
                    public void onCardSwiped(Bundle bundle) {

                        handleSwipeCard(bundle);
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
        } else
            successResponse.processFailed("Device Manager is not initialized");
    }

    private void handleSwipeCard(Bundle bundle) {
        // Save card record
        session.setAccountEntryMode(Session.ACCOUNT_ENTRY_MODE_MAGCARD);
        if(Constant.isShowDummyCard)
        {
            session.setPan(Constant.DummyCardNo);
            session.setTrack2Data(Constant.DummyTrack2Data);
            session.setExpirationDate(Constant.DummyExpiryDate);
        }
        else{
            session.setPan(bundle.getString("PAN"));
            session.setTrack2Data(bundle.getString("TRACK2"));
            session.setExpirationDate(bundle.getString("EXPIRED_DATE"));
        }

        String Service_Code = bundle.getString("SERVICE_CODE");
        if (Service_Code.startsWith("2") || Service_Code.startsWith("6")) {

            successResponse.processFailed("CARD SWIPE NOT ALLOWED");

        } else {
            String CardHolderName = "";
            String TRACK1 = bundle.getString("TRACK1");
            if (TRACK1.indexOf("^") > 0) {

                String[] TrackData = TRACK1.trim().split("\\^");
                if (TrackData.length >= 2) {
                    CardHolderName = TrackData[1].trim();

                    CardHolderName = CardHolderName.replace("/", "");
                    session.setCardholderName(CardHolderName.trim());

                }
            }
            onConfirmCardRecord(session.getPan(), session.getExpirationDate());
        }
    }
    public void onConfirmCardRecord(String pan, String date) {

        //Here we are managing our Output

        String EntryMode = session.getAccountEntryMode();

        String FirstCurrencyCode = "";
        String AID = "";
        String CardHolderName = "";
        String ApplicationName = "";
        String TransactionCount = "";
        String UnPredictableNumber = "";
        String Track2 = "";
        try {
            if (!(session.getCardholderName() != null && session.getCardholderName().length() > 0)) {

                CardHolderName = Util.hexToAscii(EMV.getInstance().getTLV("5F20"));
                if (CardHolderName != null && CardHolderName.length() > 0) {
                    CardHolderName = CardHolderName.replace("/", "");
                    session.setCardholderName(CardHolderName.trim());
                }

                String ExpiryDateFull = EMV.getInstance().getTLV("5F24");


                ApplicationName = Util.hexToAscii(EMV.getInstance().getTLV("9F12"));
                session.setIssuerMessage(ApplicationName);

                UnPredictableNumber = EMV.getInstance().getTLV("9F37");
            } else
                CardHolderName = session.getCardholderName();


            Track2 = session.getTrack2Data();


            AID = session.getAid();
            session.setAid(AID);
        } catch (Exception ex) {

        }

        CardReadOutput CardOutput = new CardReadOutput();
        CardOutput.setCardAID(AID);
        CardOutput.setCardAppName(ApplicationName);
        CardOutput.setCardExpiry(date);
        CardOutput.setCardNo(pan);
        CardOutput.setCurrencyCode(FirstCurrencyCode);
        CardOutput.setInsertModeCode(EntryMode);
        CardOutput.setInsertMode(TransactionUtils.CardCustomInsertMode(EntryMode));
        CardOutput.setCustomerName(CardHolderName);
        CardOutput.setTransactionCount(TransactionCount);
        CardOutput.setTrack2Data(Track2);

//
//        CardOutput.setEMVData(TLVData);

        successResponse.processFinish(CardOutput);

    }

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
