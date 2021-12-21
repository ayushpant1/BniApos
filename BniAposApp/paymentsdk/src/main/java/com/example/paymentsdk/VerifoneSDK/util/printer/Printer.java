package com.example.paymentsdk.VerifoneSDK.util.printer;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.example.paymentsdk.R;
import com.vfi.smartpos.deviceservice.aidl.PrinterListener;
import com.vfi.smartpos.deviceservice.aidl.IPrinter;
import com.vfi.smartpos.deviceservice.constdefine.BarcodeFormat;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;

/**
 * Printer API.
 */

public class Printer {
    private static final int WIDTH = 372;

    private static int currentFont=1;
    /**
     * Printer object.
     */
    private IPrinter printer = getPrinter ();
    private IPrinter getPrinter()
    {
        try {
            return CTIApplication.getVerifoneDeviceService().getPrinter();
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Creator.
     */
    private static class Creator {
        private static final Printer INSTANCE = new Printer();
    }

    /**
     * Get printer instance.
     */
    public static Printer getInstance() {
        return Creator.INSTANCE;
    }



    /**
     * Constructor.
     */

    /**
     * Get status.
     */
    public void getStatus() throws RemoteException {
        int ret = printer.getStatus();
        //0 i.e. success
        if (ret != 0) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Set gray.
     */
    public void setPrnGray(int gray) throws RemoteException {
        printer.setGray(gray);
    }
    public void addText(int align,String text) {
        try {
            addTexttoPrinter(align, text);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Print text.
     */
    public void addTexttoPrinter(int align, String text) throws RemoteException {
//         * <li>font(int)
//	 *      |- 0:small(size16),
//	 *      |- 1:normal(size24),
//	 *      |- 2:normal_bigger(size24 double height & bold)
//	 *      |- 3:large(size32)
//                *      |- 4:large_bigger(size32 double height & bold)
//	 *      |- 5:huge(size48)
//                * </li>
//	 * <li>fontStyle(String)</li>
//	 *      |--/xxxx/xx.ttf(absolute path, custom font by user) </li>
//	 * <li>align(int) - 0:left, 1:center, 2:right</li>
//	 * <li>bold(boolean) - true粗体 - false常规</li>
//	 * <li>newline(boolean) - true:换行, false:不换行</li>
        Bundle printConfig = new Bundle();
        printConfig.putInt("font", currentFont);
        printConfig.putInt("align", align);

        printer.addText(printConfig, text);
    }

    /**
     * Print barcode.
     */
    public void addBarCode(int align, int codeWith, int codeHeight, String barcode) throws RemoteException {
//    * <li>align(int) - 0:left, 1:center, 2:right</li>
//	 * <li>height(int) - height of barcode</li>
//	 * <li>barCodeType(int) - type of barcode(default BarcodeFormat.CODE_128)</li>
        Bundle barCodeConfig = new Bundle();
        barCodeConfig.putInt("align", align);
        barCodeConfig.putInt("height", codeHeight);
        barCodeConfig.putInt("barCodeType", BarcodeFormat.CODE_128.ordinal());

        printer.addBarCode(barCodeConfig, barcode);
    }

    /**
     * Print QR code.
     */
    public void addQrCode(int align, int imageHeight, int ecLevel, String qrCode) throws RemoteException {
//    * <li>offset(int) - 打印起始位置 </li>
//	 * <li>expectedHeight(int) - 期望高度</li>
        Bundle QrCodeConfig = new Bundle();
        QrCodeConfig.putInt("offset", align);
        QrCodeConfig.putInt("expectedHeight", imageHeight);
        printer.addQrCode(QrCodeConfig, qrCode);

    }

    /**
     * Print image.
     */
    public void addImage(int align, byte[] imageData) throws RemoteException {

        Bundle imgConfig = new Bundle();
        imgConfig.putInt("offset", 80);
        imgConfig.putInt("height",70);
        imgConfig.putInt("width",250);
        printer.addImage(imgConfig, imageData);
    }



    /**
     * Feed line.
     */
    public void feedLine(int line) throws RemoteException {

        printer.feedLine(line);

    }





    /**
     * Start print.
     */
    public void start( PrinterListener onPrintListener) throws RemoteException {
        printer.startPrint(onPrintListener);
    }

    /**
     * Set ASC size.
     */
    public static void setCurrentFont(int FontSize) throws RemoteException {
        currentFont = FontSize;
    }



    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;


    static {

        errorCodes = new Hashtable<>();
        errorCodes.put((int)(0x00), R.string.succeed);

        errorCodes.put((int)(0xF0), R.string.printer_paper_ended);
        errorCodes.put((int)(0xF2), R.string.printer_hardware_error);
        errorCodes.put((int)(0xF3), R.string.printer_overheat);
        errorCodes.put((int)(0xF5), R.string.printer_buffer_overflow);
        errorCodes.put((int)(0xE1), R.string.printer_low_vol);
        errorCodes.put((int)(0xF4), R.string.printer_paper_ending);
        errorCodes.put((int)(0xFB), R.string.printer_engine_error);
        errorCodes.put((int)(0xFC), R.string.printer_pe_not_found);
        errorCodes.put((int)(0xEE), R.string.printer_paper_jam);
        errorCodes.put((int)(0xF6), R.string.printer_no_bm);
        errorCodes.put((int)(0xF7), R.string.printer_busy);
        errorCodes.put((int)(0xF8), R.string.printer_bm_black);
        errorCodes.put((int)(0xE6), R.string.printer_power_on);
        errorCodes.put((int)(0xE0), R.string.printer_lift_head);
        errorCodes.put((int)(0xE2), R.string.printer_cutter_position_error);
        errorCodes.put((int)(0xE3), R.string.printer_low_temperature);
    }

    /**
     * Get error id.
     */

    public static int getErrorId(int errorCode) {
        if (errorCodes.containsKey(errorCode)) {
            return errorCodes.get(errorCode);
        }

        return R.string.other_error;
    }

    public void addImages(List<byte[]> images) throws RemoteException {
        for (byte[] image : images) {
            Format format = new Format();
            format.setAlign(Format.ALIGN_CENTER);
            addImage(format, image);
        }
    }

    /**
     * Add image.
     * <p>
     * addImage into printer in order
     *
     * @throws RemoteException exception
     */
    public void addImage(byte[] image) throws RemoteException {
        Format format = new Format();
        format.setAlign(Format.ALIGN_LEFT);
        addImage(format, image);
    }

    /**
     * Add image.
     */
    public void addImage(Format format, byte[] image) throws RemoteException {
        Printer.getInstance().addImage(format.getAlign(), image);
    }

    /**
     * Init web view.
     * <p>
     * Just invoke once when application start
     *
     * @param context context
     */
    public static void initWebView(Context context) {
       // com.printerutils.PrinterUtils.initWebView(context,WIDTH);
    }

    /**
     * Print.
     *
     * @return Single allows getting print results using Rx .
     */
    public Completable print() {
        return Completable.create(e -> printer.startPrint(new PrinterListener.Stub() {
            @Override
            public void onFinish() throws RemoteException {
                e.onComplete();
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                e.onError(new Exception("nymph_printer_print_error"));
            }
        }));
    }
}
