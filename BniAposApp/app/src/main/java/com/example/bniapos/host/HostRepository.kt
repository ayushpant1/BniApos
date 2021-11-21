package com.example.bniapos.host

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.bniapos.database.DatabaseClient
import com.example.bniapos.models.WORKFLOW
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HostRepository() : HostRepositoryInterface {

    private val apiInterface: ApiInterface by lazy {
        ApiInterface.create()
    }

    override suspend fun postData(
        context: Activity,
        jsonObject: JsonObject,
        url: String,
        currentWORKFLOW: WORKFLOW,
        isBpWorkflow: Boolean,
        bpWorkflowOutputData: String
    ) {

        val jsonRequest = jsonObject.toTransactionRequest(currentWORKFLOW)

        apiInterface.postToHost(url).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.d("failure", t?.message!!)
                val transactionResponse = jsonRequest.saveToDatabase(context, currentWORKFLOW)

                val tranasactionList =
                    DatabaseClient.getInstance(context)?.appDatabase?.transactionResponseDao()
                        ?.getAll()
                Toast.makeText(context, "Transaction saved to database", Toast.LENGTH_LONG).show()

                if (isBpWorkflow) {
                    val returnIntent = Intent()
                    returnIntent.putExtra("cpResponse", Gson().toJson(transactionResponse))
                    returnIntent.putExtra("bpResponse", bpWorkflowOutputData)
                    context.setResult(Activity.RESULT_OK, returnIntent)
                }
                context.finish()

            }

        })
    }


}
