package com.example.paymentsdk.sdk.Common;

import android.content.Context;
import android.os.RemoteException;


/**
 *
 */
public abstract class TerminalSearchInsertCard implements AutoCloseable {

    /**
     * @param _context
     * @param _successResponse
     */
    public TerminalSearchInsertCard(Context _context, ISuccessResponse _successResponse) {
    }

    /**
     * @param timeout
     * @throws RemoteException
     */
    public abstract void searchCard(int timeout) throws RemoteException;

    /**
     * @throws Exception
     */
    @Override
    public abstract void close() throws Exception;
}
