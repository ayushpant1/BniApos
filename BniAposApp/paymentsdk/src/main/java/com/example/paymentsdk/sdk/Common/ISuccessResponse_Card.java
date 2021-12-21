package com.example.paymentsdk.sdk.Common;


import com.example.paymentsdk.CardReadOutput;

public interface ISuccessResponse_Card {
    void processFinish(CardReadOutput CardOutput);

    void PinProcessConfirm(CardReadOutput output);

    void PinProcessFailed(String Exception);

    void processFailed(String Exception);

    default void processFailed(String Exception, String EntryMode) {

    }

    void Communication(boolean breakEMVConnection);

    void processTimeOut();

    void TransactionApproved();

    void TransactionDeclined();

    default void EMVProcessing() {
    }

    default void CardInserted() {
    }

    default void SelectVerificationOption() {
    }

    default void SelectCardApplication(CardReadOutput CardOutput) {
    }

    default void processSignature() {
    }

    default void processCDCVM() {

    }
}


