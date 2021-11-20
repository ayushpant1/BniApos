package com.example.bniapos.host

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bniapos.database.DatabaseClient
import com.example.bniapos.models.WORKFLOW
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
        currentWORKFLOW: WORKFLOW
    ) {

        val jsonRequest = jsonObject.toTransactionRequest(currentWORKFLOW)

        apiInterface.postToHost(url).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.d("failure", t?.message!!)
                jsonRequest.saveToDatabase(context, currentWORKFLOW)

                val tranasactionList =
                    DatabaseClient.getInstance(context)?.appDatabase?.transactionResponseDao()
                        ?.getAll()
                Toast.makeText(context, "Transaction saved to database", Toast.LENGTH_LONG).show()
                context.finish()

            }

        })
    }


}
