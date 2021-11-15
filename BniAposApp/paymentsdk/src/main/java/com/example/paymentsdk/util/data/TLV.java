package com.example.paymentsdk.util.data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * TLV utils.
 */

public class TLV implements Serializable {

    /**
     * Byte array data.
     */
    private byte[] data;

    /**
     * Tag.
     */
    private String tag;

    /**
     * Length.
     */
    private int length = -1;

    /**
     * Value.
     */
    private byte[] value;

    /**
     * Make from data.
     */
    public static TLV fromData(String tagName, byte[] value) {
        byte[] tag = BytesUtil.hexString2Bytes(tagName);
        TLV d = new TLV();
        d.data = BytesUtil.merge(tag, makeLengthData(value.length), value);
        d.tag = tagName;
        d.length = value.length;
        d.value = value;
        return d;
    }

    /**
     * Make from raw data.
     */
    public static TLV fromRawData(byte[] data, int offset) {
        int len = getDataLength(data, offset);
        TLV d = new TLV();
        d.data = BytesUtil.subBytes(data, offset, len);
        d.getTag();
        d.getLength();
        d.getBytesValue();
        return d;
    }

    /**
     * Get tag.
     */
    public String getTag() {
        if (this.tag != null) {
            return this.tag;
        }
        int tLen = getTLength(this.data, 0);
        return this.tag = BytesUtil.bytes2HexString(BytesUtil.subBytes(this.data, 0, tLen));
    }

    /**
     * Get length.
     */
    public int getLength() {
        if (this.length > -1) {
            return this.length;
        }
        int offset = getTLength(this.data, 0);
        int l = getLLength(this.data, offset);
        if (l == 1) {
            return this.data[offset];
        }

        int afterLen = 0;
        for (int i = 1; i < l; i++) {
            afterLen <<= 8;
            afterLen |= this.data[(offset + i)] & 0xFF;
        }
        return this.length = afterLen;
    }

    /**
     * Get value.
     */
    public String getValue() {
        byte[] result = getBytesValue();
        if (result == null) {
            return null;
        }
        return BytesUtil.bytes2HexString(result);
    }

    /**
     * Get raw data.
     */
    public byte[] getRawData() {
        return this.data;
    }

    /**
     * Get bytes value.
     */
    public byte[] getBytesValue() {
        if (this.value != null) {
            return this.value;
        }
        int l = getLength();
        return this.value = BytesUtil.subBytes(this.data, this.data.length - l, l);
    }

    /**
     * Whether valid.
     */
    public boolean isValid() {
        return this.data != null;
    }

    /**
     * Get tag's length.
     */
    private static int getTLength(byte[] data, int offset) {
        if ((data[offset] & 0x1F) == 31) {
            return 2;
        }
        return 1;
    }

    /**
     * Get length's length.
     */
    private static int getLLength(byte[] data, int offset) {
        if ((data[offset] & 0x80) == 0) {
            return 1;
        }
        return (data[offset] & 0x7F) + 1;
    }

    /**
     * Get data's length.
     */
    private static int getDataLength(byte[] data, int offset) {
        int tLen = getTLength(data, offset);
        int lLen = getLLength(data, offset + tLen);
        int vLen = calcValueLength(data, offset + tLen, lLen);
        return tLen + lLen + vLen;
    }

    /**
     * Calculate value length.
     */
    private static int calcValueLength(byte[] l, int offset, int lLen) {
        if (lLen == 1) {
            return l[offset];
        }

        int vLen = 0;
        for (int i = 1; i < lLen; i++) {
            vLen <<= 8;
            vLen |= l[(offset + i)] & 0xFF;
        }
        return vLen;
    }

    /**
     * Make length data.
     */
    private static byte[] makeLengthData(int len) {
        if (len > 127) {
            byte[] lenData = new byte[4];
            int validIndex = -1;
            for (int i = 0; i < lenData.length; i++) {
                lenData[i] = ((byte) (len >> 8 * (3 - i) & 0xFF));
                if ((lenData[i] != 0) && (validIndex < 0)) {
                    validIndex = i;
                }
            }

            lenData = BytesUtil.subBytes(lenData, validIndex, -1);
            lenData = BytesUtil.merge(new byte[][]{{(byte) (0x80 | lenData.length)}, lenData});
            return lenData;
        }

        return new byte[]{(byte) len};
    }

    /**
     * Equals.
     */
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TLV && !((this.data == null)
                || (((TLV) obj).data == null)) && Arrays.equals(this.data, ((TLV) obj).data);
    }

    /**
     * Make string data.
     */
    public String toString() {
        if (this.data == null) {
            return super.toString();
        }
        return BytesUtil.bytes2HexString(this.data);
    }
}