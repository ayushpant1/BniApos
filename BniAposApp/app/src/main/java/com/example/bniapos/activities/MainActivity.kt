package com.example.bniapos.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bniapos.R
import com.example.bniapos.convertToDataString
import com.example.bniapos.database.DatabaseClient
import com.example.bniapos.database.entities.ControlTable
import com.example.bniapos.enums.ControlType
import com.example.bniapos.models.ControlList
import com.example.paymentsdk.CardReadOutput
import com.example.paymentsdk.Common.ISuccessResponse_Card
import com.example.paymentsdk.Common.TerminalCardApiHelper
import com.example.paymentsdk.util.transaction.TransactionConfig
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    private var mainScreenId = 1
    private var llParentBody: LinearLayout? = null
    private var btnNext: Button? = null
    private var output: MutableMap<String, Any>? = HashMap()
    private var filteredObjectList: MutableList<ControlList>? = ArrayList()
    private var cardReadOutput: CardReadOutput? = null
    private var emvProcessor: TerminalCardApiHelper? = null
    private var transactionConfig: TransactionConfig? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        llParentBody = findViewById(R.id.ll_parent_body)
        btnNext = findViewById(R.id.btn_next)
        Toast.makeText(
            this,
            intent.getStringExtra("menuName") + intent.getIntExtra("menuId", 0),
            Toast.LENGTH_LONG
        ).show()

        val json = loadJSONFromAsset()
        val jsonTable = loadTableJSONFromAsset()
        val gson = Gson()
        val objectList = gson.fromJson(json, Array<ControlList>::class.java).asList()
        val objectListTable = gson.fromJson(jsonTable, Array<ControlTable>::class.java).asList()
        storeToDatabase(objectListTable)


        loadScreen(objectList, mainScreenId)

        btnNext?.setOnClickListener {
            if (btnNext?.text == "Submit") {
                submitData()
            } else {
                if (validateRequiredValues()) {
                    setValues()
                    Toast.makeText(this@MainActivity, output.toString(), Toast.LENGTH_LONG).show()
                    loadNextScreen(objectList)
                }
            }
        }


    }

    private fun validateRequiredValues(): Boolean {
        filteredObjectList?.forEach { controls ->
            when (controls.controlType.uppercase()) {
                ControlType.TEXT.name -> {
                    if (controls.minLength != 0) {
                        val editText = controls.controlObject as EditText
                        if (editText.text.length !in controls.minLength..controls.maxLength) {
                            Toast.makeText(
                                this@MainActivity,
                                controls.label + " is not valid", Toast.LENGTH_LONG
                            ).show()
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    private fun submitData() {
        setValues()
        Toast.makeText(this@MainActivity, output.toString(), Toast.LENGTH_LONG).show()
    }

    private fun setValues() {
        filteredObjectList?.forEach { controls ->
            when (controls.controlType.uppercase()) {
                ControlType.TEXT.name -> {
                    val editText = controls.controlObject as EditText
                    output?.put(controls.controlKey, editText.text)
                }
                ControlType.CARD.name, ControlType.SECUREPIN.name -> {
                    output?.put(controls.controlKey, cardReadOutput!!)
                }
            }
        }
    }


    private fun storeToDatabase(objectListTable: List<ControlTable>) {
        objectListTable.forEach { controlTable ->
            DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
                ?.insert(controlTable)
        }
    }

    private fun loadScreen(objectList: List<ControlList>, screenId: Int) {
        var screenDataSet: MutableMap<String, List<ControlTable>>? = HashMap()
        var btnText = "Submit"
        filteredObjectList =
            objectList.filter { controlList -> controlList.screenId == screenId }.sortedWith(
                compareBy { it.sortOrder }).toMutableList()
        objectList.forEach { controls ->
            if (controls.screenId > screenId) {
                btnText = "Next"
            }
        }
        filteredObjectList!!.forEach { controls ->
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            when (controls.controlType.uppercase()) {
                ControlType.TEXT.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_edit_text, null)
                    val tilDynamic: TextInputLayout = view.findViewById(R.id.til_dynamic)
                    val edittext: EditText = view.findViewById(R.id.dynamic_edit_text)
                    controls.label.let {
                        tilDynamic.hint = controls.label
                    }
                    controls.defaultValue.let {
                        edittext.setText(controls.defaultValue)
                    }
                    controls.controlObject = edittext as Object
                    llParentBody?.addView(view)
                }

                ControlType.CARD.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_card_layout, null)
                    llParentBody?.addView(view)
                    btnNext?.visibility = GONE
                    runOnUiThread {
                        emvProcessor =
                            TerminalCardApiHelper(
                                this@MainActivity,
                                object : ISuccessResponse_Card {
                                    override fun processFinish(CardOutput: CardReadOutput?) {
                                        cardReadOutput = CardReadOutput()
                                        cardReadOutput = CardOutput
                                        loadNextScreen(objectList)
                                    }

                                    override fun PinProcessConfirm(output: CardReadOutput?) {
                                        Log.d("CardOutput", output.toString())
                                    }

                                    override fun PinProcessFailed(Exception: String?) {
                                        Log.d("CardOutput", Exception.toString())
                                    }

                                    override fun processFailed(Exception: String?) {
                                        Log.d("CardOutput", Exception.toString())
                                    }

                                    override fun Communication(breakEMVConnection: Boolean) {
                                        Log.d("CardOutput", "communication")
                                    }

                                    override fun processTimeOut() {
                                        Log.d("CardOutput", "timeout")
                                    }

                                    override fun TransactionApproved() {
                                        Log.d("CardOutput", "approved")
                                    }

                                    override fun TransactionDeclined() {
                                        Log.d("CardOutput", "declined")
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

                ControlType.SECUREPIN.name -> {
                    transactionConfig?.isPinInputNeeded = true
                    emvProcessor?.publishEMVDataStep1(
                        transactionConfig?.amount!!,
                        0,
                        cardReadOutput,
                        false,
                        true
                    )
                }


                ControlType.RADIO.name -> {
                    val view: View = inflater.inflate(R.layout.dynamic_radio_buttons, null)
                    val radioGroup: RadioGroup = view.findViewById(R.id.rg_dynamic)
                    val rgText: TextView = view.findViewById(R.id.rg_text_dynamic)
                    rgText.text = controls.label
                    val data = getData(controls.dataSet)
                    val rb = arrayOfNulls<RadioButton>(data.size)
                    for (i in data.indices) {
                        rb[i] = RadioButton(this)
                        rb[i]!!.text = data[i].name
                        rb[i]!!.id = i
                        radioGroup.addView(rb[i])
                    }
                    radioGroup.check(0)
                    output?.put(controls.controlKey, data.get(0).value!!)
                    radioGroup.setOnCheckedChangeListener(object :
                        RadioGroup.OnCheckedChangeListener {
                        override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                            output?.put(controls.controlKey, data.get(p1).value!!)
                        }

                    })
                    controls.controlObject = radioGroup as Object
                    llParentBody?.addView(view)
                }
                ControlType.DROPDOWN.name -> {
                    var data: List<ControlTable>? = ArrayList()
                    var spnData: List<String>? = ArrayList()
                    val view: View = inflater.inflate(R.layout.dynamic_spinner, null)
                    val spn: Spinner = view.findViewById(R.id.spn_dynamic)
                    view.tag = "dd_" + controls.controlKey
                    val spnTextDynamic: TextView = view.findViewById(R.id.spn_text_dynamic)
                    spnTextDynamic.text = controls.label
                    controls.controlObject = spn as Object
                    if (controls.relatedControlKey.isNullOrBlank()) {
                        data = getData(controls.dataSet)
                    } else {
                        spn.isEnabled = false
                    }
                    screenDataSet?.put(controls.controlKey, data!!)
                    spnData = data?.convertToDataString()
                    spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            p0?.getItemAtPosition(p2)
                            if (p2 != 0) {
                                data = screenDataSet?.get(controls.controlKey)
                                filteredObjectList!!.forEach {
                                    if (it.relatedControlKey == controls.controlKey) {
                                        val referenceData = getData(
                                            it.dataSet,
                                            data?.get(p2 - 1)!!.value!!
                                        )
                                        screenDataSet?.put(it.controlKey, referenceData)
                                        val relatedSpnData = referenceData?.convertToDataString()
                                        val relatedView: View =
                                            llParentBody!!.findViewWithTag("dd_" + it.controlKey)
                                        val relatedSpn: Spinner =
                                            relatedView.findViewById(R.id.spn_dynamic)
                                        relatedSpn.isEnabled = true
                                        setAdapter(relatedSpnData, relatedSpn)
                                    }
                                }

                                output?.put(controls.controlKey, data?.get(p2 - 1)!!.value!!)
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

        /*  val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
          val view: View = inflater.inflate(R.layout.dynamic_button, null)
          btnNext = (view as Button)

          llParentBody?.addView(view)*/
        btnNext?.text = btnText
    }

    private fun loadNextScreen(objectList: List<ControlList>) {
        runOnUiThread {
            llParentBody?.removeAllViews()
            if (btnNext?.text == "Next") {
                mainScreenId += 1
                loadScreen(objectList, mainScreenId)
            } else {
                submitData()
            }
        }
    }

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

    private fun getData(dataSet: String, value: String? = null): List<ControlTable> {
        var data: List<ControlTable>? = ArrayList()
        if (value == null) {
            data = DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
                ?.getDataSet(dataSet)
        } else {
            data = DatabaseClient.getInstance(applicationContext)?.appDatabase?.controlDao()
                ?.getDataSetWithReferenceData(dataSet, value)
        }

        return data!!
    }

    fun loadJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        var json: String? = null
        json = try {
            val `is`: InputStream = getAssets().open("controls.json")
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
        val jsonArray = jsonObject.getJSONArray("controlList")

        return jsonArray.toString()
    }

    fun loadTableJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        var json: String? = null
        json = try {
            val `is`: InputStream = getAssets().open("control_table_data.json")
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


