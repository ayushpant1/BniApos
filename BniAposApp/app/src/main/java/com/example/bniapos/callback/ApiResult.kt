package com.example.bniapos.callback

import com.google.gson.JsonObject

interface ApiResult {

    fun onSuccess(jsonRequest: JsonObject)


    fun onFailure(message: String)

}