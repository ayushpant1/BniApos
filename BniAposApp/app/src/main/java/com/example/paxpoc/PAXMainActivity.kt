package com.example.paxpoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bniapos.R
import com.pax.gl.IGL
import com.pax.ippi.dal.interfaces.IDal
import com.pax.ippi.emv.interfaces.IEmv
import com.pax.ippi.impl.NeptuneUser

class PAXMainActivity : AppCompatActivity() {
    var dal: IDal? = null
    var gl: IGL? = null
    var emv: IEmv? = null
    private var neptuneUser: NeptuneUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pax_main_activity)
        neptuneUser = NeptuneUser.getInstance(this.applicationContext)
        dal = neptuneUser!!.service.dal
        gl = neptuneUser!!.service.gl
        emv = neptuneUser!!.service.emv

        val cardReaderHelper = dal?.cardReaderHelper

        val pollingResult = cardReaderHelper?.polling(null, 100000)

    }

}