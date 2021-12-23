package com.example.bniapos.helpers

import android.content.Context
import com.example.bniapos.enums.TransactionRequestKeys
import com.example.bniapos.models.MasterPrintFormat
import com.example.bniapos.utils.CurrencyUtility
import com.example.bniapos.utils.DateTimeUtils
import com.example.bniapos.utils.SharedPreferenceUtils
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

object TransactionPrintingHelper {
    fun ProcessPrintingTags(
        _context: Context, _Params: ArrayList<MasterPrintFormat>,
        jsonParamRequest: JSONObject, jsonParamResponse: JSONObject
    ): ArrayList<MasterPrintFormat> {
        try {

            var currentIndex = 0
            for (eachParam in _Params) {
                try {
                    val TAG: String =
                        eachParam!!.Key?:""//Here we are using Key .. .so Need to Remove all other Data from here...
                    when (TAG) {

                        "amount",
                        TransactionRequestKeys.AMT.name -> {
                            var Amount = ProcessFromResponse("AMT",jsonParamRequest,jsonParamResponse)

                            if(Amount.equals(""))
                                Amount = ProcessFromResponse("amount",jsonParamRequest,jsonParamResponse)
                            eachParam.Text = (CurrencyUtility.ReturnCurrency(Amount))?:""
                            _Params[currentIndex] = eachParam
                        }
                        TransactionRequestKeys.TXNDT.name -> {
                            val date = Date()
                            val formattedDate: String =
                                DateTimeUtils.GetDate(jsonParamRequest.getString("TXNDT"))
                            val formattedTime: String =
                                DateTimeUtils.GetTime(jsonParamRequest.getString("TXNDT"))
                            eachParam.Text = ("$formattedDate $formattedTime")
                            _Params[currentIndex] = eachParam
                        }
                        "DATE" -> {
                            val formattedDate: String =
                                DateTimeUtils.GetDate(jsonParamRequest.getString("TXNDT"))
                            eachParam.Text = (formattedDate)
                            _Params[currentIndex] = eachParam
                        }
                        "TIME" -> {
                            val formattedTime: String =
                                DateTimeUtils.GetTime(jsonParamRequest.getString("TXNDT"))
                            eachParam.Text = (formattedTime)
                            _Params[currentIndex] = eachParam
                        }

                        "MRN" -> {
                            eachParam.Text = (
                                SharedPreferenceUtils.getInstance(_context).getMerchantName()
                            )
                            _Params[currentIndex] = eachParam
                        }
                        "MAL1" -> {
                            eachParam.Text = (
                               SharedPreferenceUtils.getInstance(_context).getAddressLine1()
                            )
                            _Params[currentIndex] = eachParam
                        }
                        "MAL2" -> {
                            eachParam.Text = (
                                    SharedPreferenceUtils.getInstance(_context).getAddressLine2()
                            )
                            _Params[currentIndex] = eachParam
                        }
                        "CTY" -> {
                            eachParam.Text = ( SharedPreferenceUtils.getInstance(_context).getMerchantCity())
                            _Params[currentIndex] = eachParam
                        }
                        "AQN" -> {
                            eachParam.Text = (
                                    SharedPreferenceUtils.getInstance(_context).getAcquirerName()
                            )
                            _Params[currentIndex] = eachParam
                        }

                        "","BNK", "FLN0", "FLN1", "FLN2", "FLN3", "FLN4", "FLN5" -> {
                            var Footer_Text: String? = eachParam.Printheader

                            if (Footer_Text == null) Footer_Text = ""
                            if (Footer_Text.length > 0) {
                                eachParam.Text = (Footer_Text)
                                eachParam.Printheader = ("")
                                _Params[currentIndex] = eachParam
                            }
                        }

                        TransactionRequestKeys.CARDNO.name -> {


//                        output.put("CARD NO", InputUtil.ShowLastDigitsOnly(
//                                ProcessFromResponse("CardNo", jsonParamRequest, jsonParamResponse)));

                            //Bye Pass for BRI
                            var Mode: String = ProcessFromResponse(
                                TransactionRequestKeys.POSENT.name,
                                jsonParamRequest,
                                jsonParamResponse
                            )
                            if (Mode != null && Mode.length > 0) {
                                Mode = if (Mode.startsWith("05")) {
                                    "DIP"
                                } else if (Mode.startsWith("02")) {
                                    "SWIPE"
                                } else if (Mode.startsWith("07")) {
                                    "TAP"
                                } else ""
                            }
                            //InputUtil.ShowLastDigitsOnly(
                            val CardNo: String = ProcessFromResponse(
                                TransactionRequestKeys.CARDNO.name,
                                jsonParamRequest,
                                jsonParamResponse
                            ) + " " + Mode
                            eachParam.Text = (CardNo)
                            _Params[currentIndex] = eachParam
                        }

                        TransactionRequestKeys.EXPIRY.name -> {
                            val EXPIRY = "**/**"
                            eachParam.Text = (EXPIRY)
                            _Params[currentIndex] = eachParam
                        }

                        else -> {
                            try {
                                if (eachParam.Text == null || eachParam.Text
                                        .length <= 0
                                ) {
                                    val Value: String = ProcessFromResponse(
                                        TAG,
                                        jsonParamRequest,
                                        jsonParamResponse
                                    )
                                    eachParam.Text = (Value)
                                }
                                _Params[currentIndex] = eachParam
                            } catch (ex: Exception) {
                            }
                        }
                    }
                } catch (ex: Exception) {
                }
                currentIndex++
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return _Params
    }
    fun ProcessFromResponse(
        Key: String?,
        jsonParamRequest: JSONObject,
        jsonParamResponse: JSONObject
    ): String {
        try {
            if (!jsonParamResponse.isNull(Key)) {
                return jsonParamResponse[Key].toString()
            }
            if (!jsonParamRequest.isNull(Key)) {
                return jsonParamRequest[Key].toString()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }

}