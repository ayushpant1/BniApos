package com.example.bniapos.utils

import android.app.Activity
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.host.HostRepository
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object CommonUtility {
    fun performLogon(
        context: Activity,
        apiResult: ApiResult,
        clientId: String,
        clientSecret: String,
        username: String,
        password: String
    ) {
        val requestClientId = clientId
        val requestClientSecret = clientSecret
        val requestUsername = username
        val requestPassword = password
        val url = AppConstants.BASE_URL + AppConstants.LOGON_URL
        val authorization = AppConstants.LOGON_AUTHORIZATION

        //temporary for now will change later
        val grantType = AppConstants.TEMP_GRANT_TYPE

        val hostRepository = HostRepository()
        MainScope().launch {
            hostRepository.performLogon(
                context,
                url,
                authorization,
                grantType,
                apiResult
            )

        }
    }
}