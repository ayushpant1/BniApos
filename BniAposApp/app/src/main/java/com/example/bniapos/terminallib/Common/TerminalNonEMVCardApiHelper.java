package com.example.bniapos.terminallib.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;


import com.example.bniapos.R;
import com.example.bniapos.terminallib.CardReadOutput;
import com.example.bniapos.terminallib.api.ICCpuReader;
import com.example.bniapos.terminallib.api.MagReader;
import com.example.bniapos.terminallib.api.PinpadForMKSK;
import com.example.bniapos.terminallib.api.RFReader;
import com.example.bniapos.terminallib.util.data.BytesUtil;
import com.example.bniapos.terminallib.util.pinpad.KeyId;
import com.example.bniapos.terminallib.util.pinpad.MockKey;
import com.usdk.apiservice.aidl.data.ApduResponse;
import com.usdk.apiservice.aidl.data.BytesValue;
import com.usdk.apiservice.aidl.data.IntValue;
import com.usdk.apiservice.aidl.icreader.OnInsertListener;
import com.usdk.apiservice.aidl.icreader.PowerMode;
import com.usdk.apiservice.aidl.icreader.Voltage;
import com.usdk.apiservice.aidl.magreader.OnSwipeListener;
import com.usdk.apiservice.aidl.pinpad.EncKeyFmt;
import com.usdk.apiservice.aidl.pinpad.KeyAlgorithm;
import com.usdk.apiservice.aidl.pinpad.KeyType;
import com.usdk.apiservice.aidl.pinpad.OnPinEntryListener;
import com.usdk.apiservice.aidl.rfreader.CardType;
import com.usdk.apiservice.aidl.rfreader.OnPassListener;

import java.util.List;

public class TerminalNonEMVCardApiHelper {

    private Context context;

    /**
     * Rf card type.
     */
    private int rfCardType = -1;
    RFReader rfReader = null;
    ICCpuReader icReader = null;
    MagReader mgReader  = null;
    String TAG = "NonEMVCardHelper";

    public ISuccessResponse_Card successResponse = null;
    //Context _context,
    public TerminalNonEMVCardApiHelper(ISuccessResponse_Card _successResponse)
    {
      //  this.context = _context;
        this.successResponse = _successResponse;
    }

