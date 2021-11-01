package com.example.bniapos.models

import com.google.gson.annotations.SerializedName

data class MenuList(

    @SerializedName("id") val id: Int,
    @SerializedName("parentId") val parentId: Int,
    @SerializedName("displayText") val displayText: String,
    @SerializedName("IconName") val iconName: String,
    @SerializedName("sortOrder") val sortOrder: Int,
    @SerializedName("Type") val type: String,
    @SerializedName("schemaId") val schemaId: Int,

    )