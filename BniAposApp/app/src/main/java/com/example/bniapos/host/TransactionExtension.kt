package com.example.bniapos.host

import com.example.bniapos.enums.TransactionRequestKeys
import com.example.bniapos.models.WORKFLOW
import com.example.paymentsdk.CardReadOutput
import com.google.gson.Gson
import com.google.gson.JsonObject

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
            TransactionRequestKeys.EMV.name -> {
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
    return "782367823648932"
}

fun getMtId(): String {
    return "983269832694"
}

fun getTbId(): String {
    return "091820980129"
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
        jsonObject.get(TransactionRequestKeys.AMT.name).toString()
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


