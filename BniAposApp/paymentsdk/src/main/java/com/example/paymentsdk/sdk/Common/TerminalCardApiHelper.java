package com.example.paymentsdk.sdk.Common;

import android.content.Context;
import android.os.RemoteException;

import com.example.paymentsdk.CardReadOutput;
import com.example.paymentsdk.sdk.util.transaction.TransactionConfig;


public abstract class TerminalCardApiHelper implements AutoCloseable {

    public TerminalCardApiHelper(Context _context, ISuccessResponse_Card _successResponse)
    {
    }


    public abstract void publishEMVDataStep1(long Amount, long AdditionalAmount,
                                             CardReadOutput cardOutput, boolean breakEMIFlow,
                                             boolean isPinEntryRequired) throws RemoteException;



    public abstract void ProcessEMVCompletionFlow(String EMVD,String IAC);
    public abstract void inputOnlinePin() throws RemoteException;
    public abstract void byePassOnlinePin() throws RemoteException;
    public abstract  void handleMultipleAppSelection(AIDFile aid) throws RemoteException;

    public abstract void startCardScan(TransactionConfig config, String STAN, boolean isRefundTxn) throws  RemoteException;
    public abstract void startCardProcessor(TransactionConfig config, String BatchNo,String InvoiceNo,
                                   String TxnName,String ProcessingCode);


    @Override
    public abstract void close() throws Exception;
    public abstract void StopProcess();

}
