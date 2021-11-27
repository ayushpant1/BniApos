package com.example.bniapos.activities

import MenuLink
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bniapos.R
import com.example.bniapos.convertToDataString
import com.example.bniapos.database.DatabaseClient
import com.example.bniapos.database.entities.ControlTable
import com.example.bniapos.enums.BpControlType
import com.example.bniapos.host.HostRepository
import com.example.bniapos.models.CTRLS
import com.example.bniapos.models.ControlList
import com.example.bniapos.models.WORKFLOW
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


class BpControlsActivity : AppCompatActivity() {

    // private val TAG = MainActivity().localClassName

    private var mainScreenId = 1
    private var llParentBody: LinearLayout? = null
    private var btnNext: Button? = null
    private var output: MutableMap<String, Any>? = HashMap()
    private var filteredObjectList: MutableList<CTRLS>? = ArrayList()

    private var controlList: List<CTRLS>? = null
    private var menu: MenuLink? = null


    private val submit = "Submit"
    private val next = "Next"

    private val controlKeyTransactionType = "TXNTYPE"

    private var continueBpWorkflow = false

    private var currentWorkflow: WORKFLOW? = null
    private var workflowId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        llParentBody = findViewById(R.id.ll_parent_body)
        btnNext = findViewById(R.id.btn_next)

        menu = intent.getSerializableExtra(SubMenuActivity.MENU) as MenuLink
        Toast.makeText(
            this,
            menu?.displayText,
            Toast.LENGTH_LONG
        ).show()

        val json = loadJSONFromAsset()
        val jsonTable = loadTableJSONFromAsset()
        val gson = Gson()
        workflowId = intent.getIntExtra(SubMenuActivity.WORKFLOW_ID, 0)
        val workflowList = gson.fromJson(json, Array<WORKFLOW>::class.java).asList()
        currentWorkflow = workflowList.firstOrNull { workflow -> workflow.iD == workflowId }
        controlList = currentWorkflow?.cTRLS
        val objectListTable = gson.fromJson(jsonTable, Array<ControlTable>::class.java).asList()
        storeToDatabase(objectListTable)
        loadScreen(controlList!!, mainScreenId)
        btnNext?.setOnClickListener {
            if (validateRequiredValues()) {
                if (!continueBpWorkflow && output!!.containsKey(controlKeyTransactionType)) {
                    setValues()
                    gotoCpControlScreen()
                } else {
                    if (btnNext?.text == submit) {
                        submitData()
                    } else {
                        setValues()
                        Toast.makeText(
                            this@BpControlsActivity,
                            output.toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        loadNextScreen(controlList!!)

                    }
                }
            }
        }


    }

    /**
     * method responsible to navigate to CpControlsActivity whenever there is a transactionType in the current screen
     */

    private fun gotoCpControlScreen() {
        val intent = Intent(this@BpControlsActivity, CpControlsActivity::class.java)
        intent.putExtra(
            CpControlsActivity.CONTROL_DATA,
            output?.get(controlKeyTransactionType).toString()
        )
        intent.putExtra(
            CpControlsActivity.BP_WORKFLOW_OUTPUT_DATA,
            Gson().toJsonTree(output)
                .asJsonObject.toString()
        )

        intent.putExtra(
            CpControlsActivity.BP_WORKFLOW,
            true
        )
        startActivityForResult(intent, 1)
    }

    /**
     * method responsible to validate the current screen values
     */

