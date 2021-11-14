package com.example.bniapos.terminallib.util.emv;


import com.example.bniapos.terminallib.util.data.BytesUtil;
import com.example.bniapos.terminallib.util.data.TLVList;

import java.io.Serializable;

/**
 * Base parameter.
 */

class BaseParameter implements Serializable, EmvParameter {
    BaseParameter() {
        this.terminalType = 0x22;
        this.terminalCapabilities = new byte[]{(byte) 0xE0, (byte) 0xF8, (byte) 0xC8};
        this.additionalTerminalCapabilities = new byte[]{(byte) 0x6F, 0x00, (byte) 0xF0, (byte) 0xF0, 0x01};
        this.countryCode = new byte[]{0x03, 0x60};
        this.currencyCode = new byte[]{0x03, 0x60};
        this.defaultTac = BytesUtil.hexString2ByteArray("DC4000A800");
        this.denialTac = BytesUtil.hexString2ByteArray("0010000000");
        this.onlineTac = BytesUtil.hexString2ByteArray("DC4004F800");
//        this.defaultTac = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
//        this.denialTac = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
//        this.onlineTac = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
    }

    /**
     * Terminal type.
     * <p>
     * Default value: 0x22.
     * <p>
     * EMV tag: EMV_TAG_TM_TERMTYPE (9F35)
     */
    private Byte terminalType;

    /**
     * Terminal capabilities. A buffer of length 3.
     * <p>
     * Default value: [0xE0, 0xF1, 0xC8].
     * <p>
     * EMV tag: EMV_TAG_TM_CAP (9F33)
     */
    private byte[] terminalCapabilities;

    /**
     * Additional terminal  capabilities. A buffer of length 5.
     * <p>
     * Default value: [0x6F, 0x00, 0xF0, 0xF0, 0x01]
     * <p>
     * EMV tag: EMV_TAG_TM_CAP_AD (9F40)
     */
    private byte[] additionalTerminalCapabilities;

    /**
     * Country code. A buffer of length 2.
     * <p>
     * Default value: [0x01, 0x56]
     * <p>
     * EMV tag: EMV_TAG_TM_CNTRYCODE (9F1A)
     */
    private byte[] countryCode;

    /**
     * Currency Code. A buffer of length 2.
     * <p>
     * Default value: [0x01, 0x56]
     * <p>
     * EMV tag: EMV_TAG_TM_CURCODE (5F2A)
     */
    private byte[] currencyCode;

    /**
     * Default TAC. A buffer of length 5.
     * <p>
     * Default value: [0x00, 0x00, 0x00, 0x00, 0x00]
     * <p>
     * EMV tag: DEF_TAG_TAC_DEFAULT (DF918110)
     */
    private byte[] defaultTac;

    /**
     * Denial TAC. A buffer of length 5.
     * <p>
     * Default value: [0x00, 0x00, 0x00, 0x00, 0x00]
     * <p>
     * EMV tag: DEF_TAG_TAC_DECLINE (DF918111)
     */
    private byte[] denialTac;

    /**
     * Online TAC. A buffer of length 5.
     * <p>
     * Default value: [0x00, 0x00, 0x00, 0x00, 0x00]
     * <p>
     * EMV tag: DEF_TAG_TAC_ONLINE (DF918112)
     */
    private byte[] onlineTac;

    /**
     * Floor limit amount, in cent. Eg. 1111 represents RMB 11.11.
     * <p>
     * EMV tag: EMV_TAG_TM_FLOORLMT (9F1B)
     */
    private Integer floorLimit;

    /**
     * Biased random selection threshold.
     * <p>
     * If authorized amount higher than biased random selection threshold, the chance of online authorization will be increase.
     * <p>
     * EMV tag: DEF_TAG_RAND_SLT_THRESHOLD (DF91810C)
     */
    private Long randomSelectionThreshold;

