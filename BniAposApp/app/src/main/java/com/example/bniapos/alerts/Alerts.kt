package com.example.bniapos.alerts

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.bniapos.R
import com.example.bniapos.callbacks.ButtonInterface


object Alerts {

    fun successAlert(activity: Context, message: String?, btnInterface: ButtonInterface) {
        val inflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.success_alert, null)
        val tvOk = view.findViewById<View>(R.id.tv_ok) as LinearLayout
        val tvMessage = view.findViewById<View>(R.id.tv_message) as TextView
        tvMessage.text = message
        val alertDialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(activity)
        alertDialogBuilder.setView(view)
        val dialog: AlertDialog = alertDialogBuilder.create();
        tvOk.setOnClickListener {
            dialog.dismiss()
            btnInterface.onClicked(dialog)
        }
        dialog.setCancelable(false)
        dialog.show()
    }

}