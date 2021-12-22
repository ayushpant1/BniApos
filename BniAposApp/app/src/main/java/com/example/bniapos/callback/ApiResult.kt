package com.example.bniapos.callback

import com.google.gson.JsonObject

interface ApiResult {
    fun onSuccess(response: Any)
    fun onFailure(message: String)
}