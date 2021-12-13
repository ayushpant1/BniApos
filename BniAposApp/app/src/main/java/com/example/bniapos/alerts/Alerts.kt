package com.example.bniapos.alerts

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.widget.*
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


    fun customWebViewAlert(activity: Context, message: String?, btnInterface: ButtonInterface) {
        //for testing
        val dummyData = "<Html>    \n" +
                "<Head>  \n" +
                "<title>  \n" +
                "Example of Paragraph tag  \n" +
                "</title>  \n" +
                "</Head>  \n" +
                "<Body>   \n" +
                "<p> <!-- It is a Paragraph tag for creating the paragraph -->  \n" +
                "<b> HTML </b> stands for <i> <u> Hyper Text Markup Language. </u> </i> It is used to create a web pages and applications. This language   \n" +
                "is easily understandable by the user and also be modifiable. It is actually a Markup language, hence it provides a flexible way for designing the  \n" +
                "web pages along with the text.   \n" +
                "</p>  \n" +
                "HTML file is made up of different elements. <b> An element </b> is a collection of <i> start tag, end tag, attributes and the text between them</i>.   \n" +
                "</p>  \n" +
                "</Body>  \n" +
                "</Html>  "
        //testing ends


        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.custom_web_view)
        val manager = activity.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        val width: Int
        val height: Int
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.defaultDisplay.width
            height = manager.defaultDisplay.height
        } else {
            val point = Point()
            manager.defaultDisplay.getSize(point)
            width = point.x
            height = point.y
        }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = width - 20
        lp.height = height - 100
        dialog.window!!.attributes = lp
        dialog.setCancelable(true)
        val imgClose = dialog.findViewById<View>(R.id.ll_close) as LinearLayout
        val wvPrint = dialog.findViewById<View>(R.id.wv_print) as WebView

        //for testing data is dummyData,please replace with message
        wvPrint.loadData(dummyData, "text/html", "UTF-8");
        imgClose.setOnClickListener {
            dialog.dismiss()
            btnInterface.onClicked()
        }

        dialog.show()
    }

}