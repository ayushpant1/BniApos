package com.example.bniapos.terminallib.util.transaction;

import com.cti.generic.Terminal.Constant;
import com.cti.generic.sdk.util.data.DateUtil;
import com.cti.generic.sdk.util.data.StringUtil;

import java.util.Locale;

/**
 * Session
 */

public class Session {
    public final static int TERMINAL_READER_CAPACITY_MAG = 2;
    public final static int TERMINAL_READER_CAPACITY_CONTACT = 5;
    public final static int TERMINAL_READER_CAPACITY_CONTACTLESS = 6;

    /**
     * Card are - domestic.
     * <p>
     * 卡片所属区域 - 内卡。
     */
    public final static int CARD_AREA_DOMESTIC = 0;

    /**
     * Care area - foreign.
     * <p>
     * 卡片所属区域 - 外卡。
     */
    public final static int CARD_AREA_FOREIGN = 1;

    /**
     * Settle type - Debit, like sale transaction.
     * <p>
     * 结算类型：借记。从卡扣钱，如消费。
     */
    public final static int SETTLE_TYPE_DEBIT = 1;

    /**
     * Settle type - Credit, like refund transaction.
     * <p>
     * 结算类型：贷记。往卡加钱，如退货。
     */
    public final static int SETTLE_TYPE_CREDIT = 2;

    /**
     * Settle type - Other. No need to settle.
     * 其他。不统计。
     */
    public final static int SETTLE_TYPE_OTHER = 0;

    public final static String CUP = "CUP";
    public final static String VIS = "VIS";
    public final static String MCC = "MCC";
    public final static String MAE = "MAE";
    public final static String JCB = "JCB";
    public final static String DCC = "DCC";
    public final static String AMX = "AMX";

    /**
     * Not E-Cash transaction.
     */
    public final static int ECASH_FLAG_NON = 0;

    /**
     * Contact E-Cash transaction.
     */
    public final static int ECASH_FLAG_CONTACT = 1;

    /**
     * Contactless E-Cash transaction.
     */
    public final static int ECASH_FLAG_CONTACTLESS = 2;

    /**
     * 账号输入方式 - 未指明。
     * Account input method - None.
     */
    public final static String ACCOUNT_ENTRY_MODE_NON = "0";

    /**
     * 账号输入方式 - 手工
     * Account input method - Manual.
     */
    public final static String ACCOUNT_ENTRY_MODE_MANUAL = "1";

    /**
     * 账号输入方式 - 磁条卡
     * Account input method - Magcard.
     */
    public final static String ACCOUNT_ENTRY_MODE_MAGCARD = "02";
    //public final static String ACCOUNT_ENTRY_MODE_MAGCARD = "90";

    /**
     * Account input method - Contact card.
     */
    public final static String ACCOUNT_SERVICE_ENTRY_MODE_CONTACT = "05";

    /**
     * 账号输入方式 - 快速 PBOC 借/贷记 CARD_TYPE_IC 卡读入（非接触式）
     * Account input method - Contactless card.
     */
    public final static String ACCOUNT_ENTRY_MODE_CONTACTLESS = Constant.POSENT_Contactless;

    /**
     * Account input method - Pre card.
     */
    public final static String ACCOUNT_ENTRY_MODE_PRE_PAY = "92";

    /**
     * 账号输入方式 - 标准 PBOC 借/贷记 CARD_TYPE_IC 卡读入（非接触式）
     * Account input method - Standard PBOC.
     */
    public final static String ACCOUNT_ENTRY_MODE_PBOC = "98";

    /**
     * The third position of point of service entry mode
     */
    public final static String PIN_ENTRY_MODE_EXIST = "1";

    /**
     * 交易中不包含 PIN。
     * The transaction is not included PIN.
     */
    public final static String PIN_ENTRY_MODE_NOT_EXIST = "2";

    /**
     * 此交易不是 Fallback 的 60.5
     * This transaction is not fallback.
     */
    public final static String IC_CONDITION_NORMAL = "0";

    /**
     * 此交易是 Fallback 的 60.5
     * This transaction is fallback.
     */
    public final static String IC_CONDITION_FALLBACK = "2";

    /**
     * IC卡交易过程中无任何标记，默认值
     * IC card transaction process without any mark, the default value.
     */
    public final static int IC_REMARK_NONE = 0;

