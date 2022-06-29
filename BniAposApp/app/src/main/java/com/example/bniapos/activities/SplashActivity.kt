package com.example.bniapos.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.example.bniapos.R
import com.example.bniapos.alerts.Alerts
import com.example.bniapos.alerts.ProgressDialog
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.callbacks.ButtonInterface
import com.example.bniapos.helpers.InitializationHelper
import com.example.bniapos.wrapper.BniWrapper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.

        var bniWrapper = BniWrapper()
        MainScope().launch {
            //first call this method for initialization
            val result = bniWrapper.initialize(this@SplashActivity)
            Log.d("result", result.toString())


          /*  val transactionResult = bniWrapper.showAllMenus(this@SplashActivity)
            Log.d("transactionResult", transactionResult.toString())

*/
            /**
             * use this method to start a transaction directly
             */

            val transactionResult = bniWrapper.startTransaction(2500114, this@SplashActivity)
            Log.d("transactionResult", transactionResult.toString())
        }

        //testInit()
//        Handler().postDelayed({
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    fun testInit() {
        ProgressDialog.showDialog(this@SplashActivity)
        var initHelper = InitializationHelper()
        initHelper.init(this@SplashActivity, false, true, true, false, apiResult)
        initHelper.PerformInitialization()
    }

    private val apiResult = object : ApiResult {
        override fun onSuccess(jsonResponse: Any) {
            ProgressDialog.dismissDialog()
            val intent = Intent(
                this@SplashActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        override fun onFailure(message: String) {
            Log.d("Failure", message)
            ProgressDialog.dismissDialog()
            //handle failure
            val buttonInterface: ButtonInterface = object : ButtonInterface {
                override fun onClicked(alertDialogBuilder: AlertDialog?) {
                    this@SplashActivity.finish()
                }
            }
            Alerts.customWebViewAlert(
                this@SplashActivity,
                message,
                buttonInterface
            )
        }

    }
}