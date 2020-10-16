package com.techbayportal.wataya.helper

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    @JvmStatic
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}