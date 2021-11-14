package com.example.bniapos.terminallib.util.emv;

import android.os.RemoteException;
import android.util.Log;

import com.cti.generic.sdk.api.EMV;
import com.cti.generic.sdk.util.data.BytesUtil;
import com.cti.generic.sdk.util.transaction.Session;
import com.cti.generic.Terminal.TerminalModels.TransactionConfig;
import com.usdk.apiservice.aidl.emv.ActionFlag;
import com.usdk.apiservice.aidl.emv.CandidateAID;
import com.usdk.apiservice.aidl.emv.EMVData;
import com.usdk.apiservice.aidl.emv.KernelID;

import java.util.List;
import java.util.Map;

/**
 * Emv parameter initializer.
 */

public class EmvParameterInitializer {
    private static final String PURE_ECASH_AID = "A000000333010106";

    private EMV emv;
    private Session session;
    private TransactionConfig transactionConfig;

    private BaseParameter defaultBaseParameter;
    private PbocParameter defaultPbocParameters;
    private VisaParameter defaultVisaParameters;
    private MasterParameter defaultMasterParameters;
    private BaseParameter defaultBasePaypassParameter;

    public EmvParameterInitializer(EMV emv, Session session, TransactionConfig transactionConfig) {
        this.emv = emv;
        this.session = session;
        this.transactionConfig = transactionConfig;

        this.defaultBaseParameter = EmvData.findBaseParameter(EmvData.DEFULT_PARAMETER_KEY);
        this.defaultPbocParameters = EmvData.findPbocParameter(EmvData.DEFULT_PARAMETER_KEY);
        this.defaultVisaParameters = EmvData.findVisaParameter(EmvData.DEFULT_PARAMETER_KEY, null);
        this.defaultMasterParameters = EmvData.findMasterParameter(EmvData.DEFULT_PARAMETER_KEY);
        this.defaultBasePaypassParameter = EmvData.findBaseParameter_PayPass(EmvData.DEFULT_PARAMETER_KEY);
    }

    /**
     * Init emv aids.
     */
    public void initEmvAids() throws RemoteException {
        // clear all the aids in emv kernel
        emv.manageAID(ActionFlag.CLEAR, null, true);

        // add all aids from emv data to emv kernel.
        for (Map.Entry<String, Boolean> entry : EmvData.aids.entrySet()) {
            emv.manageAID(ActionFlag.ADD, entry.getKey(), entry.getValue());
        }
    }
    public static int getAIDPriority(List<CandidateAID> AIDs) {
        int currentPriority = -1;
        int position = 0;
        int selectedPosition = 0;

        for (CandidateAID tAID : AIDs) {
            int tPriority = EmvData.getAIDPriorityIndicator(BytesUtil.byteArray2HexString(tAID.getAID()));
            if (tPriority < currentPriority || currentPriority == -1) {
                currentPriority = tPriority;
                selectedPosition=position;
            }
            position++;
        }
        return selectedPosition;
    }

    /**
     * Set parameters to EMV kernel according to AID, kernel ID and pid.
     */
    public void initEmvParameters(String aid, byte kernelId, String pid, String accountEntryMode) throws RemoteException, EmvParameterException {
        BaseParameter baseParameter = new BaseParameter();
        EmvParameter parameter = null;


        switch (kernelId) {
            case KernelID.EMV:
                // EMV parameters already set.
                baseParameter = getEmvParameter(aid);
                Log.i("Type","EMV");
                break;
            case KernelID.PBOC:
                baseParameter = getEmvParameter(aid);
                // To be compatible with old PBOC card, if card returns 5F34, can still continue the process.
                emv.setTLV(kernelId, EmvTags.DEF_TAG_ALLOW_DUP_ICC_SAMEVALUE, "01");

                parameter = this.getPbocParameter(aid, accountEntryMode);
                ((PbocParameter) parameter).setSupportECash(transactionConfig.iseCashSupported());
                if (((PbocParameter) parameter).getSupportECash() && !transactionConfig.isEcashCardOnlineEnabled()) {
                    if (aid.equals(PURE_ECASH_AID)) {
                        baseParameter.getTerminalCapabilities()[1] &= 0xBF;
                    }
                }
                break;
            case KernelID.MASTER:
                baseParameter = getEmvParameter_Paypass(aid);
                parameter = this.getMasterParameter(aid, accountEntryMode);
                break;
            case KernelID.VISA:
                baseParameter = getEmvParameter(aid); //change with Paywave
                parameter = this.getVisaParameter(pid, aid, accountEntryMode);
                break;
        }

        // Set other specific parameters
//        if (transactionConfig.isPinInputNeeded()) {
//            baseParameter.getTerminalCapabilities()[1] &= 0xF0;
//        }

        emv.setTLVList(kernelId, baseParameter.pack());

        if (parameter == null) {
            // Contactless transaction requires to set parameters.
            if (accountEntryMode.equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
                throw new EmvParameterException("Required parameters for contactless transaction were not found!");
            }
            return;
        }

        emv.setTLVList(kernelId, parameter.pack());
    }

    /**
     * Get base parameters.
     */
    private BaseParameter getEmvParameter(String aid) throws EmvParameterException {
        BaseParameter parameter = EmvData.findBaseParameter(aid);
        if (parameter != null) {
            return parameter;
        }

        if (this.defaultBaseParameter != null) {
            return this.defaultBaseParameter;
        }

        // Error, finish this EMV process and notify user to set parameters.
        throw new EmvParameterException(
                "No EMV parameters partly matched by AID and invalid default EMV parameters.");
    }

