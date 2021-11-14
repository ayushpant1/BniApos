package com.example.bniapos.terminallib.util.emv;


import com.example.bniapos.terminallib.util.data.BytesUtil;
import com.example.bniapos.terminallib.util.data.TLVList;

import java.io.Serializable;

/**
 * PBOC parameter.
 */

class PbocParameter implements Serializable, EmvParameter {
    PbocParameter() {
        this.transactionProperties = new byte[]{0x26, 0x00, 0x00, (byte) 0x80};
        this.isSupportSm = false;
    }

    /**
     * Support E-cash or not.
     * <p>
     * - true: Support
     * <p>
     * - false: Not support
     * <p>
     * EMV tag: C_TAG_TM_9F7A (9F7A)
     */
    private Boolean isSupportECash;

    /**
     * Support SM algorithm or not.
     * <p>
     * - true: Support
     * <p>
     * - false: Not support
     * <p>
     * EMV tag: C_TAG_TM_DF69 (DF69)
     */
    private Boolean isSupportSm;

    /**
     * Amount limit of E-cash transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, it will require online and offline transaction is not allowed.
     * <p>
     * EMV tag: C_TAG_TM_9F7B (9F7B)
     */
    private Long ecLimit;

    /**
     * Terminal transaction properties(TTQ). A buffer of length 4.
     * <p>
     * Default value: [0x26, 0x00, 0x00, 0x80]
     * <p>
     * EMV tag: C_TAG_TM_9F66 (9F66)
     */
    private byte[] transactionProperties;

    /**
     * Amount limit of contactless transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, contactless transaction will be denied.
     * <p>
     * EMV tag: C_TAG_TM_TRANS_LIMIT (DF8124)
     */
    private Long rfTransactionLimit;

    /**
     * Amount limit of contactless CVM in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, cardholder validation is needed.
     * <p>
     * EMV tag: C_TAG_TM_CVM_LIMIT (DF8126)
     */
    private Long rfCvmLimit;

    /**
     * Amount limit of contactless online transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, it will require online and offline transaction is not allowed.
     * <p>
     * EMV tag: C_TAG_TM_FLOOR_LIMIT (DF8123)
     */
    private Long rfFloorLimit;

    public Boolean getSupportECash() {
        return isSupportECash;
    }

    public void setSupportECash(Boolean supportECash) {
        isSupportECash = supportECash;
    }

    public Boolean getSupportSm() {
        return isSupportSm;
    }

    public void setSupportSm(Boolean supportSm) {
        isSupportSm = supportSm;
    }

    public Long getEcLimit() {
        return ecLimit;
    }

    public void setEcLimit(Long ecLimit) {
        this.ecLimit = ecLimit;
    }

    public byte[] getTransactionProperties() {
        return transactionProperties;
    }

    public void setTransactionProperties(byte[] transactionProperties) {
        this.transactionProperties = transactionProperties;
    }

    public Long getRfTransactionLimit() {
        return rfTransactionLimit;
    }

    public void setRfTransactionLimit(Long rfTransactionLimit) {
        this.rfTransactionLimit = rfTransactionLimit;
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

    /**
     * Return Hex string of TLV list.
     */
    public String pack() throws EmvParameterException {
        TLVList tlvList = new TLVList();
        if (this.isSupportECash == null) {
            throw new EmvParameterException("Please set E-Cash flag.");
        }
        tlvList.addTLV(EmvTags.C_TAG_TM_9F7A, new byte[]{(byte) (this.isSupportECash ? 1 : 0)});

        tlvList.addTLV(EmvTags.C_TAG_TM_DF69, new byte[]{(byte) (this.isSupportSm ? 1 : 0)});

        if (this.ecLimit == null || this.ecLimit < 0) {
            throw new EmvParameterException("E-Cash limit should be an integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.C_TAG_TM_9F7B, BytesUtil.toBCDAmountBytes(this.ecLimit));

        if (this.rfTransactionLimit == null || this.rfTransactionLimit < 0) {
            throw new EmvParameterException("Amount should be an integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.C_TAG_TM_TRANS_LIMIT, BytesUtil.toBCDAmountBytes(this.rfTransactionLimit));


        if (this.rfCvmLimit == null || this.rfCvmLimit < 0) {
            throw new EmvParameterException("Amount should be an integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.C_TAG_TM_CVM_LIMIT, BytesUtil.toBCDAmountBytes(this.rfCvmLimit));

        if (this.rfFloorLimit == null || this.rfFloorLimit < 0) {
            throw new EmvParameterException("Amount should be an integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.C_TAG_TM_FLOOR_LIMIT, BytesUtil.toBCDAmountBytes(this.rfFloorLimit));

        if (this.transactionProperties == null || this.transactionProperties.length != 4) {
            throw new EmvParameterException("Transaction properties should be a buffer of length 4.");
        }
        tlvList.addTLV(EmvTags.C_TAG_TM_9F66, this.transactionProperties);

        return tlvList.toString();
    }
}
