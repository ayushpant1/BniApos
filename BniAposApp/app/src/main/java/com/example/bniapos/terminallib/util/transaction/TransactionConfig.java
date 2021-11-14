package com.example.bniapos.terminallib.util.transaction;

import java.util.Date;

/**
 * Transaction load to config a transaction capacity.
 */

public class TransactionConfig {

    /**
     * GAC flag, None.
     */
    public final static int GAC_NONE = 0;

    /**
     * GAC flag,force online,valid for onlineable terminal.
     */
    public final static int GAC_FORCE_ONLINE = 1;

    /**
     * GAC flag,force offline,valid for offlineable terminal.
     */
    public final static int GAC_FORCE_OFFLINE = 2;

    /**
     * GAC flag,force denial,valid for all terminal.
     */
    public final static int GAC_FORCE_DENIAL = 3;

    /**
     * Process purpose,normal transaction
     */
    public final static int TRANSACTION_TYPE_FULL = 0;

    /**
     * Simple EMV transaction flow.
     */
    public final static int TRANSACTION_TYPE_SIMPLE = 1;

    /**
     * Process purpose,inquire EC logs.
     */
    public final static int TRANSACTION_TYPE_ECLOG = 2;

    /**
     * Process purpose,inquire CARD_TYPE_IC logs.
     */
    public final static int TRANSACTION_TYPE_ICLOG = 3;

    /**
     * Process purpose,flash card recovery.
     */
    public final static int TRANSACTION_TYPE_RECOVERY = 4;

    /**
     * Process purpose,inquire balance.
     */
    public final static int TRANSACTION_TYPE_BALANCE = 5;

    /**
     * Disable eCash card online.
     */
    private boolean ecashCardOnlineEnabled;

    /**
     * Whether support the use of magnetic stripe card.
     */
    private boolean magCardSupported;

    /**
     * Whether support manually input card No.
     */
    private boolean manuallyInputSupported;

    /**
     * Whether support the contact CARD_TYPE_IC card.
     */
    private boolean contactIcCardSupported;

    /**
     * Whether support radio frequency card.
     */
    private boolean rfCardSupported = true;

    /**
     * Whether forbid pure e-cash transaction online process.
     */
    private boolean eCashOnlineForbidden = true;

    /**
     * Whether exist online process proc.
     */
    private boolean onlineNeeded = true;

    /**
     * Whether need to input pin.
     */
    private boolean pinInputNeeded = true;



    /**
     * The minimum length of the pin.
     */
    private long minPinLength = 4;

    /**
     * The maximum length of the pin.
     */
    private long maxPinLength = 12;

    /**
     * The length rule of the pin
     */
    private byte[] pinRule = new byte[]{0, 6, 7, 8, 9, 10, 11, 12};

    /**
     * The time limit of input pin.
     */
    private int pinTimeout = 60;
    /**
     * Transaction amount.
     */
    private long amount;

    /**
     * Transaction date.
     */
    private Date transactionDate;

    /**
     * EMV transaction code, take 2 byte from the process code of request entity.
     */
    private String transactionCode;

    /**
     * Transaction certificate.
     */
    private String certificate;

    /**
     * Rf card qpboc supported.
     * When user set value to this(not null), it will apply to terminal transaction properties(TTQ).
     */
    private Boolean rfQPbocSupported;

    /**
     * Rf card pay pass enabled.
     */
    private boolean rfPayPassSupported;

    /**
     * Process rf card debit credit full process.
     * <p>
     * When user set value to this(not null), it will apply to terminal transaction properties(TTQ).
     */
    private Boolean rfDebitCreditSupported;

    /**
     * GAC flag.
     */
    private int gacFlag = GAC_NONE;

    /**
     * Process purpose.
     */
    private int transactionType = TRANSACTION_TYPE_FULL;

    /**
     * Whether check rfTransLimit.
     */
    private boolean rfTransactionAmountLimitCheckNeeded = true;

    /**
     * Is eCash supported or not.
     */
    private boolean eCashSupported;

    /**
     * Trans result.
     */
    private String transactionResult;

    /**
     *
     * Whether check script result.
     */
    private boolean scriptResultCheckNeeded;

    private boolean rfOnlineForced;

    /**
     *
     * Whether QPS supported.
     */
    private boolean isQPSSupported = false;

    /**
     *
     * If it'not the complete process and need to confirm the card number, so set the parameter as true
     */
    private  boolean isRfwouldStopEvent = false;

    private boolean balanceNeeded;

    public TransactionConfig() {
    }

    public boolean isMagCardSupported() {
        return magCardSupported;
    }

    public void setMagCardSupported(boolean magCardSupported) {
        this.magCardSupported = magCardSupported;
    }

    public boolean isManuallyInputSupported() {
        return manuallyInputSupported;
    }

