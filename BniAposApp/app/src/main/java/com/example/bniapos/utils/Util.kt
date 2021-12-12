package com.example.bniapos.utils

import android.app.Activity
import com.google.gson.JsonObject


object Util {
    /**
     * Merge "source" into "target". If fields have equal name, merge them recursively.
     * Null values in source will remove the field from the target.
     * Override target values with source values
     * Keys not supplied in source will remain unchanged in target
     *
     * @return the merged object (target).
     */
    @Throws(Exception::class)
    fun deepMerge(source: JsonObject, target: JsonObject): JsonObject? {
        for ((key, value) in source.entrySet()) {
            if (!target.has(key)) {
                //target does not have the same key, so perhaps it should be added to target
                if (!value.isJsonNull) //well, only add if the source value is not null
                    target.add(key, value)
            } else {
                if (!value.isJsonNull) {
                    if (value.isJsonObject) {
                        //source value is json object, start deep merge
                        deepMerge(value.asJsonObject, target[key].asJsonObject)
                    } else {
                        target.add(key, value)
                    }
                } else {
                    target.remove(key)
                }
            }
        }
        return target
    }


    fun getReferenceNo(context: Activity): String {
        val transactionDate = DateTimeUtils.getCurrentDateTimeYYYYMMDDHHMMssSSSSS()
        val randomNo = (0..9).random().toString()
        val agenCounterCode = SharedPreferenceUtils.getInstance(context).getAgenCounterCode()
        return "$transactionDate$randomNo$agenCounterCode"
    }
}