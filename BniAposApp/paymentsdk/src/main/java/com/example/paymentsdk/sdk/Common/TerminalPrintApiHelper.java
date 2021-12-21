package com.example.paymentsdk.sdk.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;
import java.util.List;

/**
 * Abstract class to Handle Terminal Printing Actions
 */
public abstract class TerminalPrintApiHelper {

    /**
     * Creating Instance of Print Helper Class
     * @param _context
     * @param toast
     * @param dialog
     * @param _successResponse
     */
    public TerminalPrintApiHelper(Context _context, Toast toast, AlertDialog dialog, ISuccessResponse _successResponse) {
    }

    /**
     *
     * @param value
     * @param reciept
     * @param Header
     * @throws RemoteException
     */
    public abstract void execute(String value, PrintReciept reciept, String Header) throws RemoteException;

    /**
     *
     * @param value
     * @param AllLines
     * @param Header
     * @throws RemoteException
     */
    public abstract void executePrint(String value, List<PrintFormat> AllLines, String Header) throws RemoteException;
}
