package com.example.bniapos.terminallib.util.emv;

/**
 * EMV parameter.
 */

interface EmvParameter {

    String pack() throws EmvParameterException;
}
