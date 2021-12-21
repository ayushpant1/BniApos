package com.example.paymentsdk.sdk.Common;

import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.CardReadOutput;
import com.example.paymentsdk.LandiSDK.api.PinpadForMKSK;
import com.example.paymentsdk.LandiSDK.util.pinpad.KeyId;
import com.example.paymentsdk.sdk.util.data.BytesUtil;
import com.usdk.apiservice.aidl.pinpad.DESMode;
import com.usdk.apiservice.aidl.pinpad.EncKeyFmt;
import com.usdk.apiservice.aidl.pinpad.KeyAlgorithm;
import com.usdk.apiservice.aidl.pinpad.KeyType;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;
import com.vfi.smartpos.deviceservice.aidl.PinpadKeyType;

public class TerminalSecurity {
    public static final int mainKeyId = 97; //Master Key ID

    public static final int workKeyId = 1; //Session Key ID

    public static final int dataKeyId = 2; //Data Encrytpion Key ID

    public static boolean LoadMasterKey(byte[] MasterKey, byte[] checkValue) {
        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                try {
                    PinpadForMKSK.getInstance().open();
                    // Set key algorithm
                    PinpadForMKSK.getInstance().setKeyAlgorithm(KeyAlgorithm.KA_TDEA);
                    // Set encrypted key format
                    PinpadForMKSK.getInstance().setEncKeyFormat(EncKeyFmt.ENC_KEY_FMT_NORMAL);
                    PinpadForMKSK.getInstance().format();
                    if (PinpadForMKSK.getInstance().isKeyExist(KeyType.MAIN_KEY)) {
                        PinpadForMKSK.getInstance().deleteKey(KeyType.MAIN_KEY);
                    }

                    PinpadForMKSK.getInstance().switchToWorkMode();
                    PinpadForMKSK.getInstance().loadPlainTextKey(KeyType.MAIN_KEY, KeyId.mainKey, MasterKey);
                    PinpadForMKSK.getInstance().close();
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case Constant.Terminal_Verfione: {
                try {
                    IPinpad ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                    boolean bRet = false;
                    byte[] mainKey = MasterKey;// BytesUtil.hexString2ByteArray(MockKey.mainKey); //Get it from server...
                    bRet = ipinpad.loadMainKey(mainKeyId, mainKey, checkValue); //checkValue can be null as well
                    return bRet;

                } catch (RemoteException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        return false;
    }


    public static boolean LoadSessionKey(byte[] SessionKey) {
        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {

                try {

                    if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                        // Open
                        PinpadForMKSK.getInstance().open();
                        if (PinpadForMKSK.getInstance().isKeyExist(KeyId.pinKey))
                            PinpadForMKSK.getInstance().deleteKey(KeyId.pinKey);
                        PinpadForMKSK.getInstance().loadEncKey(KeyType.PIN_KEY,
                                KeyId.mainKey, KeyId.pinKey, SessionKey, null);

//                PinpadForMKSK.getInstance().loadEncKey(KeyType.PIN_KEY,
//                        KeyId.mainKey, KeyId.tdkKey, SessionKey, null);

                        PinpadForMKSK.getInstance().close();
                    }
                    return true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            case Constant.Terminal_Verfione: {
                try {


                    IPinpad ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                    TerminalSecurity.ClearSessionKey(ipinpad);
                    boolean bRet = false;
                    bRet = ipinpad.loadWorkKeyWithDecryptType(PinpadKeyType.PINKEY, mainKeyId, workKeyId, 0,
                            SessionKey, null);

                    return bRet;

                } catch (RemoteException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean LoadDataEncryptionKey(byte[] SessionKey) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            switch (Constant.CurrentTerminal()) {
                case Constant.Terminal_Landi: {
                    try {
                        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                            PinpadForMKSK.getInstance().open();
                            PinpadForMKSK.getInstance().loadEncKey(KeyType.DEK_KEY, KeyId.mainKey, KeyId.dekKey, SessionKey, null);
                            PinpadForMKSK.getInstance().close();
                        }
                        return true;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                case Constant.Terminal_Verfione: {
                    try {


                        IPinpad ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                        TerminalSecurity.ClearSessionKey(ipinpad);
                        boolean bRet = false;
                        bRet = ipinpad.loadWorkKeyWithDecryptType(PinpadKeyType.PINKEY, mainKeyId, workKeyId, 0,
                                SessionKey, null);

                        return bRet;

                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static boolean IsSessionKeyAvailable() {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            switch (Constant.CurrentTerminal()) {
                case Constant.Terminal_Landi: {

                    boolean retval = false;
                    try {
                        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                            PinpadForMKSK.getInstance().open();
                            retval = PinpadForMKSK.getInstance().isKeyExist(KeyId.pinKey);
                            PinpadForMKSK.getInstance().close();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return retval;
                }
                case Constant.Terminal_Verfione: {
                    try {
                        IPinpad ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                        if (ipinpad == null) {
                            ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                        }
                        return ipinpad.isKeyExist(PinpadKeyType.PINKEY, workKeyId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static void ClearSessionKey(IPinpad ipinpad) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            switch (Constant.CurrentTerminal()) {
                case Constant.Terminal_Landi: {

                    try {

                        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                            // Open
                            PinpadForMKSK.getInstance().open();
                            if (PinpadForMKSK.getInstance().isKeyExist(KeyId.pinKey))
                                PinpadForMKSK.getInstance().deleteKey(KeyId.pinKey);

                            if (PinpadForMKSK.getInstance().isKeyExist(KeyId.dekKey))
                                PinpadForMKSK.getInstance().deleteKey(KeyId.dekKey);


                            PinpadForMKSK.getInstance().close();
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();

                    }
                    break;
                }
                case Constant.Terminal_Verfione: {
                    try {
                        if (ipinpad == null) {
                            ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                        }
                        ipinpad.clearKey(PinpadKeyType.PINKEY, workKeyId);

                    } catch (RemoteException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

    public static String EncryptData(String Data) {
        if (Constant.targetDevice.equalsIgnoreCase("terminal")) {
            switch (Constant.CurrentTerminal()) {
                case Constant.Terminal_Landi: {

                    try {
                        if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                            PinpadForMKSK.getInstance().open();
                            DESMode des = new DESMode();
                            byte[] data = PinpadForMKSK.getInstance().calculateDes
                                    (KeyId.dekKey, des, null, BytesUtil.hexString2ByteArray(Data));
                            PinpadForMKSK.getInstance().close();

                            return BytesUtil.bytes2HexString(data);

                        }
                    } catch (RemoteException ex) {
                        Log.e(Constant.LogKey, ex.getLocalizedMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(Constant.LogKey, ex.getLocalizedMessage());
                    }
                    return "";
                }
                case Constant.Terminal_Verfione: {
                    try {
                        IPinpad ipinpad = CTIApplication.getVerifoneDeviceService().getPinpad(1);
                        boolean bRet = false;
                        //    byte[] data = ipinpad.encryptTrackDataWithAlgorithmType(0, dataKeyId,1,
                        //           BytesUtil.hexString2ByteArray(Data),false);

                        byte[] data = ipinpad.calculateByDataKey(dataKeyId, 1, 1, 0, BytesUtil.hexString2ByteArray(Data),
                                null);

                        return BytesUtil.bytes2HexString(data);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return "";
                }
                default: {
                    return "";
                }
            }
        } else return "";
    }

    public static String CreateAndEncryptSecureData(long Amount, CardReadOutput _CardOutput) {
        //Only Card No and
        String retval = "";
        String ConvertedAmount = BytesUtil.ConvertNumericWithLeadingZeros(Amount, 12);
        String ByteLength_Amount = BytesUtil.ConvertNumericWithLeadingZeros(ConvertedAmount.length() / 2, 4);
        retval += ByteLength_Amount + ConvertedAmount;
        if (_CardOutput != null && _CardOutput.getCardNo() != null && _CardOutput.getCardNo().length() > 0) {
            String CardNo = _CardOutput.getCardNo();
            if (CardNo.length() % 2 != 0) {
                CardNo = "0" + CardNo; //extra Padding to make length in byte length 2.
            }
            String ByteLength_Card = BytesUtil.ConvertNumericWithLeadingZeros(CardNo.length() / 2, 4);
            retval += ByteLength_Card + CardNo; //Creating a Length Value of CardNo
        }
        if (_CardOutput != null && _CardOutput.getEMVData() != null && _CardOutput.getEMVData().length() > 0) {
            retval += _CardOutput.getEMVData(); // EMVD Already have ByteLength in start from EMV Processor
        }
        if (_CardOutput != null && _CardOutput.getTrack2Data() != null && _CardOutput.getTrack2Data().length() > 0) {
            String Track2Data = _CardOutput.getTrack2Data().replace("=", "D");
            Track2Data = Track2Data.replace(" ", "");
            if (Track2Data.length() % 2 != 0) {
                Track2Data = "0" + Track2Data; //extra Padding to make length in byte length 2.
            }
            String ByteLength_T2D = BytesUtil.ConvertNumericWithLeadingZeros(Track2Data.length() / 2, 4);
            retval += ByteLength_T2D + Track2Data; //Creating a Length Value of CardNo
        }
        String ByteLength_Total = BytesUtil.ConvertNumericWithLeadingZeros(
                retval.length() / 2, 4);
        retval = ByteLength_Total + retval; //Merging Total Length of DATA
        retval = EncryptData(retval);
        return retval;
    }

    public static void ChangeSystemDateTime(String DateTime) {

        try {
            if (Constant.CurrentTerminal().equalsIgnoreCase(Constant.Terminal_Landi))
                CTIApplication.getDeviceService().getDeviceManager().updateSystemDatetime(DateTime);
        } catch (Exception ex) {
            Log.d("CTIPayment-TerminalDate", ex.getLocalizedMessage());
        }

    }
}
