package com.example.bniapos.helpers

import android.app.Activity

import android.content.Context

import com.example.bniapos.callback.ApiResult
import com.example.bniapos.models.TerminalParams

import com.example.bniapos.models.UpdateRequest
import com.example.bniapos.models.UpdateResponse
import com.example.bniapos.utils.AppConstants
import com.example.bniapos.utils.CommonUtility
import com.example.bniapos.utils.SharedPreferenceUtils

import java.lang.Exception


class InitializationHelper {
    private var _fileName = "InitializationHelper.kt"
    private var _AddExtraAdminMenus = false
    private var _forceInit = false
    private var _isUpdateDisplayMenu = false
    private var _isDisplayResultDialogs = false
    private var _isPrintSlipAfterSuccess = false
    private var currentChangeNo = 0
    private var isFirstTimeInit = false
    var _context: Context? = null
    var delegate: ApiResult? = null

    fun init(
        context: Context?, forceInit: Boolean,
        isDisplayResultDialog: Boolean, isPrintSlipAfterSuccess: Boolean,
        isAddExtraAdminMenus: Boolean,
        responseCarrier: ApiResult?
    ) {
        _context = context
        delegate = responseCarrier
        _forceInit = forceInit
        _isDisplayResultDialogs = isDisplayResultDialog
        _isPrintSlipAfterSuccess = isPrintSlipAfterSuccess
        _AddExtraAdminMenus = isAddExtraAdminMenus

    }

    fun CreateInitRequest(): UpdateRequest {
        val request = UpdateRequest()
        try {
            request.HSN = "20801451" //Constant.HSN
            request.MMID = "1020000000040495"
            request.MTID = "1040000000052280"
        } catch (ex: Exception) {
            delegate?.onFailure(ex.localizedMessage)
        }
        return request
    }


    fun PerformInitialization() {
        try {
            currentChangeNo = if (_forceInit) {
                //For Full Initialization.

                0
            } else SharedPreferenceUtils.getInstance(_context).getChangeNo()
            if (currentChangeNo == 0) {
                isFirstTimeInit = true
            }
            Initialization()
        } catch (ex: Exception) {
            CommonUtility.handleError(
                _context, ex,
                "InitializationHelper"
            )
        }

    }


    fun Initialization() {
        try {
            val request = CreateInitRequest()
            request.CN = currentChangeNo.toString()

            InitializationAction(request)
        } catch (e: Exception) {
            CommonUtility.handleError(_context, e, "fun:initialization - $_fileName")
        }
    }

    private fun InitializationAction(request: UpdateRequest) {
        val url =
            "https://demo.payment2go.co.id/AposHost/AndroidApi/ApiHost/getupdates"//"https://demo.payment2go.co.id/AposHost/AndroidApi/ApiHost/getupdates"
        val apiResult: ApiResult = object : ApiResult {
            override fun onSuccess(response: Any) {
                InitializationSuccess(response as UpdateResponse)
            }

            override fun onFailure(message: String) {
                delegate?.onFailure(message)
            }

        }
        CommonUtility.performInitialization(
            _context as Activity,
            url, apiResult, request
        )
    }

    private fun InitializationSuccess(response: UpdateResponse) {
        if (response != null) {
            if (response.ResponseCode != null && response.ResponseCode.equals("00", true)
            ) {
                val ResponseXML: String? = response.CD
                val ChangeNo: String? = response.CN
                val ActionType: String? = response.AT
                val ChangeType: String? = response.CT
                val ServerDateTime: String? = response.DT
                if (ChangeNo != null && ChangeNo.length > 0) {
                    val _currentChangeNo = ChangeNo.toInt()
                    if (isFirstTimeInit) {
                        //may be delete all contents from init tables and add content in blank tables
                        isFirstTimeInit = false

                    }
                    //case when we received that there is no further changes available on the server
                    if (_currentChangeNo == -1) {
                        if (ServerDateTime != null && ServerDateTime.length > 0) {
                            //function to update system datetime
                        }
                        var UpdateApplicable = false
                        var ApkName = ""
                        var ApkURL = ""
                        if (_isUpdateDisplayMenu) DesignDisplayMenu()

                        if (!UpdateApplicable) {
                            if (_isPrintSlipAfterSuccess) {
                                //add function for printing action
                            }
                            ShowDisplayDialog(
                                true,
                                "Initialization Success!!", "Success"
                            )
                        }
                    } else if (_currentChangeNo > 0) {
                        currentChangeNo = _currentChangeNo
                        SharedPreferenceUtils.getInstance(_context).setChangeNo(currentChangeNo)
                        if (ChangeType.equals(
                                "TP",
                                ignoreCase = true
                            )
                        ) _isUpdateDisplayMenu = true
                        if (ResponseXML != null && ResponseXML.length > 0)
                            ParseResponse(
                                _context, ChangeType, ActionType,
                                ResponseXML
                            )
                        Initialization()
                    }
                } else {

                    ShowDisplayDialog(
                        false,
                        "Initialization Failed...",
                        "No data received from server for update., Please try again"
                    )
                }
            }
        }
    }

    fun ParseResponse(_context: Context?, Change: String?, Action: String?, XML: String?) {
        var ActionFor = ""
        when (Change?.uppercase()) {

            "RP" -> {
                ActionFor = "RECEIPT"
            }
            "TP" -> {
                ActionFor = "TERMINALPARAMETERS"
            }
        }
        if (ActionFor != null) {
            when (ActionFor.uppercase()) {
                "TERMINALPARAMETERS" -> {
//                  //GET CURRENT TERMINAL RECORD FROM DB
                    var currentTerminalRecord: TerminalParams? = null
//                    if (allTerminalRecords != null && allTerminalRecords.size > 0) currentTerminalRecord =
//                        allTerminalRecords[0]
                    val Params: TerminalParams? =
                        XMLParserHelper.ParseXMLTerminalParameters(XML, currentTerminalRecord)
                    if (Params != null) {
                        //UPDATE IN TERMINAL RECORD DB
                        //STORE VALUES IN SHARED PREFERENCES
                        SharedPreferenceUtils.getInstance(_context).setAllowedPaymentTypes(Params.AllowedPayments)
                        SharedPreferenceUtils.getInstance(_context).setAllowedTransactionTypes(Params.AllowedTransactions)

//

                    }
                }
                "RECEIPT" -> {
                    //STORE IN DB
                }

            }
        }
    }


    private fun DesignDisplayMenu() {
        //update Menu Logic here after init
    }

    private fun ShowDisplayDialog(isSuccess: Boolean, Title: String, Message: String) {
        val _ActivityResponse: ApiResult? = delegate
        if (_isDisplayResultDialogs) {
            //handle UI to display success message

        } else {
            if (isSuccess) {
                delegate?.onSuccess("Success")
            } else {
                delegate?.onFailure(Message)
            }
        }
    }
}