    public void setManuallyInputSupported(boolean manuallyInputSupported) {
        this.manuallyInputSupported = manuallyInputSupported;
    }

    public boolean isContactIcCardSupported() {
        return contactIcCardSupported;
    }

    public void setContactIcCardSupported(boolean contactIcCardSupported) {
        this.contactIcCardSupported = contactIcCardSupported;
    }

    public boolean isRfCardSupported() {
        return rfCardSupported;
    }

    public void setRfCardSupported(boolean rfCardSupported) {
        this.rfCardSupported = rfCardSupported;
    }

    public boolean iseCashOnlineForbidden() {
        return eCashOnlineForbidden;
    }

    public void seteCashOnlineForbidden(boolean eCashOnlineForbidden) {
        this.eCashOnlineForbidden = eCashOnlineForbidden;
    }

    public boolean isOnlineNeeded() {
        return onlineNeeded;
    }

    public void setOnlineNeeded(boolean onlineNeeded) {
        this.onlineNeeded = onlineNeeded;
    }

    public boolean isPinInputNeeded() {
        return pinInputNeeded;
    }

    public void setPinInputNeeded(boolean pinInputNeeded) {
        this.pinInputNeeded = pinInputNeeded;
    }

    public long getMinPinLength() {
        return minPinLength;
    }

    public void setMinPinLength(long minPinLength) {
        this.minPinLength = minPinLength;
    }

    public long getMaxPinLength() {
        return maxPinLength;
    }

    public void setMaxPinLength(long maxPinLength) {
        this.maxPinLength = maxPinLength;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public Boolean isRfQPbocSupported() {
        return rfQPbocSupported;
    }

    public void setRfQPbocSupported(Boolean rfQPbocSupported) {
        this.rfQPbocSupported = rfQPbocSupported;
    }

    public boolean isRfPayPassSupported() {
        return rfPayPassSupported;
    }

    public void setRfPayPassSupported(boolean rfPayPassSupported) {
        this.rfPayPassSupported = rfPayPassSupported;
    }

    public Boolean isRfDebitCreditSupported() {
        return rfDebitCreditSupported;
    }

    public void setRfDebitCreditSupported(Boolean rfDebitCreditSupported) {
        this.rfDebitCreditSupported = rfDebitCreditSupported;
    }

    public int getGacFlag() {
        return gacFlag;
    }

    public void setGacFlag(int gacFlag) {
        this.gacFlag = gacFlag;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isRfTransactionAmountLimitCheckNeeded() {
        return rfTransactionAmountLimitCheckNeeded;
    }

    public void setRfTransactionAmountLimitCheckNeeded(boolean rfTransactionAmountLimitCheckNeeded) {
        this.rfTransactionAmountLimitCheckNeeded = rfTransactionAmountLimitCheckNeeded;
    }

    public boolean iseCashSupported() {
        return eCashSupported;
    }

    public void seteCashSupported(boolean eCashSupported) {
        this.eCashSupported = eCashSupported;
    }

    public String getTransactionResult() {
        return transactionResult;
    }

    public void setTransactionResult(String transactionResult) {
        this.transactionResult = transactionResult;
    }

    public boolean isScriptResultCheckNeeded() {
        return scriptResultCheckNeeded;
    }

    public void setScriptResultCheckNeeded(boolean scriptResultCheckNeeded) {
        this.scriptResultCheckNeeded = scriptResultCheckNeeded;
    }

    public boolean isRfOnlineForced() {
        return rfOnlineForced;
    }

    public void setRfOnlineForced(boolean rfOnlineForced) {
        this.rfOnlineForced = rfOnlineForced;
    }

    public byte[] getPinRule() {
        return pinRule;
    }

    public void setPinRule(byte[] pinRule) {
        this.pinRule = pinRule;
    }

    public int getPinTimeout() {
        return pinTimeout;
    }

    public void setPinTimeout(int pinTimeout) {
        this.pinTimeout = pinTimeout;
    }

    public boolean isEcashCardOnlineEnabled() {
        return ecashCardOnlineEnabled;
    }

    public void setEcashCardOnlineEnabled(boolean ecashCardOnlineEnabled) {
        this.ecashCardOnlineEnabled = ecashCardOnlineEnabled;
    }

    public boolean isQPSSupported() {
        return isQPSSupported;
    }

    public void setQPSSupported(boolean QPSSupported) {
        isQPSSupported = QPSSupported;
    }

    public boolean isRfwouldStopEvent() {
        return isRfwouldStopEvent;
    }

    public void setRfwouldStopEvent(boolean rfwouldStopEvent) {
        isRfwouldStopEvent = rfwouldStopEvent;
    }

    public boolean isBalanceNeeded() {
        return balanceNeeded;
    }

    public void setBalanceNeeded(boolean balanceNeeded) {
        this.balanceNeeded = balanceNeeded;
    }
}