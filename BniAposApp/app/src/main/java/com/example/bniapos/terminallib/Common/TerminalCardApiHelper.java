package com.example.bniapos.terminallib.Common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.cti.generic.Terminal.Common.CustomUtils;
import com.cti.generic.Terminal.Constant;

import com.cti.generic.Terminal.TerminalModels.AIDFile;
import com.cti.generic.Terminal.TerminalModels.CardReadOutput;
import com.cti.generic.Terminal.util.InputUtil;
import com.cti.generic.Terminal.util.TransactionUtils;
import com.cti.generic.Terminal.util.Util;
import com.cti.generic.R;
import com.cti.generic.sdk.api.Beeper;
import com.cti.generic.sdk.api.DeviceManager;
import com.cti.generic.sdk.api.EMV;
import com.cti.generic.sdk.api.LED;
import com.cti.generic.sdk.api.PinpadForDUKPT;
import com.cti.generic.sdk.api.PinpadForMKSK;
import com.cti.generic.sdk.util.data.BytesUtil;
import com.cti.generic.sdk.util.data.DateUtil;
import com.cti.generic.sdk.util.data.StringUtil;
import com.cti.generic.sdk.util.data.TLV;
import com.cti.generic.sdk.util.data.TLVList;
import com.cti.generic.sdk.util.emv.EmvData;
import com.cti.generic.sdk.util.emv.EmvParameterException;
import com.cti.generic.sdk.util.emv.EmvParameterInitializer;
import com.cti.generic.sdk.util.emv.EmvTags;
import com.cti.generic.sdk.util.pinpad.KeyId;
import com.cti.generic.sdk.util.transaction.Session;
import com.cti.generic.Terminal.TerminalModels.TransactionConfig;
import com.usdk.apiservice.aidl.constants.RFDeviceName;
import com.usdk.apiservice.aidl.emv.ACType;
import com.usdk.apiservice.aidl.emv.ActionFlag;
import com.usdk.apiservice.aidl.emv.CAPublicKey;
import com.usdk.apiservice.aidl.emv.CVMFlag;
import com.usdk.apiservice.aidl.emv.CVMMethod;
import com.usdk.apiservice.aidl.emv.CandidateAID;
import com.usdk.apiservice.aidl.emv.CardRecord;
import com.usdk.apiservice.aidl.emv.EMVError;
import com.usdk.apiservice.aidl.emv.EMVEventHandler;
import com.usdk.apiservice.aidl.emv.EMVTag;
import com.usdk.apiservice.aidl.emv.FinalData;
import com.usdk.apiservice.aidl.emv.KernelINS;
import com.usdk.apiservice.aidl.emv.OfflinePinVerifyResult;
import com.usdk.apiservice.aidl.emv.SearchCardListener;
import com.usdk.apiservice.aidl.emv.TransData;
import com.usdk.apiservice.aidl.emv.WaitCardFlag;
import com.usdk.apiservice.aidl.led.Light;
import com.usdk.apiservice.aidl.pinpad.OfflinePinVerify;
import com.usdk.apiservice.aidl.pinpad.OnPinEntryListener;
import com.usdk.apiservice.aidl.pinpad.PinPublicKey;
import com.usdk.apiservice.aidl.pinpad.PinVerifyResult;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cti.generic.Terminal.util.Util.saveLogs;

public class TerminalCardApiHelper implements AutoCloseable {

    private Context context;
    private CardReadOutput _CardOutput;
    private String className = "LandiTerminalCardApiHelper";

    private boolean isCardHolderVerificationPerformed=false;
    private boolean isRefund = false;

    private String panNumber = "";
    /**
     * Emv parameter initializer.
     */
    private EmvParameterInitializer emvParameter;

    /**
     * Session for transaction.
     */
    private Session session;

    private static final String TAG = "EMV_Ingenico";

    /**
     * Card record.
     */
    private CardRecord cardRecord;

    private boolean isCardDataRecorded=false;


    public static final String SEARCH_CARD_FIRST = "SEARCH_CARD_FIRST";

    private enum TransactionEvent {
        EMV_EVENT_WAIT_CARD,
        EMV_EVENT_CARD_CHECKED,
        EMV_EVENT_FINAL_SELECT,
        EMV_EVENT_READ_RECORD,
        EMV_EVENT_CARD_HOLDER_VERIFY,
        EMV_EVENT_ONLINE_PROCESS,
        EMV_EVENT_END_PROCESS,
        EMV_EVENT_SEND_OUT,
        CARD_EVENT_SWIPE,
        CARD_EVENT_INSERT,
        CARD_EVENT_TAP,
        MAG_EVENT_ONLINE_PROCESS,
        PIN_EVENT_INPUT_CONFIRM,
        PIN_EVENT_INPUT_CANCEL,
        PIN_EVENT_INPUT_ERROR,
        PRT_EVENT_FINISHED,
        PRT_EVENT_ERROR,
        PRT_EVENT_HTML_ERROR,
        COMM_EVENT_COMPLETED,
    }

    /**
     * Is in EMV process.
     */
    private boolean isEMVProcess;

    /**
     * Is search card first.
     */
    private boolean isSearchCardFirst=true;

    private boolean isBreakEMIFlow=false;

    /**
     * Transaction config.
     */
    private TransactionConfig transactionConfig;

    public ISuccessResponse_Card successResponse = null;
    private String getEntryMode()
    {
       return (session!= null && session.getAccountEntryMode()!=null)?session.getAccountEntryMode() :Constant.POSENT_Not_Available;
    }
    //Context _context,
    public TerminalCardApiHelper(Context _context, ISuccessResponse_Card _successResponse)
    {
        this.session = null;
        this._CardOutput = null;

       this.context = _context;
        this.successResponse = _successResponse;

        session = new Session();
        this._CardOutput = new CardReadOutput();
        _CardOutput.setTransactionCategoryCode("00");
        transactionConfig = new TransactionConfig();
        emvParameter = new EmvParameterInitializer(EMV.getInstance(), session, transactionConfig);
    }
    private void stopEMVProcess() throws RemoteException {
        EMV.getInstance().stopSearch();
        EMV.getInstance().halt();

        if (isEMVProcess) {
            EMV.getInstance().stopProcess();
        }
    }
    /**
     * Send message.
     */
    protected void sendMessage(int tag, Object object) {
        Message message = new Message();
        message.what = tag;
        message.obj = object;
        receiveMessage(message);
    }

    /**
     * Send message.
     */
    protected void sendMessage(int tag, int arg1, Object object) {
        Message message = new Message();
        message.what = tag;
        message.arg1 = arg1;
        message.obj = object;
        receiveMessage(message);
    }

    protected void receiveMessage(Message message) {


        try {
            if (TransactionEvent.EMV_EVENT_WAIT_CARD.ordinal() == message.what) {
                handleWaitCard((int) message.obj);

            } else if (TransactionEvent.EMV_EVENT_CARD_CHECKED.ordinal() == message.what) {
                handleCardChecked((int) message.obj);

            } else if (TransactionEvent.EMV_EVENT_FINAL_SELECT.ordinal() == message.what) {
                handleFinalSelect((FinalData) message.obj);

            } else if (TransactionEvent.EMV_EVENT_READ_RECORD.ordinal() == message.what) {
                handleReadRecord((CardRecord) message.obj);

            } else if (TransactionEvent.EMV_EVENT_CARD_HOLDER_VERIFY.ordinal() == message.what) {
                handleCardHolderVerify((CVMMethod) message.obj);

            } else if (TransactionEvent.EMV_EVENT_ONLINE_PROCESS.ordinal() == message.what
                    || TransactionEvent.MAG_EVENT_ONLINE_PROCESS.ordinal() == message.what) {
                handleOnlineProcess();

            } else if (TransactionEvent.EMV_EVENT_END_PROCESS.ordinal() == message.what) {
                handleEndProcess(message.arg1, (TransData) message.obj);

            } else if (TransactionEvent.EMV_EVENT_SEND_OUT.ordinal() == message.what) {
                handleSendOut((int) message.obj);

            } else if (TransactionEvent.CARD_EVENT_SWIPE.ordinal() == message.what) {
                handleSwipeCard((Bundle) message.obj);

            } else if (TransactionEvent.CARD_EVENT_INSERT.ordinal() == message.what) {
                handleInsertCard();

            } else if (TransactionEvent.CARD_EVENT_TAP.ordinal() == message.what) {
                handleTapCard();

            } else if (TransactionEvent.PIN_EVENT_INPUT_CONFIRM.ordinal() == message.what) {
                handlePinConfirm((byte[]) message.obj);

            } else if (TransactionEvent.PIN_EVENT_INPUT_CANCEL.ordinal() == message.what) {
                handlePinCancel();

            } else if (TransactionEvent.PIN_EVENT_INPUT_ERROR.ordinal() == message.what) {
                handlePinError((int) message.obj);

            } else if (TransactionEvent.COMM_EVENT_COMPLETED.ordinal() == message.what) {
                handleCommunicationCompleted((String) message.obj);
            }
        } catch (RemoteException e) {

                successResponse.processFailed(e.getLocalizedMessage(),getEntryMode());
            try {
                stopEMVProcess();
            } catch (RemoteException e1) {

                     successResponse.processFailed(e1.getLocalizedMessage(),getEntryMode());
            }
        }
    }

    /**
     * Handle card record read.
     */
    private void handleReadRecord(CardRecord cardRecord) throws RemoteException {


        this.cardRecord = cardRecord;

        // Save the expiration.
        if (cardRecord.getExpiry() != null && cardRecord.getExpiry().length > 0) {
            String dateExpiration = BytesUtil.byteArray2HexString(cardRecord.getExpiry());
            // Format expiration (from YYYYMMDD to YYMM)
            dateExpiration = dateExpiration.substring(2, 4) +"/"+ dateExpiration.substring(4, 6);

            session.setExpirationDate(dateExpiration);
        }

        // Save card number.
        if (cardRecord.getPan() != null && cardRecord.getPan().length > 0) {
            String primaryAccNo = StringUtil.getDigits(BytesUtil.byteArray2HexString(cardRecord.getPan()));

            session.setPan(primaryAccNo);
        }

        // Whether if RF card flow.
        if (session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
            // RF card do not to need confirm card information, just response card record read.
            handleCardRecordConfirmed();
        } else {

            onConfirmCardRecord(session.getPan(), session.getExpirationDate());
        }
    }

