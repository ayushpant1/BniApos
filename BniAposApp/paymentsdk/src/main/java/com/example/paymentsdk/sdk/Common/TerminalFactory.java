package com.example.paymentsdk.sdk.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.paymentsdk.LandiSDK.Common.LandiHeartbeatHelper;
import com.example.paymentsdk.LandiSDK.Common.LandiTerminalApiHelper;
import com.example.paymentsdk.LandiSDK.Common.LandiTerminalCardApiHelper;
import com.example.paymentsdk.LandiSDK.Common.LandiTerminalPrintApiHelper;
import com.example.paymentsdk.LandiSDK.Common.LandiTerminalSearchInsertCard;
import com.example.paymentsdk.LandiSDK.util.emv.LandiEmvData;
import com.example.paymentsdk.VerifoneSDK.Common.VFHeartbeatHelper;
import com.example.paymentsdk.VerifoneSDK.Common.VFTerminalApiHelper;
import com.example.paymentsdk.VerifoneSDK.Common.VFTerminalCardApiHelper;
import com.example.paymentsdk.VerifoneSDK.Common.VFTerminalPrintApiHelper;
import com.example.paymentsdk.VerifoneSDK.Common.VFTerminalSearchInsertCard;
import com.example.paymentsdk.VerifoneSDK.util.emv.VFEmvData;


public class TerminalFactory {
    public static TerminalPrintApiHelper GetPrinterContext(Context _context, Toast toast, AlertDialog dialog, ISuccessResponse _successResponse) {

        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                return new LandiTerminalPrintApiHelper(_context, toast, dialog, _successResponse);
            }
            case Constant.Terminal_Verfione: {
                return new VFTerminalPrintApiHelper(_context, toast, dialog, _successResponse);
            }
        }

        return new LandiTerminalPrintApiHelper(_context, toast, dialog, _successResponse);
    }

    public static TerminalSearchInsertCard GetCardSearchContext(Context _context, ISuccessResponse _successResponse) {

        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                return new LandiTerminalSearchInsertCard(_context, _successResponse);
            }
            case Constant.Terminal_Verfione: {
                return new VFTerminalSearchInsertCard(_context, _successResponse);
            }
        }

        return new LandiTerminalSearchInsertCard(_context, _successResponse);
    }

    public static TerminalCardApiHelper GetCardAPIHelper(Context _context, ISuccessResponse_Card _successResponse) {

        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                return new LandiTerminalCardApiHelper(_context, _successResponse);
            }
            case Constant.Terminal_Verfione: {
                return new VFTerminalCardApiHelper(_context, _successResponse);
            }
        }

        return new LandiTerminalCardApiHelper(_context, _successResponse);
    }

    public static TerminalApiHelper GetAPIHelper(ISuccessResponse _successResponse) {

        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                return new LandiTerminalApiHelper(_successResponse);
            }
            case Constant.Terminal_Verfione: {
                return new VFTerminalApiHelper(_successResponse);
            }
        }

        return new LandiTerminalApiHelper(_successResponse);
    }

    public static HeartbeatHelper GetHeartbeatHelper(Context context) {

        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                return new LandiHeartbeatHelper(context);
            }
            case Constant.Terminal_Verfione: {
                return new VFHeartbeatHelper(context);
            }
        }

        return new LandiHeartbeatHelper(context);
    }

    public static void updateEMVAIDFromOutside() {
        switch (Constant.CurrentTerminal()) {
            case Constant.Terminal_Landi: {
                LandiEmvData.updateEMVAIDFromOutside();
            }
            case Constant.Terminal_Verfione: {
                VFEmvData.updateEMVAIDFromOutside();
            }
        }

        LandiEmvData.updateEMVAIDFromOutside();
    }
}
