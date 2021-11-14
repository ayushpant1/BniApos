package com.example.bniapos.terminallib.Common;

import java.io.Serializable;


public class PrintFormat implements Serializable {

    boolean isText;
    String Value;
    String AlignMode;

    public PrintFormat(String _Value,boolean _isText,String _AlignMode)
    {
        this.isText = _isText;
        this.Value = _Value;
        this.AlignMode= _AlignMode;

    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getAlignMode() {
        return AlignMode;
    }

    public void setAlignMode(String alignMode) {
        AlignMode = alignMode;
    }
}
