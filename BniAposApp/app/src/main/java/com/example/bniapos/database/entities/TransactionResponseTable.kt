package com.example.bniapos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transaction_response_table")
open class TransactionResponseTable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @SerializedName("MTID")
    @ColumnInfo(name = "mtId")
    var mtId: String? = null


    @SerializedName("MMID")
    @ColumnInfo(name = "mmId")
    var mmId: String? = null

    @SerializedName("TBID")
    @ColumnInfo(name = "tbID")
    var tbId: String? = null

    @SerializedName("TXNDATE")
    @ColumnInfo(name = "txn_date")
    var txnDate: String? = null

    @SerializedName("INV")
    @ColumnInfo(name = "inv")
    var inv: String? = null

    @SerializedName("SCID")
    @ColumnInfo(name = "scId")
    var scId: String? = null

    @SerializedName("TXNTYPE")
    @ColumnInfo(name = "txnType")
    var txnType: String? = null


    @SerializedName("STAN")
    @ColumnInfo(name = "stan")
    var stan: String? = null

    @SerializedName("POSENT")
    @ColumnInfo(name = "posent")
    var posent: String? = null

    @SerializedName("PANSEQ")
    @ColumnInfo(name = "panseq")
    var panseq: String? = null

    @SerializedName("AMT")
    @ColumnInfo(name = "amt")
    var amt: String? = null

    @SerializedName("EMV")
    @ColumnInfo(name = "emv")
    var emv: String? = null

    @SerializedName("EXPDATE")
    @ColumnInfo(name = "exp_date")
    var expDate: String? = null

    @SerializedName("CARDNO")
    @ColumnInfo(name = "card_no")
    var cardNo: String? = null
}