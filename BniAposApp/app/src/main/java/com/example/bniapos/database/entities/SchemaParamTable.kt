package com.example.bniapos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "schema_param_table")
open class SchemaParamTable {
    @PrimaryKey
    var schemaId = 0

    @ColumnInfo(name = "receiptLineItem")
    var receiptLineItem: String? = null


}