package com.example.paymentsdk.util.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TLV list utils.
 */

public class TLVList implements Serializable {

    /**
     * List data.
     */
    private List<TLV> data = new ArrayList<>();

    /**
     * Make to binary.
     */
    public byte[] toBinary() {
        byte[][] allData = new byte[this.data.size()][];
        for (int i = 0; i < this.data.size(); i++) {
            allData[i] = this.data.get(i).getRawData();
        }
        return BytesUtil.merge(allData);
    }

    /**
     * Add TLV.
     */
    public void addTLV(TLV tlv) {
        if (tlv.isValid()) {
            this.data.add(tlv);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Add TLV.
     */
    public void addTLV(String tag, byte[] value) {
        this.addTLV(TLV.fromData(tag, value));
    }

    /**
     * Make to string data.
     */
    public String toString() {
        if (this.data.isEmpty()) {
            return super.toString();
        }
        return BytesUtil.bytes2HexString(toBinary());
    }
}