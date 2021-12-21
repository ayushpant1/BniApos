package com.example.paymentsdk.VerifoneSDK.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import com.example.paymentsdk.R;
import com.example.paymentsdk.VerifoneSDK.util.printer.Format;
import com.example.paymentsdk.VerifoneSDK.util.printer.Printer;
import com.example.paymentsdk.sdk.Common.Constant;
import com.example.paymentsdk.sdk.Common.ISuccessResponse;
import com.example.paymentsdk.sdk.Common.PrintFormat;
import com.example.paymentsdk.sdk.Common.PrintReciept;
import com.example.paymentsdk.sdk.Common.TerminalPrintApiHelper;
import com.vfi.smartpos.deviceservice.aidl.PrinterListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class VFTerminalPrintApiHelper extends TerminalPrintApiHelper {

    private Context context;
    /**
     * Toast.
     */
    private Toast toast;

    /**
     * Alert dialog.
     */
    private AlertDialog dialog;

    Context getContext() {
        return context;
    }

    /**
     * Show toast message.
     */
    void showToast(String message) {
        message = message != null ? message : context.getString(R.string.unknown_error);

        toast.setText(message);
        toast.show();
    }

    /**
     * Show toast message by resource id.
     */
    void showToast(int resId) {
        showToast(context.getString(resId));
    }

    /**
     * Show dialog by resource id.
     */
    void showDialog(int resId, boolean cancelable) {

        dialog.setMessage(context.getString(resId));
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(cancelable);

    }

    void showDialog(String message, boolean cancelable) {
        dialog.setMessage(message);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(cancelable);

    }
    /**
     * Show dialog by resource id.
     */
    void showDialog(DialogInterface.OnDismissListener dismissListener, int resId) {
        dialog.setOnDismissListener(dismissListener);
        showDialog(resId, true);
    }

    void showDialog(DialogInterface.OnDismissListener dismissListener, String message) {
        dialog.setOnDismissListener(dismissListener);
        showDialog(message, true);
    }

    /**
     * Hide dialog.
     */
    void hideDialog() {

        dialog.cancel();
    }

    /**
     * Get status.
     */
    private void getStatus() throws RemoteException {
        // Get status
       Printer.getInstance().getStatus();

        // Show message
        showToast(R.string.succeed);
    }



    public ISuccessResponse successResponse = null;

    private static final int FONT_SIZE_SMALL = 0;
    private static final int FONT_SIZE_NORMAL = 1;
    private static final int FONT_SIZE_LARGE = 2;
    //Context _context,
    public VFTerminalPrintApiHelper(Context _context, Toast toast, AlertDialog dialog, ISuccessResponse _successResponse) {

        super(_context,toast,dialog,_successResponse);
        this.context = _context;
        this.toast = toast;
        this.dialog = dialog;
        this.successResponse = _successResponse;
    }

    public void execute(String value, PrintReciept reciept, String Header) throws RemoteException {
        if (value.equals(getContext().getString(R.string.print))) {
            print(reciept,Header);

        } else if (value.equals(getContext().getString(R.string.get_printer_status))) {
            getStatus();

        }
        // *** YET TO IMPLEMENT ***
//        else if (value.equals(getContext().getString(R.string.feed_paper))) {
//            feedPaper();
//
//        } else if (value.equals(getContext().getString(R.string.print_html5))) {
//            printByHtml5();
//
//        } else if (value.equals(getContext().getString(R.string.print_multi_languages))) {
//            printMultiLanguages();
//        }
    }
    public void executePrint(String value, List<PrintFormat> AllLines, String Header) throws RemoteException {
        if (value.equals(getContext().getString(R.string.print))) {
            printCustom(AllLines,Header);

        } else if (value.equals(getContext().getString(R.string.get_printer_status))) {
            getStatus();

        }
        // *** YET TO IMPLEMENT ***
//        else if (value.equals(getContext().getString(R.string.feed_paper))) {
//            feedPaper();
//
//        } else if (value.equals(getContext().getString(R.string.print_html5))) {
//            printByHtml5();
//
//        } else if (value.equals(getContext().getString(R.string.print_multi_languages))) {
//            printMultiLanguages();
//        }
    }

    private void printCustom(List<PrintFormat> AllLines, String Header) throws RemoteException {
        // Get statue
      // Printer.getInstance().getStatus();

        int CurrentFontLetters =48; //48-> small    32-> medium    24-> large
        // Show dialog
        showDialog(R.string.waiting_for_printing, false);
        setFontSpec(FONT_SIZE_SMALL);
               // Set gray
       Printer.getInstance().setPrnGray(7);        // Print image

        if ( isAssetExists("print_logo.bmp") ) {  // the resouce exists...
            Printer.getInstance().addImage(Format.ALIGN_CENTER, readAssetsFile("print_logo.bmp"));
        }

        for (PrintFormat format:AllLines) {
            if(!format.isText())
            {
                switch (format.getValue().toLowerCase())
                {
                    case"small":
                    {
                        CurrentFontLetters = 48;
                        setFontSpec(FONT_SIZE_SMALL);
                        break;

                    }
                    case"normal":
                    {
                        CurrentFontLetters=32;
                        setFontSpec(FONT_SIZE_NORMAL);
                        break;

                    }
                    case "large":
                    {
                        CurrentFontLetters=24;
                        setFontSpec(FONT_SIZE_LARGE);
                        break;

                    }
                    case "linespace":
                    {
                       Printer.getInstance().feedLine(Integer.valueOf(format.getAlignMode()));

                        break;

                    }
                    case "image":
                    {
                       Printer.getInstance().addImage(Format.ALIGN_CENTER, readAssetsFile(format.getAlignMode()));

                        break;
                    }
                    case "qrcode":
                    {
                        // Print QR code
                       Printer.getInstance().addQrCode(Format.ALIGN_CENTER, 340, 2, format.getAlignMode());

                        break;
                    }
                    case "barcode":
                    {
                        // Print Bar code

                       Printer.getInstance().addBarCode(Format.ALIGN_CENTER, 4,  120, format.getAlignMode());

                     //  Printer.getInstance().addBarCode(AlignMode.CENTER, 340, ECLevel.ECLEVEL_Q, format.getAlignMode());
                        break;
                    }
                    default:
                    {
                        setFontSpec(FONT_SIZE_SMALL);
                        break;
                    }
                }
            }
            if(format.isText())
            {
                switch (format.getAlignMode().toLowerCase()) {

                    case "left": {

                        handleMultiLinePrint(CurrentFontLetters, format.getValue(), Format.ALIGN_LEFT);

                        //Format.ALIGN_CENTER
                       Printer.getInstance().feedLine(0);
                        break;
                    }
                    case "right": {
                        handleMultiLinePrint(CurrentFontLetters, format.getValue(), Format.ALIGN_RIGHT);
                        //Format.ALIGN_RIGHT
                       Printer.getInstance().feedLine(0);
                        break;
                    }
                    case "center": {

                        handleMultiLinePrint(CurrentFontLetters, format.getValue(), Format.ALIGN_CENTER);

                       Printer.getInstance().feedLine(0);
                        break;
                    }
                }
            }
        }

        setFontSpec(FONT_SIZE_SMALL);
        if(Header.length() > 0)
           Printer.getInstance().addText(Format.ALIGN_CENTER, Header);

       Printer.getInstance().addText(Format.ALIGN_CENTER,"Version V-"+ Constant.VER);

        // Print QR code
        //   Printer.getInstance().addQrCode(AlignMode.LEFT, 240, ECLevel.ECLEVEL_Q, "www.landicorp.com");

        // Feed lines
       Printer.getInstance().feedLine(5);


        // Start printing
       Printer.getInstance().start(new PrinterListener.Stub() {

            @Override
            public void onFinish() {
                hideDialog();
                successResponse.processFinish("");
               // showToast(R.string.succeed);
            }

            @Override
            public void onError(int error) {
               // showToast(Printer.getErrorId(error));

                hideDialog();
                successResponse.processFailed("printError : "+context.getString( Printer.getErrorId(error)));


            }
        });
    }
    private void handleMultiLinePrint(int perLineChars,String text,int textAlignMode ) {
        String ExtraPrint = "";
        try {
            int TotalCharsLeftToPrint = text.length();
            int currentPrintIndex = 0;
            do {
                if (TotalCharsLeftToPrint > perLineChars) {

                    String ValueToPrint = text.substring(currentPrintIndex, currentPrintIndex + (perLineChars - 1));
                    Printer.getInstance().addText(textAlignMode, ExtraPrint + ValueToPrint + ExtraPrint);
                    currentPrintIndex = currentPrintIndex + perLineChars;
                    TotalCharsLeftToPrint = TotalCharsLeftToPrint - perLineChars;


                } else {

                    String ValueToPrint = text.substring(currentPrintIndex, currentPrintIndex + TotalCharsLeftToPrint);
                    Printer.getInstance().addText(textAlignMode, ExtraPrint + ValueToPrint + ExtraPrint);
                    TotalCharsLeftToPrint = 0;
                    currentPrintIndex = 0;

                }
            } while (TotalCharsLeftToPrint != 0);
        } catch (Exception ex) {
            Log.e("print Error", ex.getLocalizedMessage());
        }
    }

    /**
     * Print *
     **/
    private void print(PrintReciept receipt,String Header) throws RemoteException {
        // Get statue
        Printer.getInstance().getStatus();

        // Show dialog
        showDialog(R.string.waiting_for_printing, false);

        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(Format.ALIGN_CENTER, Header);
        // Set gray
        Printer.getInstance().setPrnGray(8);

        if ( isAssetExists("print_logo.bmp") ) {  // the resouce exists...
            // Print image
            Printer.getInstance().addImage(Format.ALIGN_CENTER, readAssetsFile("print_logo.bmp"));
        }

        // Print text with normal font size
        setFontSpec(FONT_SIZE_NORMAL);
        //  Printer.getInstance().addText(AlignMode.CENTER, "BCA");
        //  Printer.getInstance().addText(AlignMode.CENTER, "BCA TEST OUTLET");
        //  Printer.getInstance().addText(AlignMode.CENTER, "MOTI NAGAR");
        //  Printer.getInstance().addText(AlignMode.CENTER,"---------------------------------");

        // Print text with small font size
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(Format.ALIGN_LEFT, "Date: " + receipt.getDate() + "     " + "Time: " + receipt.getTime());

        Printer.getInstance().addText(Format.ALIGN_LEFT, "MID: " + receipt.getMMID());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "TID: " + receipt.getMTID());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "MOB./CARD: " + receipt.getMobile());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "BATCH: " + receipt.getBatch());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "PAYMENT ID: " + receipt.getPayID());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "INVOICE NO: " + receipt.getInvoiceNo());
        Printer.getInstance().addText(Format.ALIGN_LEFT, receipt.getSaleType());

        setFontSpec(FONT_SIZE_NORMAL);
        Printer.getInstance().addText(Format.ALIGN_CENTER, receipt.getIssuerType());


        // Print text with small font size
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(Format.ALIGN_LEFT, "AMOUNT:" + receipt.getAmount());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "DISCOUNT:" + receipt.getDiscount());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "ET AMOUNT:" + receipt.getET_Amount());
        setFontSpec(FONT_SIZE_NORMAL);
        Printer.getInstance().addText(Format.ALIGN_CENTER, "---------------------------------");
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(Format.ALIGN_LEFT, "NET AMOUNT:" + receipt.getNet_Amount());
        Printer.getInstance().addText(Format.ALIGN_LEFT, "AUTH CODE:" + receipt.getAuth_Code());
        setFontSpec(FONT_SIZE_NORMAL);
        Printer.getInstance().addText(Format.ALIGN_CENTER, "---------------------------------");


        // Print text with small font size
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(Format.ALIGN_CENTER, receipt.getFooterText1());
        Printer.getInstance().addText(Format.ALIGN_CENTER, receipt.getFooterText2());
        Printer.getInstance().addText(Format.ALIGN_CENTER, receipt.getFooterText3());
        Printer.getInstance().addText(Format.ALIGN_CENTER, receipt.getFooterText4());


        // Print QR code
        //   Printer.getInstance().addQrCode(AlignMode.LEFT, 240, ECLevel.ECLEVEL_Q, "www.landicorp.com");

        // Feed lines
        Printer.getInstance().feedLine(5);

        // Start printing
        Printer.getInstance().start(new PrinterListener.Stub() {

            @Override
            public void onFinish() {


                hideDialog();
                successResponse.processFinish("");
                //showToast(R.string.succeed);

            }

            @Override
            public void onError(int error) {

                hideDialog();
                successResponse.processFailed("printError :" + context.getString(Printer.getErrorId(error)));
                // showToast(context.getString( Printer.getErrorId(error)));


            }
        });
    }

    /**
     * Set font spec.
     */
    private void setFontSpec(int fontSpec) throws RemoteException {
        switch (fontSpec) {

            case FONT_SIZE_SMALL:
                Printer.setCurrentFont(0);

                break;

            case FONT_SIZE_NORMAL:
                Printer.setCurrentFont(1);
                break;

            case FONT_SIZE_LARGE:
                Printer.setCurrentFont(3);
                break;
        }
    }




    // get image buffer from id
    private byte[] getBitmapByte(int id) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BitmapFactory.decodeResource(context.getResources(), id).compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public byte[] image2byte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }
    /**
     * Read the file in the assets directory.
     */
    private byte[] readAssetsFile(String fileName) throws RemoteException {
        InputStream input = null;
        try {
            input = getContext().getAssets().open(fileName);
            byte[] buffer = new byte[input.available()];
            int size = input.read(buffer);
            if (size == -1) {
                throw new RemoteException(getContext().getString(R.string.read_fail));
            }
            return buffer;
        } catch (IOException e) {
            throw new RemoteException(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    showToast(e.getLocalizedMessage());
                }
            }
        }
    }
    private boolean isAssetExists(String pathInAssetsDir){
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            if(null != inputStream ) {
                return true;
            }
        }  catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }




}
