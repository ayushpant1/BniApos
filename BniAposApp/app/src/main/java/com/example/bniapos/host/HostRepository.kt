package com.example.bniapos.host

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import com.example.bniapos.alerts.Alerts
import com.example.bniapos.alerts.ProgressDialog
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.callbacks.ButtonInterface
import com.example.bniapos.database.DatabaseClient
import com.example.bniapos.enums.TransactionResponseKeys
import com.example.bniapos.models.WORKFLOW
import com.example.bniapos.models.responsemodels.LogonResponse
import com.example.bniapos.utils.Util
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HostRepository : HostRepositoryInterface {

    private val apiInterface: ApiInterface by lazy {
        ApiInterface.create()
    }

    override suspend fun postData(
        context: Activity,
        jsonObject: JsonObject,
        url: String,
        currentWORKFLOW: WORKFLOW,
        apiResult: ApiResult,
        transactionType: Int,
        isBpWorkflow: Boolean,
        bpWorkflowOutputData: String?
    ) {

        val jsonRequest = jsonObject.toTransactionRequest(currentWORKFLOW, transactionType, context)
        Util.deepMerge(jsonRequest, jsonObject)!!
        Log.d("HTTP Url - ",url)
        Log.d("HTTP Request - ",jsonObject.toString())
        ProgressDialog.showDialog(context)
        apiInterface.postToHost(url, jsonRequest)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                    Log.d("success", "")

                    val splitResponse = currentWORKFLOW.rESP.split(",")
                    val responseBody = response?.body() as JsonObject
                    Log.d("HTTP Response - ",responseBody.toString())
                    if (responseBody?.has("RSPC") && responseBody?.get("RSPC")?.asString.equals(
                            "00",
                            true
                        )
                    ) {
                        var responseFormatter=""
                        splitResponse.forEach {
                            if (responseBody.has(it)) {
                                val value = responseBody?.get(it)?.asString
                                if(value !=null) {
                                    jsonObject.addProperty(it, value)
                                    responseFormatter += "<br/><b>$it</b>:$value"
                                }
                            }
                        }

                        val splitResponseData = currentWORKFLOW.dataResponse.split(",")
                        if (!splitResponseData.isNullOrEmpty() && response?.body()
                                ?.has("data") ?: false
                        ) {
                            val responseBodyData = response?.body()?.get("data") as JsonObject
                            splitResponseData.forEach {

                                if (responseBodyData.has(it)) {
                                    val value = responseBodyData?.get(it)?.asString
                                    if(value != null) {
                                        jsonObject.addProperty(it, value)
                                        responseFormatter += "<br/><b>$it</b>:$value"
                                    }
                                }
                            }
                        }

                        ProgressDialog.dismissDialog()
                        DatabaseClient.getInstance(context)?.appDatabase?.transactionResponseDao()
                            ?.getAll()
                        val buttonInterface: ButtonInterface = object : ButtonInterface {

                            override fun onClicked(alertDialogBuilder: AlertDialog?) {
                                if (isBpWorkflow) {
                                    val returnIntent = Intent()
                                    returnIntent.putExtra("response", Gson().toJson(jsonObject))
                                    context.setResult(Activity.RESULT_OK, returnIntent)
                                }
                                apiResult.onSuccess(jsonObject)
                            }
                        }


                        Alerts.customWebViewAlert(
                            context,
                            "Transaction Response Recieved \n\n" +
                                    responseFormatter,
                            buttonInterface
                        )

                    }
                    else
                    {
                        ProgressDialog.dismissDialog()
                        if (responseBody?.has("RSPM") && responseBody?.get("RSPM")!=null) {
                            apiResult.onFailure(responseBody?.get("RSPM").asString)
                        }else  apiResult.onFailure("Transaction Declined")
                    }
                }

                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    Log.d("http failure", t?.message!!)
                    ProgressDialog.dismissDialog()
                    apiResult.onFailure(t?.message!!)


                }

            })
    }

    override suspend fun performLogon(
        context: Activity,
        url: String,
        authorization: String,
        grantType: String,
        apiResult: ApiResult
    ) {

        ProgressDialog.showDialog(context)
        apiInterface.performLogon(url, grantType, authorization)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>?,
                    response: Response<JsonObject>?
                ) {
                    ProgressDialog.dismissDialog()
                    apiResult.onSuccess(response!!.body())
                }

                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    Log.d("failure", t?.message!!)
                    apiResult.onFailure(t.message!!)
                }

            })
    }


}
