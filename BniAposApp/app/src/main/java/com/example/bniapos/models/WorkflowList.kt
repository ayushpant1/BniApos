package com.example.bniapos.models

import com.google.gson.annotations.SerializedName

data class WorkflowList(


    @SerializedName("WORKFLOW") val wORKFLOW: List<WORKFLOW>


)

data class WORKFLOW(

    @SerializedName("ID") val iD: Int,
    @SerializedName("NAME") val nAME: String,
    @SerializedName("ENDPOINT") val eNDPOINT: String,
    @SerializedName("TYPE") val tYPE: String,
    @SerializedName("REVERSAL") val rEVERSAL: Int,
    @SerializedName("VOID") val vOID: Int,
    @SerializedName("RETRY") val rETRY: Int,
    @SerializedName("CTRLS") val cTRLS: CTRLS
)

data class CTRLS(

    @SerializedName("CTRL") val cTRL: List<String>,
    @SerializedName("REQ") val rEQ: String,
    @SerializedName("RESP") val rESP: String,
    @SerializedName("NEXTWORKFLOWID") val nEXTWORKFLOWID: Int
)