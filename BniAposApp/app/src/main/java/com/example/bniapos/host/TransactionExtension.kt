package com.example.bniapos.host

import android.app.Activity
import com.example.bniapos.enums.TransactionRequestKeys
import com.example.bniapos.models.WORKFLOW
import com.example.bniapos.utils.DateTimeUtils
import com.example.bniapos.utils.SharedPreferenceUtils
import com.example.bniapos.utils.Util
import com.example.paymentsdk.CardReadOutput
import com.google.gson.Gson
import com.google.gson.JsonObject

fun JsonObject.toTransactionRequest(
    currentWORKFLOW: WORKFLOW,
    transactionType: Int,
    context: Activity
): JsonObject {
    val jsonObject = JsonObject()
    val cardReadOutput = getCardInfo(this)
    val splitRequest = currentWORKFLOW.rEQ.split(",")
    splitRequest.forEach {
        when (it) {
            TransactionRequestKeys.MTID.name -> {
                jsonObject.addProperty(it, getMtId(context))
            }
            TransactionRequestKeys.MMID.name -> {
                jsonObject.addProperty(it, getMmId(context))
            }
            TransactionRequestKeys.CKEY.name -> {
                jsonObject.addProperty(it, getCkey())
            }
            TransactionRequestKeys.AUTH.name -> {
                jsonObject.addProperty(it, getAuth(context))
            }
            TransactionRequestKeys.DID.name -> {
                jsonObject.addProperty(it, getDid())
            }
            TransactionRequestKeys.PTYPE.name -> {
                jsonObject.addProperty(it, getPType())
            }
            TransactionRequestKeys.REFNO.name -> {
                jsonObject.addProperty(it, getRefNo(context))
            }
            TransactionRequestKeys.ACODE.name -> {
                jsonObject.addProperty(it, getACode())
            }
            TransactionRequestKeys.ACNO.name -> {
                jsonObject.addProperty(it, getACNo(this))
            }
            TransactionRequestKeys.TBID.name -> {
                jsonObject.addProperty(it, getTbId(context))
            }
            TransactionRequestKeys.TXNDT.name -> {
                jsonObject.addProperty(
                    it,
                    getTxnDate(cardReadOutput)
                )
            }
            TransactionRequestKeys.INV.name -> {
                jsonObject.addProperty(it, getInv(context))
            }
            TransactionRequestKeys.SCID.name -> {
                jsonObject.addProperty(it, getScId(currentWORKFLOW))
            }
            TransactionRequestKeys.TXNTYPE.name -> {
                jsonObject.addProperty(
                    it,
                    transactionType.toString()
                )
            }
            TransactionRequestKeys.STAN.name -> {
                jsonObject.addProperty(it, getStan(context))
            }
            TransactionRequestKeys.POSENT.name -> {
                jsonObject.addProperty(
                    it,
                    getPosent(cardReadOutput)
                )
            }
            TransactionRequestKeys.PANSEQ.name -> {
                jsonObject.addProperty(
                    it,
                    getPanSeq(cardReadOutput)
                )
            }
            TransactionRequestKeys.AMT.name -> {
                jsonObject.addProperty(
                    it,
                    getAmt(cardReadOutput, this)
                )
            }
            TransactionRequestKeys.EMV.name,
            TransactionRequestKeys.EMVD.name -> {
                jsonObject.addProperty(it, getEmv(cardReadOutput))
            }
            TransactionRequestKeys.PINB.name -> {
                jsonObject.addProperty(it, getPinB(cardReadOutput))
            }
            TransactionRequestKeys.EXPIRY.name,
            TransactionRequestKeys.EXPDATE.name -> {
                jsonObject.addProperty(
                    it,
                    getExpDate(cardReadOutput)
                )
            }
            TransactionRequestKeys.CARDNO.name -> {
                jsonObject.addProperty(
                    it,
                    getCardNo(cardReadOutput)
                )
            }
            TransactionRequestKeys.DESC.name -> {
                jsonObject.addProperty(
                    it,
                    this.get(TransactionRequestKeys.DESC.name).toString()
                )
            }
            TransactionRequestKeys.CNTRY.name -> {
                jsonObject.addProperty(
                    it,
                    this.get(TransactionRequestKeys.CNTRY.name).toString()
                )
            }


            TransactionRequestKeys.country.name -> {
                if (jsonObject.has(TransactionRequestKeys.country.name)) {
                    jsonObject.addProperty(
                        it,
                        this.get(TransactionRequestKeys.country.name).toString()
                    )
                }
            }
            TransactionRequestKeys.state.name -> {
                if (jsonObject.has(TransactionRequestKeys.state.name)) {
                    jsonObject.addProperty(
                        it,
                        this.get(TransactionRequestKeys.state.name).toString()
                    )
                }
            }
            TransactionRequestKeys.city.name -> {
                if (jsonObject.has(TransactionRequestKeys.city.name)) {
                    jsonObject.addProperty(
                        it,
                        this.get(TransactionRequestKeys.city.name).toString()
                    )
                }
            }
            TransactionRequestKeys.RMRK.name -> {
                jsonObject.addProperty(
                    it,
                    this.get(TransactionRequestKeys.RMRK.name).toString()
                )
            }
            TransactionRequestKeys.PHN.name -> {
                jsonObject.addProperty(
                    it,
                    this.get(TransactionRequestKeys.PHN.name).toString()
                )
            }
            TransactionRequestKeys.FRSTNM.name -> {
                jsonObject.addProperty(
                    it,
                    this.get(TransactionRequestKeys.FRSTNM.name).toString()
                )
            }
            TransactionRequestKeys.LSTNM.name -> {
                jsonObject.addProperty(
                    it,
                    this.get(TransactionRequestKeys.LSTNM.name).toString()
                )
            }
            /*  TransactionRequestKeys.IID.name -> {
                  jsonObject.addProperty(
                      it,
                      geiId()
                  )
              }*/
            TransactionRequestKeys.VOIDROC.name -> {
                jsonObject.addProperty(
                    it,
                    getVoidRoc()
                )
            }
            TransactionRequestKeys.T2D.name -> {
                jsonObject.addProperty(
                    it,
                    getT2D(cardReadOutput)
                )
            }
            /*    TransactionRequestKeys.CSID.name -> {
                    jsonObject.addProperty(
                        it,
                        getCsId(cardReadOutput)
                    )
                }*/

        }

    }
    return jsonObject

}

