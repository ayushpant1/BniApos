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
        CTIApplication.setContext(applicationContext)
        bindSdkDeviceService(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        CTIApplication.unbindServiceConnection()
        exitProcess(0)
    }

    companion object {
        private fun bindSdkDeviceService(context: Context) {
            CTIApplication.bindSdkDeviceService(context)
        }
    }

}