package com.example.bniapos.utils

import java.math.BigDecimal
import java.text.DecimalFormat

object CurrencyUtility {
    fun ReturnCurrency(Amount: String): String? {
        return ReturnCurrency(BigDecimal.valueOf(Amount.toDouble()), true, false)
    }

    fun ReturnCurrency(Amount: Double): String? {
        return ReturnCurrency(BigDecimal.valueOf(Amount), true, false)
    }

    fun ReturnCurrency(Amount: Double, isShowCurrency: Boolean): String? {
        return ReturnCurrency(BigDecimal.valueOf(Amount), isShowCurrency, false)
    }



    fun ReturnCurrency(Amount: BigDecimal): String? {
        return ReturnCurrency(Amount, true, false)
    }

    fun ReturnCurrency(
        Amount: BigDecimal,
        isShowCurrency: Boolean,
        showNegativeZeroValue: Boolean
    ): String? {
        var Amount = Amount
        var negativeValue = ""
        if (Amount.toDouble() < 0) {
            negativeValue = "-"
            Amount = BigDecimal(-Amount.toDouble())
        } else if (Amount.toDouble() == 0.0 && showNegativeZeroValue) negativeValue = "-"

        val decimalFormat = DecimalFormat("#,##0")
        val firstNumberAsString = decimalFormat.format(Amount)
        return negativeValue + if (isShowCurrency) "Rp " + firstNumberAsString else "" + firstNumberAsString


    }
}