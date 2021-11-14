package com.example.bniapos.terminallib.util.emv;

import com.cti.generic.sdk.util.data.BytesUtil;
import com.cti.generic.sdk.util.data.TLVList;

import java.io.Serializable;

/**
 * Master parameter.
 */

class MasterParameter implements Serializable, EmvParameter {
    MasterParameter() {
        // Support mag card and EMV mode.
        this.mode = MODE_MAG_EMV;

        // Support signature and online PIN
        this.cvmCapReq = 0x60;

        // No CVM
        this.cvmCapNoReq = 0x08;

        // Support signature
        this.cvmCapMagReq = 0x20;

        // No CVM
        this.cvmCapMagNoReq = 0x00;

        this.transactionCurrencyExponent = 0x02;
        this.magCardModeVersion = new byte[]{0x00, 0x01};
        this.balanceFlag = BALANCE_FLAG_UNSUPPORT;
        this.supportRecovery = true;
        this.supportCdv = true;
        this.rfTransactionLimitCdv = 500L;
        this.merchantCustomData = new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        this.transactionCategoryCode = 0x52;

    }

    /**
     * Magnetic stripe card process.
     */
    public final static int MODE_MAG = 0x00;

    /**
     * EMV process.
     */
    public final static int MODE_EMV = 0x01;

    /**
     * Supports magnetic stripe card and EMV process.
     */
    public final static int MODE_MAG_EMV = 0x02;

    /**
     * Not support reading balance.
     */
    public final static int BALANCE_FLAG_UNSUPPORT = 0x00;

    /**
     * Only support reading balance before GAC.
     */
    public final static int BALANCE_FLAG_PRE_GAC = 0x01;

    /**
     * Only support reading balance after GAC.
     */
    public final static int BALANCE_FLAG_POST_GAC = 0x02;

    /**
     * Supports reading balance before and after GAC.
     */
    public final static int BALANCE_FLAG_BOTH = 0x03;

    /**
     * Process mode.
     * <p>
     * Value range: @MasterParameter.MasterProcessMode
     * <p>
     * Default value: MODE_MAG_EMV
     * <p>
     * EMV tag: DEF_TAG_M_TRANS_MODE (DF918201)
     */
    private Integer mode;

    /**
     * Pay Pass balance support indicator.
     * <p>
     * Value range: @MasterParameter.BalanceFlag
     * <p>
     * Default value: BALANCE_FLAG_UNSUPPORT
     * <p>
     * EMV tag: DEF_TAG_M_BALANCE_SUP (DF918202)
     */
    private Integer balanceFlag;

    /**
     * Supports tracks torn transactions and recovery or not.
     * <p>
     * - true: Support.
     * <p>
     * - false: Not support.
     * <p>
     * Default value: true
     * <p>
     * EMV tag: DEF_TAG_M_TORN_TRANS (DF918203)
     */
    private Boolean supportRecovery;

    /**
     * Support verifying cardholder by device or not.
     * <p>
     * - true: Support.
     * <p>
     * - false: Not support.
     * <p>
     * Default value: true
     * <p>
     * EMV tag: DEF_TAG_M_CDV_SUP (DF918204)
     */
    private Boolean supportCdv;

    /**
     * Amount limit of contactless transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, contactless transaction will be denied.
     * <p>
     * EMV tag: M_TAG_TM_TRANS_LIMIT (DF8124)
     */
    private Long rfTransactionLimit;

    /**
     * Amount limit of contactless transaction when supports CDV, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, contactless transaction will be denied.
     * <p>
     * Default value: 500
     * <p>
     * EMV tag: M_TAG_TM_TRANS_LIMIT_CDV (DF8125)
     */
    private Long rfTransactionLimitCdv;

    /**
     * Amount limit of contactless CVM in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, cardholder validation is needed.
     * <p>
     * EMV tag: M_TAG_TM_CVM_LIMIT (DF8126)
     */
    private Long rfCvmLimit;

    /**
     * Amount limit of contactless online transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, it will require online and offline transaction is not allowed.
     * <p>
     * EMV tag: M_TAG_TM_FLOOR_LIMIT (DF8123)
     */
    private Long rfFloorLimit;

    /**
     * CVM capability when current transaction is MChip and transaction requests CVM.
     * <p>
     * Value range: [0x00-0xFF]
     * <p>
     * EMV tag: DEF_TAG_M_REQ_CVM (DF918205)
     */
    private Byte cvmCapReq;

