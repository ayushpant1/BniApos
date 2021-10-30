package com.example.bniapos.models

import com.google.gson.annotations.SerializedName

data class ControlList(

    @SerializedName("controlKey") val controlKey: String,
    @SerializedName("controlType") val controlType: String,
    @SerializedName("defaultValue") val defaultValue: String,
    @SerializedName("relatedControlKey") val relatedControlKey: String,
    @SerializedName("label") val label: String,
    @SerializedName("screenId") val screenId: Int,
    @SerializedName("sortOrder") val sortOrder: Int,
    @SerializedName("maxLength") val maxLength: Int,
    @SerializedName("minLength") val minLength: Int,
    @SerializedName("dataSet") val dataSet: String,
    @SerializedName("object") var controlObject: Object,
    @SerializedName("controlReferenceValue") var controlReferenceValue: String
)