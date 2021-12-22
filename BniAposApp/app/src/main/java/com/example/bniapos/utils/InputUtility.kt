package com.example.bniapos.utils

object InputUtility {
    fun isValidInt(text: String): Boolean {
        return text.matches(Regex("-?(0|[1-9]\\d*)"))
    }
}