    /**
     * CVM capability when current transaction is MChip and transaction doesn't request CVM.
     * <p>
     * Value range: [0x00-0xFF]
     * <p>
     * EMV tag: DEF_TAG_M_REQ_NOCVM (DF918206)
     */
    private Byte cvmCapNoReq;

    /**
     * CVM capability when current transaction is MStripe and transaction requests CVM.
     * <p>
     * Value range: [0x00-0xFF]
     * <p>
     * EMV tag: DEF_TAG_M_MAG_REQ_CVM (DF918207)
     */
    private Byte cvmCapMagReq;

    /**
     * CVM capability when current transaction is MStripe and transaction doesn't request CVM.
     * <p>
     * Value range: [0x00-0xFF]
     * <p>
     * EMV tag: DEF_TAG_M_MAG_REQ_NOCVM (DF918208)
     */
    private Byte cvmCapMagNoReq;

    /**
     * Transaction Currency Exponent
     * <p>
     * Default value: 0x02
     * <p>
     * EMV tag: EMV_TAG_TM_CUREXP (5F36)
     */
    private Byte transactionCurrencyExponent;

    /**
     * Merchant custom data that may be requested by the card. A buffer of length 20.
     * <p>
     * Default value: 20 bytes of 0x01.
     * <p>
     * EMV tag: M_TAG_TM_9F7C (9F7C)
     */
    private byte[] merchantCustomData;

    /**
     * This is a data object defined by MasterCard which indicates the type of transaction being performed, and which may be used in card risk management.
     * <p>
     * Default value: 0x01
     * <p>
     * EMV tag: M_TAG_TM_9F53 (9F53)
     */
    private Byte transactionCategoryCode;

    /**
     * Version number assigned by the payment system for the specific mag-stripe mode functionality of the Kernel. A buffer of length 2.
     * <p>
     * Default value: [0x00, 0x01]
     * <p>
     * EMV tag: M_TAG_TM_9F6D (9F6D)
     */
    private byte[] magCardModeVersion;

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getBalanceFlag() {
        return balanceFlag;
    }

    public void setBalanceFlag(Integer balanceFlag) {
        this.balanceFlag = balanceFlag;
    }

    public Boolean isRecoveryFlag() {
        return supportRecovery;
    }

    public void setSupportRecovery(Boolean supportRecovery) {
        this.supportRecovery = supportRecovery;
    }

    public Boolean isCdvFlag() {
        return supportCdv;
    }

    public void setSupportCdv(Boolean supportCdv) {
        this.supportCdv = supportCdv;
    }

    public Long getRfTransactionLimit() {
        return rfTransactionLimit;
    }

    public void setRfTransactionLimit(Long rfTransactionLimit) {
        this.rfTransactionLimit = rfTransactionLimit;
    }

    public Long getRfTransactionLimitCdv() {
        return rfTransactionLimitCdv;
    }

    public void setRfTransactionLimitCdv(Long rfTransactionLimitCdv) {
        this.rfTransactionLimitCdv = rfTransactionLimitCdv;
    }

    public Long getRfCvmLimit() {
        return rfCvmLimit;
    }

    public void setRfCvmLimit(Long rfCvmLimit) {
        this.rfCvmLimit = rfCvmLimit;
    }

    public Long getRfFloorLimit() {
        return rfFloorLimit;
    }

    public void setRfFloorLimit(Long rfFloorLimit) {
        this.rfFloorLimit = rfFloorLimit;
    }

    public Byte getCvmCapReq() {
        return cvmCapReq;
    }

    public void setCvmCapReq(Byte cvmCapReq) {
        this.cvmCapReq = cvmCapReq;
    }

    public Byte getCvmCapNoReq() {
        return cvmCapNoReq;
    }

    public void setCvmCapNoReq(Byte cvmCapNoReq) {
        this.cvmCapNoReq = cvmCapNoReq;
    }

    public Byte getCvmCapMagReq() {
        return cvmCapMagReq;
    }

    public void setCvmCapMagReq(Byte cvmCapMagReq) {
        this.cvmCapMagReq = cvmCapMagReq;
    }

    public Byte getCvmCapMagNoReq() {
        return cvmCapMagNoReq;
    }

    public void setCvmCapMagNoReq(Byte cvmCapMagNoReq) {
        this.cvmCapMagNoReq = cvmCapMagNoReq;
    }

    public Byte getTransactionCurrencyExponent() {
        return transactionCurrencyExponent;
    }