    /**
     * 标记IC卡交易过程中生成TC
     * Generated TC during the transaction process for mark IC card.
     */
    public final static int IC_REMARK_TC_GENERATED = 1;

    /**
     * 标记IC卡交易过程中ARPC出错
     * ARPC error during the transaction process for mark IC card.
     */
    public final static int IC_REMARK_ARPC_ERROR = 2;

    /**
     * POS授权
     * POS authorization.
     */
    public final static String AUTHORIZATION_POS = "0";

    /**
     * 电话授权
     * Telephone authorization.
     */
    public final static String AUTHORIZATION_TELEPHONE = "1";

    /**
     * 代授权
     * Agency authorization.
     */
    public final static String AUTHORIZATION_INSTEAD = "2";

    /**
     * International credit card code.
     */
    private String internationalCreditCardCode;

    /**
     * Transaction name.
     */
    private String transactionName;

    /**
     * Values in @CardArea
     */
    private int cardArea = CARD_AREA_DOMESTIC;

    /**
     * Values in @SettleType
     */
    private int settleType = SETTLE_TYPE_DEBIT;

    /**
     * 0 field: message code.
     */
    private String messageTypeCode = "0200";

    /**
     * 2 field: Primary account number
     */
    private String pan;

    /**
     * 3 field: processing code
     */
    private String processingCode = "000000";

    /**
     * 60.1 message type number.
     * <p>
     * 消息类型编码。
     */
    private String messageTypeNumber;

    /**
     * 4 field: Amount transaction
     */
    private long transactionAmount;

    /**
     * Transaction year. (yyyy)
     */
    private String transactionYear;

    /**
     * 12 field: Date local transaction (hhmmss).
     * <p>
     * This will be update when message returned by host has field 12.
     */
    private String transactionTime;


    /**
     * 13 field: Date local transaction (MMDD).
     * <p>
     * This will be update when message returned by host has field 13.
     */
    private String transactionDate;

    /**
     * 11 field: Systems trace audit number
     */
    private String systemTraceAuditNumber;

    /**
     * 14 field: Date expiration
     */
    private String expirationDate;

    /**
     * 15 field: Settle Date
     */
    private String settleDate;

    /**
     * 23 field: Card sequence number.only for CARD_TYPE_IC card transaction.
     */
    private String panSequenceNumber;

    /**
     * Account entry mode.
     * <p>
     * The first 2 bytes in 22 field.
     */
    private String accountEntryMode;

    /**
     * Pin entry mode.
     * <p>
     * The 3rd byte in 22 field.
     */
    private String pinEntryMode = PIN_ENTRY_MODE_NOT_EXIST;

    /**
     * 25 field: Point of service condition code
     */
    private String pointOfServiceConditionCode = "00";

    /**
     * 32 filed: Acquiring institution identification code.
     */
    private String acquiringInstitutionIdCode;

    /**
     * 35 field: Track2 data
     */
    private String track2Data;

    /**
     * 36 field: Track3 data.
     */
    private String track3Data;

    /**
     * 37 field: retrieval reference number.
     */
    private String retrievalReferenceNumber;

    /**
     * 38 field: Auth Code
     */
    private String authCode;

    /**
     * 39 field: Response Code.
     */
    private String responseCode;

    /**
     * CARD_TYPE_IC offline transaction auth response code.
     */
    private String authResponseCode = "";

    /**
     * 48 field: Input mode of the into card
     * <p>
     * 转入卡输入方式。
     */
    private String intoAccountEntryMode;

    /**
     * Into account entry mode;
     * <p>
     * 转入卡 PIN 输入方式。
     */
    private String intoPinEntryMode;

    /**
     * 49 field: Currency code transaction
     */
    private String currencyCode;

    /**
     * 52 field: Pin Block
     */
    private String pinBlock;

    /**
     * Batch number。60.1
     */
    private String batchNumber;

    /**
     * Ecash Balance.
     * <p>
     * Using "Long" in case need to check if balance is set.
     */
    private Long balance;

    /**
     * Remarks for print.
     */
    private String remarks;

    /**
     * Previous retrieval reference number. Field 37.
     * <p>
     * 检索参考号。
     */
    private String originalRetrievalReferenceNumber;

    /**
     * Previous auth code. Field 38.
     */
    private String originalAuthCode;

