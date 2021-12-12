package com.example.bniapos.host

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @POST("{url}")
    fun postToHost(
        @Path("url", encoded = true) url: String,
        @Body jsonString: JsonObject
    ): Call<JsonObject>

    companion object {

        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.nuuneoi.com")
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}