    /**
     * Handle send out.
     */
    private void handleSendOut(int command) throws RemoteException {
        if (command == KernelINS.CLOSE_RF) {
            Beeper.getInstance().startBeep(500);
        }
    }

    /**
     * Handle card holder verify.
     */
    private void handleCardHolderVerify(final CVMMethod cvmMethod) throws RemoteException {
        switch (cvmMethod.getCVM()) {
            case CVMFlag.EMV_CVMFLAG_OFFLINEPIN: {

                inputOfflinePin(cvmMethod.getPINTimes());
                _CardOutput.setRFU1("OFFLINE");
                break;
            }
            case CVMFlag.EMV_CVMFLAG_ONLINEPIN: {
                //inputOnlinePin();
                _CardOutput.setRFU1("ONLINE");
                inputOnlinePin();

                break;
            }


            case CVMFlag.EMV_CVMFLAG_SIGNATURE: {

                _CardOutput.setRFU1("Signature");

                _CardOutput.setPinType("signature");

                if (_CardOutput.getRID().equalsIgnoreCase(Constant.VisaRID)) {
                    _CardOutput.setRFU1("Signature VISA");
                    successResponse.processSignature();
                }
                //if transaction config has pin input enable than may be we go for Select Verification option
                byePassOnlinePin();

                break;

            }
            case CVMFlag.EMV_CVMFLAG_NOCVM: {
                _CardOutput.setRFU1("NOCVM");

                if (transactionConfig.isPinInputNeeded())
                    successResponse.SelectVerificationOption();
                else {
                    successResponse.processSignature();
                    byePassOnlinePin();
                }
                break;
            }
            default: {
                _CardOutput.setRFU1("DEFAULT :" + cvmMethod.getCVM());
                successResponse.SelectVerificationOption();
            }

        }
    }

