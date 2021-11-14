package com.example.bniapos.terminallib.util.emv;

import com.cti.generic.sdk.util.data.BytesUtil;
import com.usdk.apiservice.aidl.emv.CAPublicKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EMV data.
 */

public class EmvData {
    public static final String DEFULT_PARAMETER_KEY = "FFFFFFFFFF";

    public static final int TRANS_PROP_FLAG_RF_QPBOC = 6;
    public static final int TRANS_PROP_FLAG_RF_DEBIT_CREDIT = 7;

    public static final int KEY_TYPE_AID = 0;
    public static final int KEY_TYPE_PID = 1;

    /**
     * Public keys. Use RID and index to identify a public key.
     */
    public static List<CAPublicKey> publicKeys =  StaticEmvData.getMockPublicKeys();//new ArrayList<CAPublicKey>();

    /**
     * AIDs that terminal supports.
     * <p>
     * Key is aid, value is whether partly match supported.
     */
    public static Map<String, Boolean> aids =  StaticEmvData.getMockAids();//MockEmvData.getSystemAIDs();//

    /**
     * EMV basic parameters.
     */
    public static Map<String, BaseParameter> baseParameters = StaticEmvData.getMockBaseParameters();// MockEmvData.getSystemBaseParameters();

    public static Map<String, BaseParameter> baseParametersPayWave = StaticEmvData.getMockBaseParameters();// MockEmvData.getSystemBaseParameters();

    public static Map<String, BaseParameter> baseParametersPaypass = StaticEmvData.getMockBaseParameters_Paypass();// MockEmvData.getSystemBaseParameters();

    /**
     * Master parameters.
     * <p>
     * - key: AID. Default master parameters use DEFULT_PARAMETER_KEY.
     * <p>
     * - value: MasterParameter
     */
    public static Map<String, MasterParameter> masterParameters = StaticEmvData.getMockMasterParameters();//Need to get this from server//MockEmvData.getSystemMasterParameters(); //

    /**
     * PBOC parameters.
     * <p>
     * - key: AID. Default PBOC parameters use DEFULT_PARAMETER_KEY.
     * <p>
     * - value: PbocParameter
     */
    public static Map<String, PbocParameter> pbocParameters =  StaticEmvData.getMockPbocParameters();//Need to get this from Server

    /**
     * VISA parameters.
     * <p>
     * - key: PID. Default VISA parameters use DEFULT_PARAMETER_KEY.
     * <p>
     * - value: VisaParameter
     */
    public static Map<String, VisaParameter> visaParameters = StaticEmvData.getMockVisaParameters(); //Need to get this from Server

    /**
     * Reader configuration parameters.
     */
    public static byte[] getRcp(boolean statusCheck, boolean zeroCheck, boolean option, boolean transactionLimitActive, boolean floorLimitActive, boolean cmvLimitActive) {
        byte rcp = 0x00;
        if (statusCheck) {
            rcp = (byte) (rcp | 0x80);
        }

        if (zeroCheck) {
            rcp = (byte) (rcp | 0x40);
        }

        if (option) {
            rcp = (byte) (rcp | 0x20);
        }

        if (transactionLimitActive) {
            rcp = (byte) (rcp | 0x10);
        }

        if (cmvLimitActive) {
            rcp = (byte) (rcp | 0x08);
        }

        if (floorLimitActive) {
            rcp = (byte) (rcp | 0x04);
        }

        return new byte[]{rcp, 0x00};
    }

    /**
     * Find the specified public key from the public key list.
     */
    public static CAPublicKey getPublicKey(byte[] rid, byte index) {
        for (CAPublicKey pubKey : publicKeys) {
            try {
                if(pubKey != null) {
                    if (BytesUtil.bytecmp(pubKey.getRid(), rid, 5) == 0 && (pubKey.getIndex() == index)) {
                        return pubKey;
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static int getAIDPriorityIndicator(String AID) {
        int position = 0;
        for (Map.Entry<String, Boolean> entry : EmvData.aids.entrySet()) {

            if (AID.contains(entry.getKey()))
                break;
            position++;
        }

        return position;
    }

    /**
     * Find EMV parameters by specified key.
     */
    public static <T extends EmvParameter> T findParameter(String targetKey, Map<String, T> parameters, int keyType) {
        if (targetKey == null || targetKey.isEmpty() || parameters == null) {
            return null;
        }

        Set<Map.Entry<String, T>> set = parameters.entrySet();
        Iterator<Map.Entry<String, T>> iterator = set.iterator();

        // the best partly matched key
        String bestMatchedKey = "";

        while (iterator.hasNext()) {
            Map.Entry<String, T> entry = iterator.next();

            String key = entry.getKey().toString();

            // Totally matched
            if (key.equalsIgnoreCase(targetKey)) {
                // deep clone the found parameter
                return deepClone(entry.getValue());
            }

            // default parameter key needs to be completely matched.
            if (targetKey.equals(DEFULT_PARAMETER_KEY)) {
                continue;
            }

            boolean partlyMatchSupported = true;

            // Each AID has a parameter which indicate whether support partly match。
            if (keyType == KEY_TYPE_AID) {
                partlyMatchSupported = aids.containsKey(targetKey)? aids.get(targetKey) : false;
            }
            if (!partlyMatchSupported) {
                continue;
            }

            // Support partly match
            // If aid's length is less than tempKey's, that means they don't match, no need to try partly match.
            if (targetKey.length() > key.length() && targetKey.toLowerCase().startsWith(key.toLowerCase())) {
                // partly match success
                // update the best matched key
                if (bestMatchedKey.length() < key.length()) {
                    bestMatchedKey = key;
                }
            }
        }

        // Partly matched
        if (bestMatchedKey.length() > 0) {
            return deepClone(parameters.get(bestMatchedKey));
        }

        return null;
    }

    /**
     * Gets EMV parameters of specified AID. Supports partly match.
     */
    public static BaseParameter findBaseParameter(String aid) {
        return findParameter(aid, baseParameters, KEY_TYPE_AID);
    }

    /**
     * Gets EMV parameters of specified Paywave AID. Supports partly match.
     */
    public static BaseParameter findBaseParameter_Paywave(String aid) {
        return findParameter(aid, baseParametersPayWave, KEY_TYPE_AID);
    }

    /**
     * Gets EMV parameters of specified Paypass AID. Supports partly match.
     */
    public static BaseParameter findBaseParameter_PayPass(String aid) {
        return findParameter(aid, baseParametersPaypass, KEY_TYPE_AID);
    }

    /**
     * Gets PBOC parameters of specified AID. Supports partly match.
     */
    public static PbocParameter findPbocParameter(String aid) {
        return findParameter(aid, pbocParameters, KEY_TYPE_AID);
    }

    /**
     * Gets Master parameters of specified AID. Supports partly match.
     */
    public static MasterParameter findMasterParameter(String aid) {
        return findParameter(aid, masterParameters, KEY_TYPE_AID);
    }

    /**
     * Gets VISA parameters of specified PID. Supports partly match.
     */
    public static VisaParameter findVisaParameter(String pid, String aid) {
        VisaParameter parameter;
        // Use PID to match first.
        parameter = findParameter(pid, visaParameters, KEY_TYPE_PID);
        if (parameter != null) {
            return parameter;
        }

        // No VISA parameter matched by PID, continue to use AID to match.
        return findParameter(aid, visaParameters, KEY_TYPE_AID);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepClone(T obj) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = null;
        try {
            oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (T) oi.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
