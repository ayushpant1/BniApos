package com.example.bniapos.host

import android.app.Activity
import android.content.Context
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.models.UpdateRequest
import com.example.bniapos.models.WORKFLOW
import com.google.gson.JsonObject
import org.json.JSONObject

interface HostRepositoryInterface {
    /**
     * method responsible to post request to the server
     * @param context
     * @param jsonObject request json
     * @param url host url
     * @param currentWORKFLOW current workflow
     * @param isBpWorkflow cp workflow or bp
     * @param bpWorkflowOutputData bpWorkflowOutputData
     */

    suspend fun postData(
        context: Activity,
        jsonObject: JsonObject,
        url: String,
        currentWORKFLOW: WORKFLOW,
        apiResult: ApiResult,
        transactionType: Int,
        isBpWorkflow: Boolean = false,
        bpWorkflowOutputData: String? = null
    )


    /**
     * method responsible to perform Logon
     * @param context
     */

    suspend fun performLogon(
        context: Activity,
        url: String,
        authorization: String,
        grantType: String,
        apiResult: ApiResult
    )

    /**
     * method responsible to perform initialization
     * @param context
     */

    suspend fun performIntialization(
        context: Activity,
        url: String,
        request: UpdateRequest,
        apiResult: ApiResult
    )

}