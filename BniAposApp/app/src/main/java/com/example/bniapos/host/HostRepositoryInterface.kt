package com.example.bniapos.host

import android.app.Activity
import android.content.Context
import com.example.bniapos.models.WORKFLOW
import com.google.gson.JsonObject
import org.json.JSONObject

interface HostRepositoryInterface {
    suspend fun postData(
        context: Activity,
        jsonObject: JsonObject,
        url: String,
        currentWORKFLOW: WORKFLOW,
        isBpWorkflow: Boolean,
        bpWorkflowOutputData: String
    )

}