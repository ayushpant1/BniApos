package com.example.bniapos.host

import com.google.gson.JsonObject
import org.json.JSONObject

interface HostRepositoryInterface {
    suspend fun postData(jsonObject: JSONObject,url:String)

}