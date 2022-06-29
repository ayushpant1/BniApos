package com.example.bniapos

import android.app.Application
import android.content.Context
import com.example.paymentsdk.CTIApplication
import kotlin.system.exitProcess

class BniApplication : Application() {


    /**
     * Create.
     */
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        CTIApplication.setContext(applicationContext)
        bindSdkDeviceService(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        CTIApplication.unbindServiceConnection()
        exitProcess(0)
    }

    companion object {
        var context: Context? = null

        private fun bindSdkDeviceService(context: Context) {
            CTIApplication.bindSdkDeviceService(context)
        }
    }

}