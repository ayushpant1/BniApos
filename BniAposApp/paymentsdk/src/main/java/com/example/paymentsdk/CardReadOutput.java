package com.example.paymentsdk;



import com.example.paymentsdk.util.AIDFile;

import java.io.Serializable;
import java.util.List;

public class CardReadOutput implements Serializable {
    String CardNo;

    String InsertMode;

    String CardExpiry;

    String CardAID;

    String CardAppName;

    String CustomerName;

    String CurrencyCode;

    String TransactionCount;

    String EMVData;

    String PANSEQ;

    String TVRData;

    String TSIData; //Transaction Status Information

    String RawEMVData;

    String Track2Data;

    String TransactionCategoryCode;

    String TransactionCertificate;

    String STAN;

    String InsertModeCode;

    String PinType;

    String RID;

    String PubKIndex;

    boolean pubKeyExist;

    String PinVerificationResult;


    List<AIDFile> AllAIDs;

    String TerminalCapability;
    String AdditionalTerminalCapability;
    String TxnDate;
    String TxnAmount;
    String OtherAmount;
    String TACDenial;
    String TACOnline;

    String TACDefault;
    String IACDenial;
    String IACOnline;
    String IACDefault;
    String ApplicationInterchangeProfile;
    String CardHolderVerificationMethod;
    String IssuerApplicationData;
    String TxnCategoryCode;
    String UnpredicatableNumber;


    String RFU1;



    public String getRFU1() {
        return RFU1;
    }

    public void setRFU1(String RFU1) {
        this.RFU1 = RFU1;
    }


    public String getTerminalCapability() {
        return TerminalCapability;
    }

    public void setTerminalCapability(String terminalCapability) {
        TerminalCapability = terminalCapability;
    }

    public String getAdditionalTerminalCapability() {
        return AdditionalTerminalCapability;
    }

    public void setAdditionalTerminalCapability(String additionalTerminalCapability) {
        AdditionalTerminalCapability = additionalTerminalCapability;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String txnDate) {
        TxnDate = txnDate;
    }

    public String getTxnAmount() {
        return TxnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        TxnAmount = txnAmount;
    }

    public String getOtherAmount() {
        return OtherAmount;
    }

    public void setOtherAmount(String otherAmount) {
        OtherAmount = otherAmount;
    }

    public String getTACDenial() {
        return TACDenial;
    }

    public void setTACDenial(String TACDenial) {
        this.TACDenial = TACDenial;
    }

    public String getTACOnline() {
        return TACOnline;
    }

    public void setTACOnline(String TACOnline) {
        this.TACOnline = TACOnline;
    }

    public String getTACDefault() {
        return TACDefault;
    }

    public void setTACDefault(String TACDefault) {
        this.TACDefault = TACDefault;
    }

    public String getIACDenial() {
        return IACDenial;
    }

    public void setIACDenial(String IACDenial) {
        this.IACDenial = IACDenial;
    }

    public String getIACOnline() {
        return IACOnline;
    }

    public void setIACOnline(String IACOnline) {
        this.IACOnline = IACOnline;
    }

    public String getIACDefault() {
        return IACDefault;
    }

    public void setIACDefault(String IACDefault) {
        this.IACDefault = IACDefault;
    }

    public String getApplicationInterchangeProfile() {
        return ApplicationInterchangeProfile;
    }

    public void setApplicationInterchangeProfile(String applicationInterchangeProfile) {
        ApplicationInterchangeProfile = applicationInterchangeProfile;
    }

    public String getCardHolderVerificationMethod() {
        return CardHolderVerificationMethod;
    }

    public void setCardHolderVerificationMethod(String cardHolderVerificationMethod) {
        CardHolderVerificationMethod = cardHolderVerificationMethod;
    }

    public String getIssuerApplicationData() {
        return IssuerApplicationData;
    }

    public void setIssuerApplicationData(String issuerApplicationData) {
        IssuerApplicationData = issuerApplicationData;
    }

    public String getTxnCategoryCode() {
        return TxnCategoryCode;
    }

