package com.example.paymentsdk.VerifoneSDK.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.CardReadOutput;
import com.example.paymentsdk.VerifoneSDK.util.data.BytesUtil;
import com.example.paymentsdk.VerifoneSDK.util.data.DateUtil;
import com.example.paymentsdk.VerifoneSDK.util.data.TLV;
import com.example.paymentsdk.VerifoneSDK.util.data.TLVList;
import com.example.paymentsdk.VerifoneSDK.util.emv.EmvTags;
import com.example.paymentsdk.VerifoneSDK.util.emv.VFEmvData;
import com.example.paymentsdk.VerifoneSDK.util.transaction.Session;
import com.example.paymentsdk.sdk.Common.AIDFile;
import com.example.paymentsdk.sdk.Common.Constant;
import com.example.paymentsdk.sdk.Common.ISuccessResponse_Card;
import com.example.paymentsdk.sdk.Common.TerminalCardApiHelper;
import com.example.paymentsdk.sdk.Common.TerminalSecurity;
import com.example.paymentsdk.sdk.Common.TransactionUtils;
import com.example.paymentsdk.sdk.util.transaction.TransactionConfig;
import com.vfi.smartpos.deviceservice.aidl.CheckCardListener;
import com.vfi.smartpos.deviceservice.aidl.EMVHandler;
import com.vfi.smartpos.deviceservice.aidl.IBeeper;
import com.vfi.smartpos.deviceservice.aidl.IEMV;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;
import com.vfi.smartpos.deviceservice.aidl.OnlineResultHandler;
import com.vfi.smartpos.deviceservice.aidl.PinInputListener;
import com.vfi.smartpos.deviceservice.constdefine.ConstCheckCardListener;
import com.vfi.smartpos.deviceservice.constdefine.ConstIPBOC;
import com.vfi.smartpos.deviceservice.constdefine.ConstIPinpad;
import com.vfi.smartpos.deviceservice.constdefine.ConstOnlineResultHandler;
import com.vfi.smartpos.deviceservice.constdefine.ConstPBOCHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class VFTerminalCardApiHelper extends TerminalCardApiHelper {

    boolean isCardHolderVerificationPerformed = false;

    boolean _isOnlinePin = false;

    int _retryTimes = 5;
    /**
     * Goods.
     */
    public final static byte SERVICE_TYPE_GOODS_SERVICE = 0x00;

    /**
     * CashBack.
     */
    public final static byte SERVICE_TYPE_CASH_BACK = 0x09;

    /**
     * Cash.
     */
    public final static byte SERVICE_TYPE_CASH = 0x01;

    /**
     * Refund.
     */
    public final static byte SERVICE_TYPE_REFUND = 0x20;

    private Context context;
    private CardReadOutput _CardOutput;

    // keys
    int mainKeyId = 97;
    int workKeyId = 1;


    IEMV iemv;
    IPinpad ipinpad;
    IBeeper iBeeper;

    EMVHandler emvHandler;
    PinInputListener pinInputListener;

    String TAG = "EMV_Verifone";

    String savedPan = "";

    /**
     * Is in EMV process.
     */
    private boolean isEMVProcess;

    /**
     * Is search card first.
     */
    private boolean isSearchCardFirst = true;

    private boolean isBreakEMIFlow = false;


    private boolean isCardDataRecorded = false;

    private boolean isRefund = false;

    private String AccountEntryMode = "";

    /**
     * Transaction config.
     */
    private TransactionConfig transactionConfig;

    public ISuccessResponse_Card successResponse = null;

    public VFTerminalCardApiHelper(Context _context, ISuccessResponse_Card _successResponse) {

        super(_context, _successResponse);
        this.context = _context;
        this.successResponse = _successResponse;

        transactionConfig = new TransactionConfig();
        try {
            //Set AID / RID File settings Here..
            iemv = CTIApplication.getVerifoneDeviceService().getEMV();
            ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
            iBeeper = CTIApplication.getVerifoneDeviceService().getBeeper();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

        InitializeAID();
        initializeEMV();
        initializePinInputListener();
    }

    void initializeEMV() {

        emvHandler = new EMVHandler.Stub() {
            @Override
            public void onRequestAmount() throws RemoteException {

            }

            @Override
            public void onSelectApplication(List<Bundle> appList) throws RemoteException {
                if (appList != null && appList.size() > 0) {
                    HashMap<byte[], String> allAIDS = new HashMap<byte[], String>();

                    List<AIDFile> aidFileList = new ArrayList<>();
                    for (Bundle aidBundle : appList) {
                        AIDFile aidFile = new AIDFile();
                        String aidName = aidBundle.getString("aidName");
                        String aid = aidBundle.getString("aid");
                        String aidLabel = aidBundle.getString("aidLabel");
                        aidFile.setAID(BytesUtil.hexString2ByteArray(aid));
                        aidFile.setAPN(BytesUtil.hexString2ByteArray(aidName));
                        aidFile.setAppLabel(BytesUtil.hexString2ByteArray(aidLabel));
                        aidFileList.add(aidFile);
                    }
                    _CardOutput.setAllAIDs(aidFileList);
                    if (allAIDS.size() == 1) {
                        iemv.importAppSelection(1);
                    } else {
                        successResponse.SelectCardApplication(_CardOutput);
                    }
                }
            }


            @Override
            public void onConfirmCardInfo(Bundle info) throws RemoteException {
                Log.d(TAG, "onConfirmCardInfo...");

                //confirm it, go to next step

                savedPan =
                        info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_PAN_String);

                String Track2 = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String);
                String CardSNo = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_CARD_SN_String);
                String ServiceCode = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_SERVICE_CODE_String);
                String ExpiredDate = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_EXPIRED_DATE_String);


                //Here we are managing our Output

                onConfirmCardRecord(savedPan, ExpiredDate, Track2);


            }

            @Override
            public void onRequestInputPIN(boolean isOnlinePin, int retryTimes) throws RemoteException {
                toastShow("onRequestInputPIN isOnlinePin:" + isOnlinePin);
                _isOnlinePin = isOnlinePin;
                _retryTimes = retryTimes;

                isCardHolderVerificationPerformed = true;
                if (!transactionConfig.isPinInputNeeded()) {
                    iemv.importPin(1, null);
                } else
                    doPinPad();

                //here we can decide, to go with onlinepin or offline pin..
                //currently no clarity on offline pin, so calling pinpad only
                // show the pin pad, import the pin block


            }

            @Override
            public void onConfirmCertInfo(String certType, String certInfo) throws RemoteException {
                toastShow("onConfirmCertInfo, type:" + certType + ",info:" + certInfo);

                iemv.importCertConfirmResult(ConstIPBOC.importCertConfirmResult.option.CONFIRM);
            }


            @Override
            public void onRequestOnlineProcess(Bundle aaResult) throws RemoteException {

                Log.d(TAG, "onRequestOnlineProcess...");
                int result = aaResult.getInt(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_RESULT_int);
                Log.d(TAG, "onRequestOnlineProcess...Result : " + result);
                toastShow("onRequestOnlineProcess result=" + result);
                switch (result) {
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_AARESULT_ARQC:
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_QPBOC_ARQC:
                        toastShow(aaResult.getString(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_ARQC_DATA_String));
                        break;
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_PAYPASS_EMV_ARQC:
                        break;
                }
                //if card has not forced for input pin....
                if (!isCardHolderVerificationPerformed && transactionConfig.isPinInputNeeded()) {
                    try {
                        doPinPad();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    FinalizingRequestForEMV();
                }
            }

            @Override
            public void onTransactionResult(int result, Bundle data) throws RemoteException {
                Log.d(TAG, "onTransactionResult");
                String msg = data.getString("ERROR");
                toastShow("onTransactionResult result = " + result + ",msg = " + msg);

                switch (result) {
                    case ConstOnlineResultHandler.onProccessResult.result.Paypass_Complete: {

                        successResponse.TransactionApproved();
                        for (String key : data.keySet()) {
                            String expiry = data.getString(key);
                            String Pan = data.getString(key);
                            ;
                            String Track2 = data.getString(key);
                            ;

                        }

                        // onConfirmCardRecord()
                        break;
                    }
                    case ConstOnlineResultHandler.onProccessResult.result.TC:
                        successResponse.TransactionApproved();
                        break;
                    case ConstOnlineResultHandler.onProccessResult.result.Offline_TC:
                        successResponse.TransactionApproved();

                        break;
                    case ConstOnlineResultHandler.onProccessResult.result.Online_AAC: {

                        //May be we don't need below condition
                        byte[] TVRValue = iemv.getCardData(Integer.toHexString(0X95).toUpperCase());
                        toastShow("Transaction Analysis Failed : " + BytesUtil.byteArray2HexString(TVRValue));
                        successResponse.processFailed("Transaction Analysis Failed");
                        break;
                    }


                    case ConstPBOCHandler.onTransactionResult.result.EMV_CARD_BIN_CHECK_FAIL: {
                        // read card fail
                        toastShow("read card fail");
                        successResponse.processFailed("Unable to detect BIN Range");
                        break;
                    }
                    case ConstPBOCHandler.onTransactionResult.result.EMV_MULTI_CARD_ERROR: {
                        // multi-cards found
                        toastShow("Multi Card Error");
                        successResponse.processFailed("Multi Card Error");
                        break;
                    }
                }
            }
        };
    }

    public void startCardScan(TransactionConfig config, String STAN, boolean isRefundTxn) throws RemoteException {

        transactionConfig = config;
        isRefund = isRefundTxn;

        doSearchCard(config);
    }

    void doSearchCard(final TransactionConfig transType) {

        Bundle cardOption = new Bundle();
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_Contactless_boolean, transactionConfig.isRfCardSupported());
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_SmartCard_boolean, transactionConfig.isContactIcCardSupported());
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_MagneticCard_boolean, transactionConfig.isMagCardSupported());


        try {
            iemv.checkCard(cardOption, 60, new CheckCardListener.Stub() {
                        @Override
                        public void onCardSwiped(Bundle track) throws RemoteException {
                            Log.d(TAG, "onCardSwiped ...");
                            iemv.stopCheckCard();
                            iemv.abortEMV();

                            iBeeper.startBeep(200);

                            String pan = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_PAN_String);
                            String track1 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK1_String);
                            String track2 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK2_String);
                            String track3 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK3_String);
                            String serviceCode = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_SERVICE_CODE_String);
                            Log.d(TAG, "onCardSwiped ...1");

                            if (serviceCode.startsWith("2") || serviceCode.startsWith("6")) {

                                successResponse.processFailed("CARD SWIPE NOT ALLOWED");

                            } else {
                                String validDate = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_EXPIRED_DATE_String);
                                if (validDate.length() > 0) {

                                }
                                Log.d(TAG, "onCardSwiped ...3");
                                AccountEntryMode = Session.ACCOUNT_ENTRY_MODE_MAGCARD;
                                onConfirmCardRecord(pan, validDate, track2);
                                successResponse.Communication(true);
                                isBreakEMIFlow = true;
                                isEMVProcess = false;
                            }

                        }

                        @Override
                        public void onCardPowerUp() throws RemoteException {
                            iemv.stopCheckCard();
                            iemv.abortEMV();
                            iBeeper.startBeep(200);
                            AccountEntryMode = Session.ACCOUNT_SERVICE_ENTRY_MODE_CONTACT;
                            successResponse.CardInserted();
                            doEMV(ConstIPBOC.startEMV.intent.VALUE_cardType_smart_card, transType);
                        }

                        @Override
                        public void onCardActivate() throws RemoteException {
                            iemv.stopCheckCard();
                            iemv.abortEMV();
                            iBeeper.startBeep(200);
                            AccountEntryMode = Session.ACCOUNT_ENTRY_MODE_CONTACTLESS;
                            doEMV(ConstIPBOC.startEMV.intent.VALUE_cardType_contactless, transType);

                        }

                        @Override
                        public void onTimeout() throws RemoteException {
                            toastShow("timeout");
                        }

                        @Override
                        public void onError(int error, String message) throws RemoteException {
                            toastShow("error:" + error + ", msg:" + message);
                        }
                    }
            );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * @brief sample of EMV
     * <p>
     * \code{.java}
     * \endcode
     * @version
     * @see
     */
    void doEMV(int type, TransactionConfig transType) {

        isEMVProcess = true;
        //
        Log.i(TAG, "start EMV demo");

        Bundle emvIntent = new Bundle();
        emvIntent.putInt(ConstIPBOC.startEMV.intent.KEY_cardType_int, type);
//        emvIntent.putLong(ConstIPBOC.startEMV.intent.KEY_authAmount_long, 1 );


//        if( transType.TRANSACTION_TYPE_SIMPLE ) {
        //    emvIntent.putLong(ConstIPBOC.startEMV.intent.KEY_authAmount_long, Long.valueOf(transType.getAmount() ) );
        //   }
        //  emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_merchantName_String, merchantName );

        //   emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_merchantId_String, merchantID );  // 010001020270123
        //  emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_terminalId_String, terminalID );   // 00000001
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportQ_boolean, ConstIPBOC.startEMV.intent.VALUE_supported);

        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportSM_boolean, ConstIPBOC.startEMV.intent.VALUE_supported);
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isQPBOCForceOnline_boolean, ConstIPBOC.startEMV.intent.VALUE_unforced);
        if (type == ConstIPBOC.startEMV.intent.VALUE_cardType_contactless) {
            emvIntent.putLong(ConstIPBOC.startEMV.intent.KEY_authAmount_long, transType.getAmount());

            emvIntent.putByte(ConstIPBOC.startEMV.intent.KEY_transProcessCode_byte, (byte) 0x00);
        }
        emvIntent.putBoolean("isSupportPBOCFirst", false);

        try {

            iemv.startEMV(ConstIPBOC.startEMV.processType.full_process, emvIntent, emvHandler);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief set main key and work key
     * <p>
     * \code{.java}
     * \endcode
     * @version
     * @see
     */
    void doSetKeys() {
        // Load Main key
        // 758F0CD0C866348099109BAF9EADFA6E

        // TerminalSecurity.ClearSessionKey(ipinpad);
//
//        boolean bRet = false;
//       try {
//            byte[] mainKey = BytesUtil.hexString2ByteArray(Constant.PINBMasterKey);// BytesUtil.hexString2ByteArray(MockKey.mainKey); //Get it from server...
//           bRet = ipinpad.loadMainKey(mainKeyId, mainKey, null);
//
//
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

//         Load work key
//         89B07B35A1B3F47E89B07B35A1B3F488

//
//        try {
//            //0 for 3DES Master Key
//            bRet =
//                    ipinpad.loadWorkKey(PinpadKeyType.PINKEY, mainKeyId, workKeyId,
//                            BytesUtil.hexString2ByteArray(Constant.PINBSessionKey), null);
////
//            ipinpad.loadWorkKeyWithDecryptType(PinpadKeyType.PINKEY,mainKeyId,workKeyId,0,
//                    BytesUtil.hexString2ByteArray(Constant.PINBSessionKey), null);
////
//
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

        if (!(TerminalSecurity.IsSessionKeyAvailable())) {
            successResponse.processFailed("INVALID PINPAD");
        }
    }


    /**
     * @brief show the pinpad
     * <p>
     * \code{.java}
     * \endcode
     * @version
     * @see
     */
    public void inputOnlinePin() throws RemoteException {
        doPinPad();
    }

    public void byePassOnlinePin() throws RemoteException {
        try {
            iemv.importPin(1, null);
        } catch (Exception ex) {
            successResponse.processFailed("EMV PIN Byepass failed");
        }
    }

    public void handleMultipleAppSelection(AIDFile aid) throws RemoteException {
        boolean isValidAID = false;
        if (aid != null) {
            isValidAID = true;
            iemv.importAppSelection(1);
        }
        if (!isValidAID)
            successResponse.Communication(true);
    }

    void doPinPad() {

        Bundle param = new Bundle();
        Bundle globleparam = new Bundle();
        String panBlock = savedPan;
        byte[] pinLimit = transactionConfig.getPinRule();
        _isOnlinePin = isEMVProcess ? _isOnlinePin : true;

        param.putByteArray(ConstIPinpad.startPinInput.param.KEY_pinLimit_ByteArray, pinLimit);
        param.putInt(ConstIPinpad.startPinInput.param.KEY_timeout_int, 20);
        param.putBoolean(ConstIPinpad.startPinInput.param.KEY_isOnline_boolean, _isOnlinePin);
        param.putString(ConstIPinpad.startPinInput.param.KEY_pan_String, panBlock);
        param.putInt(ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_3DES);

        if (!_isOnlinePin && isEMVProcess) {
            param.putString(ConstIPinpad.startPinInput.param.KEY_promptString_String, "OFFLINE PIN, retry times:" + _retryTimes);
        }

        try {

            ipinpad.startPinInput(workKeyId, param, globleparam, pinInputListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    /**
     * @brief initialize the pin pad listener
     * <p>
     * \code{.java}
     * \endcode
     * @version
     * @see
     */
    void initializePinInputListener() {
        doSetKeys();
        pinInputListener = new PinInputListener.Stub() {
            @Override
            public void onInput(int len, int key) throws RemoteException {
                Log.d(TAG, "PinPad onInput, len:" + len + ", key:" + key);
            }

            @Override
            public void onConfirm(byte[] data, boolean isNonePin) throws RemoteException {
                Log.d(TAG, "PinPad onConfirm");
                handlePinConfirm(data);

                //Earlier Logic to handle Pin Confirm.. now using above method handlePinConfirm(byte[])

//                String PINBLOCK = BytesUtil.byteArray2HexString(data);// new String(data, StandardCharsets.UTF_8);
//
//                _CardOutput.setPINBlock(PINBLOCK);
//                iemv.importPin(1, data );
//               // successResponse.PinProcessConfirm(BytesUtil.byteArray2HexString(data));
//                if(isBreakEMIFlow)
//                {
//                    FinalizingRequestForEMV();
//                }
//                else
//                {
//                 //   handlePinConfirm(data);
//                }
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG, "PinPad onCancel");

                handlePinCancel();
                //Earlier Logic to handle Pin Cancel.. now using above method handlePinCancel()

//                successResponse.PinProcessFailed("Pin Cancelled");
//
//                // Whether not EMV process
//                if (!isEMVProcess) {
//                    stopEMVProcess();
//                    return;
//                }
//
//

            }

            @Override
            public void onError(int errorCode) throws RemoteException {

                handlePinError(errorCode);
                //Earlier Logic to handle Pin Confirm.. now using above method handlePinError(int)
                //successResponse.PinProcessFailed("pin entry failed: error code: "+errorCode);
            }
        };
    }

    private void InitializeAID() {
        toastShow("Set AID start");
        VFEmvData emvSetAidRid = new VFEmvData(iemv);

        //We need to clear and set this information where ever we are updating our AID / RID List etc.. not here everytime


        emvSetAidRid.setAID(ConstIPBOC.updateRID.operation.clear);
        emvSetAidRid.setAID(ConstIPBOC.updateRID.operation.append);


    }


    @Override
    public void close() throws Exception {
        try {
            stopEMVProcess();
        } catch (Exception ex) {

        }

    }

    private void onConfirmCardRecord(String pan, String date, String Track2) {
        String EntryMode = AccountEntryMode;
        savedPan = pan;
        String FirstCurrencyCode = "";
        String AID = "";
        String CardHolderName = "";
        String ApplicationName = "";
        String TransactionCount = "";
        String ExpiryDateFull = "";
        String UnPredictableNumber = "";
        String TSI = "";
        String TVR = "";
        try {


            byte[] data9F12 = iemv.getCardData("9F12");
            if (data9F12 != null) {
                ApplicationName = new String(data9F12, StandardCharsets.UTF_8); //BytesUtil.byteArray2HexString(data9F12);
            } else {
                byte[] data50 = iemv.getCardData("50");
                if (data50 != null) {
                    ApplicationName = BytesUtil.byteArray2HexString(data50);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCardConfirmResult : " + e.getLocalizedMessage());
        }
        try {

            byte[] data9B = iemv.getCardData("9B");

            TSI = BytesUtil.byteArray2HexString(data9B);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCardConfirmResult : " + e.getLocalizedMessage());
        }
        try {

            byte[] data95 = iemv.getCardData("95");

            TVR = BytesUtil.byteArray2HexString(data95);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCardConfirmResult : " + e.getLocalizedMessage());
        }
        try {

            byte[] data84 = iemv.getCardData("84");

            AID = BytesUtil.byteArray2HexString(data84);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCardConfirmResult : " + e.getLocalizedMessage());
        }
        try {

            byte[] data5F20 = iemv.getCardData("5F20");
            if (data5F20 != null && data5F20.length > 0) {
                CardHolderName = new String(data5F20, StandardCharsets.UTF_8).replaceAll("  ", "");// BytesUtil.byteArray2HexString(data5F20).replaceAll("  ", "");
                if (CardHolderName.trim().length() == 0) {
                    CardHolderName = "-";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCardConfirmResult : " + e.getLocalizedMessage());
        }

        CardReadOutput CardOutput = new CardReadOutput();
        CardOutput.setCardAID(AID);
        CardOutput.setCardAppName(ApplicationName);
        CardOutput.setCardExpiry(date);
        CardOutput.setCardNo(savedPan);
        CardOutput.setCurrencyCode(FirstCurrencyCode);
        CardOutput.setInsertModeCode(EntryMode);
        CardOutput.setInsertMode(TransactionUtils.CardCustomInsertMode(EntryMode));
        CardOutput.setCustomerName(CardHolderName);
        CardOutput.setTransactionCount(TransactionCount);
        CardOutput.setTrack2Data(Track2);

        if (AccountEntryMode.equalsIgnoreCase(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
            _CardOutput = CardOutput;
            _CardOutput.setTransactionCategoryCode("00");
            String CardExpiry = _CardOutput.getCardExpiry();
            if (CardExpiry.length() > 4) {
                CardExpiry = CardExpiry.substring(0, 4);
                _CardOutput.setCardExpiry(CardExpiry);
            }
            AddRequestForEMV();
            successResponse.processFinish(_CardOutput);

            // we will be sending common var, in this case
            //as it contains all the response for emv and no further processing required.
        } else {

            successResponse.processFinish(CardOutput);
            isCardDataRecorded = true;
        }


    }

    public void ManageCardDetailsFromOutSide(CardReadOutput CardDetails) {
        AccountEntryMode = Session.ACCOUNT_ENTRY_MODE_MAGCARD;
        savedPan = CardDetails.getCardNo();
        isBreakEMIFlow = true;

    }

    public void publishEMVDataStep1(long Amount, long AdditionalAmount,
                                    CardReadOutput cardOutput, boolean breakEMIFlow, boolean isPinEntryRequired) throws RemoteException {


        if (isEMVProcess && !isPinEntryRequired)
            successResponse.EMVProcessing();

        _CardOutput = cardOutput;
        transactionConfig.setAmount(Amount);
        transactionConfig.setPinInputNeeded(isPinEntryRequired);
        handleCardRecordConfirmed();
    }

    private void handleCardRecordConfirmed() throws RemoteException {
        try {

            // If the card type is mag card or manually input card or contact simple process, then stop emv
            if (AccountEntryMode.equals(Session.ACCOUNT_ENTRY_MODE_MAGCARD)
                    || AccountEntryMode.equals(Session.ACCOUNT_ENTRY_MODE_MANUAL)
                    || isBreakEMIFlow
                    || (AccountEntryMode.equals(Session.ACCOUNT_SERVICE_ENTRY_MODE_CONTACT)
                    && transactionConfig.getTransactionType() == TransactionConfig.TRANSACTION_TYPE_SIMPLE)) {

                // Stop EMV
                stopEMVProcess();

                if (transactionConfig.isPinInputNeeded()) {
                    // request input online pin
                    doPinPad();
                } else {
                    // request online process
                    //handleOnlineProcess();

                    FinalizingRequestForEMV();

                }

                return;

            }

            //byte[] rid = BytesUtil.subBytes(BytesUtil.hexString2ByteArray( _CardOutput.getCardAID()), 0, 5);


            String[] RID = iemv.getRID();

            List<String> TLVEMVData = new ArrayList<String>();

            // for ic card or rf card, response the card result to emv.
            TLVList tlvList = new TLVList();
            tlvList.addTLV(EmvTags.EMV_TAG_TM_AUTHAMNTN, BytesUtil.toBCDAmountBytes(transactionConfig.getAmount()));
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();
            tlvList.addTLV(EmvTags.EMV_TAG_TM_OTHERAMNTN, BytesUtil.toBCDAmountBytes(0L));
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();
            tlvList.addTLV(EmvTags.EMV_TAG_TM_TRANSDATE, BytesUtil.hexString2ByteArray(DateUtil.getDate(new Date(), "yyMMdd")));
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();
            tlvList.addTLV(EmvTags.EMV_TAG_TM_TRANSTIME, BytesUtil.hexString2ByteArray(DateUtil.getDate(new Date(), "HHmmss")));
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();
            tlvList.addTLV(EmvTags.EMV_TAG_TM_TRSEQCNTR, BytesUtil.hexString2ByteArray(_CardOutput.getSTAN()));
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();
            if (isRefund) {
                tlvList.addTLV(EmvTags.DEF_TAG_SERVICE_TYPE, new byte[]{SERVICE_TYPE_REFUND});
            } else {
                tlvList.addTLV(EmvTags.DEF_TAG_SERVICE_TYPE, new byte[]{SERVICE_TYPE_GOODS_SERVICE});
            }
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();
            tlvList.addTLV(EmvTags.DEF_TAG_START_RECOVERY, new byte[]{(byte) 0x00}); // 0- false, 1- true
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();


            // Accumulated amount.
            tlvList.addTLV(EmvTags.DEF_TAG_ACCUMULATE_AMOUNT, BytesUtil.toBCDAmountBytes(0L));
            TLVEMVData.add(tlvList.toString());
            tlvList = new TLVList();

            // Pan in black.
            tlvList.addTLV(EmvTags.DEF_TAG_PAN_IN_BLACK, new byte[]{(byte) 0x0}); // 0- false, 1- true
            TLVEMVData.add(tlvList.toString());
            iemv.setEMVData(TLVEMVData);
            iemv.importCardConfirmResult(ConstIPBOC.importCardConfirmResult.pass.allowed);

        } catch (Exception ex) {
            stopEMVProcess();

            successResponse.processFailed(ex.getLocalizedMessage());
        }
    }

    /**
     * Handle pin error.
     */
    private void handlePinError(int error) throws RemoteException {
        try {

            successResponse.PinProcessFailed("Pin Failed : Error Code - " + error);

            // Whether not EMV process
            if (!isEMVProcess) {
                return;
            }


            //EMV.getInstance().responseEvent(TLV.fromData(EMVTag.DEF_TAG_CHV_STATUS, new byte[]{PinpadForMKSK.VERIFY_STATUS_FAIL}).toString());
        } catch (Exception e) {
            stopEMVProcess();

            successResponse.processFailed(e.getLocalizedMessage());
        }
    }

    /**
     * Handle pin cancel.
     */
    private void handlePinCancel() throws RemoteException {
        // Close pinpad

        successResponse.PinProcessFailed("Pin Cancelled");

        // Whether not EMV process
        if (!isEMVProcess) {
            stopEMVProcess();
            return;
        }

        if (isCardHolderVerificationPerformed) {
            iemv.importPin(1, null);
        } else {
            FinalizingRequestForEMV();
        }
    }

    /**
     * Handle pin confirm.
     */
    private void handlePinConfirm(byte[] data) throws RemoteException {

        // Whether not EMV process
        if (!isEMVProcess) {

            try {
                if (data != null) {

                    String PINBLOCK = BytesUtil.byteArray2HexString(data);// new String(data, StandardCharsets.UTF_8);

                    _CardOutput.setPINBlock(PINBLOCK);
                }

            } catch (Exception e) {
                successResponse.PinProcessFailed(e.getLocalizedMessage());

            }
            FinalizingRequestForEMV();


            return;
        }


        //  session.setPinEntryMode(Session.PIN_ENTRY_MODE_EXIST);
        if (data != null) {
            String PINBLOCK = BytesUtil.byteArray2HexString(data);// new String(data, StandardCharsets.UTF_8);

            _CardOutput.setPINBlock(PINBLOCK);
        }

        iemv.importPin(1, data);

    }

    private void AddRequestForEMV() {
        try {
            successResponse.EMVProcessing();
            toastShow("ADD REQUEST FOR EMV");
            String TVR = "", ARQC = "", PANSeqNo = "", CardNo = "", CID = "", TxnCurrencyCode = "", AIP = "", AID = "", TxnDate = "", TSI = "", TxnType = "",
                    AuthAmount = "", otherAmount = "", AVN = "", IAD = "", TerminalCountryCode = "", IFD = "", TC = "", CVM = "", TType = "", ATC = "", UnpredicatableNo = "",
                    TxnSequenceCounter = "", TxnCategory = "";
            byte[] tlv;
            TLVList tlvList = new TLVList();

            String Track2Data = "";

            int tag = 0X57;

            try {
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    Track2Data = BytesUtil.byteArray2HexString(tlv);

                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            try {
                //TVR Result : Transaction Verification Result
                tag = 0X95;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TVR = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TVR)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Application Cryptogram
                tag = 0X9F26;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    ARQC = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(ARQC)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //PAN Sequence No
                tag = 0X5F34;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    PANSeqNo = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(PANSeqNo)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //CardNo
                tag = 0X5A;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {

                    CardNo = BytesUtil.byteArray2HexString(tlv);
                    CardNo = BytesUtil.toSpecificSizeString(CardNo, 16, "F", "left");
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(CardNo)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            try {
                //Cryptogram Information Data
                tag = 0X9F27;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    CID = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(CID)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                // Terminal - Transaction Currency Code

                tag = 0X5F2A;


                TxnCurrencyCode = Constant.BaseCurrencyCode;
                tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TxnCurrencyCode)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Application Interchange Profile

                tag = 0X82;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    AIP = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(AIP)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Application Identification

                tag = 0X84;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    AID = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(AID)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Transaction Date

                tag = 0X9A;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TxnDate = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TxnDate)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Transaction Status Information

                tag = 0X9B;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TSI = BytesUtil.byteArray2HexString(tlv);
                    //     tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TSI)));
                    Log.d(TAG, "TSI :" + TSI);
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Transaction Type


                tag = 0X9C;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TxnType = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TxnType)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Authorize Amount
                tag = 0X9F02;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    AuthAmount = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(AuthAmount)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Other Amount

                tag = 0X9F03;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    otherAmount = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(otherAmount)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                // Application Version Number

                tag = 0X9F09;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    AVN = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(AVN)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Issuer Application Data

                tag = 0X9F10;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    IAD = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(IAD)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            try {
                tag = 0X9F1A;

                TerminalCountryCode = Constant.BaseCountryCode;
                tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TerminalCountryCode)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Terminal Serial No.
                tag = 0X9F1E;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    IFD = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(IFD)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Terminal Capabilities
                tag = 0X9F33;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TC = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TC)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Cardholder Verification Method

                tag = 0X9F34;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    CVM = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(CVM)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                //Transaction Type
                tag = 0X9F35;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TType = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TType)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                //Application Transaction Counter

                tag = 0X9F36;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    ATC = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(ATC)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Unpredictable Number

                tag = 0X9F37;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    UnpredicatableNo = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(UnpredicatableNo)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                tag = 0X9F41;
                tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                if (null != tlv && tlv.length > 0) {
                    TxnSequenceCounter = BytesUtil.byteArray2HexString(tlv);
                    tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes(TxnSequenceCounter)));
                } else {
                    Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            tag = 0X9F53;

            tlvList.addTLV(TLV.fromData(Integer.toHexString(tag), BytesUtil.hexString2Bytes("00")));


//            if (_CardOutput != null && _CardOutput.getTransactionCategoryCode() != null &&
//                    _CardOutput.getTransactionCategoryCode().length() > 0) {
//                String TxnCategoryCode = _CardOutput.getTransactionCategoryCode();
//                tlvList.addTLV(TLV.fromData("9F53", BytesUtil.hexString2Bytes(TxnCategoryCode)));
//            }


            String ByteLength = BytesUtil.ConvertNumericWithLeadingZeros((tlvList.toString().length()) / 2, 4);

            String EMVD = ByteLength + tlvList.toString();

            String RAWEMVData = "";

            String BCDEncodedEMV = "";
            for (byte eachByte : BytesUtil.hexString2ByteArray(EMVD)
            ) {

                if (RAWEMVData.length() > 0)
                    RAWEMVData += " ";
                String eachByteHex = BytesUtil.byte2HexString(eachByte);

                RAWEMVData += eachByteHex;

                BCDEncodedEMV += eachByteHex;

//                BCDEncodedEMV +=
//                        Util.hexToAscii(eachByteHex);

                //  BCDEncodedEMV += BCDEMV;
            }


            _CardOutput.setTransactionCount(ATC);
            _CardOutput.setRawEMVData(RAWEMVData);
            _CardOutput.setEMVData(BCDEncodedEMV);
            _CardOutput.setTrack2Data(Track2Data);
            _CardOutput.setTransactionCertificate(ARQC);
            _CardOutput.setTVRData(TVR);
            _CardOutput.setTSIData(TSI);
            _CardOutput.setPANSEQ(PANSeqNo);
        } catch (Exception ex) {
            Log.e("EMV Error", ex.getLocalizedMessage());
        }
    }

    private void FinalizingRequestForEMV() {

        if (isEMVProcess)
            AddRequestForEMV();
        else {
            //handle required params in case of SWIPE etc.
            String PANSeqNo = "00";
            _CardOutput.setPANSEQ(PANSeqNo);
        }

        //get PINBLOCK FROM SESSION


        successResponse.processFinish(_CardOutput);

    }

    public void ProcessEMVCompletionFlow(String EMVD, String AuthCode) {

        boolean isValidRequest = true;
        String Field55 = EMVD;
        if (isEMVProcess) {
            try {
                if (EMVD != null && !EMVD.isEmpty()) {

                    if (Field55.length() > 0 && !Field55.startsWith("91")) {
                        int startIndex = Field55.indexOf("91");
                        Field55 = Field55.substring(startIndex, Field55.length());
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                isValidRequest = false;
                successResponse.processFailed("Txn failed during EMV Processing.");
            }
        }

        try {
            if (isValidRequest) {
                Bundle onlineResult = new Bundle();
                Log.d(TAG, "Process EMV Completion" + EMVD + "  : " + AuthCode);
                onlineResult.putBoolean(EmvTags.DEF_TAG_ONLINE_STATUS, true);

                //91 TAG  0A Value i.e. 10 ...we get in TLV FORMAT .. SPLIT TAG AND LENGTH TO PASS ONLY VALUE


                if (EMVD != null && EMVD.length() > 10) {
                    onlineResult.putString(EmvTags.DEF_TAG_HOST_TLVDATA, Field55);
                }


                onlineResult.putString(EmvTags.DEF_TAG_AUTHORIZE_FLAG, "01");


                if (AuthCode != null && AuthCode.length() > 0) {
                    AuthCode = BytesUtil.toSpecificSizeString(AuthCode.trim(), 12, "0", "right");
                    onlineResult.putString(EmvTags.EMV_TAG_TM_AUTHCODE, AuthCode); //get from server..
                }

                if (!isEMVProcess) {

                    successResponse.TransactionApproved();
                    return;
                }


                iemv.inputOnlineResult(onlineResult, new OnlineResultHandler.Stub() {
                    @Override
                    public void onProccessResult(int result, Bundle data) throws RemoteException {
                        Log.i(TAG, "onProccessResult callback:");
                        String str = "RESULT:" + result +
                                "\nTC_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_TC_DATA_String, "not defined") +
                                "\nSCRIPT_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_SCRIPT_DATA_String, "not defined") +
                                "\nREVERSAL_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_REVERSAL_DATA_String, "not defined");
                        toastShow(str);

                        switch (result) {
                            case ConstOnlineResultHandler.onProccessResult.result.TC:
                                successResponse.TransactionApproved();
                                break;
                            case ConstOnlineResultHandler.onProccessResult.result.Offline_TC:
                                successResponse.TransactionApproved();

                                break;
                            case ConstOnlineResultHandler.onProccessResult.result.Online_AAC: {
                                if (Constant.isForceApprove_ARPC_EMV)
                                    successResponse.TransactionApproved();
                                else
                                    successResponse.TransactionDeclined();

                                break;
                            }
                            default: {
                                if (Constant.isForceApprove_ARPC_EMV)
                                    successResponse.TransactionApproved();
                                else
                                    successResponse.TransactionDeclined();
                                break;
                            }

                        }
                    }
                });


//
//            iemv.importOnlineResult(onlineResult, new OnlineResultHandler.Stub() {
//                @Override
//                public void onProccessResult(int result, Bundle data) throws RemoteException {
//                    emvHandler.onTransactionResult(result, data);
//                }
//            });
            }
        } catch (Exception ex) {
            successResponse.processFailed("Txn failed during EMV Processing.");
            Log.d(TAG, "Transaction Declined");
        }

    }

    public void startCardProcessor(TransactionConfig config, String BatchNo, String InvoiceNo,
                                   String TxnName, String ProcessingCode) {
//        ,Long TransactionAmount,
//        TransactionConfig _transConfig
        // Set session.
//        session.setTransactionName(TxnName);//"Sale"
//
//        session.setProcessingCode(ProcessingCode); //"000000"
//        //  session.setSystemTraceAuditNumber(InvoiceNo);
//        session.setBatchNumber(BatchNo);

        // Set transaction config.

        transactionConfig = config;

    }

    public void StopProcess() {
        stopEMVProcess();
    }

    private void stopEMVProcess() {
        try {
            iemv.stopCheckCard();
            iemv.abortEMV();
        } catch (Exception ex) {
            Log.e("Stop EMV", ex.getLocalizedMessage());
        }
    }

    // log & display
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String string = msg.getData().getString("string");
            super.handleMessage(msg);
            Log.d(TAG, msg.getData().getString("msg"));
            Toast.makeText(context, msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();

        }
    };

    void toastShow(String str) {
        Message msg = new Message();
        msg.getData().putString("msg", str);
        Log.d(TAG, str);
        //  handler.sendMessage(msg);
    }

}
