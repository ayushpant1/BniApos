package com.example.bniapos.models

import com.google.gson.annotations.SerializedName


data class WORKFLOW(

    @SerializedName("ID") val iD: Int,
    @SerializedName("NAME") val nAME: String,
    @SerializedName("ENDPOINT") val eNDPOINT: String,
    @SerializedName("TYPE") val tYPE: String,
    @SerializedName("REVERSAL") val rEVERSAL: Int,
    @SerializedName("RETRY") val rETRY: Int,
    @SerializedName("VOID") val vOID: Int,
    @SerializedName("CARDINPUTTYPE") val cARDINPUTTYPE: Int,
    @SerializedName("CTRLS") val cTRLS: List<CTRLS>,
    @SerializedName("REQ") val rEQ: String,
    @SerializedName("RESP") val rESP: String,
    @SerializedName("NEXTWORKFLOWID") val nEXTWORKFLOWID: Int
)


data class CTRLS(
    @SerializedName("KEY") val kEY: String,
    @SerializedName("LABEL") val lABEL: String,
    @SerializedName("CTYPE") val cTYPE: String,
    @SerializedName("MINSIZE") val mINSIZE: Int,
    @SerializedName("MAXSIZE") val mAXSIZE: Int,
    @SerializedName("DVAL") var dVAL: String,
    @SerializedName("SCN") val sCN: Int,
    @SerializedName("ORD") val oRD: Int,
    @SerializedName("dataSet") val dataSet: String,
    @SerializedName("relatedControlKey") val relatedControlKey: String,
    @SerializedName("object") var controlObject: Object,
)