    /**
     * Get base parameters.
     */
    private BaseParameter getEmvParameter_Paypass(String aid) throws EmvParameterException {
        BaseParameter parameter = EmvData.findBaseParameter_PayPass(aid);
        if (parameter != null) {
            return parameter;
        }

        if (this.defaultBasePaypassParameter != null) {
            return this.defaultBasePaypassParameter;
        }

        // Error, finish this EMV process and notify user to set parameters.
        throw new EmvParameterException(
                "No EMV parameters partly matched by AID and invalid default EMV parameters.");
    }

    /**
     * Get Pboc parameter by id.
     */
    private PbocParameter getPbocParameter(String aid, String accountEntryMode) throws EmvParameterException {

        PbocParameter parameter = EmvData.findPbocParameter(aid);

        if (parameter == null) {
            if (this.defaultPbocParameters == null) {
                // Error, finish this EMV process and notify user to set parameters.
                throw new EmvParameterException(
                        "No PBOC parameters partly matched by AID and invalid default PBOC parameters.");
            } else {
                // Else default parameters already set when init EMV.
                parameter = this.defaultPbocParameters;
            }
        }

        if (accountEntryMode.equals(Session.ACCOUNT_SERVICE_ENTRY_MODE_CONTACT)) {
            return parameter;
        }

        if (transactionConfig.isRfTransactionAmountLimitCheckNeeded()) {
            if (session.getTransactionAmount() > parameter.getRfTransactionLimit()) {
                throw new EmvParameterException("[EMV_OTHER_INTERFACE]Amount exceeds limit.");
            }
        }
        if (transactionConfig.isRfOnlineForced()) {
            parameter.setRfFloorLimit(0L);
        }

        if (transactionConfig.isRfOnlineForced()) {
            parameter.setRfFloorLimit(0L);
        }

        byte[] transactionProperties = parameter.getTransactionProperties();

        // If use has set rfQPbocSupported, apply its value to transaction properties.
        if (transactionConfig.isRfQPbocSupported() != null) {
            transactionProperties[0] = updateTransactionProperties(transactionProperties[0], EmvData.TRANS_PROP_FLAG_RF_QPBOC,
                    transactionConfig.isRfQPbocSupported());
        }

        // If use has set rfDebitCreditSupported, apply its value to transaction properties.
        if (transactionConfig.isRfDebitCreditSupported() != null) {
            transactionProperties[0] = updateTransactionProperties(transactionProperties[0],
                    EmvData.TRANS_PROP_FLAG_RF_DEBIT_CREDIT,
                    transactionConfig.isRfDebitCreditSupported());
        }

        return parameter;
    }

    /**
     * Set the specified bit of transaction properties.
     */
    private byte updateTransactionProperties(byte firstByteOfTransactionProperties, int flag, boolean value) {
        if (flag == EmvData.TRANS_PROP_FLAG_RF_QPBOC) {
            firstByteOfTransactionProperties = value ? (byte) (firstByteOfTransactionProperties | 0x20) : (byte) (firstByteOfTransactionProperties & 0xDF);

        } else if (flag == EmvData.TRANS_PROP_FLAG_RF_DEBIT_CREDIT) {
            firstByteOfTransactionProperties = value ? (byte) (firstByteOfTransactionProperties | 0x40) : (byte) (firstByteOfTransactionProperties & 0xBF);
        }

        return firstByteOfTransactionProperties;
    }

    /**
     * Get master parameter by aid.
     */
    private MasterParameter getMasterParameter(String aid, String accountEntryMode) throws EmvParameterException {
        if (!accountEntryMode.equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
            // no need to set MasterParameter when contact transaction.
            return null;
        }

        MasterParameter parameter = EmvData.findMasterParameter(aid);

        if (parameter == null) {
            parameter = defaultMasterParameters;
        }

        if (parameter == null) {
            // Error, finish this EMV process and notify user to set parameters.
            throw new EmvParameterException(
                    "No Master parameters partly matched by AID and invalid default Master parameters.");
        }

        if (transactionConfig.isRfOnlineForced()) {
            parameter.setRfFloorLimit(0L);
        }

        return parameter;
    }

    /**
     * Get visa parameter by pid.
     */
    private VisaParameter getVisaParameter(String pid, String aid, String accountEntryMode) throws EmvParameterException {
        if (!accountEntryMode.equals(Session.ACCOUNT_ENTRY_MODE_CONTACTLESS)) {
            // no need to set VisaParameter when contact transaction.
            return null;
        }

        VisaParameter parameter = EmvData.findVisaParameter(pid, aid);

        if (parameter == null) {
            parameter = this.defaultVisaParameters;
        }

        if (parameter == null) {
            // Error, finish this EMV process and notify user to set parameters.
            throw new EmvParameterException(
                    "No VISA parameters matched by PID and invalid default VISA parameters.");
        }

        byte[] transactionProperties = parameter.getTransactionProperties();

        if (transactionConfig.isRfOnlineForced()) {
            parameter.setRfFloorLimit(0L);
        }

        // If use has set rfQPbocSupported, apply its value to transaction properties.
        if (transactionConfig.isRfQPbocSupported() != null) {
            transactionProperties[0] = updateTransactionProperties(transactionProperties[0], EmvData.TRANS_PROP_FLAG_RF_QPBOC,
                    transactionConfig.isRfQPbocSupported());
        }

        // If use has set rfDebitCreditSupported, apply its value to transaction properties.
        if (transactionConfig.isRfDebitCreditSupported() != null) {
            transactionProperties[0] = updateTransactionProperties(transactionProperties[0],
                    EmvData.TRANS_PROP_FLAG_RF_DEBIT_CREDIT,
                    transactionConfig.isRfDebitCreditSupported());
        }

        return parameter;
    }
}
