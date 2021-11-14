package com.example.bniapos.terminallib.util.emv;

import com.cti.generic.sdk.util.data.BytesUtil;
import com.cti.generic.sdk.util.data.TLVList;

import java.io.Serializable;

/**
 * VISA parameter.
 */

class VisaParameter implements Serializable, EmvParameter {
    VisaParameter() {
        this.transactionProperties = new byte[]{0x26, 0x06, 0x00, 0x00};
    }

    /**
     * Card reader configuration parameters.
     * <p>
     * EMV tag: V_TAG_RD_RCP (DF06)
     */
    private byte[] rcp;

    /**
     * Terminal transaction properties.
     * <p>
     * Default value: [0x26, 0x06, 0x00, 0x00]
     * <p>
     * EMV tag: V_TAG_TM_9F66 (9F66)
     */
    private byte[] transactionProperties;

    /**
     * Amount limit of contactless transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, contactless transaction will be denied.
     * <p>
     * EMV tag: V_TAG_TM_TRANS_LIMIT (DF8124)
     */
    private Long rfTransactionLimit;

    /**
     * Amount limit of contactless CVM in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, cardholder validation is needed.
     * <p>
     * EMV tag: V_TAG_TM_CVM_LIMIT (DF8126)
     */
    private Long rfCvmLimit;

    /**
     * Amount limit of contactless online transaction, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * If exceeds this limit, it will require online and offline transaction is not allowed.
     * <p>
     * EMV tag: V_TAG_TM_FLOOR_LIMIT (DF8123)
     */
    private Long rfFloorLimit;

    public byte[] getRcp() {
        return rcp;
    }

    public void setRcp(byte[] rcp) {
        this.rcp = rcp;
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

        // Optional
        if (this.rcp != null) {
            if (this.rcp.length != 2) {
                throw new EmvParameterException("RCP should be a buffer of length 2.");
            }
            tlvList.addTLV(EmvTags.V_TAG_RD_RCP, this.rcp);
        }

        if (this.transactionProperties == null || this.transactionProperties.length != 4) {
            throw new EmvParameterException("Transaction properties should be a buffer of length 4.");
        }
        tlvList.addTLV(EmvTags.V_TAG_TM_9F66, this.transactionProperties);

        // Optional
        if (this.rfTransactionLimit != null) {
            if (this.rfTransactionLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.V_TAG_TM_TRANS_LIMIT, BytesUtil.toBCDAmountBytes(this.rfTransactionLimit));
        }

        // Optional
        if (this.rfCvmLimit != null) {
            if (this.rfCvmLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.V_TAG_TM_CVM_LIMIT, BytesUtil.toBCDAmountBytes(this.rfCvmLimit));
        }

        // Optional
        if (this.rfFloorLimit != null) {
            if (this.rfFloorLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.V_TAG_TM_FLOOR_LIMIT, BytesUtil.toBCDAmountBytes(this.rfFloorLimit));
        }

        return tlvList.toString();
    }
}
