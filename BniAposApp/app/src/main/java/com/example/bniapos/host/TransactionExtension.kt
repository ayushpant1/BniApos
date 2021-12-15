package com.example.bniapos.host

import android.app.Activity
import android.view.SurfaceControl
import com.example.bniapos.enums.TransactionRequestKeys
import com.example.bniapos.models.WORKFLOW
import com.example.bniapos.utils.DateTimeUtils
import com.example.bniapos.utils.SharedPreferenceUtils
import com.example.bniapos.utils.Util
import com.example.paymentsdk.CardReadOutput
import com.google.gson.Gson
import com.google.gson.JsonElement
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
        val value = getMasterValue(
            context, cardReadOutput,
            this, transactionType, currentWORKFLOW, it
        )
        jsonObject.addProperty(it, value)
    }

    val splitRequestEnc = currentWORKFLOW.encryptedRequest.split(",")
    splitRequestEnc.forEach {
        val value = getMasterValue(
            context, cardReadOutput,
            this, transactionType, currentWORKFLOW, it
        )
        jsonObject.addProperty(it, encrypt(value))
    }
    if (currentWORKFLOW.dataRequest != null && currentWORKFLOW.dataRequest.isNotEmpty()) {

        val jsonObjectData = JsonObject()
        val splitRequestData = currentWORKFLOW.dataRequest.split(",")

        splitRequestData.forEach {
            val value = getMasterValue(
                context, cardReadOutput,
                this, transactionType, currentWORKFLOW, it
            )
            jsonObjectData.addProperty(it, value)
        }
        jsonObject.add("data", jsonObjectData)
    }


    return jsonObject

}

fun getMasterValue(
    context: Activity,
    cardReadOutput: CardReadOutput,
    jsonObject: JsonObject,
    transactionType: Int,
    currentWORKFLOW: WORKFLOW,
    key: String
): String? {
    return when (key) {
        TransactionRequestKeys.MTID.name -> {
            getMtId(context)
        }
        TransactionRequestKeys.IID.name -> {
            getIId()
        }
        TransactionRequestKeys.MMID.name -> {
            getMmId(context)
        }
        TransactionRequestKeys.CKEY.name -> {
            getCkey()
        }
        TransactionRequestKeys.AUTH.name -> {
            getAuth(context)
        }
        TransactionRequestKeys.DID.name -> {
            getDid()
        }
        TransactionRequestKeys.PTYPE.name -> {
            getPType()
        }
        TransactionRequestKeys.reffNum.name,
        TransactionRequestKeys.REFNO.name -> {
            getRefNo(context)
        }
        TransactionRequestKeys.ACODE.name -> {
            getACode()
        }
        TransactionRequestKeys.ACNO.name -> {
            getACNo(jsonObject)
        }
        TransactionRequestKeys.TBID.name -> {
            getTbId(context)
        }
        TransactionRequestKeys.TXNDT.name -> {

            getTxnDate(cardReadOutput)

        }
        TransactionRequestKeys.INV.name -> {
            getInv(context)
        }
        TransactionRequestKeys.SCID.name -> {
            getScId(currentWORKFLOW)
        }
        TransactionRequestKeys.TXNTYPE.name -> {

            transactionType.toString()

        }
        TransactionRequestKeys.STAN.name -> {
            getStan(context)
        }
        TransactionRequestKeys.POSENT.name -> {

            getPosent(cardReadOutput)

        }
        TransactionRequestKeys.PANSEQ.name -> {

            getPanSeq(cardReadOutput)

        }
        TransactionRequestKeys.AMT.name -> {

            getAmt(cardReadOutput, jsonObject)

        }
        TransactionRequestKeys.EMV.name,
        TransactionRequestKeys.EMVD.name -> {
            getEmv(cardReadOutput)
        }
        TransactionRequestKeys.PINB.name -> {
            getPinB(cardReadOutput)
        }
        TransactionRequestKeys.EXPIRY.name,
        TransactionRequestKeys.EXPDATE.name -> {

            getExpDate(cardReadOutput)

        }
        TransactionRequestKeys.CARDNO.name -> {

            getCardNo(cardReadOutput)

        }


        TransactionRequestKeys.VOIDROC.name -> {

            getVoidRoc()

        }
        TransactionRequestKeys.T2D.name -> {

            getT2D(cardReadOutput)

        }
        else -> {
            if (jsonObject.has(key)) {
                jsonObject.get(key).asString
            } else ""
        }
        /*    TransactionRequestKeys.CSID.name -> {
            jsonObject.addProperty(
                it,
                getCsId(cardReadOutput)
            )
        }*/

    }
}

fun encrypt(valueToEncrypt: String?): String? {
    return valueToEncrypt
}

fun getIId(): String? {
    return "2"
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

fun getAuth(context: Activity): String {
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