    private fun validateRequiredValues(): Boolean {
        filteredObjectList?.forEach { controls ->
            when (controls.cTYPE.uppercase()) {
                BpControlType.TEXT.name -> {
                    if (controls.mINSIZE != 0) {
                        val editText = controls.controlObject as EditText
                        if (editText.text.length !in controls.mINSIZE..controls.mAXSIZE) {
                            Toast.makeText(
                                this@BpControlsActivity,
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
                this@BpControlsActivity,
                Gson().toJsonTree(output)
                    .asJsonObject, "http://google.nuuneoi.com", currentWorkflow!!
            )

        }
        Toast.makeText(this@BpControlsActivity, output.toString(), Toast.LENGTH_LONG).show()
    }


    /**
     * method responsible to set current screen controls value to hashMap
     */

    private fun setValues() {
        filteredObjectList?.forEach { controls ->
            when (controls.cTYPE.uppercase()) {
                BpControlType.TEXT.name -> {
                    val editText = controls.controlObject as EditText
                    output?.put(controls.kEY, editText.text.toString())
                }
            }
        }
    }

    /**
     * method responsible to store dataset of the controls
     * @param controlListTable List of Dataset of controls.
     */

    private fun storeToDatabase(controlListTable: List<ControlTable>) {
        DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
            ?.delete()
        controlListTable.forEach { controlTable ->
            DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
                ?.insert(controlTable)
        }
    }

    /**
     * method responsible to load  screen controls
     * @param controlList List of Controls.
     * @param screenId screenId to be loaded.
     */

    private fun loadScreen(controlList: List<CTRLS>, screenId: Int) {
        val screenDataSet: MutableMap<String, List<ControlTable>> = HashMap()
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
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            when (controls.cTYPE.uppercase()) {
                BpControlType.TEXT.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_edit_text, null)
                    val tilDynamic: TextInputLayout = view.findViewById(R.id.til_dynamic)
                    val edittext: EditText = view.findViewById(R.id.dynamic_edit_text)
                    controls.lABEL.let {
                        tilDynamic.hint = controls.lABEL
                    }
                    controls.dVAL.let {
                        edittext.setText(controls.dVAL)
                    }
                    controls.controlObject = edittext as Object
                    llParentBody?.addView(view)
                }


                BpControlType.RADIO.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_radio_buttons, null)
                    val radioGroup: RadioGroup = view.findViewById(R.id.rg_dynamic)
                    val rgText: TextView = view.findViewById(R.id.rg_text_dynamic)
                    rgText.text = controls.lABEL
                    val data = getData(controls.dataSet)
                    val rb = arrayOfNulls<RadioButton>(data.size)
                    for (i in data.indices) {
                        rb[i] = RadioButton(this)
                        rb[i]!!.text = data[i].name
                        rb[i]!!.id = i
                        radioGroup.addView(rb[i])
                    }
                    radioGroup.check(0)
                    if (controls.kEY == controlKeyTransactionType)
                        output?.put(controls.kEY, data[0].name!! + "$" + data[0].value!!)
                    else
                        output?.put(controls.kEY, data[0].value!!)
                    radioGroup.setOnCheckedChangeListener { p0, p1 ->
                        if (controls.kEY == controlKeyTransactionType)
                            output?.put(
                                controls.kEY,
                                data[p1].name!! + "$" + data[p1].value!!
                            )
                        else
                            output?.put(
                                controls.kEY, data[p1].value!!
                            )
                    }
                    controls.controlObject = radioGroup as Object
                    llParentBody?.addView(view)
                }
                BpControlType.DROPDOWN.name -> {
                    var data: List<ControlTable>? = ArrayList()
                    var spnData: List<String>? = ArrayList()
                    val view: View = inflater.inflate(R.layout.dynamic_spinner, null)
                    val spn: Spinner = view.findViewById(R.id.spn_dynamic)
                    view.tag = "dd_" + controls.kEY
                    val spnTextDynamic: TextView = view.findViewById(R.id.spn_text_dynamic)
                    spnTextDynamic.text = controls.lABEL
                    controls.controlObject = spn as Object
                    if (controls.relatedControlKey.isNullOrBlank()) {
                        data = getData(controls.dataSet)
                    } else {
                        spn.isEnabled = false
                    }
                    screenDataSet.put(controls.kEY, data!!)
                    spnData = data.convertToDataString()
                    spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            p0?.getItemAtPosition(p2)
                            if (p2 != 0) {
                                data = screenDataSet.get(controls.kEY)
                                filteredObjectList!!.forEach {
                                    if (it.relatedControlKey == controls.kEY) {
                                        val referenceData = getData(
                                            it.dataSet,
                                            data?.get(p2 - 1)!!.value!!
                                        )
                                        screenDataSet.put(it.kEY, referenceData)
                                        val relatedSpnData = referenceData.convertToDataString()
                                        val relatedView: View =
                                            llParentBody!!.findViewWithTag("dd_" + it.kEY)
                                        val relatedSpn: Spinner =
                                            relatedView.findViewById(R.id.spn_dynamic)
                                        relatedSpn.isEnabled = true
                                        setAdapter(relatedSpnData, relatedSpn)
                                    }
                                }

                                output?.put(controls.kEY, data?.get(p2 - 1)!!.value!!)
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                    setAdapter(spnData, spn)

                    llParentBody?.addView(view)
                }
            }
        }
        btnNext?.text = btnText
    }

    /**
     * method responsible to call load next screen controls
     * @param controlList List of Controls.
     */

    private fun loadNextScreen(controlList: List<CTRLS>) {
        runOnUiThread {
            llParentBody?.removeAllViews()
            if (btnNext?.text == next) {
                mainScreenId += 1
                loadScreen(controlList, mainScreenId)
            } else {
                submitData()
            }
        }
    }

    /**
     * method responsible to set spinner adapter
     * @param spnData Spinner Data.
     * @param spn Spinner Object.
     */

    private fun setAdapter(spnData: List<String>?, spn: Spinner) {
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            spnData!!
        )


        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spn.adapter = ad
    }

    /**
     * method responsible to get data of controls from the database
     * @param dataSet The dataset which is required.
     * @param value The reference value which is used to fetch the dataset with reference to the reference value.
     */

    private fun getData(dataSet: String, value: String? = null): List<ControlTable> {
        var data: List<ControlTable>? = ArrayList()
        data = if (value == null) {
            DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
                ?.getDataSet(dataSet)
        } else {
            DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
                ?.getDataSetWithReferenceData(dataSet, value)
        }

        return data!!
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                var cpResponse = data?.getStringExtra("cpResponse")
                var bpResponse = data?.getStringExtra("bpResponse")
                continueBpWorkflow = true
                loadNextScreen(controlList!!)
            }
        }
    }

    /**
     * Load control list from assets
     */

    private fun loadJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        var json: String? = null
        json = try {
            val `is`: InputStream = assets.open("workflow_bp.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        val jsonObject = JSONObject(json!!)
        val jsonArray = jsonObject.getJSONArray("WORKFLOW")

        return jsonArray.toString()
    }


    /**
     * Load control list data from assets which will be responsible for data given to controls
     */
    private fun loadTableJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        var json: String? = null
        json = try {
            val `is`: InputStream = assets.open("control_table_data.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        val jsonObject = JSONObject(json!!)
        val jsonArray = jsonObject.getJSONArray("controlTableData")

        return jsonArray.toString()
    }


}


