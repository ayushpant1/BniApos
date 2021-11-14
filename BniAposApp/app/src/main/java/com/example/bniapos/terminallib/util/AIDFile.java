package com.example.bniapos.terminallib.util;

import android.os.Parcel;
import android.os.Parcelable;

public class AIDFile implements Parcelable {
    public static final Creator<AIDFile> CREATOR = new Creator<AIDFile>() {
        public AIDFile createFromParcel(Parcel source) {
            AIDFile candAID = new AIDFile();
            candAID.setAID(source.createByteArray());
            candAID.setAppLabel(source.createByteArray());
            candAID.setAPN(source.createByteArray());
            candAID.setAPIDFlag(source.readByte());
            candAID.setAPID(source.readByte());
            candAID.setLangPref(source.createByteArray());
            candAID.setIssCTIndexFlag(source.readByte());
            candAID.setIssCTIndex(source.readByte());
            candAID.setKernelID(source.createByteArray());
            return candAID;
        }

        public AIDFile[] newArray(int size) {
            return new AIDFile[size];
        }
    };
    private byte[] auAID;
    private byte[] auAppLabel;
    private byte[] auAPN;
    private byte ucAPIDFlag;
    private byte ucAPID;
    private byte[] auLangPref;
    private byte ucIssCTIndexFlag;
    private byte ucIssCTIndex;
    private byte[] auKernelID;

    public AIDFile() {
    }

    public byte[] getAID() {
        return this.auAID;
    }

    public void setAID(byte[] AID) {
        this.auAID = AID;
    }

    public byte[] getAppLabel() {
        return this.auAppLabel;
    }

    public void setAppLabel(byte[] appLabel) {
        this.auAppLabel = appLabel;
    }

    public byte[] getAPN() {
        return this.auAPN;
    }

    public void setAPN(byte[] APN) {
        this.auAPN = APN;
    }

    public byte getAPIDFlag() {
        return this.ucAPIDFlag;
    }

    public void setAPIDFlag(byte apidFlag) {
        this.ucAPIDFlag = apidFlag;
    }

    public byte getAPID() {
        return this.ucAPID;
    }

    public void setAPID(byte apid) {
        this.ucAPID = apid;
    }

    public byte[] getLangPref() {
        return this.auLangPref;
    }

    public void setLangPref(byte[] langPref) {
        this.auLangPref = langPref;
    }

    public byte getIssCTIndexFlag() {
        return this.ucIssCTIndexFlag;
    }

    public void setIssCTIndexFlag(byte issCTIndexFlag) {
        this.ucIssCTIndexFlag = issCTIndexFlag;
    }

    public byte getIssCTIndex() {
        return this.ucIssCTIndex;
    }

    public void setIssCTIndex(byte IssCTIndex) {
        this.ucIssCTIndex = IssCTIndex;
    }

    public byte[] getKernelID() {
        return this.auKernelID;
    }

    public void setKernelID(byte[] kernelID) {
        this.auKernelID = kernelID;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.auAID);
        dest.writeByteArray(this.auAppLabel);
        dest.writeByteArray(this.auAPN);
        dest.writeByte(this.ucAPIDFlag);
        dest.writeByte(this.ucAPID);
        dest.writeByteArray(this.auLangPref);
        dest.writeByte(this.ucIssCTIndexFlag);
        dest.writeByte(this.ucIssCTIndex);
        dest.writeByteArray(this.auKernelID);
    }
}
