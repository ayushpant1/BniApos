package com.example.bniapos.host

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bniapos.enums.TransactionRequestKeys
import com.example.bniapos.models.WORKFLOW
import com.example.paymentsdk.CardReadOutput
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

fun JsonObject.toTransactionRequest(currentWORKFLOW: WORKFLOW): JsonObject {
    val jsonObject = JsonObject()
    val cardReadOutput = getCardInfo(this)
    val splitRequest = currentWORKFLOW.rEQ.split(",")
    splitRequest.forEach {
        when (it) {
            TransactionRequestKeys.MTID.name -> {
                jsonObject.addProperty(TransactionRequestKeys.MTID.name, getMtId())
            }
            TransactionRequestKeys.MMID.name -> {
                jsonObject.addProperty(TransactionRequestKeys.MMID.name, getMmId())
            }
            TransactionRequestKeys.CKEY.name -> {
                jsonObject.addProperty(TransactionRequestKeys.CKEY.name, getCkey())
            }
            TransactionRequestKeys.AUTH.name -> {
                jsonObject.addProperty(TransactionRequestKeys.AUTH.name, getAuth())
            }
            TransactionRequestKeys.DID.name -> {
                jsonObject.addProperty(TransactionRequestKeys.DID.name, getDid())
            }
            TransactionRequestKeys.PTYPE.name -> {
                jsonObject.addProperty(TransactionRequestKeys.PTYPE.name, getPType())
            }
            TransactionRequestKeys.REFNO.name -> {
                jsonObject.addProperty(TransactionRequestKeys.REFNO.name, getRefNo())
            }
            TransactionRequestKeys.ACODE.name -> {
                jsonObject.addProperty(TransactionRequestKeys.ACODE.name, getACode())
            }
            TransactionRequestKeys.ACNO.name -> {
                jsonObject.addProperty(TransactionRequestKeys.ACNO.name, getACNo(this))
            }
            TransactionRequestKeys.TBID.name -> {
                jsonObject.addProperty(TransactionRequestKeys.TBID.name, getTbId())
            }
            TransactionRequestKeys.TXNDATE.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.TXNDATE.name,
                    getTxnDate(cardReadOutput)
                )
            }
            TransactionRequestKeys.INV.name -> {
                jsonObject.addProperty(TransactionRequestKeys.INV.name, getInv())
            }
            TransactionRequestKeys.SCID.name -> {
                jsonObject.addProperty(TransactionRequestKeys.SCID.name, getScId())
            }
            TransactionRequestKeys.TXNTYPE.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.TXNTYPE.name,
                    getTxnType(currentWORKFLOW)
                )
            }
            TransactionRequestKeys.STAN.name -> {
                jsonObject.addProperty(TransactionRequestKeys.STAN.name, getStan())
            }
            TransactionRequestKeys.POSENT.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.POSENT.name,
                    getPosent(cardReadOutput)
                )
            }
            TransactionRequestKeys.PANSEQ.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.PANSEQ.name,
                    getPanSeq(cardReadOutput)
                )
            }
            TransactionRequestKeys.AMT.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.AMT.name,
                    getAmt(cardReadOutput, this)
                )
            }
            TransactionRequestKeys.EMV.name,
            TransactionRequestKeys.EMVD.name -> {
                jsonObject.addProperty(TransactionRequestKeys.EMV.name, getEmv(cardReadOutput))
            }
            TransactionRequestKeys.PINB.name -> {
                jsonObject.addProperty(TransactionRequestKeys.PINB.name, getPinB(cardReadOutput))
            }
            TransactionRequestKeys.EXPDATE.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.EXPDATE.name,
                    getExpDate(cardReadOutput)
                )
            }
            TransactionRequestKeys.CARDNO.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.CARDNO.name,
                    getCardNo(cardReadOutput)
                )
            }
            TransactionRequestKeys.DESC.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.DESC.name,
                    this.get(TransactionRequestKeys.DESC.name).toString()
                )
            }
            TransactionRequestKeys.CNTRY.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.CNTRY.name,
                    this.get(TransactionRequestKeys.CNTRY.name).toString()
                )
            }

            TransactionRequestKeys.TXNTYPE.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.TXNTYPE.name,
                    this.get(TransactionRequestKeys.TXNTYPE.name).toString()
                )
            }
            TransactionRequestKeys.country.name -> {
                if (jsonObject.has(TransactionRequestKeys.country.name)) {
                    jsonObject.addProperty(
                        TransactionRequestKeys.country.name,
                        this.get(TransactionRequestKeys.country.name).toString()
                    )
                }
            }
            TransactionRequestKeys.state.name -> {
                if (jsonObject.has(TransactionRequestKeys.state.name)) {
                    jsonObject.addProperty(
                        TransactionRequestKeys.state.name,
                        this.get(TransactionRequestKeys.state.name).toString()
                    )
                }
            }
            TransactionRequestKeys.city.name -> {
                if (jsonObject.has(TransactionRequestKeys.city.name)) {
                    jsonObject.addProperty(
                        TransactionRequestKeys.city.name,
                        this.get(TransactionRequestKeys.city.name).toString()
                    )
                }
            }
            TransactionRequestKeys.RMRK.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.RMRK.name,
                    this.get(TransactionRequestKeys.RMRK.name).toString()
                )
            }
            TransactionRequestKeys.PHN.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.PHN.name,
                    this.get(TransactionRequestKeys.PHN.name).toString()
                )
            }
            TransactionRequestKeys.FRSTNM.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.FRSTNM.name,
                    this.get(TransactionRequestKeys.FRSTNM.name).toString()
                )
            }
            TransactionRequestKeys.LSTNM.name -> {
                jsonObject.addProperty(
                    TransactionRequestKeys.LSTNM.name,
                    this.get(TransactionRequestKeys.LSTNM.name).toString()
                )
            }
        }

    }
    return jsonObject

}


fun getMmId(): String {
    return "1020000000002"
}

fun getMtId(): String {
    return "1040000000001"
}

fun getTbId(): String {
    return "091820980129"
}

fun getCkey(): String {
    return "RFVNTVINSzA2QjZKOjk5REE4OGZMR3hGVGFCYkk="
}

fun getAuth(): String {
    return "THISISAUTHTOKEN"
}

fun getDid(): String {
    return "2616272837430654"
}

fun getPType(): String {
    return "2500116"
}

@RequiresApi(Build.VERSION_CODES.O)
fun getRefNo(): String {
    return "2021120917054000001"
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
    return cardReadOutput.txnDate
}

fun getInv(): String {
    return (0..1000).random().toString()
}

fun getScId(): String {
    return (0..1000).random().toString()
}

fun getTxnType(currentWORKFLOW: WORKFLOW): String {
    return currentWORKFLOW.nAME
}

fun getStan(): String {
    return (0..10).random().toString()
}

fun getPosent(cardReadOutput: CardReadOutput): String {
    return cardReadOutput.insertMode
}

fun getPanSeq(cardReadOutput: CardReadOutput): String {
    return cardReadOutput.panseq
}

fun getAmt(cardReadOutput: CardReadOutput, jsonObject: JsonObject): String {
    return if (cardReadOutput.txnAmount == null) {
        jsonObject.get(TransactionRequestKeys.AMT.name).asString
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
    return cardReadOutput.cardExpiry
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