fun geiId(): String? {
    return ""
}

fun getVoidRoc(): String? {
    return ""

}

fun getCsId(cardReadOutput: CardReadOutput): String? {
    return ""
}


fun getT2D(cardReadOutput: CardReadOutput): String? {
    return cardReadOutput.track2Data
}


fun getMmId(context: Activity): String {
    return SharedPreferenceUtils.getInstance(context).getMmId()
}

fun getMtId(context: Activity): String {
    return SharedPreferenceUtils.getInstance(context).getMtId()
}

fun getTbId(context: Activity): String {
    return SharedPreferenceUtils.getInstance(context).getTbId()
}

fun getCkey(): String {
    return "RFVNTVlNSzA2QjZKOjk5REE4OGZMR3hGVGFCYkk="
}

fun getAuth(context:Activity): String {
    return SharedPreferenceUtils.getInstance(context).getAuthCode()
}

fun getDid(): String {
    return "2616272837430654"
}

fun getPType(): String {
    return "2500116"
}

fun getRefNo(context: Activity): String {
    return Util.getReferenceNo(context)
}

fun getACode(): String {
    return "0021"
}

fun getACNo(jsonObject: JsonObject): String {
    return if (jsonObject.has("ACNO"))
        jsonObject.get("ACNO").asString
    else
        ""
}

fun getTxnDate(cardReadOutput: CardReadOutput): String {
    return DateTimeUtils.getCurrentDateTimeYYMMDDHHMMSS()
}

fun getInv(context: Activity): String {
    return SharedPreferenceUtils.getInstance(context).getInvoiceNo()
}

fun getScId(currentWORKFLOW: WORKFLOW): String {
    return currentWORKFLOW.iD.toString()
}

fun getTxnType(currentWORKFLOW: WORKFLOW): String {
    return currentWORKFLOW.nAME
}

fun getStan(context: Activity): String {
    var Stan = SharedPreferenceUtils.getInstance(context).getStan()
    return String.format("%05d", Stan)
    SharedPreferenceUtils.getInstance(context).setStan(Stan + 1)
}

fun getAgentCounterCode(context: Activity): String {
    var Stan = SharedPreferenceUtils.getInstance(context).getStan()
    return String.format("%05d", Stan)
    SharedPreferenceUtils.getInstance(context).setStan(Stan + 1)
}

fun getPosent(cardReadOutput: CardReadOutput): String {
    return "05"//cardReadOutput.insertMode
}

fun getPanSeq(cardReadOutput: CardReadOutput): String {
    return cardReadOutput.panseq
}

fun getAmt(cardReadOutput: CardReadOutput, jsonObject: JsonObject): String {
    return if (cardReadOutput.txnAmount == null) {
        if (jsonObject != null && jsonObject.has(TransactionRequestKeys.AMT.name)) {
            jsonObject.get(TransactionRequestKeys.AMT.name).asString
        } else {
            "0000"
        }
    } else
        cardReadOutput.txnAmount
}

fun getEmv(cardReadOutput: CardReadOutput): String {
    return cardReadOutput.emvData
}

fun getPinB(cardReadOutput: CardReadOutput): String? {
    return cardReadOutput.pinBlock
}

fun getExpDate(cardReadOutput: CardReadOutput): String {
    return cardReadOutput.cardExpiry.replace("/", "")
}

fun getCardNo(cardReadOutput: CardReadOutput): String {
    return cardReadOutput.cardNo
}


fun getCardInfo(jsonObject: JsonObject): CardReadOutput {
    return if (jsonObject.has("PIN"))
        Gson().fromJson(jsonObject.get("PIN"), CardReadOutput::class.java)
    else
        CardReadOutput()
}


