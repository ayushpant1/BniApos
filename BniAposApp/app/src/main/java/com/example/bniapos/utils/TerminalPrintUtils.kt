package com.example.bniapos.utils

import com.example.bniapos.models.MasterPrintFormat
import com.example.paymentsdk.sdk.Common.PrintFormat
import java.lang.Exception
import java.util.ArrayList

object TerminalPrintUtils {
    var TotalSpaceSize = 48 //48

    var TotalSpaceSize_Small = 48 //48

    var TotalSpaceSize_Normal = 32 //32

    var TotalSpaceSize_Large = 24 //24

    fun PrintInvoiceFormat_LineFormatting(allParams: ArrayList<MasterPrintFormat>): ArrayList<PrintFormat> {
        val _retval: ArrayList<PrintFormat> = ArrayList<PrintFormat>()
        try {
            var previousOrder = -1
            var previousParam: MasterPrintFormat? = null
            for (eachParam in allParams) {
                var currentOrder: Int = eachParam!!.Order
                if (previousParam != null && currentOrder == previousOrder) {
                    //same order logic;
                    ProcessPrintingMultipleTAGs(_retval, previousParam, eachParam)
                    previousParam = null
                    previousOrder = -1
                } else if (previousParam == null) { //i.e. may be first case or after executing 2 same order case..
                    previousParam = eachParam
                    previousOrder = currentOrder
                } else {
                    //execute previous param..
                    ProcessPrintingTAGs_SingleLine(_retval, previousParam)

                    //add current param in previous param for next line execution
                    previousParam = eachParam
                    previousOrder = currentOrder
                }
                currentOrder = eachParam!!.Order
            }
            if (previousParam != null && previousOrder >= 0) {
                //i.e. this is last printing tag, which was not processed in loop
                ProcessPrintingTAGs_SingleLine(_retval, previousParam)
                previousParam = null
                previousOrder = -1
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return _retval
    }
    private fun ProcessPrintingMultipleTAGs(
        _retval: MutableList<PrintFormat>,
        previousParam: MasterPrintFormat?,
        currentParam: MasterPrintFormat?
    ) {
        var alignment = "center"
        when (previousParam!!.Alignment) {
            "L" -> {
                alignment = "left"
            }
            "R" -> {
                alignment = "right"
            }
        }
        val FontSize = 3 // Integer.valueOf(currentTAG.get);
        when (FontSize) {
            1, 2, 3 -> {
                TotalSpaceSize = TotalSpaceSize_Small
                _retval.add(PrintFormat("small", false, "center"))
            }
            4 -> {
                TotalSpaceSize = TotalSpaceSize_Normal
                _retval.add(PrintFormat("normal", false, "center"))
            }
            5 -> {
                TotalSpaceSize = TotalSpaceSize_Large
                _retval.add(PrintFormat("large", false, "center"))
            }
        }
        var printText = ""
        var SpaceText = ""
        var previousText = ""
        if (previousParam != null && previousParam!!.Text != null && previousParam!!.Text.length > 0
        ) previousText = previousParam!!.Printheader + previousParam.Text
        var currentText = ""
        if (currentParam != null && currentParam!!.Text != null && currentParam.Text.length > 0
        ) currentText = currentParam.Printheader + currentParam.Text
        val SpaceRequired: Int = TotalSpaceSize - (previousText.length + currentText.length)
        for (idx in 0 until SpaceRequired) {
            SpaceText += " "
        }
        if (previousText.length > 0 || currentText.length > 0) {
            printText =
                if (alignment.equals("left", ignoreCase = true) || alignment.equals(
                        "center",
                        ignoreCase = true
                    )
                ) previousText + SpaceText + currentText else currentText + SpaceText + previousText
            _retval.add(PrintFormat(printText, true, alignment))
        }
    }

    private fun ProcessPrintingTAGs_SingleLine(
        _retval: MutableList<PrintFormat>,
        currentParam: MasterPrintFormat
    ) {
        var alignment = "center"
        when (currentParam.Alignment) {
            "L" -> {
                alignment = "left"
            }
            "R" -> {
                alignment = "right"
            }
        }
        val FontSize: Int = Integer.valueOf(currentParam.Font)
        when (FontSize) {
            1, 2, 3 -> {
                TotalSpaceSize = TotalSpaceSize_Small
                _retval.add(PrintFormat("small", false, "center"))
            }
            4 -> {
                TotalSpaceSize = TotalSpaceSize_Normal
                _retval.add(PrintFormat("normal", false, "center"))
            }
            5 -> {
                TotalSpaceSize = TotalSpaceSize_Large
                _retval.add(PrintFormat("large", false, "center"))
            }
        }
        val printHeader: String = currentParam.Printheader
        if (printHeader.toLowerCase().startsWith("-printline")) {
            _retval.add(PrintFormat("normal", false, "center"))
            var Value = ""
            for (i in 0 until TotalSpaceSize - 2) {
                Value += "-"
            }
            _retval.add(PrintFormat(Value, true, "center"))
        } else if (printHeader.toLowerCase().startsWith("-blankline")) {
            _retval.add(PrintFormat(" ", true, "center"))
        } else if (printHeader.toLowerCase().startsWith("-imageprint")) {
            if (currentParam.Text != null && currentParam.Text
                    .length > 0 && currentParam.Text.toLowerCase().endsWith(".bmp")
            ) {
                _retval.add(PrintFormat("image", false, currentParam.Text.toLowerCase()))
            }
        } else if (printHeader.toLowerCase().startsWith("aquirerimage")
            || printHeader.toLowerCase().startsWith("acquirerimage") ||
            printHeader.toLowerCase().startsWith("-acquirerimage")
        ) {
            if (currentParam.Text != null && currentParam.Text.length > 0) {
                _retval.add(
                    PrintFormat(
                        "acquirerimage",
                        false,
                        currentParam.Text.toLowerCase().toString() + ".bmp"
                    )
                )
            }
        } else if (printHeader.toLowerCase().startsWith("-qrcode")) {
            if (currentParam.Text != null && currentParam.Text.length > 0) {
                _retval.add(PrintFormat("qrcode", false, currentParam.Text))
            }
        } else if (printHeader.toLowerCase().startsWith("-barcode")) {
            if (currentParam.Text != null && currentParam.Text.length > 0) {
                _retval.add(PrintFormat("barcode", false, currentParam.Text))
            }
        } else if (printHeader.toLowerCase().startsWith("logstart")) {
            if (currentParam.Text != null && currentParam.Text.length > 0) {
                _retval.add(PrintFormat("logstart", false, ""))
            }
        } else {
            val Text1 = printHeader.length
            val Text2 = if (currentParam.Text != null) currentParam.Text.length else 0
            val SpaceRequired: Int = TotalSpaceSize - (Text1 + Text2)
            var SpaceText = ""
            if (printHeader.length > 0 && alignment.equals("center", ignoreCase = true)) {
                for (idx in 0 until SpaceRequired) {
                    SpaceText += " "
                }
            }
            if (currentParam.Text != null && currentParam.Text.length > 0) _retval.add(
                PrintFormat(printHeader + SpaceText + currentParam.Text, true, alignment)
            )
        }
    }

}