    /**
     * The reservation number. Used in reservation transaction.
     * <p>
     * 预约号。
     */
    private String reservationNumber;

    /**
     * Previous batch number. 60.3
     */
    private String originalBatchNumber;

    /**
     * Previous voucher number.
     * <p>
     * 原凭证号。
     */
    private String originalVoucherNumber;

    /**
     * voucher number
     */
    private String voucherNumber;
    /**
     * Previous transaction date
     */
    private String originalTransactionDate;

    /**
     * Filed 44 of iso8583.
     */
    private String field44;

    /**
     * Filed 55 of iso8583.
     */
    private String field55;

    /**
     * Filed 62 of iso8583.
     */
    private String field62;

    /**
     * Field 63.2
     */
    private String issuerMessage;
    private String cpuMessage;
    private String acquireMessage;

    /**
     * Is FallBack flag or not, 0: Not fallback; 2: Already fallback
     * Values in @IcConditionCode
     */
    private String icConditionCode = IC_CONDITION_NORMAL;

    private String aid = null;

    /**
     * First currency code. TAG 9F51 in card
     */
    private String firstCurrencyCode;

    /**
     * Second currency code. TAG DF71 in card
     */
    private String secondCurrencyCode;

    /**
     * Values in {@link #eCashFlag}
     */
    private int eCashFlag;

    /**
     * Whether ic card transaction generate transaction certificate(TC).
     * or Remark authenticate response cryptogram(ARPC) error.
     */
    private int icRemark;

    /**
     * 持卡人姓名(Cardholder Name)
     */
    private String cardholderName;

    /**
     * Whether CARD_TYPE_IC card trans successfull.
     */
    private boolean icSuccessfull;

    /**
     * 联机处理是否成功。
     * <p>
     * Online process succeeded or not.
     */
    private boolean onlineProcessSucceeded;

    /**
     * Wheher batch upload.
     */
    private boolean alreadyBatchUpload;

    /**
     * Offline transaction flag.
     */
    private boolean offlineTransaction;

    /**
     *  该QPS交易是否免签名
     *  This  transaction is not need signature
     *
     *  true  免签名
     *  false 需要签名；
     */
    private  boolean QpsNoSignature = false;

    /**
     *  该QPS交易是否免密码
     *  This transaction is not need input PIN
     *
     *  true  免密
     *  false 需要输入密码；
     */
    private  String QpsNoInputPin = "0";

    /**
     *  signature data for print and upload
     */
    private String signatureData;

    /**
     * 交易特征码
     * transaction code for signature
     */
    private String signatureSpecialCode;

    /**
     * signature data upload times
     */
    private int signatureUploadTimes = 0;

    /**
     * telephone No. for signature
     */
    private String signaturePhoneNo;

    /**
     * Is third party calling
     */
    private boolean thirdPartyCalling;

    /**
     * Offline settlement authorization method.
     */
    private String authMethod;

    /**
     * Offline settlement authorization institution code.
     */
    private String authInstCode;

    public Session() {
        String now = DateUtil.getCurrentTime("yyyyMMddHHmmss");

        // set transaction date time.
        setTransactionYear(now.substring(0, 4));
        setTransactionDate(now.substring(4, 8));
        setTransactionTime(now.substring(8, 14));
    }

