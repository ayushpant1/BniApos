package com.example.bniapos.host

import android.util.Log
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HostRepository():HostRepositoryInterface {

  private  val apiInterface:ApiInterface by lazy {
      ApiInterface.create()
  }

    override suspend fun postData(jsonObject: JSONObject,url:String) {
        apiInterface.postToHost(url).enqueue( object : Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>?) {
            }
            override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
                Log.d("failure",t?.message!!)
            }

        })
    }


}
