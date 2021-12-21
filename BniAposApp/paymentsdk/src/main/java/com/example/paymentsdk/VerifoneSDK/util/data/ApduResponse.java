package com.example.paymentsdk.VerifoneSDK.util.data;

public class ApduResponse {
    private byte[] mData;
    private byte mSW1;
    private byte mSW2;

    public ApduResponse(byte[] Response) {
       if(Response != null && Response.length >= 2) {

           if (Response.length > 2) {
               this.mData = BytesUtil.subBytes(Response, 0, Response.length - 2);
           }
           this.mSW1 = Response[Response.length-2];
           this.mSW2 = Response[Response.length-1];

       }
//        this.mData = in.createByteArray();
//        this.mSW1 = in.readByte();
//        this.mSW2 = in.readByte();
    }

    public byte[] getData() {
        return this.mData;
    }

    public void setData(byte[] data) {
        this.mData = data;
    }

    public byte getSW1() {
        return this.mSW1;
    }

    public void setSW1(byte sw1) {
        this.mSW1 = sw1;
    }

    public byte getSW2() {
        return this.mSW2;
    }

    public void setSW2(byte sw2) {
        this.mSW2 = sw2;
    }

    public int describeContents() {
        return 0;
    }



}