    public String getInternationalCreditCardCode() {
        return internationalCreditCardCode;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public int getCardArea() {
        return cardArea;
    }

    public int getSettleType() {
        return settleType;
    }

    public String getMessageTypeCode() {
        return messageTypeCode;
    }

    public String getPan() {
        return pan;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public Long getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getSystemTraceAuditNumber() {
        return systemTraceAuditNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getPanSequenceNumber() {
        return panSequenceNumber;
    }

    public String getPointOfServiceEntryMode() {
        return accountEntryMode + pinEntryMode;
    }

    public String getAccountEntryMode() {
        return accountEntryMode;
    }

    public String getPinEntryMode() {
        return pinEntryMode;
    }

    public String getPointOfServiceConditionCode() {
        return pointOfServiceConditionCode;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public String getTrack3Data() {
        return track3Data;
    }


    public String getAuthCode() {
        return authCode;
    }

    public String getIntoAccountEntryMode() {
        return intoAccountEntryMode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getPinBlock() {
        return pinBlock;
    }

    public String getBatchNumber() {
        return batchNumber;
    }



    public Long getBalance() {
        return balance;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTransactionDateTime() {
        return getTransactionYear() + getTransactionDate() + getTransactionTime();
    }

    public String getTransactionYear() {
        return transactionYear;
    }

    public void setTransactionYear(String transactionYear) {
        this.transactionYear = transactionYear;
    }

    public String getOriginalAuthCode() {
        return originalAuthCode;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public String getOriginalBatchNumber() {
        return originalBatchNumber;
    }

    public String getOriginalVoucherNumber() {
        return originalVoucherNumber;
    }

    public String getOriginalTransactionDate() {
        return originalTransactionDate;
    }


    public void setInternationalCreditCardCode(String internationalCreditCardCode) {
        this.internationalCreditCardCode = internationalCreditCardCode;
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public String getAuthInstCode() {
        return authInstCode;
    }

    public void setAuthInstCode(String authInstCode) {
        this.authInstCode = authInstCode;
    }

    public void setCardArea(int cardArea) {
        this.cardArea = cardArea;
    }

    public void setSettleType(int settleType) {
        this.settleType = settleType;
    }

    public void setMessageTypeCode(String messageTypeCode) {
        this.messageTypeCode = messageTypeCode;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public void setTransactionAmount(long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = systemTraceAuditNumber;

        // VoucherNumber is equal to systemTraceAuditNumber
        setVoucherNumber(systemTraceAuditNumber);
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setPanSequenceNumber(String panSequenceNumber) {
        this.panSequenceNumber = panSequenceNumber;
    }

//    public void setPointOfServiceEntryMode(String pointOfServiceEntryMode) {
//        this.pointOfServiceEntryMode = pointOfServiceEntryMode;
//    }

    public void setPointOfServiceConditionCode(String pointOfServiceConditionCode) {
        this.pointOfServiceConditionCode = pointOfServiceConditionCode;
    }

    public void setTrack2Data(String track2Data) {
        this.track2Data = track2Data;
    }

    public void setTrack3Data(String track3Data) {
        this.track3Data = track3Data;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setIntoAccountEntryMode(String intoAccountEntryMode) {
        this.intoAccountEntryMode = intoAccountEntryMode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setPinBlock(String pinBlock) {
        this.pinBlock = pinBlock;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setOriginalAuthCode(String originalAuthCode) {
        this.originalAuthCode = originalAuthCode;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setOriginalBatchNumber(String originalBatchNumber) {
        this.originalBatchNumber = originalBatchNumber;
    }

    public void setOriginalVoucherNumber(String originalVoucherNumber) {
        this.originalVoucherNumber = originalVoucherNumber;
    }

    public void setOriginalTransactionDate(String originalTransactionDate) {
        this.originalTransactionDate = originalTransactionDate;
    }

    public int geteCashFlag() {
        return eCashFlag;
    }

    public void seteCashFlag(int eCashFlag) {
        this.eCashFlag = eCashFlag;
    }

    public void setField44(String field44) {
        this.field44 = field44;
    }

    public String getField44() {

        return field44;
    }

    public void setField55(String field55) {
        this.field55 = field55;
    }

    public String getField55() {

        return field55;
    }

    public void setField62(String field62) {
        this.field62 = field62;
    }

    public String getField62() {
        return field62;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getAcquiringInstitutionIdCode() {
        return acquiringInstitutionIdCode;
    }

    public void setAcquiringInstitutionIdCode(String acquiringInstitutionIdCode) {
        this.acquiringInstitutionIdCode = acquiringInstitutionIdCode;
    }

    public String getOriginalRetrievalReferenceNumber() {
        return originalRetrievalReferenceNumber;
    }

    public void setOriginalRetrievalReferenceNumber(String originalRetrievalReferenceNumber) {
        this.originalRetrievalReferenceNumber = originalRetrievalReferenceNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getIssuerMessage() {
        return issuerMessage;
    }

    public void setIssuerMessage(String issuerMessage) {
        this.issuerMessage = issuerMessage;
    }

    public String getCpuMessage() {
        return cpuMessage;
    }

    public void setCpuMessage(String cpuMessage) {
        this.cpuMessage = cpuMessage;
    }

    public String getAcquireMessage() {
        return acquireMessage;
    }

    public void setAcquireMessage(String acquireMessage) {
        this.acquireMessage = acquireMessage;
    }

    public String getDisplayAmount() {
        return String.format(Locale.getDefault(), "%.2f", this.transactionAmount / 100.0);
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }


    public String getDisplayPan() {
        //Add a space for every four digits
        return StringUtil.formatString(this.pan, "xxxx xxxx xxxx xxxx xxxx", true);
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getIcConditionCode() {
        return icConditionCode;
    }

    public void setIcConditionCode(String icConditionCode) {
        this.icConditionCode = icConditionCode;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setPinEntryMode(String pinEntryMode) {
        this.pinEntryMode = pinEntryMode;
    }

    public void setAccountEntryMode(String accountEntryMode) {
        this.accountEntryMode = accountEntryMode;
    }

    public String getIntoPinEntryMode() {
        return intoPinEntryMode;
    }

    public void setIntoPinEntryMode(String intoPinEntryMode) {
        this.intoPinEntryMode = intoPinEntryMode;
    }

    public String getMessageTypeNumber() {
        return messageTypeNumber;
    }

    public void setMessageTypeNumber(String messageTypeNumber) {
        this.messageTypeNumber = messageTypeNumber;
    }


    public int getIcRemark() {
        return icRemark;
    }

    public void setIcRemark(int icRemark) {
        this.icRemark = icRemark;
    }

    public String getFirstCurrencyCode() {
        return firstCurrencyCode;
    }

    public void setFirstCurrencyCode(String firstCurrencyCode) {
        this.firstCurrencyCode = firstCurrencyCode;
    }

    public String getSecondCurrencyCode() {
        return secondCurrencyCode;
    }

    public void setSecondCurrencyCode(String secondCurrencyCode) {
        this.secondCurrencyCode = secondCurrencyCode;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public boolean isOnlineProcessSucceeded() {
        return onlineProcessSucceeded;
    }

    public void setOnlineProcessSucceeded(boolean onlineProcessSucceeded) {
        this.onlineProcessSucceeded = onlineProcessSucceeded;
    }

    public boolean isAlreadyBatchUpload() {
        return alreadyBatchUpload;
    }

    public void setAlreadyBatchUpload(boolean alreadyBatchUpload) {
        this.alreadyBatchUpload = alreadyBatchUpload;
    }

    public boolean isOfflineTransaction() {
        return offlineTransaction;
    }

    public void setOfflineTransaction(boolean offlineTransaction) {
        this.offlineTransaction = offlineTransaction;
    }

    public boolean isIcSuccessfull() {
        return icSuccessfull;
    }

    public void setIcSuccessfull(boolean icSuccessfull) {
        this.icSuccessfull = icSuccessfull;
    }

    public String getAuthResponseCode() {
        return authResponseCode;
    }

    public void setAuthResponseCode(String authResponseCode) {
        this.authResponseCode = authResponseCode;
    }

    public static int getTerminalReaderCapacityMag() {
        return TERMINAL_READER_CAPACITY_MAG;
    }

    public boolean isQpsNoSignature() {
        return QpsNoSignature;
    }

    public void setQpsNoSignature(boolean qpsNoSignature) {
        QpsNoSignature = qpsNoSignature;
    }

    public String getQpsNoInputPin() {
        return QpsNoInputPin;
    }

    public void setQpsNoInputPin(String qpsNoInputPin) {
        QpsNoInputPin = qpsNoInputPin;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }

    public String getSignatureSpecialCode() {
        return signatureSpecialCode;
    }

    public void setSignatureSpecialCode(String signatureSpecialCode) {
        this.signatureSpecialCode = signatureSpecialCode;
    }

    public int getSignatureUploadTimes() {
        return signatureUploadTimes;
    }

    public void setSignatureUploadTimes(int signatureUploadTimes) {
        this.signatureUploadTimes = signatureUploadTimes;
    }

    public String getSignaturePhoneNo() {
        return signaturePhoneNo;
    }

    public void setSignaturePhoneNo(String signaturePhoneNo) {
        this.signaturePhoneNo = signaturePhoneNo;
    }

    public boolean isThirdPartyCalling() {
        return thirdPartyCalling;
    }

    public void setThirdPartyCalling(boolean thirdPartyCalling) {
        this.thirdPartyCalling = thirdPartyCalling;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }
}
