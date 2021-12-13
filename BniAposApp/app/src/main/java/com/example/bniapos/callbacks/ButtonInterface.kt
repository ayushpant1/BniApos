package com.example.bniapos.callbacks

import android.app.AlertDialog

interface ButtonInterface {

    fun onClicked(alertDialogBuilder: AlertDialog? = null)
}