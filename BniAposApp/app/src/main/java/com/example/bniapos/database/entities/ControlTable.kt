package com.example.bniapos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "control_table")
open class ControlTable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @SerializedName("value")
    @ColumnInfo(name = "value")
    var value: String? = null


    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String? = null

    @SerializedName("dataSetKey")
    @ColumnInfo(name = "data_set_key")
    var dataSetKey: String? = null

    @SerializedName("referenceDataSetKey")
    @ColumnInfo(name = "reference_data_set_key")
    var referenceDataSetKey: String? = null
}