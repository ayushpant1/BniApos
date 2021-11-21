package com.example.bniapos.host

import android.content.Context
import com.example.bniapos.database.DatabaseClient
import com.example.bniapos.database.entities.TransactionResponseTable
import com.example.bniapos.enums.TransactionResponseKeys
import com.example.bniapos.models.WORKFLOW
import com.google.gson.JsonObject

fun JsonObject.saveToDatabase(
    context: Context,
    currentWORKFLOW: WORKFLOW
): TransactionResponseTable {
    val splitRequest = currentWORKFLOW.rESP.split(",")
    val transactionResponseTable = TransactionResponseTable()
    splitRequest.forEach {
        when (it) {
            TransactionResponseKeys.MTID.name -> {
                this.get(TransactionResponseKeys.MTID.name).let { element ->
                    transactionResponseTable.mtId == element.toString()
                }
            }
            TransactionResponseKeys.MMID.name -> {
                this.get(TransactionResponseKeys.MMID.name).let { element ->
                    transactionResponseTable.mmId = element.toString()
                }
            }
            TransactionResponseKeys.TBID.name -> {
                this.get(TransactionResponseKeys.TBID.name).let { element ->
                    transactionResponseTable.tbId = element.toString()
                }
            }
            TransactionResponseKeys.TXNDATE.name -> {
                this.get(TransactionResponseKeys.TXNDATE.name).let { element ->
                    transactionResponseTable.txnDate = element.toString()
                }
            }
            TransactionResponseKeys.INV.name -> {
                this.get(TransactionResponseKeys.INV.name).let { element ->
                    transactionResponseTable.inv = element.toString()
                }
            }
            TransactionResponseKeys.SCID.name -> {
                this.get(TransactionResponseKeys.SCID.name).let { element ->
                    transactionResponseTable.scId = element.toString()
                }
            }
            TransactionResponseKeys.TXNTYPE.name -> {
                this.get(TransactionResponseKeys.TXNTYPE.name).let { element ->
                    transactionResponseTable.txnType = element.toString()
                }
            }
            TransactionResponseKeys.STAN.name -> {
                this.get(TransactionResponseKeys.STAN.name).let { element ->
                    transactionResponseTable.stan = element.toString()
                }
            }
            TransactionResponseKeys.POSENT.name -> {
                this.get(TransactionResponseKeys.POSENT.name).let { element ->
                    transactionResponseTable.posent = element.toString()
                }
            }
            TransactionResponseKeys.PANSEQ.name -> {
                this.get(TransactionResponseKeys.PANSEQ.name).let { element ->
                    transactionResponseTable.panseq = element.toString()
                }
            }
            TransactionResponseKeys.AMT.name -> {
                this.get(TransactionResponseKeys.AMT.name).let { element ->
                    transactionResponseTable.amt = element.toString()
                }
            }
            TransactionResponseKeys.EMV.name -> {
                this.get(TransactionResponseKeys.EMV.name).let { element ->
                    transactionResponseTable.emv = element.toString()
                }
            }
            TransactionResponseKeys.EXPDATE.name -> {
                this.get(TransactionResponseKeys.EXPDATE.name).let { element ->
                    transactionResponseTable.expDate = element.toString()
                }
            }
            TransactionResponseKeys.CARDNO.name -> {
                this.get(TransactionResponseKeys.CARDNO.name).let { element ->
                    transactionResponseTable.cardNo = element.toString()
                }
            }
        }

    }

    DatabaseClient.getInstance(context)?.appDatabase?.transactionResponseDao()
        ?.insert(transactionResponseTable)

    return transactionResponseTable
}




