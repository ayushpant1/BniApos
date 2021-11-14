package com.example.bniapos.terminallib.Common;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.cti.generic.CTIApplication;
import com.cti.generic.Terminal.Constant;
import com.cti.generic.Terminal.TerminalModels.CardReadOutput;
import com.cti.generic.Terminal.util.SingeltonActivity;
import com.cti.generic.sdk.api.PinpadForDUKPT;
import com.cti.generic.sdk.api.PinpadForMKSK;
import com.cti.generic.sdk.util.data.BytesUtil;
import com.cti.generic.sdk.util.pinpad.KeyId;
import com.usdk.apiservice.aidl.pinpad.DESMode;
import com.usdk.apiservice.aidl.pinpad.EncKeyFmt;
import com.usdk.apiservice.aidl.pinpad.KeyAlgorithm;
import com.usdk.apiservice.aidl.pinpad.KeyType;
public class TerminalSecurity {

    public static boolean LoadMasterKey(byte[] MasterKey, byte[] ksnData) {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                PinpadForDUKPT.getInstance().open();
                // Set key algorithm
                PinpadForDUKPT.getInstance().setKeyAlgorithm(KeyAlgorithm.KA_TDEA);
                // Set encrypted key format
                PinpadForDUKPT.getInstance().setEncKeyFormat(EncKeyFmt.ENC_KEY_FMT_NORMAL);
                PinpadForDUKPT.getInstance().format();
                if (PinpadForDUKPT.getInstance().isKeyExist(KeyType.MAIN_KEY)) {
                    PinpadForDUKPT.getInstance().deleteKey(KeyType.MAIN_KEY);
                }
                PinpadForDUKPT.getInstance().loadPlainTextKey(KeyType.MAIN_KEY, KeyId.mainKey, MasterKey);
                // Switch to work mode
                PinpadForDUKPT.getInstance().switchToWorkMode();
                PinpadForDUKPT.getInstance().initDUKPTIkKSN(KeyType.MAIN_KEY, ksnData);
                PinpadForDUKPT.getInstance().close();
            } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                // Open
                PinpadForMKSK.getInstance().open();
                // Set key algorithm
                PinpadForMKSK.getInstance().setKeyAlgorithm(KeyAlgorithm.KA_TDEA);
                // Set encrypted key format
                PinpadForMKSK.getInstance().setEncKeyFormat(EncKeyFmt.ENC_KEY_FMT_NORMAL);
                PinpadForMKSK.getInstance().format();
                if (PinpadForMKSK.getInstance().isKeyExist(KeyType.MAIN_KEY)) {
                 //   PinpadForMKSK.getInstance().deleteKey(KeyType.MAIN_KEY);
                }
                PinpadForMKSK.getInstance().switchToWorkMode();
                PinpadForMKSK.getInstance().loadPlainTextKey(KeyType.MAIN_KEY, KeyId.mainKey, MasterKey);
                PinpadForMKSK.getInstance().close();
            }
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean LoadSessionKey(Context _context,byte[] SessionKey) {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                // Open
                // Toast.makeText(_context, "PK : Open Instance", Toast.LENGTH_LONG).show();
                PinpadForMKSK.getInstance().open();
               // Toast.makeText(_context, "PK : Key Exists", Toast.LENGTH_LONG).show();
//                if (PinpadForMKSK.getInstance().isKeyExist(KeyId.pinKey)) {
//                    Toast.makeText(_context, "PK : Delete Key", Toast.LENGTH_LONG).show();
//                    PinpadForMKSK.getInstance().deleteKey(KeyId.pinKey);
//                }
               // Toast.makeText(_context, "PK : Load Enc Key", Toast.LENGTH_LONG).show();
                PinpadForMKSK.getInstance().loadEncKey(KeyType.PIN_KEY,
                        KeyId.mainKey, KeyId.pinKey, SessionKey, null);
                // Toast.makeText(_context, "PK : Close Instance", Toast.LENGTH_LONG).show();
                PinpadForMKSK.getInstance().close();
            }
            return true;
        } catch (RemoteException e) {
            Toast.makeText(_context, "Inside PK Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
            return false;
        }
    }
    public static boolean LoadDataEncryptionKey(Context _context, byte[] SessionKey) {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
                //Toast.makeText(_context, "DK : Open Instance", Toast.LENGTH_LONG).show();
                PinpadForMKSK.getInstance().open();
                //Toast.makeText(_context, "DK : IS KEY Exist", Toast.LENGTH_LONG).show();
//                if (PinpadForMKSK.getInstance().isKeyExist(KeyId.dekKey)) {
//                    Toast.makeText(_context, "DK : DELETE KEY", Toast.LENGTH_LONG).show();
//                    PinpadForMKSK.getInstance().deleteKey(KeyId.dekKey);
//                }
                //Toast.makeText(_context, "DK : Load ENC KEY", Toast.LENGTH_LONG).show();
                PinpadForMKSK.getInstance().loadEncKey(KeyType.DEK_KEY,
                        KeyId.mainKey, KeyId.dekKey, SessionKey, null);
                //Toast.makeText(_context, "DK : close Instance", Toast.LENGTH_LONG).show();
                PinpadForMKSK.getInstance().close();
            }
            return true;
        } catch (RemoteException e) {
            Toast.makeText(_context, "Inside DK Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }
    public static boolean IsSessionKeyAvailable() {
        boolean retval = false;
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                PinpadForDUKPT.getInstance().open();
                retval = PinpadForDUKPT.getInstance().isKeyExist(KeyId.mainKey);
                PinpadForDUKPT.getInstance().close();
            } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
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
    public static void ClearSessionKey() {
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
    }

    public static String EncryptData(String Data) {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                PinpadForDUKPT.getInstance().open();
                DESMode des = new DESMode();
                byte[] data = PinpadForDUKPT.getInstance().calculateDes
                        (KeyId.dekKey, des, null, BytesUtil.hexString2ByteArray(Data));
                PinpadForDUKPT.getInstance().close();
                // Show message
                return BytesUtil.bytes2HexString(data);
            } else if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_MKSK)) {
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
    public static String CreateAndEncryptSecureData(long Amount, CardReadOutput _CardOutput) {
        //Only Card No and
        String retval = "";
        String ConvertedAmount = BytesUtil.ConvertNumericWithLeadingZeros(Amount,12);
        String ByteLength_Amount = BytesUtil.ConvertNumericWithLeadingZeros(ConvertedAmount.length() / 2, 4);
        retval += ByteLength_Amount+ConvertedAmount;
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
            CTIApplication.getDeviceService().getDeviceManager().updateSystemDatetime(DateTime);
        } catch (Exception ex) {
            Log.d("TerminalDate", ex.getLocalizedMessage());
        }
    }

    public static boolean IsMasterKeyAvailable() {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                return PinpadForDUKPT.getInstance().isKeyExist(KeyId.mainKey);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static String GetCurrentKSN() {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                PinpadForDUKPT.getInstance().open();
                byte[] ksn = PinpadForDUKPT.getInstance().getCurrentKSN(KeyId.mainKey);
                PinpadForDUKPT.getInstance().close();
                return BytesUtil.bytes2HexString(ksn);
            }
        } catch (RemoteException ex) {
            Log.e(Constant.LogKey, ex.getLocalizedMessage());
        }
        return "";
    }
    public static String EncryptTrack2Data(String Track2Data) {
        try {
            if (Constant.PINB_SecurityType.equalsIgnoreCase(Constant.PINB_DUKPT)) {
                PinpadForDUKPT.getInstance().open();
                byte[] trkData = PinpadForDUKPT.getInstance().encryptMagTrack
                        (PinpadForDUKPT.ENCRYPT_MODE_ECB, KeyId.mainKey, Track2Data.getBytes());
                PinpadForDUKPT.getInstance().close();

                // Show message
                return BytesUtil.bytes2HexString(trkData);
            }
        } catch (RemoteException ex) {
            Log.e(Constant.LogKey, ex.getLocalizedMessage());
        }
        return "";
    }

}
