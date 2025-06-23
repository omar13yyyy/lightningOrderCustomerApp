package com.lightning.androidfrontend.utils

import java.text.NumberFormat
import java.util.Locale

fun getCurrency () :String{

    return "ل.س"
}


fun priceText(amount: Number):String {
    val LTR_EMBEDDING = '\u202A'
    val POP_DIRECTIONAL = '\u202C'
    val NBSP = '\u00A0'
    val formatter = NumberFormat.getInstance(Locale.US).apply{
        minimumFractionDigits = 1
        maximumFractionDigits = 2
    }
    val formatted = formatter.format(amount)


    return "$LTR_EMBEDDING${NBSP}$formatted$POP_DIRECTIONAL ل.س"

}
fun formatNumber(amount: Number): String {
    val formatter = NumberFormat.getInstance(Locale.US)
    return formatter.format(amount)  // لا تستعمل toString() هنا
}