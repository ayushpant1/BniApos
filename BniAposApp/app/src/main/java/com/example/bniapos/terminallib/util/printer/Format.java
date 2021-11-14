package com.example.bniapos.terminallib.util.printer;


import androidx.annotation.IntDef;

import com.usdk.apiservice.aidl.printer.ECLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Print format.
 * Author jacklee
 */

public class Format {
    public static final int FONT_SIZE_SMALL = 0;
    public static final int FONT_SIZE_NORMAL = 1;
    public static final int FONT_SIZE_LARGE = 2;

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

    @IntDef({ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Align {
    }

    @IntDef({FONT_SIZE_SMALL, FONT_SIZE_NORMAL, FONT_SIZE_LARGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FontSpec {
    }

    @IntDef({ECLevel.ECLEVEL_H, ECLevel.ECLEVEL_L, ECLevel.ECLEVEL_M, ECLevel.ECLEVEL_Q})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EC {
    }

    private int align;
    private int fontSpec;
    private int offset;
    private int codeWidth;
    private int codeHeight;
    private int ecLevel;

    public Format() {
    }

    public Format(@Align int align, @FontSpec int fontSpec) {
        this.align = align;
        this.fontSpec = fontSpec;
    }

    public Format(int offset) {
        this.offset = offset;
    }

    public Format(int align, int codeWidth, int codeHeight) {
        this.align = align;
        this.codeWidth = codeWidth;
        this.codeHeight = codeHeight;
    }

    public
    @Align
    int getAlign() {
        return align;
    }

    public void setAlign(@Align int align) {
        this.align = align;
    }

    public
    @FontSpec
    int getFontSpec() {
        return fontSpec;
    }

    public void setFontSpec(@FontSpec int fontSpec) {
        this.fontSpec = fontSpec;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCodeWidth() {
        return codeWidth;
    }

    public int getCodeHeight() {
        return codeHeight;
    }

    public int getEcLevel() {
        return ecLevel;
    }

    public void setEcLevel(@EC int ecLevel) {
        this.ecLevel = ecLevel;
    }
}
