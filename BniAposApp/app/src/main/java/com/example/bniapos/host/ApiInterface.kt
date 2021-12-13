package com.example.bniapos.host

import com.example.bniapos.models.responsemodels.LogonResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @POST("{url}")
    fun postToHost(
        @Path("url", encoded = true) url: String,
        @Body jsonString: JsonObject
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("{url}")
    fun performLogon(
        @Path("url", encoded = true) url: String,
        @Field("grant_type") grantType: String,
        @Header("Authorization") authorization: String
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