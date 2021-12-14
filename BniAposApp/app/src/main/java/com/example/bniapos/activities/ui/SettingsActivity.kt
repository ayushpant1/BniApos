package com.example.bniapos.activities.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.bniapos.R
import com.example.bniapos.alerts.Alerts
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.callbacks.ButtonInterface
import com.example.bniapos.models.responsemodels.LogonResponse
import com.example.bniapos.utils.AppConstants
import com.example.bniapos.utils.CommonUtility
import com.example.bniapos.utils.SharedPreferenceUtils
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private var btnLogon: Button? = null
    private var tvTitle: TextView? = null
    private var imgBack: ImageView? = null

    private var tilClientId: TextInputLayout? = null
    private var tilClientSecret: TextInputLayout? = null
    private var tilUsername: TextInputLayout? = null
    private var tilPassword: TextInputLayout? = null

    private var etClientId: EditText? = null
    private var etClientSecret: EditText? = null
    private var etUsername: EditText? = null
    private var etPassword: EditText? = null

    private val apiResult = object : ApiResult {
        override fun onSuccess(jsonRequest: JsonObject) {
            val logonResponse = Gson().fromJson(jsonRequest.toString(), LogonResponse::class.java)

            if (logonResponse != null) {
                SharedPreferenceUtils
                    .getInstance(this@SettingsActivity)
                    .setAuthCode(logonResponse.accessToken)

                val buttonInterface: ButtonInterface = object : ButtonInterface {

                    override fun onClicked(alertDialogBuilder: AlertDialog?) {
                        onBackPressed()
                    }
                }
                Alerts.customWebViewAlert(
                    this@SettingsActivity,
                    "Authorization Response Received",
                    buttonInterface
                )
            }
        }

        override fun onFailure(message: String) {
            Log.d("Failure", message)
            //handle failure
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init()
        showUI()
        setOnClickListener()
    }

    private fun init() {
        btnLogon = findViewById(R.id.btn_logon)

        tvTitle = findViewById(R.id.tv_title)

        imgBack = findViewById(R.id.img_back)

        tilClientId = findViewById(R.id.til_client_id)
        tilClientSecret = findViewById(R.id.til_client_secret)
        tilUsername = findViewById(R.id.til_username)
        tilPassword = findViewById(R.id.til_password)

        etClientId = findViewById(R.id.et_client_id)
        etClientSecret = findViewById(R.id.et_client_secret)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
    }

    private fun showUI() {
        tvTitle?.text = AppConstants.SETTINGS_TITLE

        //set Default Values

        etClientId?.setText(AppConstants.DEFAULT_CLIENT_ID)
        etClientSecret?.setText(AppConstants.DEFAULT_CLIENT_SECRET)
        etUsername?.setText(AppConstants.DEFAULT_USERNAME)
        etPassword?.setText(AppConstants.DEFAULT_PASSWORD)
    }


    private fun setOnClickListener() {
        btnLogon?.setOnClickListener(this)
        imgBack?.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_logon -> {
                if (checkMandatoryFields()) {
                    val clientId = etClientId?.text.toString()
                    val clientSecret = etClientSecret?.text.toString()
                    val username = etUsername?.text.toString()
                    val password = etPassword?.text.toString()
                    CommonUtility.performLogon(
                        this@SettingsActivity,
                        apiResult,
                        clientId,
                        clientSecret,
                        username,
                        password
                    )
                }
            }
            R.id.img_back -> {
                onBackPressed()
            }
        }
    }

    private fun checkMandatoryFields(): Boolean {
        when {
            etClientId?.text!!.toString().isBlank() -> {
                tilClientId?.error = getString(R.string.enter_client_id)
                etClientId?.requestFocus()
                return false
            }
            etClientSecret?.text!!.toString().isBlank() -> {
                tilClientSecret?.error = getString(R.string.enter_client_secret)
                etClientSecret?.requestFocus()
                return false
            }
            etUsername?.text!!.toString().isBlank() -> {
                tilUsername?.error = getString(R.string.enter_username)
                etUsername?.requestFocus()
                return false
            }
            etPassword?.text!!.toString().isBlank() -> {
                tilPassword?.error = getString(R.string.enter_password)
                etPassword?.requestFocus()
                return false
            }
        }
        return true
    }

}