package com.example.bniapos.utils

import com.example.bniapos.BniApplication

object AppConstants {
    const val BASE_URL = "https://bniapi.payment2go.co.id/AndroidApi"
    var BP_URL = SharedPreferenceUtils.getInstance(BniApplication.appContext).getBpUrl()
    var CP_URL = SharedPreferenceUtils.getInstance(BniApplication.appContext).getCpUrl()
    var INIT_URL = SharedPreferenceUtils.getInstance(BniApplication.appContext).getInitUrl()


    const val DEFAULT_BP_URL = "https://bniapi.payment2go.co.id/AndroidApi/Payment"
    const val DEFAULT_CP_URL = "https://demo.payment2go.co.id/AposHost/AndroidApi/ApiHost"
    const val DEFAULT_INIT_URL =
        "https://demo.payment2go.co.id/AposHost/AndroidApi/ApiHost/getupdates"


    const val DEFAULT_STAN = 9
    const val LOGON_URL = "/Logon/token"
    const val LOGON_AUTHORIZATION =
        "Basic OTE5OGViMmMtNDU3My00NzViLWIyNDMtZmIxOTJmMzc0MDcyOjI1NjYzOWMzLTJlM2UtNDdiZC05ZTU4LWM0MDYyODFiNGViYw=="
    const val TEMP_GRANT_TYPE = "client_credentials"


    const val DEFAULT_INVOICE_NO = "0001"


    //Settings Screen Constants
    const val SETTINGS_TITLE = "Settings"
    const val DEFAULT_CLIENT_ID = "9198eb2c-4573-475b-b243-fb192f374072"
    const val DEFAULT_CLIENT_SECRET = "256639c3-2e3e-47bd-9e58-c406281b4ebc"
    const val DEFAULT_USERNAME = "DUMMYMK06B6J"
    const val DEFAULT_PASSWORD = "99DA88fLGxFTaBbI"

    const val DEFAULT_TBID = "0009"
    const val DEFAULT_MMID = "1020000000040495"
    const val DEFAULT_MTID = "1040000000052280"
    const val DEFAULT_AGEN_COUNTER_CODE = 9


    const val URL_SETTINGS_TITLE = "Url Settings"
}