package com.example.bniapos.terminallib.util.emv;

import com.cti.generic.sdk.util.data.BytesUtil;
import com.cti.generic.sdk.util.data.TLVList;

import java.io.Serializable;

/**
 * AMEX parameter.
 */

class AmexParameter implements Serializable, EmvParameter {

    /**
     * Expresspay 1.0
     */
    public final static int EXPRESS_PAY_1 = 0x00;

    /**
     * Expresspay 2.0 Magstripe only
     */
    public final static int EXPRESS_PAY_2_MAG = 0x40;

    /**
     * Expresspay 2.0 Magstripe – Mobile CVM Required
     */
    public final static int EXPRESS_PAY_2_MAG_CVM = 0x48;

    /**
     * Expresspay 2.0 EMV and Magstripe
     */
    public final static int EXPRESS_PAY_2_EMV_MAG = 0x80;

    /**
     * Expresspay Mobile (XPM)
     */
    public final static int EXPRESS_PAY_MOBILE = 0xC0;

    /**
     * Expresspay Mobile (XPM) - Mobile CVM Required
     */
    public final static int EXPRESS_PAY_MOBILE_CVM = 0xC8;

    /**
     * Contactless reader capabilities.
     * <p>
     * Value range: @AmexParameter.ContactlessReaderCapabilities
     * <p>
     * EMV tag: A_TAG_TM_9F6D (9F6D)
     */
    private Integer rfReaderCapabilities;

    /**
     * Enhanced contactless reader capabilities. A buffer of length 4.
     * <p>
     * EMV tag: A_TAG_TM_9F6E (9F6E)
     */
    private byte[] enhancedRfReaderCapabilities;

    /**
     * Terminal contactless transaction limit.
     * <p>
     * If the amount is equal to or exceeds the reader contactless transaction limit, the transaction shall be terminate.
     * <p>
     * EMV tag: A_TAG_TM_TRANS_LIMIT (DF8124)
     */
    private Long rfTransactionLimit;

    /**
     * Terminal CVM required limit.
     * <p>
     * If the amount is equal to or exceeds the CVM required limit, then CVM processing is required.
     * <p>
     * EMV tag: A_TAG_TM_CVM_LIMIT (DF8126)
     */
    private Long rfCvmLimit;

    /**
     * Terminal contactless floor limit.
     * <p>
     * If authorized amount exceeds the floor limit, online authorization is required.
     * <p>
     * EMV tag: A_TAG_TM_FLOOR_LIMIT (DF8123)
     */
    private Long rfFloorLimit;

    /**
     * A TryAgain needed or not.
     * <p>
     * - true: Try again
     * <p>
     * - false: Not try again
     * <p>
     * EMV tag: A_TAG_PREAGAIN (DF8130)
     */
    private Boolean tryAgain;

    public Integer getRfReaderCapabilities() {
        return rfReaderCapabilities;
    }

    public void setRfReaderCapabilities(Integer rfReaderCapabilities) {
        this.rfReaderCapabilities = rfReaderCapabilities;
    }

    public byte[] getEnhancedRfReaderCapabilities() {
        return enhancedRfReaderCapabilities;
    }

    public void setEnhancedRfReaderCapabilities(byte[] enhancedRfReaderCapabilities) {
        this.enhancedRfReaderCapabilities = enhancedRfReaderCapabilities;
    }

    public Long getRfTransactionLimit() {
        return rfTransactionLimit;
    }

    public void setRfTransactionLimit(Long rfTransactionLimit) {
        this.rfTransactionLimit = rfTransactionLimit;
    }

    public long getRfCvmLimit() {
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

    public Boolean isTryAgain() {
        return tryAgain;
    }

    public void setTryAgain(Boolean tryAgain) {
        this.tryAgain = tryAgain;
    }

    /**
     * Return Hex string of TLV list.
     */
    public String pack() throws EmvParameterException {
        TLVList tlvList = new TLVList();
        if (this.rfReaderCapabilities == null) {
            throw new EmvParameterException("Please set valid contactless reader capabilities.");
        }
        tlvList.addTLV(EmvTags.A_TAG_TM_9F6D, new byte[]{this.rfReaderCapabilities.byteValue()});

        if (this.enhancedRfReaderCapabilities == null || this.enhancedRfReaderCapabilities.length != 4) {
            throw new EmvParameterException("Enhanced contactless reader capabilities should be a buffer of length 4.");
        }
        tlvList.addTLV(EmvTags.A_TAG_TM_9F6E, this.enhancedRfReaderCapabilities);

        if (this.tryAgain == null) {
            throw new EmvParameterException("Please set whether to try again or not.");
        }
        tlvList.addTLV(EmvTags.A_TAG_PREAGAIN, new byte[]{(byte) (this.tryAgain ? 1 : 0)});

        // Optional
        if (this.rfTransactionLimit != null) {
            if (this.rfTransactionLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.A_TAG_TM_TRANS_LIMIT, BytesUtil.toBCDAmountBytes(this.rfTransactionLimit));
        }

        // Optional
        if (this.rfCvmLimit != null) {
            if (this.rfCvmLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.A_TAG_TM_CVM_LIMIT, BytesUtil.toBCDAmountBytes(this.rfCvmLimit));
        }

        // Optional
        if (this.rfFloorLimit != null) {
            if (this.rfFloorLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.A_TAG_TM_FLOOR_LIMIT, BytesUtil.toBCDAmountBytes(this.rfFloorLimit));
        }

        return tlvList.toString();
    }

    /**
     * Return Hex string of TLV list. Only packs three limits.
     */
    public String packLimits() throws EmvParameterException {
        TLVList tlvList = new TLVList();
        // Optional
        if (this.rfTransactionLimit != null) {
            if (this.rfTransactionLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.A_TAG_TM_TRANS_LIMIT, BytesUtil.toBCDAmountBytes(this.rfTransactionLimit));
        }

        // Optional
        if (this.rfCvmLimit != null) {
            if (this.rfCvmLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.A_TAG_TM_CVM_LIMIT, BytesUtil.toBCDAmountBytes(this.rfCvmLimit));
        }

        // Optional
        if (this.rfFloorLimit != null) {
            if (this.rfFloorLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.A_TAG_TM_FLOOR_LIMIT, BytesUtil.toBCDAmountBytes(this.rfFloorLimit));
        }

        return tlvList.toString();
    }
}
