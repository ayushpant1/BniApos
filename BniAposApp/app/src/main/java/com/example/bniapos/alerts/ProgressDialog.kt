package com.example.bniapos.alerts

import android.app.ProgressDialog
import android.content.Context
import com.example.bniapos.R

object ProgressDialog {


    private var mProgressDialog: ProgressDialog? = null

    fun showDialog(context: Context) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(
                context,
                null,
                context.resources.getString(R.string.please_wait),
                true,
                false
            )
        }
    }

    fun dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }
}