    public void setTxnCategoryCode(String txnCategoryCode) {
        TxnCategoryCode = txnCategoryCode;
    }

    public String getUnpredicatableNumber() {
        return UnpredicatableNumber;
    }

    public void setUnpredicatableNumber(String unpredicatableNumber) {
        UnpredicatableNumber = unpredicatableNumber;
    }


    public List<AIDFile> getAllAIDs() {
        return AllAIDs;
    }

    public void setAllAIDs(List<AIDFile> allAIDs) {
        AllAIDs = allAIDs;
    }

    public String getPinVerificationResult() {
        return PinVerificationResult;
    }

    public void setPinVerificationResult(String pinVerificationResult) {
        PinVerificationResult = pinVerificationResult;
    }


    public boolean isPubKeyExist() {
        return pubKeyExist;
    }

    public void setPubKeyExist(boolean pubKeyExist) {
        this.pubKeyExist = pubKeyExist;
    }


    public String getPinType() {
        return PinType;
    }

    public void setPinType(String pinType) {
        PinType = pinType;
    }

    public String getRID() {
        return RID;
    }

    public void setRID(String RID) {
        this.RID = RID;
    }

    public String getPubKIndex() {
        return PubKIndex;
    }

    public void setPubKIndex(String pubKIndex) {
        PubKIndex = pubKIndex;
    }


    public String getPANSEQ() {
        return PANSEQ;
    }

    public void setPANSEQ(String PANSEQ) {
        this.PANSEQ = PANSEQ;
    }




    public String getMaskedCardNo() {
        return MaskedCardNo;
    }

    public void setMaskedCardNo(String maskedCardNo) {
        MaskedCardNo = maskedCardNo;
    }

    String MaskedCardNo;

    public String getPINBlock() {
        return PINBlock;
    }

    public void setPINBlock(String PINBlock) {
        this.PINBlock = PINBlock;
    }

    String PINBlock;

    public String getInsertModeCode() {
        return InsertModeCode;
    }

    public void setInsertModeCode(String insertModeCode) {
        InsertModeCode = insertModeCode;
    }


    public String getSTAN() {
        return STAN;
    }

    public void setSTAN(String STAN) {
        this.STAN = STAN;
    }

    public String getTransactionCertificate() {
        return TransactionCertificate;
    }

    public void setTransactionCertificate(String transactionCertificate) {
        TransactionCertificate = transactionCertificate;
    }

    public String getTVRData() {
        return TVRData;
    }

    public void setTVRData(String TVRData) {
        this.TVRData = TVRData;
    }

    public String getEMVData() {
        return EMVData;
    }

    public void setEMVData(String EMVData) {
        this.EMVData = EMVData;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getTransactionCount() {
        return TransactionCount;
    }

    public void setTransactionCount(String transactionCount) {
        TransactionCount = transactionCount;
    }

    public String getTSIData() {
        return TSIData;
    }

    public void setTSIData(String TSIData) {
        this.TSIData = TSIData;
    }
    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getInsertMode() {
        return InsertMode;
    }

    public void setInsertMode(String insertMode) {
        InsertMode = insertMode;
    }

    public String getCardExpiry() {
        return CardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        CardExpiry = cardExpiry;
    }

    public String getCardAID() {
        return CardAID;
    }

    public void setCardAID(String cardAID) {
        CardAID = cardAID;
    }

    public String getCardAppName() {
        return CardAppName;
    }

    public void setCardAppName(String cardAppName) {
        CardAppName = cardAppName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getRawEMVData() {
        return RawEMVData;
    }

    public void setRawEMVData(String rawEMVData) {
        RawEMVData = rawEMVData;
    }

    public String getTrack2Data() {
        return Track2Data;
    }

    public void setTrack2Data(String track2Data) {
        Track2Data = track2Data;
    }

    public String getTransactionCategoryCode() {
        return TransactionCategoryCode;
    }

    public void setTransactionCategoryCode(String transactionCategoryCode) {
        TransactionCategoryCode = transactionCategoryCode;
    }
}
