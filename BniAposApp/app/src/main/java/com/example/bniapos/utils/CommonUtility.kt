package com.example.bniapos.utils

import android.app.Activity
import android.content.Context
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.host.HostRepository
import com.example.bniapos.models.MasterPrintFormat
import com.example.bniapos.models.UpdateRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    fun performInitialization(
        context: Activity,
        url: String,
        apiResult: ApiResult,
        updateRequest: UpdateRequest
    ) {
        val hostRepository = HostRepository()
        MainScope().launch {
            hostRepository.performIntialization(
                context,
                url,
                updateRequest,
                apiResult
            )

        }
    }

    fun handleError(
        context: Context?,
        error: Exception,
        errorLocation: String
    ) {

        //TODO

    }

    fun PrintFormatListToJson(formatList: ArrayList<MasterPrintFormat>): String {
        val gson = Gson()

        return gson.toJson(formatList)
    }

    fun JsonToPrintFormatList(json: String?): ArrayList<MasterPrintFormat> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<MasterPrintFormat>>() {}.type
        return gson.fromJson<Any>(json, type) as ArrayList<MasterPrintFormat>
    }
}