    /**
     * Biased random selection target percentage.
     * <p>
     * Value range: [0-99]
     * <p>
     * EMV tag: DEF_TAG_RAND_SLT_PER (DF91810D)
     */
    private Byte randomSelectionPercentage;

    /**
     * Biased random selection max target percentage.
     * <p>
     * Value range: [0-99]
     * <p>
     * EMV tag: DEF_TAG_RAND_SLT_MAXPER (DF91810E)
     */
    private Byte maxRandomSelectionPercentage;

    /**
     * Default terminal DDOL.
     * <p>
     * EMV tag: DDOL (DF918121)
     */
    private byte[] ddol;

    /**
     * Default terminal TDOL.
     * <p>
     * EMV tag: DDOL (DF918122)
     */
    private byte[] tdol;

    /**
     * Default terminal UDOL.
     * <p>
     * EMV tag: DDOL (DF918123)
     */
    private byte[] udol;

    /**
     * Reserved parameters.
     */
    private TLVList otherParameters;

    public Byte getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Byte terminalType) {
        this.terminalType = terminalType;
    }

    public byte[] getTerminalCapabilities() {
        return terminalCapabilities;
    }

    public void setTerminalCapabilities(byte[] terminalCapabilities) {
        this.terminalCapabilities = terminalCapabilities;
    }

    public byte[] getAdditionalTerminalCapabilities() {
        return additionalTerminalCapabilities;
    }

    public void setAdditionalTerminalCapabilities(byte[] additionalTerminalCapabilities) {
        this.additionalTerminalCapabilities = additionalTerminalCapabilities;
    }

    public byte[] getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(byte[] countryCode) {
        this.countryCode = countryCode;
    }

    public byte[] getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(byte[] currencyCode) {
        this.currencyCode = currencyCode;
    }

    public byte[] getDefaultTac() {
        return defaultTac;
    }

    public void setDefaultTac(byte[] defaultTac) {
        this.defaultTac = defaultTac;
    }

    public byte[] getDenialTac() {
        return denialTac;
    }

    public void setDenialTac(byte[] denialTac) {
        this.denialTac = denialTac;
    }

    public byte[] getOnlineTac() {
        return onlineTac;
    }

    public void setOnlineTac(byte[] onlineTac) {
        this.onlineTac = onlineTac;
    }

    public Integer getFloorLimit() {
        return floorLimit;
    }

    public void setFloorLimit(Integer floorLimit) {
        this.floorLimit = floorLimit;
    }

    public Long getRandomSelectionThreshold() {
        return randomSelectionThreshold;
    }

    public void setRandomSelectionThreshold(Long randomSelectionThreshold) {
        this.randomSelectionThreshold = randomSelectionThreshold;
    }

    public Byte getRandomSelectionPercentage() {
        return randomSelectionPercentage;
    }

    public void setRandomSelectionPercentage(Byte randomSelectionPercentage) {
        this.randomSelectionPercentage = randomSelectionPercentage;
    }

    public Byte getMaxRandomSelectionPercentage() {
        return maxRandomSelectionPercentage;
    }

    public void setMaxRandomSelectionPercentage(Byte maxRandomSelectionPercentage) {
        this.maxRandomSelectionPercentage = maxRandomSelectionPercentage;
    }

    public byte[] getDdol() {
        return ddol;
    }

    public void setDdol(byte[] ddol) {
        this.ddol = ddol;
    }

    public byte[] getTdol() {
        return tdol;
    }

    public void setTdol(byte[] tdol) {
        this.tdol = tdol;
    }

    public byte[] getUdol() {
        return udol;
    }

    public void setUdol(byte[] udol) {
        this.udol = udol;
    }

    public TLVList getOtherParameters() {
        return otherParameters;
    }

    public void setOtherParameters(TLVList otherParameters) {
        this.otherParameters = otherParameters;
    }

    /**
     * Return Hex string of TLV list.
     */
    public String pack() throws EmvParameterException {
        TLVList tlvList = new TLVList();
        if (this.terminalType == null) {
            throw new EmvParameterException("Please set valid terminal type.");
        }
        tlvList.addTLV(EmvTags.EMV_TAG_TM_TERMTYPE, new byte[]{this.terminalType});

        if (this.terminalCapabilities == null || this.terminalCapabilities.length != 3) {
            throw new EmvParameterException("Terminal capabilities should be a buffer of length 3.");
        }
        tlvList.addTLV(EmvTags.EMV_TAG_TM_CAP, this.terminalCapabilities);

        if (this.additionalTerminalCapabilities == null || this.additionalTerminalCapabilities.length != 5) {
            throw new EmvParameterException("Additional terminal capabilities should be a buffer of length 5.");
        }
        tlvList.addTLV(EmvTags.EMV_TAG_TM_CAP_AD, this.additionalTerminalCapabilities);

        if (this.countryCode == null || this.countryCode.length != 2) {
            throw new EmvParameterException("Country code should be a buffer of length 2.");
        }
        tlvList.addTLV(EmvTags.EMV_TAG_TM_CNTRYCODE, this.countryCode);

        if (this.currencyCode == null || this.currencyCode.length != 2) {
            throw new EmvParameterException("Currency code should be a buffer of length 2.");
        }
        tlvList.addTLV(EmvTags.EMV_TAG_TM_CURCODE, this.currencyCode);

        if (this.defaultTac == null || this.defaultTac.length != 5) {
            throw new EmvParameterException("Default TAC should be a buffer of length 5.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_TAC_DEFAULT, this.defaultTac);

        if (this.denialTac == null || this.denialTac.length != 5) {
            throw new EmvParameterException("Denial TAC should be a buffer of length 5.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_TAC_DECLINE, this.denialTac);

        if (this.onlineTac == null || this.onlineTac.length != 5) {
            throw new EmvParameterException("Online TAC should be a buffer of length 5.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_TAC_ONLINE, this.onlineTac);

        // Optional
        if (this.floorLimit != null) {
            if (this.floorLimit < 0) {
                throw new EmvParameterException("Amount should be a long integer greater than or equal to 0.");
            }
            tlvList.addTLV(EmvTags.EMV_TAG_TM_FLOORLMT, BytesUtil.toFourByteArray(this.floorLimit));
        }

        if (this.randomSelectionThreshold == null || this.randomSelectionThreshold < 0) {
            throw new EmvParameterException("Invalid random selection threshold. Amount should be a long integer greater than or equal to 0.");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_RAND_SLT_THRESHOLD, BytesUtil.toBCDAmountBytes(this.randomSelectionThreshold));

        if (this.randomSelectionPercentage < 0 || this.randomSelectionPercentage > 99) {
            throw new EmvParameterException("Random selection percentage should between [0-99].");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_RAND_SLT_PER, new byte[]{this.randomSelectionPercentage});

        if (this.maxRandomSelectionPercentage < 0 || this.maxRandomSelectionPercentage > 99) {
            throw new EmvParameterException("Max random selection percentage should between [0-99].");
        }
        tlvList.addTLV(EmvTags.DEF_TAG_RAND_SLT_MAXPER, new byte[]{this.maxRandomSelectionPercentage});

        // Optional
        if (this.ddol != null) {
            tlvList.addTLV(EmvTags.DDOL, this.ddol);
        }

        // Optional
        if (this.tdol != null) {
            tlvList.addTLV(EmvTags.TDOL, this.tdol);
        }

        // Optional
        if (this.udol != null) {
            tlvList.addTLV(EmvTags.UDOL, this.udol);
        }

        // Optional
        if (this.otherParameters != null) {
            byte[] tempBuf = BytesUtil.merge(tlvList.toBinary(), this.otherParameters.toBinary());
            return BytesUtil.bytes2HexString(tempBuf);
        }

        return tlvList.toString();
    }
}
