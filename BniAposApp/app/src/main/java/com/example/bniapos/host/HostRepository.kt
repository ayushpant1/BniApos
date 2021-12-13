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
        ProgressDialog.showDialog(context)
        apiInterface.postToHost(url, jsonRequest)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                    Log.d("success", "")
                    jsonObject.addProperty(TransactionResponseKeys.TBID.name, "72378342")
                    jsonObject.addProperty(TransactionResponseKeys.INV.name, "0239023")
                    jsonObject.addProperty(TransactionResponseKeys.TXNTYPE.name, "")
                    jsonObject.addProperty(TransactionResponseKeys.AMT.name, "112")
                    jsonObject.saveToDatabase(context, currentWORKFLOW)
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
                        "Transaction saved to database",
                        buttonInterface
                    )

                }

                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    Log.d("failure", t?.message!!)
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
