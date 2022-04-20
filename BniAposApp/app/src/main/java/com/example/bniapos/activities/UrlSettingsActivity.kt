package com.example.bniapos.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.bniapos.BniApplication
import com.example.bniapos.R
import com.example.bniapos.utils.AppConstants
import com.example.bniapos.utils.SharedPreferenceUtils
import com.google.android.material.textfield.TextInputLayout

class UrlSettingsActivity : AppCompatActivity(), View.OnClickListener {
    private var tilBpUrl: TextInputLayout? = null
    private var tilCpUrl: TextInputLayout? = null
    private var tilInitUrl: TextInputLayout? = null

    private var etBpUrl: EditText? = null
    private var etCpUrl: EditText? = null
    private var etInitUrl: EditText? = null

    private var btnClose: Button? = null

    private var tvTitle: TextView? = null
    private var imgBack: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_settings)
        initializeView()
        setValues()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        btnClose?.setOnClickListener(this)
        imgBack?.setOnClickListener(this)
    }

    private fun setValues() {
        val bpUrl = SharedPreferenceUtils.getInstance(BniApplication.appContext).getBpUrl()
        val cpUrl = SharedPreferenceUtils.getInstance(BniApplication.appContext).getCpUrl()
        val initUrl = SharedPreferenceUtils.getInstance(BniApplication.appContext).getInitUrl()

        etBpUrl?.setText(bpUrl)
        etCpUrl?.setText(cpUrl)
        etInitUrl?.setText(initUrl)

        tvTitle?.text = AppConstants.URL_SETTINGS_TITLE

    }

    private fun checkMandatoryFields(): Boolean {
        when {
            etBpUrl?.text.toString().trim() == "" -> {
                tilBpUrl?.error = "Bp URL cannot be blank"
                tilBpUrl?.requestFocus()
                return false
            }
            etCpUrl?.text.toString().trim() == "" -> {
                tilCpUrl?.error = "Cp URL cannot be blank"
                tilCpUrl?.requestFocus()
                return false
            }
            etInitUrl?.text.toString().trim() == "" -> {
                tilInitUrl?.error = "Init URL cannot be blank"
                tilInitUrl?.requestFocus()
                return false
            }
            else -> return true
        }
    }

    private fun initializeView() {
        tvTitle = findViewById(R.id.tv_title)
        imgBack = findViewById(R.id.img_back)
        tilBpUrl = findViewById(R.id.til_bp_url)
        tilCpUrl = findViewById(R.id.til_cp_url)
        tilInitUrl = findViewById(R.id.til_init_url)

        etBpUrl = findViewById(R.id.et_bp_url)
        etCpUrl = findViewById(R.id.et_cp_url)
        etInitUrl = findViewById(R.id.et_init_url)

        btnClose = findViewById(R.id.btn_close)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.img_back,
                R.id.btn_close->{
                    saveValuesAndFinish()
                }
        }
    }

    override fun onBackPressed() {
        saveValuesAndFinish()
    }

    private fun saveValuesAndFinish() {
        if (checkMandatoryFields()) {
            val bpUrl = etBpUrl?.text.toString().trim()
            val cpUrl = etCpUrl?.text.toString().trim()
            val initUrl = etInitUrl?.text.toString().trim()
            SharedPreferenceUtils.getInstance(this).setBpUrl(bpUrl)
            SharedPreferenceUtils.getInstance(this).setCpUrl(cpUrl)
            SharedPreferenceUtils.getInstance(this).setInitUrl(initUrl)
            finish()
        }
    }
}