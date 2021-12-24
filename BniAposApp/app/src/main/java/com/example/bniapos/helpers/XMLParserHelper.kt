package com.example.bniapos.helpers

import android.util.Xml
import com.example.bniapos.models.MasterPrintFormat
import com.example.bniapos.models.TerminalParams
import com.example.bniapos.utils.CommonUtility
import com.example.bniapos.utils.InputUtility
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

object XMLParserHelper {

    fun ParseXMLReceipt(XML: String): Map<Int, String>? {
        val parser = Xml.newPullParser()
        val _Output: MutableMap<Int, String> = HashMap()
        val AllPrintTags: ArrayList<MasterPrintFormat> = ArrayList<MasterPrintFormat>()
        var ReceiptID = 0
        try {
            // auto-detect the encoding from the stream
            parser.setInput(StringReader(XML))
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                var name: String? = null
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                    }
                    XmlPullParser.START_TAG -> {
                        name = parser.name
                        when (name.toUpperCase()) {
                            "RECEIPT" -> {
                            }
                            "ID" -> {
                                val ID = parser.nextText()
                                if (ID != null && ID.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                        ID
                                    )
                                ) ReceiptID = ID.toInt()
                            }
                            "TAG" -> {
                                val currentTAG: MasterPrintFormat = MasterPrintFormat()
                                val Text = parser.getAttributeValue(null, "Text")
                                if (Text != null) currentTAG.Printheader = Text
                                val Name = parser.getAttributeValue(null, "Name")
                                if (Name != null) currentTAG.Key = Name
                                val Order = parser.getAttributeValue(null, "Order")
                                if (Order != null) {
                                    if (Order != null && Order.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                            Order
                                        )
                                    ) currentTAG.Order = Order.toInt()
                                }
                                val Font = parser.getAttributeValue(null, "Font")
                                if (Font != null) {
                                    if (Font != null && Font.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                            Font
                                        )
                                    ) currentTAG.Font = Font.toInt()
                                } else currentTAG.Font = 3
                                val Alignment = parser.getAttributeValue(null, "Align")
                                if (Alignment != null) {
                                    currentTAG.Alignment = Alignment
                                }
                                AllPrintTags.add(currentTAG)
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        name = parser.name
                        when (name.toUpperCase()) {
                            "RECEIPT" -> {
                                var ReceiptFormat = ""
                                if (AllPrintTags != null && AllPrintTags.size > 0) Collections.sort(
                                    AllPrintTags,
                                    Comparator<MasterPrintFormat?> { obj1, obj2 ->
                                        Integer.valueOf(obj1.Order)
                                            .compareTo(Integer.valueOf(obj2.Order)) // To compare integer values
                                    })
                                ReceiptFormat = CommonUtility.PrintFormatListToJson(AllPrintTags)
                                _Output[ReceiptID] = ReceiptFormat
                            }
                            "ID" -> {
                            }
                            "TAG" -> {
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
        }
        return _Output
    }

    fun ParseXMLTerminalParameters(XML: String?, _TerminalParams: TerminalParams?): TerminalParams? {
        var _TerminalParams: TerminalParams? = _TerminalParams
        val parser = Xml.newPullParser()
        if (_TerminalParams == null) _TerminalParams = TerminalParams()
        try {
            // auto-detect the encoding from the stream
            parser.setInput(StringReader(XML?.uppercase()))
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                var name: String? = null
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                    }
                    XmlPullParser.START_TAG -> {
                        name = parser.name
                        when (name) {
                            "PARAMS" -> {
                            }
                            "PARAM" -> {
                                val ID = parser.getAttributeValue(null, "ID")
                                val VALUE = parser.getAttributeValue(null, "VALUE")
                                if (ID != null) {
                                    when (ID.toInt()) {
                                        1 -> {
                                            _TerminalParams.Merchant_Id = VALUE
                                        }
                                        2 -> {
                                            _TerminalParams.Terminal_Id=VALUE
                                        }
                                        3 -> {
                                            _TerminalParams.Merchant_Name=VALUE
                                        }
                                        4 -> {
                                            _TerminalParams.Merchant_Address=VALUE //Address1
                                        }
                                        5 -> {
                                            _TerminalParams.Merchant_City = VALUE //Address2
                                        }
                                        6 -> {
                                            _TerminalParams.Server_Date_Time=VALUE
                                        }
                                        7 -> {
                                            if (VALUE != null && VALUE.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                                    VALUE
                                                )
                                            ) {
                                                _TerminalParams.Number_Of_Print = VALUE.toInt()
                                            } else {
                                                _TerminalParams.Number_Of_Print = 1
                                            }
                                        }
                                        8 -> {
                                            if (VALUE != null && VALUE.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                                    VALUE
                                                )
                                            ) _TerminalParams.BatchNo = VALUE.toInt()
                                            else _TerminalParams.BatchNo = 0
                                        }
                                        9 -> {
                                            _TerminalParams.InitAppVersion = VALUE
                                        }
                                        10 -> {
                                            _TerminalParams.DownloadLocation = VALUE
                                        }
                                        11 -> {
                                            _TerminalParams.Transaction_MIN_MAX = VALUE
                                        }
                                        12 -> {
                                            _TerminalParams.primaryConnection = VALUE
                                        }
                                        13 -> {
                                            _TerminalParams.secondaryConnection = VALUE
                                        }
                                        14 -> {
                                            _TerminalParams.primaryTMSConnection = VALUE
                                        }
                                        15 -> {
                                            _TerminalParams.secondaryTMSConnection = VALUE
                                        }
                                        16 -> {
                                            if (VALUE != null && VALUE.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                                    VALUE
                                                )
                                            ) _TerminalParams.StatFrequency = VALUE.toInt() else _TerminalParams.StatFrequency = 300
                                        }
                                        17 -> {
                                            if (VALUE != null && VALUE.trim { it <= ' ' }.length > 0 && InputUtility.isValidInt(
                                                    VALUE
                                                )
                                            ) _TerminalParams.HeartFrequency = VALUE.toInt() else _TerminalParams.HeartFrequency = 600
                                        }
                                        18 -> {
                                            _TerminalParams.TerminalPIN = VALUE
                                        }
                                        19 -> {
                                            if (VALUE != null && VALUE.trim { it <= ' ' }.length > 0) {
                                                var ParamValue = VALUE.trim { it <= ' ' }
                                                ParamValue = ParamValue.replace(" ", "")
                                                _TerminalParams.AllowedTransactions = ParamValue
                                            } else _TerminalParams.AllowedTransactions = ""
                                        }
                                        20 -> {
                                            if (VALUE != null && VALUE.trim { it <= ' ' }.length > 0) {
                                                var ParamValue = VALUE.trim { it <= ' ' }
                                                ParamValue = ParamValue.replace(" ", "")
                                                _TerminalParams.AllowedPayments = ParamValue
                                            } else _TerminalParams.AllowedPayments = ""
                                        }
                                        21 -> {
                                            _TerminalParams.primaryAPIConnection = VALUE
                                        }
                                        22 -> {
                                            _TerminalParams.secondaryAPIConnection = VALUE
                                        }
                                    }
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
        }
        return _TerminalParams
    }

}