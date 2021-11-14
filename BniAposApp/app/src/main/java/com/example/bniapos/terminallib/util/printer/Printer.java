package com.example.bniapos.terminallib.util.printer;
import android.content.Context;
import android.os.RemoteException;
import com.cti.generic.CTIApplication;
import com.cti.generic.R;
import com.usdk.apiservice.aidl.printer.OnPrintListener;
import com.usdk.apiservice.aidl.printer.PrinterError;
import com.usdk.apiservice.aidl.printer.UPrinter;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import io.reactivex.Completable;

/**
 * Printer API.
 */

public class Printer {
    private static final int WIDTH = 372;
    /**
     * Printer object.
     */
    private UPrinter printer = CTIApplication.getDeviceService().getPrinter();

    /**
     * Context.
     */
    private Context context = CTIApplication.getContext();

    /**
     * Constructor.
     */

    /**
     * Get status.
     */
    public void getStatus() throws RemoteException {
        int ret = printer.getStatus();
        if (ret != PrinterError.SUCCESS) {
            throw new RemoteException(context.getString(getErrorId(ret)));
        }
    }

    /**
     * Set gray.
     */
    public void setPrnGray(int gray) throws RemoteException {
        printer.setPrnGray(gray);
    }

    /**
     * Print text.
     */
    public void addText(int align, String text) throws RemoteException {
        printer.addText(align, text);
    }

    /**
     * Print barcode.
     */
    public void addBarCode(int align, int codeWith, int codeHeight, String barcode) throws RemoteException {
        printer.addBarCode(align, codeWith, codeHeight, barcode);
    }

    /**
     * Print QR code.
     */
    public void addQrCode(int align, int imageHeight, int ecLevel, String qrCode) throws RemoteException {
        printer.addQrCode(align, imageHeight, ecLevel, qrCode);
    }

    /**
     * Print image.
     */
    public void addImage(int align, byte[] imageData) throws RemoteException {
        printer.addImage(align, imageData);
    }

    /**
     * Feed line.
     */
    public void feedLine(int line) throws RemoteException {
        printer.feedLine(line);
    }

    /**
     * Feed pix.
     */
    public void feedPix(int pix) throws RemoteException {
        printer.feedPix(pix);
    }

    /**
     * Print BMP image.
     */
    public void addBmpImage(int offset, int factor, byte[] imageData) throws RemoteException {
        printer.addBmpImage(offset, factor, imageData);
    }

    /**
     * Print BMP image by path.
     */
    public void addBmpPath(int offset, int factor, String bmpPath) throws RemoteException {
        printer.addBmpPath(offset, factor, bmpPath);
    }

    /**
     * Start print.
     */
    public void start(OnPrintListener onPrintListener) throws RemoteException {
        printer.startPrint(onPrintListener);
    }

    /**
     * Set ASC size.
     */
    public void setAscSize(int ascSize) throws RemoteException {
        printer.setAscSize(ascSize);
    }

    /**
     * Set ASC scale.
     */
    public void setAscScale(int ascScale) throws RemoteException {
        printer.setAscScale(ascScale);
    }

    /**
     * Set HZ size.
     */
    public void setHzSize(int hzSize) throws RemoteException {
        printer.setHzSize(hzSize);
    }

    /**
     * Set HZ scale.
     */
    public void setHzScale(int hzScale) throws RemoteException {
        printer.setHzScale(hzScale);
    }

    /**
     * Set X space.
     */
    public void setXSpace(int xSpace) throws RemoteException {
        printer.setXSpace(xSpace);
    }

    /**
     * Set Y space.
     */
    public void setYSpace(int ySpace) throws RemoteException {
        printer.setYSpace(ySpace);
    }

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
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(PrinterError.SUCCESS, R.string.succeed);
        errorCodes.put(PrinterError.SERVICE_CRASH, R.string.service_crash);
        errorCodes.put(PrinterError.REQUEST_EXCEPTION, R.string.request_exception);
        errorCodes.put(PrinterError.ERROR_PAPERENDED, R.string.printer_paper_ended);
        errorCodes.put(PrinterError.ERROR_HARDERR, R.string.printer_hardware_error);
        errorCodes.put(PrinterError.ERROR_OVERHEAT, R.string.printer_overheat);
        errorCodes.put(PrinterError.ERROR_BUFOVERFLOW, R.string.printer_buffer_overflow);
        errorCodes.put(PrinterError.ERROR_LOWVOL, R.string.printer_low_vol);
        errorCodes.put(PrinterError.ERROR_PAPERENDING, R.string.printer_paper_ending);
        errorCodes.put(PrinterError.ERROR_MOTORERR, R.string.printer_engine_error);
        errorCodes.put(PrinterError.ERROR_PENOFOUND, R.string.printer_pe_not_found);
        errorCodes.put(PrinterError.ERROR_PAPERJAM, R.string.printer_paper_jam);
        errorCodes.put(PrinterError.ERROR_NOBM, R.string.printer_no_bm);
        errorCodes.put(PrinterError.ERROR_BUSY, R.string.printer_busy);
        errorCodes.put(PrinterError.ERROR_BMBLACK, R.string.printer_bm_black);
        errorCodes.put(PrinterError.ERROR_WORKON, R.string.printer_power_on);
        errorCodes.put(PrinterError.ERROR_LIFTHEAD, R.string.printer_lift_head);
        errorCodes.put(PrinterError.ERROR_CUTPOSITIONERR, R.string.printer_cutter_position_error);
        errorCodes.put(PrinterError.ERROR_LOWTEMP, R.string.printer_low_temperature);
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
        printer.addImage(format.getAlign(), image);
    }

    /**
     * Init web view.
     * <p>
     * Just invoke once when application start
     *
     * @param context context
     */
    public static void initWebView(Context context) {
        com.printerutils.PrinterUtils.initWebView(context,WIDTH);
    }

    /**
     * Print.
     *
     * @return Single allows getting print results using Rx .
     */
    public Completable print() {
        return Completable.create(e -> printer.startPrint(new com.usdk.apiservice.aidl.printer.OnPrintListener.Stub() {
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
