package com.example.bniapos.wrapper

import MenuLink
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.bniapos.BniApplication
import com.example.bniapos.activities.BpControlsActivity
import com.example.bniapos.activities.CpControlsActivity
import com.example.bniapos.activities.HomeActivity
import com.example.bniapos.activities.SubMenuActivity
import com.example.bniapos.alerts.ProgressDialog
import com.example.bniapos.callback.ApiResult
import com.example.bniapos.enums.MenuType
import com.example.bniapos.helpers.InitializationHelper
import com.example.bniapos.utils.Configuration
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class BniWrapper() {
    private lateinit var channel: Channel<Boolean>

    suspend fun initialize(context: Context): Boolean {
        channel = Channel()
        val initHelper = InitializationHelper()
        initHelper.init(
            context, false,
            isDisplayResultDialog = true,
            isPrintSlipAfterSuccess = true,
            isAddExtraAdminMenus = false,
            responseCarrier = apiResult
        )
        initHelper.PerformInitialization()
        val result = channel.receive()
        channel.cancel()
        return result
    }

    fun showAllMenus(context: Context) {
        val intent = Intent(
            context,
            HomeActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun startTransaction(txnType: Int, context: Context) {
        val json = Configuration.getMenuConfig(context)
        val menuList = Gson().fromJson(json, Array<MenuLink>::class.java).asList()
        val menu = menuList.firstOrNull { it.txnType == txnType }
        if (menu != null) {
            var intent: Intent? = null
            when (menu.type.uppercase()) {
                MenuType.BP.name -> {
                    intent = Intent(context, BpControlsActivity::class.java)
                }
                MenuType.CP.name -> {
                    intent = Intent(context, CpControlsActivity::class.java)
                }
            }

            intent?.putExtra(SubMenuActivity.MENU, menu)
            intent?.putExtra(SubMenuActivity.WORKFLOW_ID, menu.workflowId)
            (context as Activity).startActivityForResult(intent, 1)
        }

    }

    private val apiResult = object : ApiResult {
        override fun onSuccess(jsonResponse: Any) {
            MainScope().launch {
                channel.send(true)
            }
        }

        override fun onFailure(message: String) {
            MainScope().launch {
                channel.send(false)
            }
        }

    }

}