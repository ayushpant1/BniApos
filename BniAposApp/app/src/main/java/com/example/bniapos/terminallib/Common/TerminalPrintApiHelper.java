package com.example.bniapos.terminallib.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.bniapos.R;
import com.example.bniapos.terminallib.util.printer.Printer;
import com.usdk.apiservice.aidl.printer.ASCScale;
import com.usdk.apiservice.aidl.printer.ASCSize;
import com.usdk.apiservice.aidl.printer.AlignMode;
import com.usdk.apiservice.aidl.printer.ECLevel;
import com.usdk.apiservice.aidl.printer.HZScale;
import com.usdk.apiservice.aidl.printer.HZSize;
import com.usdk.apiservice.aidl.printer.OnPrintListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TerminalPrintApiHelper {

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
    public TerminalPrintApiHelper(Context _context, Toast toast, AlertDialog dialog, ISuccessResponse _successResponse) {

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
            printCustom(false,AllLines,Header);

        }else if (value.equals(getContext().getString(R.string.get_printer_status))) {
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
    public void executeLogPrint(String value, List<PrintFormat> AllLines, String Header) throws RemoteException {
        if (value.equals(getContext().getString(R.string.print))) {
            printCustom(true,AllLines,Header);
        }
    }

    private void printCustom(boolean isLogPrint, List<PrintFormat> AllLines, String Header) throws RemoteException {
        // Get statue
        Printer.getInstance().getStatus();


        // Show dialog
        showDialog(R.string.waiting_for_printing, false);
        setFontSpec(FONT_SIZE_SMALL);
               // Set gray
        Printer.getInstance().setPrnGray(6);        // Print image

        if(!isLogPrint) {
            if(!Constant.isMultiAcquiringEnabled) {
                if (isAssetExists(Constant.DefaultBank + ".bmp")) {  // the resouce exists...
                    Printer.getInstance().addImage(AlignMode.CENTER, readAssetsFile(Constant.DefaultBank + ".bmp"));
                }
            }
        }


        DividePrintToMultipleCommands(AllLines,0,Header,isLogPrint);


    }
    private void DividePrintToMultipleCommands(List<PrintFormat> TotalLines,int currentCounter,String Header,boolean isLogPrint) throws RemoteException {
        int startSize = 0;
        int endSize = 50;
        if ((TotalLines.size() - currentCounter) > 50) {
            startSize = currentCounter;
            endSize = currentCounter + 50;
        } else {
            startSize = currentCounter;
            endSize = TotalLines.size();
        }
        List<PrintFormat> AllLines = TotalLines.subList(startSize, endSize);
        HandlePrint(AllLines, TotalLines, currentCounter, Header, isLogPrint);
    }
    private void HandlePrint(List<PrintFormat> AllLines,List<PrintFormat> TotalLines,int CurrentCounter, String Header,boolean isLogPrint) throws RemoteException
    {
        int CurrentFontLetters =48; //48-> small    32-> medium    24-> large
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
                    case "logstart": {
                        String FooterText1 = "";
                        if (Header != null && Header.length() > 0)
                            FooterText1 +="-"+Header.toUpperCase()+"-";

                        String FooterText2 = "-VER." + Constant.VER+"-";

                        int Text1 = FooterText1.length();
                        int Text2 = FooterText2.length();

                        int SpaceRequired = 48 - (Text1 + Text2);
                        String SpaceText = "";

                        for (int idx = 0; idx < SpaceRequired; idx++) {
                            SpaceText += " ";
                        }

                        Printer.getInstance().addText(AlignMode.CENTER, FooterText1 + SpaceText + FooterText2);
                        Printer.getInstance().feedLine(2);
                        //isLogPrint = true;
                        break;
                    }
                    case "image":
                    {
                        Printer.getInstance().addImage(AlignMode.CENTER, readAssetsFile(format.getAlignMode()));
                        break;
                    }
                    case "qrcode":
                    {
                        // Print QR code
                        Printer.getInstance().addQrCode(AlignMode.CENTER, 340, ECLevel.ECLEVEL_Q, format.getAlignMode());
                        break;
                    }
                    case "barcode": {
                        // Print Bar code

                        Printer.getInstance().addBarCode(AlignMode.CENTER, 4, 120, format.getAlignMode());

                        //   Printer.getInstance().addBarCode(AlignMode.CENTER, 340, ECLevel.ECLEVEL_Q, format.getAlignMode());
                        break;
                    }
                    case "acquirerimage": {
                        if (isAssetExists(format.getAlignMode())) {  // the resouce exists...
                            Printer.getInstance().addImage(AlignMode.CENTER, readAssetsFile(format.getAlignMode()));
                        }
                    }
                    default:
                    {
                        setFontSpec(FONT_SIZE_SMALL);
                    }
                }
            }
            if(format.isText())
            {
                switch (format.getAlignMode().toLowerCase()) {

                    case "left": {

                        handleMultiLinePrint(CurrentFontLetters, format.getValue(), AlignMode.LEFT);

                        Printer.getInstance().feedLine(0);
                        break;
                    }
                    case "right": {

                        handleMultiLinePrint(CurrentFontLetters, format.getValue(), AlignMode.RIGHT);

                        Printer.getInstance().feedLine(0);
                        break;
                    }
                    case "center": {

                        handleMultiLinePrint(CurrentFontLetters, format.getValue(), AlignMode.CENTER);

                        Printer.getInstance().feedLine(0);

                        break;
                    }
                }
            }
        }

        if ((CurrentCounter + AllLines.size()) >= TotalLines.size()) {

            setFontSpec(FONT_SIZE_SMALL);
            if (!isLogPrint) {
                String FooterText1 = "";
                if (Header != null && Header.length() > 0)
                    FooterText1 += "-" + Header.toUpperCase() + "-";

                String FooterText2 = "-VER." + Constant.VER + "-";

                int Text1 = FooterText1.length();
                int Text2 = FooterText2.length();

                int SpaceRequired = 48 - (Text1 + Text2);
                String SpaceText = "";

                for (int idx = 0; idx < SpaceRequired; idx++) {
                    SpaceText += " ";
                }

                Printer.getInstance().addText(AlignMode.CENTER, FooterText1 + SpaceText + FooterText2);
                Printer.getInstance().feedLine(5);
            } else {
                Printer.getInstance().feedLine(2);
            }
        }
        // Start printing
        Printer.getInstance().start(new OnPrintListener.Stub() {

            @Override
            public void onFinish() {
                if ((CurrentCounter + AllLines.size()) < TotalLines.size()) {

                    try {
                        DividePrintToMultipleCommands(TotalLines, CurrentCounter + AllLines.size(), Header, isLogPrint);
                    }
                    catch (Exception ex)
                    {
                        hideDialog();
                        successResponse.processFailed("printError");
                    }
                } else {

                    hideDialog();
                    successResponse.processFinish("");
                }
                // showToast(R.string.succeed);
            }

            @Override
            public void onError(int error) {


                hideDialog();
                successResponse.processFailed("printError");
                //showToast(Printer.getErrorId(error));

            }
        });
    }
    private void handleMultiLinePrint(int perLineChars,String text,int textAlignMode )
    {
        try {
            int TotalCharsLeftToPrint = text != null ? text.length() : 0;
            int currentPrintIndex = 0;
            do {
                if (TotalCharsLeftToPrint > perLineChars) {

                    String ValueToPrint =text.substring(currentPrintIndex,currentPrintIndex +(perLineChars));
                    Printer.getInstance().addText(textAlignMode, ValueToPrint);
                    currentPrintIndex=currentPrintIndex+perLineChars;
                    TotalCharsLeftToPrint = TotalCharsLeftToPrint-perLineChars;


                } else {

                    String ValueToPrint = text.substring(currentPrintIndex, currentPrintIndex +  TotalCharsLeftToPrint);
                    Printer.getInstance().addText(textAlignMode, ValueToPrint);
                    TotalCharsLeftToPrint = 0;
                    currentPrintIndex = 0;

                }
            } while (TotalCharsLeftToPrint != 0);
        }
        catch (Exception ex)
        {
            Log.e("print Error",ex.getLocalizedMessage());
        }
    }

    /**
     * Print.
     */
    private void print(PrintReciept receipt,String Header) throws RemoteException {
        // Get statue
        Printer.getInstance().getStatus();

        // Show dialog
        showDialog(R.string.waiting_for_printing, false);
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(AlignMode.CENTER, Header);
        // Set gray
        Printer.getInstance().setPrnGray(6);

        // Print image
        if ( isAssetExists("print_logo.bmp") ) {  // the resouce exists...
            Printer.getInstance().addImage(AlignMode.CENTER, readAssetsFile("print_logo.bmp"));
        }


        // Print text with normal font size
        setFontSpec(FONT_SIZE_NORMAL);
     //   Printer.getInstance().addText(AlignMode.CENTER, "BCA");
     //   Printer.getInstance().addText(AlignMode.CENTER, "BCA TEST OUTLET");
     //   Printer.getInstance().addText(AlignMode.CENTER, "MOTI NAGAR");
     //   Printer.getInstance().addText(AlignMode.CENTER,"---------------------------------");

        // Print text with small font size
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(AlignMode.LEFT, "Date: "+ receipt.getDate() +"     "+"Time: "+ receipt.getTime()  );

        Printer.getInstance().addText(AlignMode.LEFT, "MID: "+ receipt.getMMID());
        Printer.getInstance().addText(AlignMode.LEFT, "TID: " + receipt.getMTID());
        Printer.getInstance().addText(AlignMode.LEFT, "MOB./CARD: "+receipt.getMobile());
        Printer.getInstance().addText(AlignMode.LEFT, "BATCH: " + receipt.getBatch());
        Printer.getInstance().addText(AlignMode.LEFT, "PAYMENT ID: " + receipt.getPayID());
        Printer.getInstance().addText(AlignMode.LEFT, "INVOICE NO: " + receipt.getInvoiceNo());
        Printer.getInstance().addText(AlignMode.CENTER,  receipt.getSaleType());

        setFontSpec(FONT_SIZE_NORMAL);
        Printer.getInstance().addText(AlignMode.CENTER, receipt.getIssuerType());


        // Print text with small font size
        setFontSpec(FONT_SIZE_SMALL);

        Printer.getInstance().addText(AlignMode.LEFT, "AMOUNT:"+receipt.getAmount());
        Printer.getInstance().addText(AlignMode.LEFT, "DISCOUNT:"+receipt.getDiscount());
        Printer.getInstance().addText(AlignMode.LEFT, "ET AMOUNT:"+receipt.getET_Amount());
        setFontSpec(FONT_SIZE_NORMAL);
        Printer.getInstance().addText(AlignMode.CENTER,"---------------------------------");
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(AlignMode.LEFT, "NET AMOUNT:"+receipt.getNet_Amount());
        Printer.getInstance().addText(AlignMode.LEFT, "AUTH CODE:"+receipt.getAuth_Code());
        setFontSpec(FONT_SIZE_NORMAL);
        Printer.getInstance().addText(AlignMode.CENTER,"---------------------------------");



        // Print text with small font size
        setFontSpec(FONT_SIZE_SMALL);
        Printer.getInstance().addText(AlignMode.CENTER, receipt.getFooterText1());
        Printer.getInstance().addText(AlignMode.CENTER, receipt.getFooterText2());
        Printer.getInstance().addText(AlignMode.CENTER, receipt.getFooterText3());
        Printer.getInstance().addText(AlignMode.CENTER, receipt.getFooterText4());



        // Print QR code
        //    Printer.getInstance().addQrCode(AlignMode.LEFT, 240, ECLevel.ECLEVEL_Q, "www.landicorp.com");

        // Feed lines
        Printer.getInstance().feedLine(5);

        // Start printing
        Printer.getInstance().start(new OnPrintListener.Stub() {

            @Override
            public void onFinish() {


                hideDialog();
                successResponse.processFinish("");
                //showToast(R.string.succeed);

            }

            @Override
            public void onError(int error) {

                hideDialog();
                successResponse.processFailed("printError");
                // showToast(context.getString( Printer.getErrorId(error)));

                try {
                    if (Constant.StoreAPILogsOnServer)
                        CommonController.StoreAPILog(context.getString(Printer.getErrorId(error)), "printError");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Set font spec.
     */
    private void setFontSpec(int fontSpec) throws RemoteException {
        switch (fontSpec) {

            case FONT_SIZE_SMALL:
                Printer.getInstance().setHzSize(HZSize.DOT16x16);
                Printer.getInstance().setHzScale(HZScale.SC1x1);
                Printer.getInstance().setAscSize(ASCSize.DOT24x8);
                Printer.getInstance().setAscScale(ASCScale.SC1x1);

                break;
            case FONT_SIZE_NORMAL:
                Printer.getInstance().setHzSize(HZSize.DOT24x24);
                Printer.getInstance().setHzScale(HZScale.SC1x1);
                Printer.getInstance().setAscSize(ASCSize.DOT24x12);
                Printer.getInstance().setAscScale(ASCScale.SC1x1);
                break;

            case FONT_SIZE_LARGE:
                Printer.getInstance().setHzSize(HZSize.DOT32x24);
                Printer.getInstance().setHzScale(HZScale.SC1x1);
                Printer.getInstance().setAscSize(ASCSize.DOT16x8);
                Printer.getInstance().setAscScale(ASCScale.SC2x2);
                break;
        }
    }

    /**
     * Read the file in the assets directory.
     */
    public byte[] readAssetsFile(String fileName) throws RemoteException {
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
    public boolean isAssetExists(String pathInAssetsDir){
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

//    private void printByHtml5(String HTML_DATA) throws RemoteException {
//        // Get statue
//        Printer.getInstance().getStatus();
//
//        // Set gray
//        Printer.getInstance().setPrnGray(6);
//
//        // Show dialog
//        showDialog(R.string.waiting_for_printing, false);
//
//        CommonPrinterUtils.generateH5Images(HTML_DATA)
//                .doOnSuccess(images -> Printer.getInstance().addImages(images))
//                .toCompletable()
//                .doOnComplete(() -> Printer.getInstance().feedLine(5))
//                .andThen(Printer.getInstance().print())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableCompletableObserver() {
//                    @Override
//                    public void onComplete() {
//                        hideDialog();
//                       successResponse.processFinish("done");
//                    }
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        hideDialog();
//                        successResponse.processFailed(e.getMessage());
//
//                    }
//                });
//    }



}
