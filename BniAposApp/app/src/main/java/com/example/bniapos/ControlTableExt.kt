package com.example.bniapos

import com.example.bniapos.database.entities.ControlTable

fun List<ControlTable>.convertToDataString(): List<String> {
    val dataString: MutableList<String> = ArrayList()
    this.forEach {
        dataString.add(it.name!!)
    }
    dataString.add(0,"---Select---")
    return dataString
}