    public void setTransactionCurrencyExponent(Byte transactionCurrencyExponent) {
        this.transactionCurrencyExponent = transactionCurrencyExponent;
    }

    public byte[] getMerchantCustomData() {
        return merchantCustomData;
    }

    public void setMerchantCustomData(byte[] merchantCustomData) {
        this.merchantCustomData = merchantCustomData;
    }

    public Byte getTransactionCategoryCode() {
        return transactionCategoryCode;
    }

    public void setTransactionCategoryCode(Byte transactionCategoryCode) {
        this.transactionCategoryCode = transactionCategoryCode;
    }

    public byte[] getMagCardModeVersion() {
        return magCardModeVersion;
    }

    public void setMagCardModeVersion(byte[] magCardModeVersion) {
        this.magCardModeVersion = magCardModeVersion;
    }

    /**
     * Return Hex string of TLV list.
     */
    public String pack() throws EmvParameterException {
        TLVList tlvList = new TLVList();
        if (this.mode == null) {
            throw new EmvParameterException("Please set a valid process mode.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_TRANS_MODE, new byte[]{this.mode.byteValue()});

        if (this.balanceFlag == null) {
            throw new EmvParameterException("Please set balance flag.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_BALANCE_SUP, new byte[]{this.balanceFlag.byteValue()});

        if (this.supportRecovery == null) {
            throw new EmvParameterException("Please set recovery flag: support recovery or not.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_TORN_TRANS, new byte[]{(byte) (this.supportRecovery ? 1 : 0)});

        if (this.supportCdv == null) {
            throw new EmvParameterException("Please set CDV flag.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_CDV_SUP, new byte[]{(byte) (this.supportCdv ? 1 : 0)});

        if (this.rfTransactionLimit == null || this.rfTransactionLimit < 0) {
            throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_TRANS_LIMIT, BytesUtil.toBCDAmountBytes(this.rfTransactionLimit));

        if (this.rfTransactionLimitCdv == null || this.rfTransactionLimitCdv < 0) {
            throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_TRANS_LIMIT_CDV, BytesUtil.toBCDAmountBytes(this.rfTransactionLimitCdv));

        if (this.rfCvmLimit == null || this.rfCvmLimit < 0) {
            throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_CVM_LIMIT, BytesUtil.toBCDAmountBytes(this.rfCvmLimit));

        if (this.rfFloorLimit == null || this.rfFloorLimit < 0) {
            throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_FLOOR_LIMIT, BytesUtil.toBCDAmountBytes(this.rfFloorLimit));

        if (this.cvmCapReq == null) {
            throw new EmvParameterException("Please set valid CVM capability.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_REQ_CVM, new byte[]{this.cvmCapReq});

        if (this.cvmCapNoReq == null) {
            throw new EmvParameterException("Please set valid CVM capability when current transaction is MChip and transaction doesn't request CVM.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_REQ_NOCVM, new byte[]{this.cvmCapNoReq});

        if (this.cvmCapMagReq == null) {
            throw new EmvParameterException("Please set valid CVM capability when current transaction is MStripe and transaction requests CVM.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_MAG_REQ_CVM, new byte[]{this.cvmCapMagReq});

        if (this.cvmCapMagNoReq == null) {
            throw new EmvParameterException("Please set valid CVM capability when current transaction is MStripe and transaction doesn't request CVM.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_M_MAG_REQ_NOCVM, new byte[]{this.cvmCapMagNoReq});

        if (this.transactionCurrencyExponent == null) {
            throw new EmvParameterException("Please set transaction currency exponent.");
        }
        tlvList.addTLV(EmvTags.EMV_TAG_TM_CUREXP, new byte[]{this.transactionCurrencyExponent});

        if (this.merchantCustomData == null || this.merchantCustomData.length != 20) {
            throw new EmvParameterException("Merchant custom data should be a buffer of length 20.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_9F7C, this.merchantCustomData);

        if (this.transactionCategoryCode == null) {
            throw new EmvParameterException("Merchant custom data should be a buffer of length 20.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_9F53, new byte[]{this.transactionCategoryCode});

        if (this.magCardModeVersion == null || this.magCardModeVersion.length != 2) {
            throw new EmvParameterException("Mag card mode version should be a buffer of length 2.");
        }
        tlvList.addTLV(EmvTags.M_TAG_TM_9F6D, this.magCardModeVersion);

        return tlvList.toString();
    }
}