    /**
     * Init Pinpad.
     */
    public void doSetKeys() {
        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
            try {

            }
            catch (Exception ex)
            {
                Log.e(Constant.LogKey,ex.getLocalizedMessage());
            }

        } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {

            if (!(TerminalSecurity.IsSessionKeyAvailable())) {
                successResponse.processFailed("Perform LOGON Request from Main Menu.");
            }
        }
    }

    public void initOfflinePinEntry(Bundle param) throws  RemoteException {
        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {

            Intent intent = new Intent("com.landicorp.pinpad.pinentry.server.SET_SKIN");
            intent.putExtra("disorder", false);
            context.sendBroadcast(intent);
            
            PinpadForDUKPT.getInstance().open();
            PinpadForDUKPT.getInstance().startOfflinePinEntry(param, new OnPinEntryListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.d(TAG, "----- onInput -----");
                    Log.d(TAG, "len:" + len + ", key:" + key);

//                Beeper.getInstance().startBeep(200);
                }

                @Override
                public void onError(int error) {
                    Log.d(TAG, "----- onError -----");
                    Log.d(TAG, "error:" + error);

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_ERROR.ordinal(), error);
                }

                @Override
                public void onConfirm(byte[] data, boolean isNonePin) {
                    Log.d(TAG, "----- onConfirm -----");
                    Log.d(TAG, "isNonePin:" + isNonePin);

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CONFIRM.ordinal(), data);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "----- onCancel -----");

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CANCEL.ordinal(), null);
                }
            });

        } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {

            Intent intent = new Intent("com.landicorp.pinpad.pinentry.server.SET_SKIN");
            intent.putExtra("disorder", false);
            context.sendBroadcast(intent);

            PinpadForMKSK.getInstance().open();
            PinpadForMKSK.getInstance().startOfflinePinEntry(param, new OnPinEntryListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.d(TAG, "----- onInput -----");
                    Log.d(TAG, "len:" + len + ", key:" + key);

//                Beeper.getInstance().startBeep(200);
                }

                @Override
                public void onError(int error) {
                    Log.d(TAG, "----- onError -----");
                    Log.d(TAG, "error:" + error);

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_ERROR.ordinal(), error);
                }

                @Override
                public void onConfirm(byte[] data, boolean isNonePin) {
                    Log.d(TAG, "----- onConfirm -----");
                    Log.d(TAG, "isNonePin:" + isNonePin);

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CONFIRM.ordinal(), data);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "----- onCancel -----");

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CANCEL.ordinal(), null);
                }
            });
        }
    }

    public void initOnlinePinEntry(Bundle param) throws  RemoteException {
        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {

            Intent intent = new Intent("com.landicorp.pinpad.pinentry.server.SET_SKIN");
            intent.putExtra("disorder", false);
            context.sendBroadcast(intent);
            
            PinpadForDUKPT.getInstance().open();
            PinpadForDUKPT.getInstance().startPinEntry(KeyId.mainKey, param, new OnPinEntryListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.d(TAG, "----- onInput -----");
                    Log.d(TAG, "len:" + len + ", key:" + key);

//                Beeper.getInstance().startBeep(200);
                }

                @Override
                public void onError(int error) {
                    Log.d(TAG, "----- onError -----");
                    Log.d(TAG, "error:" + error);

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_ERROR.ordinal(), error);
                }

                @Override
                public void onConfirm(byte[] data, boolean isNonePin) {
                    Log.d(TAG, "----- onConfirm -----");
                    Log.d(TAG, "isNonePin:" + isNonePin + "\r\n");

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CONFIRM.ordinal(), data);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "----- onCancel -----");

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CANCEL.ordinal(), null);
                }

            });


        } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {

            Intent intent = new Intent("com.landicorp.pinpad.pinentry.server.SET_SKIN");
            intent.putExtra("disorder", false);
            context.sendBroadcast(intent);
            
            PinpadForMKSK.getInstance().open();
            PinpadForMKSK.getInstance().startPinEntry(KeyId.pinKey, param, new OnPinEntryListener.Stub() {

                @Override
                public void onInput(int len, int key) throws RemoteException {
                    Log.d(TAG, "----- onInput -----");
                    Log.d(TAG, "len:" + len + ", key:" + key);

//                Beeper.getInstance().startBeep(200);
                }

                @Override
                public void onError(int error) {
                    Log.d(TAG, "----- onError -----");
                    Log.d(TAG, "error:" + error);

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_ERROR.ordinal(), error);
                }

                @Override
                public void onConfirm(byte[] data, boolean isNonePin) {
                    Log.d(TAG, "----- onConfirm -----");
                    Log.d(TAG, "isNonePin:" + isNonePin + "\r\n");

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CONFIRM.ordinal(), data);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "----- onCancel -----");

                    sendMessage(TransactionEvent.PIN_EVENT_INPUT_CANCEL.ordinal(), null);
                }

            });
        }


    }

    public void ManageCardDetailsFromOutSide(CardReadOutput CardDetails)
    {
        session.setAccountEntryMode(Session.ACCOUNT_ENTRY_MODE_MAGCARD);
        session.setPan(CardDetails.getCardNo());
        isBreakEMIFlow=true;

    }
    /**
     * Handle card record confirm.
     */
    private void handleCardRecordConfirmed() throws RemoteException {
        try {
            // If the card type is mag card or manually input card or contact simple process, then stop emv
            if (session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_MAGCARD)
                    || session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_MANUAL)
                    || isBreakEMIFlow
                    || (session.getAccountEntryMode().equals(Session.ACCOUNT_SERVICE_ENTRY_MODE_CONTACT)
                    && transactionConfig.getTransactionType() == TransactionConfig.TRANSACTION_TYPE_SIMPLE)) {

                // Stop EMV
                stopEMVProcess();


                /**
                 * we must only show Pin option in this case, and there must be no option of choosing signature
                 */
                inputOnlinePin();
             //   successResponse.SelectVerificationOption();


                return;

            }


            // for ic card or rf card, response the card result to emv.
            TLVList tlvList = new TLVList();


           tlvList.addTLV(EmvTags.EMV_TAG_TM_AUTHAMNTN, BytesUtil.toBCDAmountBytes(session.getTransactionAmount()));
           tlvList.addTLV(EmvTags.EMV_TAG_TM_OTHERAMNTN, BytesUtil.toBCDAmountBytes(0L));
            tlvList.addTLV(EmvTags.EMV_TAG_TM_TRANSDATE, BytesUtil.hexString2ByteArray(DateUtil.getDate(new Date(), "yyMMdd")));
            tlvList.addTLV(EmvTags.EMV_TAG_TM_TRANSTIME, BytesUtil.hexString2ByteArray(DateUtil.getDate(new Date(), "HHmmss")));
            tlvList.addTLV(EmvTags.EMV_TAG_TM_TRSEQCNTR, BytesUtil.hexString2ByteArray(session.getSystemTraceAuditNumber()));
            if(isRefund)
                tlvList.addTLV(EmvTags.DEF_TAG_SERVICE_TYPE, new byte[]{EMV.SERVICE_TYPE_REFUND});
            else
                tlvList.addTLV(EmvTags.DEF_TAG_SERVICE_TYPE, new byte[]{EMV.SERVICE_TYPE_GOODS_SERVICE});
            tlvList.addTLV(EmvTags.DEF_TAG_START_RECOVERY, new byte[]{(byte) 0x00}); // 0- false, 1- true


            handleCAPK(cardRecord);

            // Get the first 5 bytes of aid.
            // Accumulated amount.
            tlvList.addTLV(EmvTags.DEF_TAG_ACCUMULATE_AMOUNT, BytesUtil.toBCDAmountBytes(0L));

            // Pan in black.
            tlvList.addTLV(EmvTags.DEF_TAG_PAN_IN_BLACK, new byte[]{(byte) 0x0}); // 0- false, 1- true


            EMV.getInstance().responseEvent(tlvList.toString());

        } catch (Exception ex) {
            stopEMVProcess();

            successResponse.processFailed(ex.getLocalizedMessage(),getEntryMode());
        }
    }
    private void handleCAPK(CardRecord cardRecord) throws  RemoteException
    {

        if(cardRecord !=null) {

            byte[] rid = BytesUtil.subBytes(cardRecord.getAID(), 0, 5);

            // Find public key from emv data.
            CAPublicKey caPublicKey = EmvData.getPublicKey(rid, cardRecord.getPubKIndex());

            if (rid != null && cardRecord != null) {
                _CardOutput.setRID(BytesUtil.byteArray2HexString(rid));
                _CardOutput.setPubKIndex(BytesUtil.byte2HexString(cardRecord.getPubKIndex()));
            }
            if (caPublicKey != null) {
                // Set public key.
                EMV.getInstance().setCAPubKey(caPublicKey);
                _CardOutput.setPubKeyExist(true);

            } else
                _CardOutput.setPubKeyExist(false);

        }
    }

    /**
     * Input offline pin.
     */
    public void inputOfflinePin(int PINTimes) throws RemoteException {

        _CardOutput.setPinType("offline");
        // Show offline pin input times
        if (PINTimes > 1) {
            //        onInputPin(String.format(getString(R.string.input_offline_pin_times), PINTimes));
        } else {
            //      onInputPin(getString(R.string.input_offline_pin_one_time));
        }

        Bundle param = new Bundle();
        param.putByteArray("panBlock", BytesUtil.hexString2Bytes(session.getPan())); //)
        param.putInt("timeout", Constant.DefaultTimeOutInSeconds);
        param.putInt("betweenPinKeyTimeout",  Constant.DefaultTimeOutInSeconds);
        param.putByteArray("pinLimit", transactionConfig.getPinRule());

        doSetKeys();
        initOfflinePinEntry(param);
    }



    /*
     * Receive PAN from other activities
     */
    public void setPan(String pan) {
        panNumber = pan;
    }
    
    /**
     * Input online pin.
     */
    public void inputOnlinePin() throws RemoteException {

        String _panCard   = panNumber;
        String _entryMode = "07";
        if (session.getPan() != null) {
            _panCard   = session.getPan();
            _entryMode = session.getAccountEntryMode();
        } else if (_CardOutput.getCardNo() != null) {
            _panCard = _CardOutput.getCardNo();
        }

        if (!_panCard.isEmpty()) {
            _CardOutput.setPinType("online");

            Bundle param = new Bundle();
            param.putByteArray("panBlock", BytesUtil.hexString2Bytes(_panCard));
            param.putInt("timeout", Constant.DefaultTimeOutInSeconds);
            param.putInt("betweenPinKeyTimeout",  Constant.DefaultTimeOutInSeconds);
            if (_entryMode.equalsIgnoreCase(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
                param.putByteArray("pinLimit", new byte[]{4, 6});
            } else {
                param.putByteArray("pinLimit", transactionConfig.getPinRule());
            }

            doSetKeys();
            initOnlinePinEntry(param);
        }
    }

    /**
     * Handle verify offline pin.
     */
    private void handleVerifyOfflinePin(int flag, byte[] random, CAPublicKey publicKey, OfflinePinVerifyResult result) {

        // Set offline pin verify params.
        String PINVerificationResult="";
        byte fmtOfPin = (flag == 1 ? OfflinePinVerify.FOPTBV_ENCRYPTED_BY_PUBLIC_KEY : OfflinePinVerify.FOPTBV_PLAIN_TEXT);
        PINVerificationResult += BytesUtil.byte2HexString(fmtOfPin) +"   ";
        int icToken = 0; // 0 is default value
        byte cmdFmt = OfflinePinVerify.VCF_DEFAULT;
        OfflinePinVerify offlinePinVerify = new OfflinePinVerify(fmtOfPin, icToken, cmdFmt, random);
        PINVerificationResult += "OfflinePinVerify(" +
                fmtOfPin + "," +
                icToken + "," +
                cmdFmt + "," +
                BytesUtil.bytes2HexString(random) + ")";

        // Set pin public key params.
        PinPublicKey pinPublicKey = new PinPublicKey();
        if (flag == 1) {

            pinPublicKey.setRid(publicKey.getRid());
            pinPublicKey.setExp(publicKey.getExp());
            pinPublicKey.setExpiredDate(publicKey.getExpDate());
            pinPublicKey.setHash(publicKey.getHash());
            pinPublicKey.setHasHash(publicKey.getHashFlag());
            pinPublicKey.setIndex(publicKey.getIndex());
            pinPublicKey.setMod(publicKey.getMod());
            Log.d("offlineKey", "    PinPublicKey(" +
                    BytesUtil.bytes2HexString(pinPublicKey.getRid()) + "," +
                    BytesUtil.bytes2HexString(pinPublicKey.getExp()) + "," +
                    BytesUtil.bytes2HexString(pinPublicKey.getExpiredDate()) + "," +
                    BytesUtil.bytes2HexString(pinPublicKey.getHash()) + "," +
                    pinPublicKey.getHasHash() + "," +
                    pinPublicKey.getIndex() + "," +
                    BytesUtil.bytes2HexString(pinPublicKey.getMod()) + ")");
        }

        // Set pin verify result.
        PinVerifyResult pinVerifyResult = new PinVerifyResult();

        // Verify offline pin by pinpad
        try {

            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {

                PinpadForDUKPT.getInstance().open();
                PinpadForDUKPT.getInstance().verifyOfflinePin(offlinePinVerify, pinPublicKey, pinVerifyResult);
                PinpadForDUKPT.getInstance().close();

            } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                PinpadForMKSK.getInstance().open();
                PinpadForMKSK.getInstance().verifyOfflinePin(offlinePinVerify, pinPublicKey, pinVerifyResult);
                PinpadForMKSK.getInstance().close();
            }

        } catch (RemoteException e) {
            result.setResult(EMV.VERIFY_OFFLINE_PIN_ERROR);
            PINVerificationResult+=" ---- FAIL ---";
            return;
        }

        // Get the verify result from pinpad
        byte sw1 = pinVerifyResult.getSW1();
        byte sw2 = pinVerifyResult.getSW2();
        byte apduRet = pinVerifyResult.getAPDURet();
         PINVerificationResult += "APDU ret = " + BytesUtil.byte2HexString(apduRet) + ", SW1 = " + BytesUtil.byte2HexString(sw1) + ", SW2 = " + BytesUtil.byte2HexString(sw2);
        Log.d(TAG, PINVerificationResult);

        // Set SW and result to EMV
        result.setSW(sw1, sw2);
        if (apduRet == (byte) 0xE6 || apduRet == (byte) 0xE7) {
            // Set success result

            result.setResult(EMV.VERIFY_OFFLINE_PIN_SUCCESS);
        } else {

            // Set APDU result
            result.setResult(apduRet);
        }
        _CardOutput.setPinVerificationResult(PINVerificationResult);

    }

    /**
     * Handle swipe card.
     */
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
        boolean onlySwipeAvailable=true;

        if(transactionConfig.isContactIcCardSupported() || transactionConfig.isRfCardSupported())
            onlySwipeAvailable=false;


        if (!onlySwipeAvailable && ( Service_Code.startsWith("2") || Service_Code.startsWith("6"))) {

            successResponse.processFailed("CARD SWIPE NOT ALLOWED");

        } else {
            // Show card record.
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

            successResponse.Communication(true);
            onConfirmCardRecord(session.getPan(), session.getExpirationDate());
        }

    }

    /**
     * Handle insert card.
     */
    private void handleInsertCard() throws RemoteException {

        if (isSearchCardFirst) {
            startEMVProcess();
        } else {
            EMV.getInstance().responseCard();
        }
    }

    /**
     * Handle tap card.
     */
    private void handleTapCard() throws RemoteException {
        try {

            if (isSearchCardFirst) {
                startEMVProcess();
            } else {
                EMV.getInstance().responseCard();
            }
        }
        catch (Exception ex)
        {
            stopEMVProcess();

              successResponse.processFailed(ex.getLocalizedMessage(),getEntryMode());
        }
    }

    /**
     * Handle pin error.
     */
    private void handlePinError(int error) throws RemoteException {
        try {
            // Show error
            //  showToast(getString(PinpadForDUKPT.getErrorId(error)));


            String Error = "";
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                Error = context.getString(PinpadForDUKPT.getErrorId(error));
                // Close pinpad
                PinpadForDUKPT.getInstance().close();

            } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                Error = context.getString(PinpadForMKSK.getErrorId(error));
                // Close pinpad
                PinpadForMKSK.getInstance().close();
            }

            successResponse.PinProcessFailed("Pin Failed :" + Error);

            // Whether not EMV process
            if (!isEMVProcess || session.getAccountEntryMode().equalsIgnoreCase(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
                return;
            }

            EMV.getInstance().responseEvent(TLV.fromData(EMVTag.DEF_TAG_CHV_STATUS, new byte[]{PinpadForMKSK.VERIFY_STATUS_FAIL}).toString());
        } catch (Exception e) {
            stopEMVProcess();

            successResponse.processFailed(e.getLocalizedMessage(),getEntryMode());
        }
    }

    /**
     * Handle pin cancel.
     */
    private void handlePinCancel() throws RemoteException {
        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
            // Close pinpad
            PinpadForDUKPT.getInstance().close();
        }
        else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
            // Close pinpad
            PinpadForMKSK.getInstance().close();
        }


        successResponse.PinProcessFailed("Pin Cancelled");

        // Whether not EMV process
        if (!isEMVProcess) {
            stopEMVProcess();
            return;
        }

        if(isCardHolderVerificationPerformed) {
            EMV.getInstance().responseEvent(TLV.fromData(EMVTag.DEF_TAG_CHV_STATUS, new byte[]{PinpadForMKSK.VERIFY_STATUS_CANCEL}).toString());
        }
        else
        {
            FinalizingRequestForEMV();
        }
    }

    /**
     * Handle pin confirm.
     */
    private void handlePinConfirm(byte[] data) throws RemoteException {

        successResponse.EMVProcessing();
        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
            // Close pinpad
            PinpadForDUKPT.getInstance().close();
        } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
            // Close pinpad
            PinpadForMKSK.getInstance().close();
        }

        if (_CardOutput.getPinType() != null && _CardOutput.getPinType().equalsIgnoreCase("online")) {

            // Whether not EMV process
            if (!isEMVProcess) {
                // sendMessage(TransactionEvent.MAG_EVENT_ONLINE_PROCESS.ordinal(), data);
                try {
                    // successResponse.PinProcessConfirm(StringUtil.getDigits(BytesUtil.bytes2HexString(data)));

                    String PINBLOCK = new String(data, StandardCharsets.UTF_8);
                    session.setPinBlock(PINBLOCK);
                    _CardOutput.setPINBlock(PINBLOCK);

                } catch (Exception e) {
                    successResponse.PinProcessFailed("Terminal Error during PIN Processing. Transaction Declined.");

                    // throw new RemoteException(e.getMessage());
                }
                if (session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS))
                    successResponse.PinProcessConfirm(_CardOutput);
                else
                    FinalizingRequestForEMV();
                return;
            }

            // Not input PIN
            if (data == null || data.length == 0) {

                byePassOnlinePin();
                return;
            }

            // Input PIN
            Log.d(TAG, "data:" + BytesUtil.bytes2HexString(data));


            String PINBLOCK = new String(data, StandardCharsets.UTF_8);
            session.setPinBlock(PINBLOCK);
            _CardOutput.setPINBlock(PINBLOCK);
            //BytesUtil.bytes2HexString(data)0


        }

        session.setPinEntryMode(Session.PIN_ENTRY_MODE_EXIST);


        //i.e. we have forced the terminal to take Pin Input...
        if (isCardHolderVerificationPerformed) {
            EMV.getInstance().responseEvent(TLV.fromData(EMVTag.DEF_TAG_CHV_STATUS, new byte[]{PinpadForMKSK.VERIFY_STATUS_SUCCESS}).toString());
        } else {
            FinalizingRequestForEMV();
        }

        //Might be we need this at the end...
        //We need to call handleOnlineProcess for communication complete with response code from server


    }

    public void byePassOnlinePin() throws RemoteException {
        successResponse.EMVProcessing();
        if (isCardHolderVerificationPerformed) {
            EMV.getInstance().responseEvent(TLV.fromData(EMVTag.DEF_TAG_CHV_STATUS, new byte[]{PinpadForMKSK.VERIFY_STATUS_BY_PASS_PIN}).toString());
        } else {
            FinalizingRequestForEMV();
        }
    }



    /**
     * Handle init EMV.
     */
    private void handleInitEMV() throws RemoteException {
        // Init aids.



       // emvParameter.initEmvAids();


        // Clear all the aids in EMV kernel.
        EMV.getInstance().manageAID(ActionFlag.CLEAR, null, true);
        // Add all aids from EMV data to EMV kernel.
        for (Map.Entry<String, Boolean> entry : EmvData.aids.entrySet()) {
            EMV.getInstance().manageAID(ActionFlag.ADD, entry.getKey(), entry.getValue());
        }

//        // Set DOL
//        EMV.getInstance().setDOL(DOLType.DDOL, "9F3704");
//
//        // Set TLV
//        EMV.getInstance().setTLV(KernelID.PBOC, EMVTag.EMV_TAG_TM_TERMTYPE, "22");
//
//        // Set TLV list
//        TLVList tlvList = new TLVList();
//        tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_CAP, BytesUtil.hexString2Bytes("E0F1C8")));
//        tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_CAP_AD, BytesUtil.hexString2Bytes("6F00F0F001")));
//        tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_CNTRYCODE, BytesUtil.hexString2Bytes("0156")));
//        tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_CURCODE, BytesUtil.hexString2Bytes("0156")));
//        tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_FLOORLMT, BytesUtil.hexString2Bytes("00000000")));
//        tlvList.addTLV(TLV.fromData(EMVTag.DEF_TAG_TAC_DECLINE, BytesUtil.hexString2Bytes("0000000000")));
//        tlvList.addTLV(TLV.fromData(EMVTag.DEF_TAG_TAC_ONLINE, BytesUtil.hexString2Bytes("FFFFFFFFFF")));
//        tlvList.addTLV(TLV.fromData(EMVTag.DEF_TAG_TAC_DEFAULT, BytesUtil.hexString2Bytes("FFFFFFFFFF")));
//        tlvList.addTLV(TLV.fromData(EMVTag.DEF_TAG_RAND_SLT_THRESHOLD, BytesUtil.hexString2Bytes("000000000000")));
//        tlvList.addTLV(TLV.fromData(EMVTag.DEF_TAG_RAND_SLT_PER, BytesUtil.hexString2Bytes("00")));
//        tlvList.addTLV(TLV.fromData(EMVTag.DEF_TAG_RAND_SLT_MAXPER, BytesUtil.hexString2Bytes("99")));
//        tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_9F66, BytesUtil.hexString2Bytes("26000080")));
 //       tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_9F7A, BytesUtil.hexString2Bytes("01")));
//       tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_9F7B, BytesUtil.hexString2Bytes("999999999999")));
//        tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_DF69, BytesUtil.hexString2Bytes("01")));
//        tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_TRANS_LIMIT, BytesUtil.hexString2Bytes("999999999999")));
//        tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_CVM_LIMIT, BytesUtil.hexString2Bytes("999999999999")));
//        tlvList.addTLV(TLV.fromData(EMVTag.C_TAG_TM_FLOOR_LIMIT, BytesUtil.hexString2Bytes("999999999999")));
//        EMV.getInstance().setTLVList(KernelID.PBOC, tlvList.toString());


    }

    public void publishEMVDataStep1(long Amount,long AdditionalAmount,
                                    CardReadOutput cardOutput,boolean breakEMIFlow,boolean isPinEntryRequired) throws RemoteException {


        _CardOutput = cardOutput;
        transactionConfig.setAmount(Amount);
        session.setTransactionAmount(Amount); //10

        transactionConfig.setPinInputNeeded(isPinEntryRequired);
        // Clear all the aids in EMV kernel.
        // Set gpo to EMV kernel

        if(!breakEMIFlow && cardOutput.getInsertMode().equalsIgnoreCase("insert")) {
        }


//        if(cardOutput.getInsertMode().equalsIgnoreCase("swipe") )
//        {
//            inputOnlinePin();
//        } else if(breakEMIFlow)
//        {
//            inputOnlinePin();
//        }
//        else
//        if (!transactionConfig.isPinInputNeeded()) {
//
//            handleOnlineProcess();
//        }
//        else
//        {

            handleCardRecordConfirmed();
       // }
        //BytesUtil.ConvertNumericWithLeadingZeros(9000,12)

//        var authorisedAmount = new ByteString("000000000001", HEX);
//        var secondaryAmount = new ByteString("000000000000", HEX);

//        var tvr = new ByteString("0000000000", HEX);
//        var transCurrencyCode = new ByteString("0978", HEX);
//        var transDate = new ByteString("090730", HEX);
//        var transType = new ByteString("21", HEX);
//        var unpredictableNumber = crypto.generateRandom(4);
//        var iccDynamicNumber = card.sendApdu(0x00, 0x84, 0x00, 0x00, 0x00);
//        var DataAuthCode = e.cardDE[0x9F45];
//
//        var Data = authorisedAmount.concat(secondaryAmount).concat(tvr).concat(transCurrencyCode).concat(transDate).concat(transType).concat(unpredictableNumber).concat(iccDynamicNumber).concat(DataAuthCode);

    }



    private List<AIDFile> TransferAIDForSystemUse(List<CandidateAID> aids)
    {
        List<AIDFile> allAIds = new ArrayList<>();
        for (CandidateAID aid:aids
             ) {
            AIDFile file = new AIDFile();
            file.setAID(aid.getAID());
            file.setAPID(aid.getAPID());
            file.setAPIDFlag(aid.getAPIDFlag());
            file.setAPN(aid.getAPN());
            file.setAppLabel(aid.getAppLabel());
            file.setIssCTIndex(aid.getIssCTIndex());
            file.setIssCTIndexFlag(aid.getIssCTIndexFlag());
            file.setKernelID(aid.getKernelID());
            file.setLangPref(aid.getLangPref());
            allAIds.add(file);
        }
        return allAIds;
    }
    /**
     * Handle app select.
     */
    private void handleAppSelect(List<CandidateAID> aids) throws RemoteException {
        // Choose the first aid.
        boolean isValidAID = false;
        if (aids != null) {
            _CardOutput.setAllAIDs(TransferAIDForSystemUse(aids));
            if (aids.size() == 1) {
//            if (aids.get(0) != null) {
                isValidAID = true;
                TLVList tlvList = new TLVList();
                tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_AID, aids.get(0).getAID()));
                EMV.getInstance().responseEvent(tlvList.toString());
//            }
            } else if (aids.size() > 1) {
                isValidAID = true;
                if (session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
                    TLVList tlvList = new TLVList();
                    tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_AID, aids.get(EmvParameterInitializer.getAIDPriority(aids)).getAID()));
                    EMV.getInstance().responseEvent(tlvList.toString());
                } else
                    successResponse.SelectCardApplication(_CardOutput);
            }
        }
        if (!isValidAID)
            successResponse.Communication(true);
    }
    public void handleMultipleAppSelection(AIDFile aid) throws RemoteException {
        boolean isValidAID = false;
        if (aid != null) {
            isValidAID = true;
            TLVList tlvList = new TLVList();
            tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_AID, aid.getAID()));
            EMV.getInstance().responseEvent(tlvList.toString());
        }
        if (!isValidAID)
            successResponse.Communication(true);
    }

    /**
     * Handle final select.
     */
    private void handleFinalSelect(FinalData finalData) throws RemoteException {
        try {
            byte kernelId = finalData.getKernelID();


            // Set transaction type(9C)
            EMV.getInstance().setTLV(kernelId, EmvTags.EMV_TAG_TM_TRANSTYPE,
                    session.getProcessingCode().substring(0, 1));

            // For QPS
            if (session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
                String FirstCurrencyCode = EMV.getInstance().getDataAPDU("9F51");
                session.setFirstCurrencyCode(FirstCurrencyCode);
                session.setSecondCurrencyCode(EMV.getInstance().getDataAPDU("DF71"));
            }

            // Init EMV parameters
            String aid = BytesUtil.bytes2HexString(finalData.getAID());
            session.setAid(aid);
            String pid = BytesUtil.bytes2HexString(finalData.getPID());

            try {
                emvParameter.initEmvParameters(aid, kernelId, pid, session.getAccountEntryMode());
            } catch (EmvParameterException e) {
                throw new RemoteException(e.getMessage());
            }

            // Set gpo to EMV kernel
            TLVList tlvList = new TLVList();

            if (session.getTransactionAmount() > 0
                    && (session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS))) {
                tlvList.addTLV(EmvTags.EMV_TAG_TM_AUTHAMNTN, BytesUtil.toBCDAmountBytes(session.getTransactionAmount()));
                tlvList.addTLV(EmvTags.EMV_TAG_TM_OTHERAMNTN, BytesUtil.toBCDAmountBytes(0L));
                tlvList.addTLV(EmvTags.EMV_TAG_TM_TRANSDATE, BytesUtil.hexString2ByteArray(DateUtil.getDate(new Date(), "yyMMdd")));
                tlvList.addTLV(EmvTags.EMV_TAG_TM_TRANSTIME, BytesUtil.hexString2ByteArray(DateUtil.getDate(new Date(), "HHmmss")));
                tlvList.addTLV(EmvTags.EMV_TAG_TM_TRSEQCNTR, BytesUtil.hexString2ByteArray(session.getSystemTraceAuditNumber()));
                if (isRefund)
                    tlvList.addTLV(EmvTags.DEF_TAG_SERVICE_TYPE, new byte[]{EMV.SERVICE_TYPE_REFUND});
                else
                    tlvList.addTLV(EmvTags.DEF_TAG_SERVICE_TYPE, new byte[]{EMV.SERVICE_TYPE_GOODS_SERVICE});

                tlvList.addTLV(EmvTags.DEF_TAG_START_RECOVERY, new byte[]{(byte) 0x00}); // 0- false, 1- true

                // Accumulated amount.
                //tlvList.addTLV(EmvTags.DEF_TAG_ACCUMULATE_AMOUNT, BytesUtil.toBCDAmountBytes(0L));

                // Pan in black.
               // tlvList.addTLV(EmvTags.DEF_TAG_PAN_IN_BLACK, new byte[]{(byte) 0x0}); // 0- false, 1- true

                if (transactionConfig.isRfOnlineForced()) {
                    tlvList.addTLV(EmvTags.DEF_TAG_GAC_CONTROL, new byte[]{EMV.GAC_ONLINE});
                } else {
                    tlvList.addTLV(EmvTags.DEF_TAG_GAC_CONTROL, new byte[]{EMV.GAC_NORMAL});
                }
            } else
                tlvList.addTLV(EmvTags.DEF_TAG_GAC_CONTROL, new byte[]{EMV.GAC_NORMAL});
            EMV.getInstance().responseEvent(tlvList.toString());
        } catch (Exception ex) {
            stopEMVProcess();
            successResponse.processFailed(ex.getLocalizedMessage(),getEntryMode());
        }
    }

    /**
     * Handle card checked.
     */
    private void handleCardChecked(int cardType) throws RemoteException {
        // Check and save card type.
        switch (cardType) {
            case 1: // IC card
                LED.getInstance().turnOffAll();
                session.setAccountEntryMode(Session.ACCOUNT_SERVICE_ENTRY_MODE_CONTACT);
                break;
            case 2: // RF card
                LED.getInstance().turnOn(Light.BLUE, Light.YELLOW);
                session.setAccountEntryMode(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS);
                break;
        }
    }

    /**
     * Handle wait card.
     */
    private void handleWaitCard(int flag) throws RemoteException {
        // Handle flag.
        switch (flag) {
            case WaitCardFlag.NORMAL:
                // This case would never happen, if you have already searchCard before startEMV.
                // Otherwise it would happen for searching card.

                searchCard();
                break;

            case WaitCardFlag.ISS_SCRIPT_UPDATE:
            case WaitCardFlag.SHOW_CARD_AGAIN:
                transactionConfig.setMagCardSupported(false);
                transactionConfig.setContactIcCardSupported(false);
                searchCard();
                break;

            case WaitCardFlag.EXECUTE_CDCVM:
                successResponse.processCDCVM();
                // Halt RF card reader.
               /* EMV.getInstance().halt();
                Log.d(TAG, "----- onWaitCard - CDCVM -----");
                // Delay and research.
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(1200);
                            transactionConfig.setMagCardSupported(false);
                            transactionConfig.setContactIcCardSupported(false);
                            searchCard();
                        } catch (RemoteException | InterruptedException e) {

                            successResponse.processFailed(e.getLocalizedMessage(),getEntryMode());
                        }
                    }
                }.start();*/
                break;

            default:
                // Stop EMV process
                stopEMVProcess();

                successResponse.processFailed(context.getString(R.string.unknown_error),getEntryMode());


                return;
        }

        // Operate LED.
        LED.getInstance().turnOffAll();
        if (transactionConfig.isRfCardSupported()) {
            LED.getInstance().turnOn(Light.BLUE);
        }
    }

    /**
     * Handle communication completed.
     */
    private void handleCommunicationCompleted(String responseCode) throws RemoteException {
        try {
            saveLogs(className, "handleCommunicationCompleted", "[~1315] Started");
            session.setResponseCode(responseCode);
            session.setOnlineProcessSucceeded(true);
            session.setIcRemark(Session.IC_REMARK_TC_GENERATED);

            TLVList tlvList = new TLVList();

            // Online status.erma


            // Check communicate result.
            saveLogs(className, "handleCommunicationCompleted", "[~1326] Communicate result");
            boolean isTransactionSucceeded;
            String respCode = session.getResponseCode();


            tlvList.addTLV(EmvTags.DEF_TAG_ONLINE_STATUS, BytesUtil.hexString2Bytes(session.isOnlineProcessSucceeded() ? "00" : "01"));
            saveLogs(className, "handleCommunicationCompleted", "[~1332] Is online process succeded");


            if (respCode != null && !respCode.isEmpty() && respCode.length() == 2) {
                tlvList.addTLV(EmvTags.EMV_TAG_TM_ARC, respCode.getBytes());
                isTransactionSucceeded = respCode.equals("00");
            } else {
                isTransactionSucceeded = false;
            }
            tlvList.addTLV(EmvTags.DEF_TAG_AUTHORIZE_FLAG, BytesUtil.hexString2Bytes(isTransactionSucceeded ? "01" : "00"));
            saveLogs(className, "handleCommunicationCompleted", "[~1342] Authorize flag");



            // Online response chip data(Field 55 data).
            if (session.getField55() != null && !session.getField55().isEmpty()) {
                String Field55 = session.getField55();
                tlvList.addTLV(EmvTags.DEF_TAG_HOST_TLVDATA, BytesUtil.hexString2Bytes(Field55));
            }
            saveLogs(className, "handleCommunicationCompleted", "[~1351] Host tlv data");

            if (session.getAuthCode() != null && !session.getAuthCode().isEmpty()) {
                tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_AUTHCODE, session.getAuthCode().getBytes()));
//                tlvList.addTLV(TLV.fromData(EmvTags.EMV_TAG_TM_AUTHCODE, BytesUtil.hexString2ByteArray(
//                        BytesUtil.toSpecificSizeString(session.getAuthCode(), 12, "0", "right"))));
//                 tlvList.addTLV(TLV.fromData(EMVTag.EMV_TAG_TM_AUTHCODE, BytesUtil.hexString2Bytes("000000000001")));
            }
            saveLogs(className, "handleCommunicationCompleted", "[~1358] Auth Code");


            //CASE OF TAP CARD....
            // Whether is in EMV process.
            if (!isEMVProcess) {
                saveLogs(className, "handleCommunicationCompleted", "[~1363]");
                if (isTransactionSucceeded) {
                    saveLogs(className, "handleCommunicationCompleted", "[~1365]");
                    transactionApproved();
                    saveLogs(className, "handleCommunicationCompleted", "[~1367]");
                    if (session.getAccountEntryMode().equalsIgnoreCase(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
                        saveLogs(className, "handleCommunicationCompleted", "[~1369]");
                        // Save transaction data.
                        onConfirmCardRecord(session.getPan(), session.getExpirationDate());
                        saveLogs(className, "handleCommunicationCompleted", "[~1372]");

                    }

                } else {
                    saveLogs(className, "handleCommunicationCompleted", "[~1377]");
                    transactionDeclined();
                    saveLogs(className, "handleCommunicationCompleted", "[~1379]");
                }
                return;
            }
            // Response to emv kernel.
            saveLogs(className, "handleCommunicationCompleted", "[~1384]");
            EMV.getInstance().responseEvent(tlvList.toString());
            saveLogs(className, "handleCommunicationCompleted", "[~1386]");

        } catch (Exception ex) {
            saveLogs(className, "handleCommunicationCompleted", "[~1389] Exception :: " + ex.toString());
            stopEMVProcess();
            ex.printStackTrace();

            successResponse.processFailed("Transaction failed while processing EMV.",getEntryMode());
        }
    }

    /**
     * Handle end process.
     */
    private void handleEndProcess(int resultCode, TransData transData) throws RemoteException {
        isEMVProcess = false;

        // Check result code.
        if (resultCode != EMVError.SUCCESS) {
            if (session.getAccountEntryMode() == null ||
                    (session.getAccountEntryMode() != null && !session.getAccountEntryMode().equals(Session.ACCOUNT_ENTRY_MODE_MAGCARD))) {

                transactionInterrupted(resultCode);
            }
            return;
        }

        String pan = StringUtil.getDigits(BytesUtil.bytes2HexString(transData.getPAN()));
        String date = StringUtil.getDigits(BytesUtil.bytes2HexString(transData.getExpiry()));
        session.setPan(pan);
        session.setExpirationDate(date);


            switch (transData.getACType()) {
                case ACType.EMV_ACTION_TC:
                    // EMV kernel approves transaction.
                    transactionApproved();

                    break;

                case ACType.EMV_ACTION_AAC:
                    // EMV kernel denies transaction.
                    transactionDeclined();
                    break;

                case ACType.EMV_ACTION_ARQC:
                    // EMV kernel requires online transaction.
                    handleOnlineProcess();
                    break;
            }

    }
    /**
     * Transaction interrupted.
     */
    private void transactionInterrupted(int resultCode) throws RemoteException {
        // Turn on red light.
        LED.getInstance().turnOffAll();
        LED.getInstance().turnOn(Light.RED);

        // Show transaction result.
        if (resultCode != EMVError.ERROR_EMV_RESULT_STOP) {
            //showToast(getString(EMV.getErrorId(resultCode)));
            stopEMVProcess();

            successResponse.processFailed(context.getString(EMV.getErrorId(resultCode)),getEntryMode());

        } else {
            stopEMVProcess();

            successResponse.processFailed(context.getString(EMV.getErrorId(resultCode)),getEntryMode());
        }
    }

    /**
     * Transaction approved.
     */
    private void transactionApproved() throws RemoteException {
        // Close all lights.
        LED.getInstance().turnOffAll();
        successResponse.TransactionApproved();




        // Show transaction result.
        //showToast(getString(R.string.transaction_approved));

//        // Show and print record.
//        onPrint(session.getDisplayAmount(), session.getPan(), session.getExpirationDate());
//        printRecordByHtml(session);
    }
    /**
     * Transaction declined.
     */
    private void transactionDeclined() throws RemoteException {
        // Turn on red light.
        if(Constant.isForceApprove_ARPC_EMV)
        {
            LED.getInstance().turnOffAll();
            successResponse.TransactionApproved();

        }
        else {
            LED.getInstance().turnOffAll();
            LED.getInstance().turnOn(Light.RED);
            successResponse.TransactionDeclined();
        }
        // Show transaction result.
       // showToast(getString(R.string.transaction_declined));
    }

    /**
     * Handle online process.
     */
    private void handleOnlineProcess() throws RemoteException {

            try {
                // If the transaction need to online process.
                if (!transactionConfig.isOnlineNeeded()) {

                    // It is simple EMV process if no need to be online,the EMV process is stopProcess at this time so that exist process directly.
                    stopEMVProcess();
                    successResponse.processFailed("Declined by Terminal",getEntryMode());
                    return;
                }
            } catch (Exception ex) {
                successResponse.processFailed("Error in Terminal Processing: "+ex.getLocalizedMessage(),getEntryMode());
                ex.printStackTrace();
            }

            // Simulate online
            new Thread() {
                @Override
                public void run() {
                    try {
                        LED.getInstance().operateGreenLight();
                        sleep(300);
                        LED.getInstance().operateGreenLight();
                        sleep(300);
                        LED.getInstance().operateGreenLight();
                        sleep(300);
                        sendMessage(TransactionEvent.COMM_EVENT_COMPLETED.ordinal(), "00"); // "00" means successful
                    } catch (RemoteException | InterruptedException e) {
                        //

                        successResponse.processFailed(e.getLocalizedMessage(),getEntryMode());
                    }
                }
            }.start();

    }
    /**
     * Show card record view.
     */
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

        CardOutput.setCurrencyCode(FirstCurrencyCode);
        CardOutput.setInsertModeCode(EntryMode);
        CardOutput.setInsertMode(TransactionUtils.CardCustomInsertMode(EntryMode));
        CardOutput.setCustomerName(CardHolderName);
        CardOutput.setTransactionCount(TransactionCount);

        if(Constant.isShowDummyCard && CardOutput.getInsertMode().equalsIgnoreCase("swipe"))
        {
            CardOutput.setCardNo(Constant.DummyCardNo);
            CardOutput.setTrack2Data(Constant.DummyTrack2Data);
        }
        else {

            CardOutput.setCardNo(pan);
            CardOutput.setTrack2Data(Track2);
        }


