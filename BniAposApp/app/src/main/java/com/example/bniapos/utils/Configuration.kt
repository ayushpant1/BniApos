package com.example.bniapos.utils

import android.content.Context
import android.os.Build
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


object Configuration {
    val path: String = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        "sdcard"
    } else {
        "Share"
    }

    private const val PREFIX_MENU = "menu"
    private const val MENU_FILE_EXTENSION = "json"
    private const val PREFIX_WORKFLOW = "workflow"
    private const val WORKFLOW_FILE_EXTENSION = "json"


    fun getMenuConfig(context: Context): String {
        val fileList = File(path).listFiles() ?: emptyArray()
        val configFiles = mapOf<String, List<File>>(
            "MENU" to fileList.filter {
                it.name.contains(PREFIX_MENU) && it.extension == MENU_FILE_EXTENSION
            }
        )
        if ((configFiles["MENU"].isNullOrEmpty())) {
            return loadMenuFromAsset(context)!!
        }

        configFiles["MENU"]?.map {
            val charset: Charset = Charsets.UTF_8
            val json: String? = try {
                val `is`: InputStream = FileInputStream(it)
                val size: Int = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, charset)
            } catch (ex: IOException) {
                ex.printStackTrace().toString()
            }
            val jsonObject = JSONObject(json!!)
            val jsonArray = jsonObject.getJSONObject("Menus").getJSONArray("MenuLink")
            return jsonArray.toString()
        }

        return null!!
    }

    fun getWorkflowConfig(context: Context): String {
        val fileList = File(path).listFiles() ?: emptyArray()
        val configFiles = mapOf<String, List<File>>(
            "WORKFLOW" to fileList.filter {
                it.name.contains(PREFIX_WORKFLOW) && it.extension == WORKFLOW_FILE_EXTENSION
            }
        )
        if ((configFiles["WORKFLOW"].isNullOrEmpty())) {
            return loadWorkflowFromAsset(context)!!
        }

        configFiles["WORKFLOW"]?.map {
            val charset: Charset = Charsets.UTF_8
            val json: String? = try {
                val `is`: InputStream = FileInputStream(it)
                val size: Int = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, charset)
            } catch (ex: IOException) {
                ex.printStackTrace().toString()
            }
            val jsonObject = JSONObject(json!!)
            val jsonArray = jsonObject.getJSONArray("WORKFLOW")
            return jsonArray.toString()
        }

        return null!!
    }

    /**
     * load menu from the assets
     */

    private fun loadMenuFromAsset(context: Context): String? {
        val charset: Charset = Charsets.UTF_8
        val json: String? = try {
            val `is`: InputStream = context.assets!!.open("menu.json")
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
        val jsonArray = jsonObject.getJSONObject("Menus").getJSONArray("MenuLink")

        return jsonArray.toString()
    }

    /**
     * load  workflow from assets
     */

    private fun loadWorkflowFromAsset(context: Context): String? {
        val charset: Charset = Charsets.UTF_8
        val json: String? = try {
            val `is`: InputStream = context.assets.open("workflow.json")
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
        val jsonArray = jsonObject.getJSONArray("WORKFLOW")

        return jsonArray.toString()
    }

}