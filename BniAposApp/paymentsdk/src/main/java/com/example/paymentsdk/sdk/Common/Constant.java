package com.example.paymentsdk.sdk.Common;

import android.os.Build;

public class Constant {
    public static final String POSENT_Not_Available = "";
    public static final String VisaRID = "";
    public static final String PINB_SecurityType = "MKSK";
    public static final String PINB_DUKPT = "";
    public static final String LogKey = "";
    public static final String PINB_MKSK = "MKSK";
    public static final int DefaultTimeOutInSeconds = 0;
    public static final boolean isShowDummyCard = false;
    public static final String DummyCardNo = "";
    public static final String DummyTrack2Data = "";
    public static final String DummyExpiryDate = "";
    public static final boolean isForceApprove_ARPC_EMV = false;
    public static final String POSENT_Contactless = "";
    public static final String BaseCurrencyCode = "";
    public static final String BaseCountryCode = "";
    public static final String HSN = "";
    public static final String targetDevice = "";
    public static final boolean isMultiAcquiringEnabled = false;
    public static final String DefaultBank = "";
    public static final String VER = "";
    public static final String PINBMasterKey = "E7F31D6A88967E03004121CC657EBD55";
    public static final String PINBSessionKey = "54825D8C49480C282B5B8C57DE8FF1C0";
    public static final boolean StoreAPILogsOnServer = false;
    public static final String Messages_TAG = "Message";

    public static final String Terminal_Landi = "LANDI";
    public static final String Terminal_Verfione = "VERIFONE";

    public static final String CurrentTerminal() {
            try {
                String Manufacturer = android.os.Build.MANUFACTURER;
                switch (Manufacturer.toLowerCase()) {
                    case "landi": {
                        return Terminal_Landi;
                    }
                    case "verifone": {
                        return Terminal_Verfione;
                    }
                }

                String modelType = Build.MODEL;
                switch (modelType.toUpperCase()) {
                    case "X990": {
                        return Terminal_Verfione;
                    }
                    case "APOS A8OVS": {
                        return Terminal_Landi;
                    }
                }

                return Terminal_Landi;

            } catch (Exception ex) {
                return Terminal_Landi;
            }
    }


}
