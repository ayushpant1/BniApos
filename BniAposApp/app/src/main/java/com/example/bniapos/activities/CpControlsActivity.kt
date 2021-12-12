package com.example.bniapos.activities

import MenuLink
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bniapos.R
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.enums.CpControlType
import com.example.bniapos.host.HostRepository
import com.example.bniapos.models.CTRLS
import com.example.bniapos.models.WORKFLOW
import com.example.bniapos.utils.AppConstants
import com.example.bniapos.utils.Configuration
import com.example.paymentsdk.CardReadOutput
import com.example.paymentsdk.Common.ISuccessResponse_Card
import com.example.paymentsdk.Common.TerminalCardApiHelper
import com.example.paymentsdk.util.transaction.TransactionConfig
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class CpControlsActivity : AppCompatActivity() {

    companion object {
        const val CONTROL_DATA = "CONTROL_DATA"
        const val BP_WORKFLOW_OUTPUT_DATA = "BP_WORKFLOW_OUTPUT_DATA"
        const val BP_WORKFLOW = "BP_WORKFLOW"
    }

    private var mainScreenId = 1
    private var llParentBody: LinearLayout? = null
    private var btnNext: Button? = null
    private var output: MutableMap<Any?, Any?>? = HashMap()
    private var filteredObjectList: MutableList<CTRLS>? = ArrayList()
    private var cardReadOutput: CardReadOutput? = null
    private var emvProcessor: TerminalCardApiHelper? = null
    private var transactionConfig: TransactionConfig? = null
    private var currentWorkflow: WORKFLOW? = null
    private var workflowList: List<WORKFLOW>? = ArrayList()
    private var controlList: List<CTRLS>? = null


    private var workflowId: Int? = null

    private var isBpWorkflow = false
    private var bpTransactionTypeName = ""

    private var bpWorkflowOutputData = ""
    private var menu: MenuLink? = null


    private val submit = "Submit"
    private val next = "Next"

    private val apiResult: ApiResult = object : ApiResult {
        override fun onSuccess(jsonRequest: JsonObject) {
            if (currentWorkflow?.nEXTWORKFLOWID == 0) {
                finish()
            } else {
                currentWorkflow = workflowList?.find { it.iD == currentWorkflow?.nEXTWORKFLOWID }
                controlList = currentWorkflow?.cTRLS
                mainScreenId = 1
                loadScreen(controlList!!, mainScreenId)
            }

        }

        override fun onFailure(message: String) {

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        llParentBody = findViewById(R.id.ll_parent_body)
        btnNext = findViewById(R.id.btn_next)
        isBpWorkflow = intent.getBooleanExtra(BP_WORKFLOW, false)
        if (isBpWorkflow) {
            val controlData = intent.getStringExtra(CONTROL_DATA) as String
            bpTransactionTypeName = controlData.split("$").first()
            workflowId = controlData.split("$")[1].toInt()
            bpWorkflowOutputData = intent.getStringExtra(BP_WORKFLOW_OUTPUT_DATA) as String
            val type: Type = object : TypeToken<Map<String?, Any>>() {}.type
            val requestMap: Map<String, Any> = Gson().fromJson(bpWorkflowOutputData, type)
            output = requestMap.toMutableMap()
        } else {
            workflowId = intent.getIntExtra(SubMenuActivity.WORKFLOW_ID, 0)
        }
        menu = intent.getSerializableExtra(SubMenuActivity.MENU) as MenuLink

        val json = Configuration.getWorkflowConfig(this)
        val gson = Gson()
        var workflowList = gson.fromJson(json, Array<WORKFLOW>::class.java).asList()
        workflowList = workflowList.filter { it.tYPE == "CP" }
        currentWorkflow = workflowList.firstOrNull { workflow -> workflow.iD == workflowId }
        controlList = currentWorkflow?.cTRLS
        if (controlList.isNullOrEmpty()) {
            Toast.makeText(this, "Workflow not attached", Toast.LENGTH_LONG).show()
            finish()
        } else {
            loadScreen(controlList!!, mainScreenId)
        }

        btnNext?.setOnClickListener {
            if (btnNext?.text == submit) {
                submitData()
            } else {
                if (validateRequiredValues()) {
                    setValues()
                    loadNextScreen(controlList!!)
                }
            }
        }


    }

    /**
     * method responsible to validate the current screen values
     */

    private fun validateRequiredValues(): Boolean {
        filteredObjectList?.forEach { controls ->
            when (controls.kEY.uppercase()) {
                CpControlType.AMT.name -> {
                    if (controls.mINSIZE != 0) {
                        val editText = controls.controlObject as EditText
                        if (editText.text.length !in controls.mINSIZE..controls.mAXSIZE) {
                            Toast.makeText(
                                this@CpControlsActivity,
                                controls.lABEL + " is not valid", Toast.LENGTH_LONG
                            ).show()
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    /**
     * method responsible to submit the data to the server
     */

    private fun submitData() {
        setValues()
        val hostRepository = HostRepository()
        MainScope().launch {
            hostRepository.postData(
                this@CpControlsActivity,
                Gson().toJsonTree(output)
                    .asJsonObject,
                AppConstants.CP_URL + "/" + currentWorkflow?.eNDPOINT, currentWorkflow!!,
                apiResult,
                menu!!.txnType,
                isBpWorkflow,
                bpWorkflowOutputData,

                )

        }
    }

    /**
     * method responsible to set current screen controls value to hashMap
     */

    private fun setValues() {
        filteredObjectList?.forEach { controls ->
            when (controls.kEY.uppercase()) {
                CpControlType.AMT.name -> {
                    val editText = controls.controlObject as EditText
                    output?.put(controls.kEY, editText.text.toString())
                }
                CpControlType.CARDNO.name, CpControlType.PIN.name -> {
                    output?.put(controls.kEY, cardReadOutput!!)
                }
            }
        }
    }

    /**
     * method responsible to load  screen controls
     * @param controlList List of Controls.
     * @param screenId screenId to be loaded.
     */

    private fun loadScreen(controlList: List<CTRLS>, screenId: Int) {
        var btnText = submit
        filteredObjectList =
            controlList.filter { controlList -> controlList.sCN == screenId }.sortedWith(
                compareBy { it.oRD }).toMutableList()
        controlList.forEach { controls ->
            if (controls.sCN > screenId) {
                btnText = next
            }
        }
        filteredObjectList!!.forEach { controls ->
            if (output!!.containsKey(controls.kEY)) {
                controls.dVAL = output!![controls.kEY].toString()
            }
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            when (controls.kEY.uppercase()) {
                CpControlType.AMT.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_edit_text, null)
                    val tilDynamic: TextInputLayout = view.findViewById(R.id.til_dynamic)
                    val edittext: EditText = view.findViewById(R.id.dynamic_edit_text)
                    controls.lABEL.let {
                        tilDynamic.hint = it
                    }
                    controls.dVAL.let {
                        edittext.setText(controls.dVAL)
                    }
                    controls.controlObject = edittext as Object
                    llParentBody?.addView(view)
                }

                CpControlType.CARDNO.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_card_layout, null)
                    val tvCardText: TextView = view.findViewById(R.id.tv_card_text)
                    controls.lABEL.let {
                        tvCardText.text = it
                    }
                    tvCardText.gravity = Gravity.CENTER
                    llParentBody?.addView(view)


                    btnNext?.visibility = GONE
                    runOnUiThread {
                        emvProcessor =
                            TerminalCardApiHelper(
                                this@CpControlsActivity,
                                object : ISuccessResponse_Card {
                                    override fun processFinish(CardOutput: CardReadOutput?) {
                                        cardReadOutput = CardReadOutput()
                                        cardReadOutput = CardOutput
                                        loadNextScreen(controlList)
                                    }

                                    override fun PinProcessConfirm(output: CardReadOutput?) {
                                        Log.d("TAG", output.toString())
                                    }

                                    override fun PinProcessFailed(Exception: String?) {
                                        Log.d("TAG", Exception.toString())
                                    }

                                    override fun processFailed(Exception: String?) {
                                        Log.d("TAG", Exception.toString())
                                    }

                                    override fun Communication(breakEMVConnection: Boolean) {
                                        Log.d("TAG", "communication")
                                    }

                                    override fun processTimeOut() {
                                        Log.d("TAG", "timeout")
                                    }

                                    override fun TransactionApproved() {
                                        Log.d("TAG", "approved")
                                    }

                                    override fun TransactionDeclined() {
                                        Log.d("TAG", "declined")
                                    }

                                })
                        transactionConfig = TransactionConfig()
                        transactionConfig?.amount = 100
                        transactionConfig?.isContactIcCardSupported = true
                        emvProcessor!!.startCardScan(
                            transactionConfig,
                            "51263", false
                        )
                    }

                }

                CpControlType.PIN.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_card_layout, null)
                    val tvCardText: TextView = view.findViewById(R.id.tv_card_text)
                    controls.lABEL.let {
                        tvCardText.text = it
                    }
                    tvCardText.gravity = Gravity.CENTER
                    llParentBody?.addView(view)


                    btnNext?.visibility = GONE
                    transactionConfig?.isPinInputNeeded = true
                    emvProcessor?.publishEMVDataStep1(
                        transactionConfig?.amount!!,
                        0,
                        cardReadOutput,
                        false,
                        true
                    )
                }


            }
        }

        btnNext?.text = btnText
    }

    /**
     * method responsible to call load next screen controls
     * @param controlList List of Controls.
     */

    private fun loadNextScreen(objectList: List<CTRLS>) {
        runOnUiThread {
            llParentBody?.removeAllViews()
            if (btnNext?.text == next) {
                mainScreenId += 1
                loadScreen(objectList, mainScreenId)
            } else {
                submitData()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        emvProcessor?.closeEmvProcess()
    }


}


