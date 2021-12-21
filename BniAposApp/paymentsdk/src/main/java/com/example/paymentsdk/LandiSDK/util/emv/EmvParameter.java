package com.example.paymentsdk.LandiSDK.util.emv;

/**
 * EMV parameter.
 */

interface EmvParameter {

    String pack() throws EmvParameterException;
}