//
//        CardOutput.setEMVData(TLVData);

        if(session.getAccountEntryMode().equalsIgnoreCase(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
            _CardOutput=CardOutput;
            _CardOutput.setTransactionCategoryCode("00");
           String CardExpiry = _CardOutput.getCardExpiry();
           if(CardExpiry.length() > 4) {
               CardExpiry = CardExpiry.substring(0, 4);
               _CardOutput.setCardExpiry(CardExpiry);
           }
            AddRequestForEMV();
            successResponse.processFinish(_CardOutput);

            // we will be sending common var, in this case
            //as it contains all the response for emv and no further processing required.
        }
        else {

            successResponse.processFinish(CardOutput);
            isCardDataRecorded = true;
        }
//        etPan.setText(pan);
//        etDate.setText(date);
//
//        // Check value.
//        btnPan.setEnabled(!etPan.getText().toString().isEmpty() && !etDate.getText().toString().isEmpty());
    }

    /**
     * Start EMV process.
     */
    private void startEMVProcess() throws RemoteException {
        try {
            // Set EMV param
            Bundle bundle = new Bundle();
            bundle.putByte("flagPSE", EMV.PSE_AID_LIST);
            bundle.putByte("flagCtlAsCb", EMV.ENABLE_CONTACTLESS_CARD_SELECT_APP);
            bundle.putBoolean("flagICCLog", false);

            // Start process
            isEMVProcess = true;
            EMV.getInstance().startProcess(bundle, new EMVEventHandler.Stub() {

                @Override
                public void onInitEMV() throws RemoteException {
                    Log.d(TAG, "----- onInitEMV -----");
                    try {

                        handleInitEMV();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onWaitCard(int flag) {
                    Log.d(TAG, "----- onWaitCard -----");
                    Log.d(TAG, "flag : " + flag);

                    if (!isSearchCardFirst || flag == WaitCardFlag.EXECUTE_CDCVM)
                        sendMessage(TransactionEvent.EMV_EVENT_WAIT_CARD.ordinal(), flag);
                }

                @Override
                public void onCardChecked(int cardType) {
                    Log.d(TAG, "----- onCardChecked -----");
                    Log.d(TAG, "cardType : " + cardType);

                    sendMessage(TransactionEvent.EMV_EVENT_CARD_CHECKED.ordinal(), cardType);
                }

                @Override
                public void onAppSelect(boolean reselect, List<CandidateAID> aids) throws RemoteException {
                    Log.d(TAG, "----- onAppSelect -----");
                    Log.d(TAG, "aids.size : " + aids.size());

                    handleAppSelect(aids);
                }

                @Override
                public void onFinalSelect(FinalData finalData) {
                    Log.d(TAG, "----- onFinalSelect -----");
                    Log.d(TAG, "KernelID:" + finalData.getKernelID());
                    Log.d(TAG, "AID:" + BytesUtil.bytes2HexString(finalData.getAID()));


                    sendMessage(TransactionEvent.EMV_EVENT_FINAL_SELECT.ordinal(), finalData);
                }

                @Override
                public void onReadRecord(CardRecord cardRecord) {

                    Log.d(TAG, "----- onReadRecord -----");
                    Log.d(TAG, "PAN:" + BytesUtil.bytes2HexString(cardRecord.getPan()));

                    sendMessage(TransactionEvent.EMV_EVENT_READ_RECORD.ordinal(), cardRecord);
                }

                @Override
                public void onCardHolderVerify(CVMMethod cvmMethod) {
                    Log.d(TAG, "----- onCardHolderVerify -----");
                    Log.d(TAG, "CVM:" + cvmMethod.getCVM());
                    Log.d(TAG, "CertType:" + cvmMethod.getCertType());
                    Log.d(TAG, "CertNo:" + cvmMethod.getCertNo());
                    Log.d(TAG, "PINTimes:" + cvmMethod.getPINTimes());
                    isCardHolderVerificationPerformed = true;
                    ///NEED TO CHECK WE CAN FORCEFULLY ALLOW TO ASK FOR PIN OR NOT..

                    //AS PER LALIT SIR.. WE HAVE TO SHOW PINPAD ..if CARD Is CALLING THIS METHOD TO VERIFY CARD HOLDER

                    //  transactionConfig.setPinInputNeeded(true);

                    sendMessage(TransactionEvent.EMV_EVENT_CARD_HOLDER_VERIFY.ordinal(), cvmMethod);
                }

                @Override
                public void onOnlineProcess(TransData transData) {

                    Log.d(TAG, "----- onOnlineProcess -----");
                    Log.d(TAG, "ACType:" + transData.getACType());
                    Log.d(TAG, "CVM:" + transData.getCVM());
                    Log.d(TAG, "FlowType:" + transData.getFlowType());
                    Log.d(TAG, "TVR: " + transData.getTLVData());


                    String CodeToVerifyInLogs = transData.getCVM() +"$"+ transData.getACType();
                    _CardOutput.setPubKIndex(_CardOutput.getPubKIndex() != null ?_CardOutput.getPubKIndex()+"()"+CodeToVerifyInLogs:"NULL()"+CodeToVerifyInLogs);
                    //if card has not forced for input pin....
                    if (!isCardHolderVerificationPerformed) {
                        try {
                             if( transactionConfig.isPinInputNeeded()) {
                                 //inputOnlinePin();
                                 successResponse.SelectVerificationOption();
                             }
                             else {
                                 if (_CardOutput.getRID().equalsIgnoreCase(Constant.VisaRID))
                                     successResponse.processSignature(); //ADVT Wants it MTIP not wants

                                 FinalizingRequestForEMV();
                             }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        FinalizingRequestForEMV();
                    }
                }

                @Override
                public void onEndProcess(int resultCode, TransData transData) {
                    Log.d(TAG, "----- onEndProcess -----");
                    Log.d(TAG, "resultCode:" + resultCode);


                    sendMessage(TransactionEvent.EMV_EVENT_END_PROCESS.ordinal(), resultCode, transData);
                }

                @Override
                public void onVerifyOfflinePin(int flag, byte[] random, CAPublicKey caPublicKey, OfflinePinVerifyResult offlinePinVerifyResult) {
                    Log.d(TAG, "----- onVerifyOfflinePin -----");
                    Log.d(TAG, "flag : " + flag);
                    handleVerifyOfflinePin(flag, random, caPublicKey, offlinePinVerifyResult);
                }

                @Override
                public void onObtainData(int command, byte[] data) {
                    Log.d(TAG, "----- onObtainData -----");
                    Log.d(TAG, command + " : " + BytesUtil.bytes2HexString(data));
                }

                @Override
                public void onSendOut(int command, byte[] data) {
                    Log.d(TAG, "----- onSendOut -----");
                    Log.d(TAG, command + " : " + BytesUtil.bytes2HexString(data));

                    sendMessage(TransactionEvent.EMV_EVENT_SEND_OUT.ordinal(), command);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void AddRequestForEMV() {
        try {

            boolean isContactLessTxn = _CardOutput.getInsertModeCode().equalsIgnoreCase(Constant.POSENT_Contactless) ? true : false;

            successResponse.EMVProcessing();
            String Track2Data = EMV.getInstance().getTLV("57");

            TLVList tlvList = new TLVList();

            String CID = EMV.getInstance().getTLV("9F27"); //Cryptogram Information Data
            if (CID != null && CID.length() > 0)
                tlvList.addTLV(TLV.fromData("9F27", BytesUtil.hexString2Bytes(CID)));

            String AuthAmount = EMV.getInstance().getTLV("9F02");//Authorizied Amount
            tlvList.addTLV(TLV.fromData("9F02", BytesUtil.hexString2Bytes(AuthAmount)));

            String otherAmount = EMV.getInstance().getTLV("9F03"); //Other Amount
            tlvList.addTLV(TLV.fromData("9F03", BytesUtil.hexString2Bytes(otherAmount)));

            String ARQC = EMV.getInstance().getTLV("9F26"); //Acquirer Request Cryptogram
            if (ARQC != null && ARQC.length() > 0) {
                // ARQC = "A1A2A3A4A5A6A7A8";
                tlvList.addTLV(TLV.fromData("9F26", BytesUtil.hexString2Bytes(ARQC)));
            }

            String AIP = EMV.getInstance().getTLV("82"); //Application Interchange Profile
            if (AIP != null && AIP.length() > 0)
                tlvList.addTLV(TLV.fromData("82", BytesUtil.hexString2Bytes(AIP)));


            String ATC = EMV.getInstance().getTLV("9F36"); //Application Transaction Counter
            if (ATC != null && ATC.length() > 0)
                tlvList.addTLV(TLV.fromData("9F36", BytesUtil.hexString2Bytes(ATC)));

            if(isContactLessTxn) {
//                String Tag_9F6C = EMV.getInstance().getTLV("9F6C");
//                if (Tag_9F6C != null && Tag_9F6C.length() > 0)
//                    tlvList.addTLV(TLV.fromData("9F6C", BytesUtil.hexString2Bytes(Tag_9F6C)));

                //Terminal Transaction Qualifier
//                  String Tag_9F66 = EMV.getInstance().getTLV("9F66");
//                if (Tag_9F66 != null && Tag_9F66.length() > 0)
//                    tlvList.addTLV(TLV.fromData("9F66", BytesUtil.hexString2Bytes(Tag_9F66)));



                String Tag_9F7C = EMV.getInstance().getTLV("9F7C");
                if (Tag_9F7C != null && Tag_9F7C.length() > 0)
                    tlvList.addTLV(TLV.fromData("9F7C", BytesUtil.hexString2Bytes(Tag_9F7C)));

                String Tag_9F6E = EMV.getInstance().getTLV("9F6E");
                if (Tag_9F6E != null && Tag_9F6E.length() > 0)
                    tlvList.addTLV(TLV.fromData("9F6E", BytesUtil.hexString2Bytes(Tag_9F6E)));


            }

            String IAD = EMV.getInstance().getTLV("9F10"); //Issuer Application Data
            if (IAD != null && IAD.length() > 0)//
                tlvList.addTLV(TLV.fromData("9F10", BytesUtil.hexString2Bytes(IAD)));

            String TC = EMV.getInstance().getTLV("9F33"); //Terminal Capabilities
            if (TC != null && TC.length() > 0)
                tlvList.addTLV(TLV.fromData("9F33", BytesUtil.hexString2Bytes(TC)));

            String TxnCurrencyCode = Constant.BaseCurrencyCode; // Terminal - Transaction Currency Code
            if (TxnCurrencyCode != null && TxnCurrencyCode.length() > 0)
                tlvList.addTLV(TLV.fromData("5F2A", BytesUtil.hexString2Bytes(TxnCurrencyCode)));

            String TerminalCountryCode = Constant.BaseCountryCode; //Terminal Country Code
            if (TerminalCountryCode != null && TerminalCountryCode.length() > 0)
                tlvList.addTLV(TLV.fromData("9F1A", BytesUtil.hexString2Bytes(TerminalCountryCode)));


            String TVR = EMV.getInstance().getTLV("95"); //Transaction Verification Result

            if (TVR != null && TVR.length() > 0 && !TVR.equalsIgnoreCase("0000000000")) { //
                tlvList.addTLV(TLV.fromData("95", BytesUtil.hexString2Bytes(TVR)));
            }

            String TxnDate = EMV.getInstance().getTLV("9A"); //Transaction Date
            if (TxnDate != null && TxnDate.length() > 0)
                tlvList.addTLV(TLV.fromData("9A", BytesUtil.hexString2Bytes(TxnDate)));


            String TSI = EMV.getInstance().getTLV("9B"); //Transaction Status Information
            // tlvList.addTLV(TLV.fromData("9B", BytesUtil.hexString2Bytes(TSI)));


            String TxnType = EMV.getInstance().getTLV("9C");//Transaction Type
            if (TxnType != null && TxnType.length() > 0)
                tlvList.addTLV(TLV.fromData("9C", BytesUtil.hexString2Bytes(TxnType)));

            String UnpredicatableNo = EMV.getInstance().getTLV("9F37"); //Unpredictable Number
            if (UnpredicatableNo != null && UnpredicatableNo.length() > 0)
                tlvList.addTLV(TLV.fromData("9F37", BytesUtil.hexString2Bytes(UnpredicatableNo)));


            String CVM = EMV.getInstance().getTLV("9F34"); //Cardholder Verification Method

            if (CVM != null && CVM.length() > 0 && !CVM.equalsIgnoreCase("000000") ) {//
                tlvList.addTLV(TLV.fromData("9F34", BytesUtil.hexString2Bytes(CVM)));
            }
//            else
//                tlvList.addTLV(TLV.fromData("9F34", BytesUtil.hexString2Bytes("000000")));

            String PANSeqNo = EMV.getInstance().getTLV("5F34"); //PAN Sequence No.
           // if(!_CardOutput.getCardAppName().startsWith("JCB")) {

//            if (!(PANSeqNo != null && PANSeqNo.length() > 0))
//                PANSeqNo = "00";  //MTIP Case 50 01 01 = Failed due to adding default value "00"

                if (!isContactLessTxn && PANSeqNo != null && PANSeqNo.length() > 0)
                    if(!_CardOutput.getCardAppName().startsWith("JCB"))
                        tlvList.addTLV(TLV.fromData("5F34", BytesUtil.hexString2Bytes(PANSeqNo)));
            //}

            if (_CardOutput.getCardAID().equalsIgnoreCase("A0000006021010")) //for debit card only
            {
                String CARDDetails = EMV.getInstance().getTLV("5A"); //Card Details.
                if (CARDDetails != null && CARDDetails.length() > 0) {
                    if (CARDDetails.length() == 16)
                        CARDDetails = BytesUtil.toSpecificSizeString(CARDDetails, 16, "F", "left");
                    else
                        CARDDetails = BytesUtil.toSpecificSizeString(CARDDetails, 20, "F", "left");

                    tlvList.addTLV(TLV.fromData("5A", BytesUtil.hexString2Bytes(CARDDetails)));
                } else if (_CardOutput != null && _CardOutput.getCardNo().length() > 0) {
                    CARDDetails = BytesUtil.toSpecificSizeString(_CardOutput.getCardNo(), 20, "F", "left");
                    tlvList.addTLV(TLV.fromData("5A", BytesUtil.hexString2Bytes(CARDDetails)));
                }
            }




            String AID = EMV.getInstance().getTLV("84"); //Application Identification
            if (AID != null && AID.length() > 0 ) {
                if (!isContactLessTxn)
                    tlvList.addTLV(TLV.fromData("84", BytesUtil.hexString2Bytes(AID)));
                else if (!(_CardOutput.getCardAppName().toLowerCase().startsWith("visa")))
                    tlvList.addTLV(TLV.fromData("84", BytesUtil.hexString2Bytes(AID)));
            }


            String AVN = EMV.getInstance().getTLV("9F09");// Application Version Number
            if (AVN != null && AVN.length() > 0 && !isContactLessTxn)
                tlvList.addTLV(TLV.fromData("9F09", BytesUtil.hexString2Bytes(AVN)));



            String SerialNo = Constant.HSN;
            if(SerialNo != null && SerialNo.length() > 8)
            {
                SerialNo = SerialNo.substring(0,8);
            }
            String IFD = Util.toHex(SerialNo);//Terminal - Interface Device Serial Number
            if (IFD != null && IFD.length() > 0 && !isContactLessTxn)
                tlvList.addTLV(TLV.fromData("9F1E", BytesUtil.hexString2Bytes(IFD)));





            String TType = EMV.getInstance().getTLV("9F35");//Terminal Type
            if (TType != null && TType.length() > 0 && !isContactLessTxn  )
                tlvList.addTLV(TLV.fromData("9F35", BytesUtil.hexString2Bytes(TType)));



            String TxnSequenceCounter = EMV.getInstance().getTLV("9F41");//Transaction sequence counter;
            if (TxnSequenceCounter != null && TxnSequenceCounter.length() > 0 && !isContactLessTxn)
                tlvList.addTLV(TLV.fromData("9F41", BytesUtil.hexString2Bytes(TxnSequenceCounter)));

            String Tag_9F53 = EMV.getInstance().getTLV("9F53");
                //CVR (Card Verification Result) or TxnCategoryCode

                if (Tag_9F53 != null && Tag_9F53.length() > 0 && !isContactLessTxn)
                    tlvList.addTLV(TLV.fromData("9F53", BytesUtil.hexString2Bytes(Tag_9F53)));
                else if (_CardOutput != null && _CardOutput.getTransactionCategoryCode() != null &&
                        _CardOutput.getTransactionCategoryCode().length() > 0 && !isContactLessTxn) { //
                    Tag_9F53 = _CardOutput.getTransactionCategoryCode();
                    if(!_CardOutput.getCardAppName().startsWith("JCB"))
                        tlvList.addTLV(TLV.fromData("9F53", BytesUtil.hexString2Bytes(Tag_9F53)));
                }


            String IACOnline = EMV.getInstance().getTLV("9F0F");
            String IACDefault = EMV.getInstance().getTLV("9F0D");
            String IACDenial = EMV.getInstance().getTLV("9F0E");


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
            _CardOutput.setTxnCategoryCode(Tag_9F53);
            _CardOutput.setPANSEQ(PANSeqNo);
            _CardOutput.setTerminalCapability(TC);
            _CardOutput.setAdditionalTerminalCapability("F000F0A001");
            _CardOutput.setTxnDate(TxnDate);
            _CardOutput.setTxnAmount(AuthAmount);
            _CardOutput.setOtherAmount(otherAmount);
            _CardOutput.setApplicationInterchangeProfile(AIP);
            _CardOutput.setCardHolderVerificationMethod(CVM);
            _CardOutput.setIssuerApplicationData(IAD);

            _CardOutput.setUnpredicatableNumber(UnpredicatableNo);

            _CardOutput.setTACDefault("DC4000A800");
            _CardOutput.setTACDenial("0010000000");
            _CardOutput.setTACOnline("DC4004F800");

            _CardOutput.setIACDefault(IACDefault);
            _CardOutput.setIACDenial(IACDenial);
            _CardOutput.setIACOnline(IACOnline);


        } catch (Exception ex) {
            Log.e("EMV Error", ex.getLocalizedMessage());
        }

    }
    private void FinalizingRequestForEMV() {

        if (isEMVProcess)
            AddRequestForEMV();
        else {
            //handle required params in case of SWIPE etc.

//            String PANSeqNo = "00";
//            _CardOutput.setPANSEQ(PANSeqNo);
        }


        //get PINBLOCK FROM SESSION


        successResponse.processFinish(_CardOutput);

    }
    public void ProcessEMVCompletionFlow(String EMVD,String IAC){
        session.setField55(EMVD);
        session.setAuthCode(IAC !=null ? IAC.trim() : "0"); //IAC
        Log.d("RESPONSE_CODE",EMVD);

//        byte AuthFlag = (session.getAuthCode() != null && session.getAuthCode() != "0")?(byte)0x01:(byte)0X00;
//
//        try {
//            EMV.getInstance().responseEvent(TLV.fromData(EMVTag.DEF_TAG_CHV_STATUS, new byte[]{AuthFlag}).toString());
//        }
//        catch (Exception ex)
//        {
//            successResponse.TransactionDeclined();
//        }
        //We need to run this after response
        sendMessage(TransactionEvent.EMV_EVENT_ONLINE_PROCESS.ordinal(), null);
     //   sendMessage(TransactionEvent.COMM_EVENT_COMPLETED.ordinal(), "00");
    }
    public void startCardScan(TransactionConfig config,String STAN,boolean isRefundTxn) throws  RemoteException {

        //  session.setSystemTraceAuditNumber(STAN);
        session.setSystemTraceAuditNumber(InputUtil.ProcessNumericToFixedDigits(8, STAN));

        isRefund=isRefundTxn;
        transactionConfig = config;
        if (config.getAmount() > 0)
            session.setTransactionAmount(config.getAmount());
        if (isSearchCardFirst)
            searchCard();
        else
            startEMVProcess();
    }

    public void startCardProcessor( TransactionConfig config, String BatchNo,String InvoiceNo,
                                   String TxnName,String ProcessingCode) {
//        ,Long TransactionAmount,
//        TransactionConfig _transConfig
        // Set session.
        session.setTransactionName(TxnName);//"Sale"

        session.setProcessingCode(ProcessingCode); //"000000"
      //  session.setSystemTraceAuditNumber(InvoiceNo);
        session.setBatchNumber(BatchNo);

        // Set transaction config.

        transactionConfig = config;




    }
    public void searchCard() throws RemoteException {
        // Set card config
        if(DeviceManager.getInstance() !=null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("supportICCard", transactionConfig.isContactIcCardSupported());
            bundle.putBoolean("supportRFCard", transactionConfig.isRfCardSupported());
            bundle.putBoolean("supportMagCard", transactionConfig.isMagCardSupported());


            if (DeviceManager.MODEL_AECR_C10.equals(DeviceManager.getInstance().getModel())) {
                bundle.putString("rfDeviceName", RFDeviceName.EXTERNAL);
            } else {
                bundle.putString("rfDeviceName", RFDeviceName.INNER);
            }

            // Start searching card
            EMV.getInstance().searchCard(bundle, Constant.DefaultTimeOutInSeconds, new SearchCardListener.Stub() {
                @Override
                public void onCardSwiped(Bundle bundle) {
                    Log.d(TAG, "----- onCardSwiped -----");
                    Log.d(TAG, "PAN : " + bundle.getString("PAN"));
                    Log.d(TAG, "TRACK1 : " + bundle.getString("TRACK1"));
                    Log.d(TAG, "TRACK2 : " + bundle.getString("TRACK2"));
                    Log.d(TAG, "TRACK3 : " + bundle.getString("TRACK3"));
                    Log.d(TAG, "SERVICE_CODE : " + bundle.getString("SERVICE_CODE"));
                    Log.d(TAG, "EXPIRED_DATE : " + bundle.getString("EXPIRED_DATE"));


                    sendMessage(TransactionEvent.CARD_EVENT_SWIPE.ordinal(), bundle);

                }

                @Override
                public void onCardInsert() {
                    Log.d(TAG, "----- onCardInsert -----");
                    try {
                        LED.getInstance().turnOffAll();
                    } catch (Exception ex) {

                    }

                    successResponse.CardInserted();
                    sendMessage(TransactionEvent.CARD_EVENT_INSERT.ordinal(), null);


                }

                @Override
                public void onCardPass(int cardType) {
                    Log.d(TAG, "----- onCardPass -----");
                    Log.d(TAG, "cardType: " + cardType);
                    try {
                        LED.getInstance().turnOn(Light.BLUE, Light.YELLOW);
                    } catch (Exception ex) {

                    }
                    session.setAccountEntryMode(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS);
                    sendMessage(TransactionEvent.CARD_EVENT_TAP.ordinal(), null);

                }

                @Override
                public void onTimeout() throws RemoteException {
                    Log.d(TAG, "----- onTimeout -----");

                    stopEMVProcess();
                    successResponse.processTimeOut();
                    // Stop EMV process
                    //   showToast(getString(R.string.wait_card_timeout));

                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    Log.d(TAG, "----- onError -----");
                    Log.d(TAG, error + " : " + message);
                    stopEMVProcess();

                    successResponse.processFailed(message,getEntryMode());

                    // Stop EMV process
                    //  showToast(getString(EMV.getErrorId(error)));

                }
            });
        }
        else
            successResponse.processFailed("Device Manager is not initialized");
    }
    @Override
    public void close() throws Exception {
        try {
            EMV.getInstance().stopSearch();
            EMV.getInstance().halt();
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                // Close pinpad
                PinpadForDUKPT.getInstance().close();
            } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                // Close pinpad
                PinpadForMKSK.getInstance().close();
            }
            //No Matter Card Data Readed or Not, If close is called than we assume we either don't need
            //Card Data or it is already provided.

            isCardDataRecorded = true;
            if (isEMVProcess) {
                EMV.getInstance().stopProcess();
            }

        }
        catch (Exception ex)
        {

        }

    }
    public void StopProcess()
    {
        try {
            if (isEMVProcess) {

            }
        }
        catch (Exception ex)
        {

        }
    }
}
