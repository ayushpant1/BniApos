package com.example.bniapos.utils

import android.content.Context
import android.content.SharedPreferences
import java.io.InvalidObjectException

class SharedPreferenceUtils {

    companion object {

        private const val PREF_STAN_KEY = "stan"
        private const val PREF_INVOICE_KEY = "invoice"
        private const val PREF_TBID_KEY = "tbId"
        private const val PREF_MMID_KEY = "mmId"
        private const val PREF_MTID_KEY = "mtId"
        private const val PREF_AUTH_KEY = "auth"
        private const val PREF_AGEN_COUNTER_CODE_KEY = "agenCounterCode"
        private const val PREF_CHANGE_NO = "changeno"
        private const val PREF_ALLOW_TXN_TYPE = "allowedTxntypes"
        private const val PREF_ALLOW_PAYMENT_TYPE = "allowedPaymentTypes"

        private const val PREF_MERCHANT_NAME = "mrn"
        private const val PREF_ADDRESS_LINE1="mal1"
        private const val PREF_ADDRESS_LINE2="mal2"
        private const val PREF_ADDRESS_CITY="city"
        private const val PREF_ACQ_NAME="aqn"

        private const val INVALID_INDEX = -1

        private var INSTANCE: SharedPreferenceUtils? = null
        private var sharedPref: SharedPreferences? = null

        fun getInstance(context: Context?): SharedPreferenceUtils {
            val tempInstance =
                INSTANCE

            if (tempInstance != null) return tempInstance
            if (context != null) {

                sharedPref = context!!.getSharedPreferences("app", Context.MODE_PRIVATE)
            } else throw InvalidObjectException("Application Context is NULL")
            val instance =
                SharedPreferenceUtils()
            INSTANCE = instance
            return instance
        }
    }

    fun setStan(stan: Int) {
        val editor = sharedPref?.edit()
        editor?.putInt(PREF_STAN_KEY, stan)?.apply()
    }

    /**
     * this function use to get stan
     *
     * @return stan.
     */
    fun getStan(): Int {
        val stan = sharedPref?.getInt(
            PREF_STAN_KEY, AppConstants.DEFAULT_STAN
        ) ?: 1
        return stan
    }


    fun setInvoiceNo(invoiceNo: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_INVOICE_KEY, invoiceNo)?.apply()
    }

    /**
     * this function use to get invoiceNo
     *
     * @return invoiceNo.
     */
    fun getInvoiceNo(): String {
        val invoiceNo = sharedPref?.getString(
            PREF_INVOICE_KEY, AppConstants.DEFAULT_INVOICE_NO
        )
        return invoiceNo.toString()
    }

    fun setTbId(tbId: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_TBID_KEY, tbId)?.apply()
    }

    /**
     * this function use to get tbId
     *
     * @return tbId.
     */
    fun getTbId(): String {
        val tbId = sharedPref?.getString(
            PREF_TBID_KEY, AppConstants.DEFAULT_TBID
        )
        return tbId.toString()
    }

    fun setMmId(mmId: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_MMID_KEY, mmId)?.apply()
    }

    /**
     * this function use to get mmId
     *
     * @return mmId.
     */
    fun getMmId(): String {
        val mmId = sharedPref?.getString(
            PREF_MMID_KEY, AppConstants.DEFAULT_MMID
        )
        return mmId.toString()
    }

    fun setMtId(mtId: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_MTID_KEY, mtId)?.apply()
    }

    /**
     * this function use to get mtId
     *
     * @return mtId.
     */
    fun getMtId(): String {
        val mtId = sharedPref?.getString(
            PREF_MTID_KEY, AppConstants.DEFAULT_MTID
        )
        return mtId.toString()
    }

    fun setAgenCounterCode(agenCounterCode: Int) {
        val editor = sharedPref?.edit()
        editor?.putInt(PREF_AGEN_COUNTER_CODE_KEY, agenCounterCode)?.apply()
    }

    /**
     * this function use to get mtId
     *
     * @return mtId.
     */
    fun getAgenCounterCode(): Int {
        val agentCounterCode = sharedPref?.getInt(
            PREF_AGEN_COUNTER_CODE_KEY, AppConstants.DEFAULT_AGEN_COUNTER_CODE
        ) ?: 1
        return agentCounterCode

    }

    fun setAuthCode(authCode: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_AUTH_KEY, authCode)?.apply()
    }


    fun getAuthCode(): String {
        val authCode = sharedPref?.getString(
            PREF_AUTH_KEY, "MYAUTHTOKEN"
        )
        return authCode.toString()
    }

    fun setChangeNo(changeNo: Int) {
        val editor = sharedPref?.edit()
        editor?.putInt(PREF_CHANGE_NO, changeNo)?.apply()
    }
    fun getChangeNo(): Int {
        val changeNo = sharedPref?.getInt(
            PREF_CHANGE_NO, 0
        )?:0
        return changeNo
    }
    fun setAllowedPaymentTypes(paymentTypes: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_ALLOW_PAYMENT_TYPE, paymentTypes)?.apply()
    }
    fun getAllowedPaymentTypes(): String {
        val paymentTypes = sharedPref?.getString(
            PREF_ALLOW_PAYMENT_TYPE, ""
        )
        return paymentTypes.toString()
    }
    fun setAllowedTransactionTypes(transactionTypes: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_ALLOW_TXN_TYPE, transactionTypes)?.apply()
    }
    fun getAllowedTransactionTypes(): String {
        val transactionTypes = sharedPref?.getString(
            PREF_ALLOW_TXN_TYPE, ""
        )
        return transactionTypes.toString()
    }


    fun setMerchantName(value: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_MERCHANT_NAME, value)?.apply()
    }
    fun getMerchantName(): String {
        val value = sharedPref?.getString(
            PREF_MERCHANT_NAME, ""
        )
        return value.toString()
    }


    fun setAddressLine1(value: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_ADDRESS_LINE1, value)?.apply()
    }
    fun getAddressLine1(): String {
        val value = sharedPref?.getString(
            PREF_ADDRESS_LINE1, ""
        )
        return value.toString()
    }



    fun setAddressLine2(value: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_ADDRESS_LINE2, value)?.apply()
    }
    fun getAddressLine2(): String {
        val value = sharedPref?.getString(
            PREF_ADDRESS_LINE2, ""
        )
        return value.toString()
    }



    fun setMerchantCity(value: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_ADDRESS_CITY, value)?.apply()
    }
    fun getMerchantCity(): String {
        val value = sharedPref?.getString(
            PREF_ADDRESS_CITY, ""
        )
        return value.toString()
    }


    fun setAcquirerName(value: String?) {
        val editor = sharedPref?.edit()
        editor?.putString(PREF_ACQ_NAME, value)?.apply()
    }
    fun getAcquirerName(): String {
        val value = sharedPref?.getString(
            PREF_ACQ_NAME, ""
        )
        return value.toString()
    }
}