    public void ClearResponse() {
        try {
            if (rfReader != null) {

                rfReader.stopSearch();
                rfReader = null;
            }
            if(icReader != null)
            {
                icReader.stopSearch();
                icReader = null;
            }
            if(mgReader != null)
            {
                mgReader.stopSearch();
                mgReader.stopSearch();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public void searchCard() throws RemoteException {



        // Search RF card
      rfReader=  RFReader.getInstance();

      rfReader.searchCard(new OnPassListener.Stub() {

            @Override
            public void onCardPass(int type) throws RemoteException {

                rfCardType = type;
               // String message = "card type : " + rfCardType(type) + "\r\n";
                String message = "";


                try {
                    switch (type) {
                        case CardType.S50_CARD:
                            message += handleS50S70Card(type);
                            break;



                        case CardType.S70_CARD:
                            message += handleS50S70Card(type);
                            break;

                        case CardType.PRO_CARD:
                        case CardType.S50_PRO_CARD:
                        case CardType.S70_PRO_CARD:
                            message += handleIndonesiaProCpuCard(type);
                            break;

                        case CardType.CPU_CARD:
                            message += handleIndonesiaProCpuCard(type);
                            break;

                        default:
                            message += R.string.unknown_card;
                            break;
                    }


                } catch (RemoteException e) {
                   successResponse.processFailed( e.getLocalizedMessage());
                   ClearResponse();
                }

                // 2017.11.08 Mifare卡的操作放另外一个activity，此处不能调用halt方法
//                RFReader.getInstance().halt();

                CardReadOutput card_output = new CardReadOutput();
                card_output.setCardNo(message);
                successResponse.processFinish(card_output);
                ClearResponse();
            }

            @Override
            public void onFail(int error) throws RemoteException {

                RFReader.getInstance().halt();
                successResponse.processFailed(String.valueOf( RFReader.getErrorId(error)));
                ClearResponse();

            }
        });


    /**
     * Search IC card.
     */

        // Search IC card
        icReader =   ICCpuReader.getInstance();
        icReader.searchCard(new OnInsertListener.Stub() {

            @Override
            public void onCardInsert() throws RemoteException {

                // Init module
                ICCpuReader.getInstance().initModule(Voltage.ICCpuCard.VOL_DEFAULT, PowerMode.DEFAULT);

                // Power up
                try {

                    BytesValue atr = new BytesValue();
                    IntValue protocol = new IntValue();
                    ICCpuReader.getInstance().powerUp(atr, protocol);

                } catch (RemoteException e) {
                   successResponse.processFailed(e.getLocalizedMessage());
                    ClearResponse();
                    return;
                }

                // Exchange APDU
                String message;
                byte[] cmdHead = {0x00, (byte) 0xa4, 0x04, 0x00, 0x0e};
                byte[] fileName = "1PAY.SYS.DDF01".getBytes();
//                byte[] cmdHead = {0x00, (byte) 0xa4, 0x04, 0x00, 0x07};
//                byte[] fileName = BytesUtil.hexString2Bytes("A0000000032010"); // Visa electron aid
                byte[] cmd = BytesUtil.merge(cmdHead, fileName, new byte[]{0x00});
                try {
                    ApduResponse apduResponse = ICCpuReader.getInstance().exchangeApdu(cmd);
                    message = "APDU ret:" + apduResponse.getAPDURet() + "|" + apduResponse.getSW1() + "|" + apduResponse.getSW2();
                } catch (RemoteException e) {
                    message = e.getLocalizedMessage();
                }

                // Power down
                try {
                    ICCpuReader.getInstance().powerDown();
                } catch (RemoteException e) {
                    message = e.getLocalizedMessage();
                }

                CardReadOutput card_output = new CardReadOutput();
                card_output.setCardNo(message);
               successResponse.processFinish(card_output);
                ClearResponse();

            }

            @Override
            public void onFail(int error) throws RemoteException {
               successResponse.processFailed(String.valueOf(ICCpuReader.getErrorId(error)));
                ClearResponse();

            }
        });


    /**
     * Search mag card.
     */


        // Search mag card
       mgReader = MagReader.getInstance();
       mgReader.searchCard(60, new OnSwipeListener.Stub() {

            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {

//                String message = "PAN:" + bundle.getString(MagReader.PAN) + "\r\n";
//                message += "TRACK1:" + bundle.getString(MagReader.TRACK1) + "\r\n";
//                message += "TRACK2:" + bundle.getString(MagReader.TRACK2) + "\r\n";
//                message += "TRACK3:" + bundle.getString(MagReader.TRACK3) + "\r\n";
//                message += "SERVICE_CODE:" + bundle.getString(MagReader.SERVICE_CODE) + "\r\n";
//                message += "EXPIRED_DATE:" + bundle.getString(MagReader.EXPIRED_DATE) + "\r\n";
                String TRACK2 = bundle.getString(MagReader.TRACK2);
                String PAN = bundle.getString(MagReader.PAN);
                if (PAN.equalsIgnoreCase("") && TRACK2.equalsIgnoreCase("012021")) {
                    PAN = "100024945";
                }
                String message = PAN;
                CardReadOutput card_output = new CardReadOutput();
                card_output.setCardNo(PAN);
                card_output.setTrack2Data(TRACK2);
                successResponse.processFinish(card_output);
                ClearResponse();

            }

            @Override
            public void onError(int error) throws RemoteException {
               successResponse.processFailed(String.valueOf(MagReader.getErrorId(error)));
                ClearResponse();

            }

            @Override
            public void onTimeout() throws RemoteException {
               successResponse.processTimeOut();
                ClearResponse();
            }
        });
    }
    private String handleProCpuCard(int cardType) throws RemoteException {
        // Activate
        BytesValue responseData = new BytesValue();
        RFReader.getInstance().activate(cardType, responseData);

        // Exchange APDU
        byte[] cmdHead = {0x00, (byte) 0xa4, 0x04, 0x00, 0x0e};
        byte[] fileName = "2PAY.SYS.DDF01".getBytes();
//        byte[] cmdHead = {0x00, (byte) 0xa4, 0x04, 0x00, 0x08};
//        byte[] fileName = BytesUtil.hexString2Bytes("A000000172950001");
        byte[] cmd = BytesUtil.merge(cmdHead, fileName, new byte[]{0x00});
        ApduResponse data = RFReader.getInstance().exchangeApdu(cmd);
        return "APDU ret:" + data.getAPDURet() + "|" + data.getSW1() + "|" + data.getSW2();
    }

    private String handleIndonesiaProCpuCard(int cardType) throws RemoteException {
        // Activate
        BytesValue responseData = new BytesValue();
        RFReader.getInstance().activate(cardType, responseData);

        // Exchange APDU
        byte[] getRandomCmd = {0x00, (byte)0x84, 0x00, 0x00, 0x08};
        byte[] getSnCmd = {0x00, (byte)0xCA, 0x01, 0x01, 0x08};
        ApduResponse random = RFReader.getInstance().exchangeApdu(getRandomCmd);
        ApduResponse sn = RFReader.getInstance().exchangeApdu(getSnCmd);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Random: ");
        stringBuilder.append(BytesUtil.byteArray2HexString((random.getData())));
        stringBuilder.append("\n");
        stringBuilder.append("SN: ");
        stringBuilder.append(BytesUtil.byteArray2HexString((sn.getData())));
        return stringBuilder.toString();
    }

    /**
     * Handle S50/S70 card.
     */
    private String handleS50S70Card(int cardType) throws RemoteException {
        BytesValue responseData = new BytesValue();
        byte[] aucMifKeyB0 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
      //  byte[] aucMifKeyB0 = {(byte) 0x49, (byte) 0x43, (byte) 0x4d, (byte) 0x53, (byte) 0x43, (byte) 0x50};
        byte[] writeData = {0x05, 0x00, 0x00, 0x00, (byte) 0xFA, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x05, 0x00, 0x00, 0x00, 0x01, (byte) 0xFE, 0x01, (byte) 0xFE};
        BytesValue readData = new BytesValue();
        byte[] uid;

        // Activate
        RFReader.getInstance().activate(cardType, responseData);

        uid = RFReader.getInstance().getCardSerialNo(responseData.getData());



        // Auth sector
  //      RFReader.getInstance().authSector(0, 1, aucMifKeyB0);
//
//        // Auth block
 //       RFReader.getInstance().authBlock(1, 1, aucMifKeyB0);

        // Auth block
//        RFReader.getInstance().authBlock(0, 1, aucMifKeyB0);
//
//        // Write block
//        RFReader.getInstance().writeBlock(30, writeData);
//
//        // Read block
  //     RFReader.getInstance().readBlock(1, readData);
     //   RFReader.getInstance().readBlock(1,readData);
//
//        // Increase value
//        RFReader.getInstance().increaseValue(30, 2);
//
//        // Decrease value
//        RFReader.getInstance().decreaseValue(30, 2);

     //   startMifareActivity(cardType, BytesUtil.bytes2HexString(uid));

       // return "UID["+ BytesUtil.bytes2HexString(uid) + "]" + "\r\n Succeed";

        String CardNo = toHexString(readData.getData()).toString();
       CardNo = "7010110766482723"; Util.hexToAscii(CardNo);

        return CardNo;
       // return BytesUtil.byteArray2HexString( uid);
    }
    public static String toHexString(byte[] data) {
        if (data == null) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < data.length; ++i) {
                String string = Integer.toHexString(data[i] & 255);
                if (string.length() == 1) {
                    stringBuilder.append("0");
                }

                stringBuilder.append(string.toUpperCase());
            }

            return stringBuilder.toString();
        }
    }

    /**
     * RF card type.
     * @param cardType
     * @return
     */
    private String rfCardType(int cardType) {
        String card = "unknow";

        switch (cardType) {
            case CardType.S50_CARD:
                card = "S50";
                break;
            case CardType.S70_CARD:
                card = "S70";
                break;
            case CardType.PRO_CARD:
                card = "PRO";
                break;
            case CardType.S50_PRO_CARD:
                card = "S50_PRO";
                break;
            case CardType.S70_PRO_CARD:
                card = "S70_PRO";
                break;
            case CardType.CPU_CARD:
                card = "CPU";
                break;
            default:
                break;
        }

        return card;
    }
    /**
     * Start mifare activity.
     *
     * @param uid
     */
    private void startMifareActivity(int cardType, String uid) {
//        Intent intent = new Intent(context, MifareActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("UID", uid);
//        intent.putExtra("CardType", cardType);
//        context.startActivity(intent);
    }

    /**
     * Load plain text key.
     */
    private void loadPlainTextKey() throws RemoteException {
        // Load plain text key
       byte[] key = BytesUtil.hexString2Bytes(MockKey.mainKey);
        PinpadForMKSK.getInstance().loadPlainTextKey(KeyType.MAIN_KEY, KeyId.mainKey, key);
    }

    /**
     * Load encrypted key.
     */

        private void loadEncryptedKey() throws RemoteException {
            // Get all keys
            byte[] pinKey = BytesUtil.hexString2Bytes(MockKey.pinEncKey);
            byte[] pinKCV = BytesUtil.hexString2Bytes(MockKey.pinEnvKCV);

            byte[] tdkKey = BytesUtil.hexString2Bytes(MockKey.tdkEncKey);
            byte[] tdkKCV = BytesUtil.hexString2Bytes(MockKey.tdkEncKCV);


//            // Load PIN key
//            PinpadForMKSK.getInstance().loadEncKey(KeyType.PIN_KEY, KeyId.mainKey, KeyId.pinKey, pinKey, pinKCV);

            // Load TDK key
            PinpadForMKSK.getInstance().loadEncKey(KeyType.PIN_KEY, KeyId.mainKey, KeyId.pinKey, tdkKey, tdkKCV);


        }


        /**
         * Format.
         */
    private void format() throws RemoteException {
        // Set key algorithm
        PinpadForMKSK.getInstance().setKeyAlgorithm(KeyAlgorithm.KA_TDEA);

        // Set encrypted key format
        PinpadForMKSK.getInstance().setEncKeyFormat(EncKeyFmt.ENC_KEY_FMT_TDES_CBC);

        // Format
        //PinpadForMKSK.getInstance().format();

    }


    /**
     * Close device.
     */
    private void close() throws RemoteException {
        // Close
        PinpadForMKSK.getInstance().close();


    }

    /**
     * Open device.
     */
    private void open() throws RemoteException {
        // Open
        PinpadForMKSK.getInstance().open();


    }
    /**
     * Delete key.
     */
    private void deleteKey() throws RemoteException {
        // Delete key
        PinpadForMKSK.getInstance().deleteKey(KeyId.mainKey);

    }


    /**
     * Get existent key IDs in key system.
     */
    private void getExistentKeyIdsInKeySystem() throws RemoteException {
        // Get key IDs
        byte keyUsage = 0x11; // mac key
        String message = "";
        List<IntValue> keyIds;
        keyIds = PinpadForMKSK.getInstance().getExistentKeyIdsInKeySystem(keyUsage);
        message += keyIds.size() + "\r\n";
        for (IntValue keyId : keyIds) {
            message += keyId.getData() + "\r\n";
        }

    }


    /**
     * Start pin entry.
     */


    public void startPinEntry(String PAN) throws RemoteException {

        open();
//
        loadPlainTextKey();
        loadEncryptedKey();
        format();
        Bundle param = new Bundle();
        param.putByteArray("pinLimit", new byte[]{4,6});
        param.putInt("time out", 300);
        param.putBoolean("isOnline", true);
        param.putByteArray("panBlock", BytesUtil.hexString2Bytes(PAN));
        PinpadForMKSK.getInstance().startPinEntry(KeyId.tdkKey, param, onPinEntryListener);

    }

    /**
     * Cancel pin entry.
     */
    public void cancelPinEntry() throws RemoteException {


        // Delay and cancel pin entry
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    PinpadForMKSK.getInstance().cancelPinEntry(0);

                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * Callback for Pinpad.
     */
    private OnPinEntryListener onPinEntryListener = new OnPinEntryListener.Stub() {
        @Override
        public void onInput(int len, int key) throws RemoteException {
            Log.d(TAG, "----- onInput -----");
            Log.d(TAG, "len:" + len + ", key:" + key);
        }

        @Override
        public void onConfirm(byte[] data, boolean isNonePin2) throws RemoteException {
            Log.d(TAG, "----- onConfirm -----");

            Log.d(TAG,"data:" + BytesUtil.bytes2HexString(data) + ", isNonePin:" + isNonePin2);


            String BCDEncoded = "";
            for (byte eachByte : data
            ) {

                String eachByteHex = BytesUtil.byte2HexString(eachByte);

                BCDEncoded +=
                        Util.hexToAscii(eachByteHex);

                //  BCDEncodedEMV += BCDEMV;
            }
            CardReadOutput _card=new CardReadOutput();
            successResponse.PinProcessConfirm(_card);
        }

        @Override
        public void onCancel() throws RemoteException {
            Log.d(TAG, "----- onCancel -----");

            Log.d(TAG,"Cancel");
        }

        @Override
        public void onError(int error) throws RemoteException {
            Log.d(TAG, "----- onError -----");
              int Error = error;
            Log.d(TAG,"ERROR");
        }
    };

}
