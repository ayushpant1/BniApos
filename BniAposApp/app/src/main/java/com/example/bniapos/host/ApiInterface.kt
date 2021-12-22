package com.example.bniapos.host

import com.example.bniapos.models.UpdateRequest
import com.example.bniapos.models.UpdateResponse
import com.example.bniapos.models.responsemodels.LogonResponse
import com.example.bniapos.utils.AppConstants
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


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

    @POST("{url}")
    fun performInit(
        @Path("url", encoded = true) url: String,
        @Body updateRequest: UpdateRequest
    ): Call<UpdateResponse>



    companion object {

        fun create(): ApiInterface {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConstants.BASE_URL+"/")
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiInterface::class.java)

        }
    }
}