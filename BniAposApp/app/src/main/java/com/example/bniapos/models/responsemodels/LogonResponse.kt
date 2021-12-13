package com.example.bniapos.models.responsemodels

import com.google.gson.annotations.SerializedName

data class LogonResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: String,
    @SerializedName("scope") val scope: String,
)
