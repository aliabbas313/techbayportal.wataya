package com.techbayportal.wataya.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.techbayportal.wataya.Application

class AppStatus {
    var connectivityManager: ConnectivityManager? = null
    var wifiInfo: NetworkInfo? = null
    var mobileInfo: NetworkInfo? = null
    var connected = false

    //            System.out.println("CheckConnectivity Exception: " + e.getMessage());
//            Log.v("connectivity", e.toString());
    val isOnline: Boolean
        get() {
            try {
                connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager!!.activeNetworkInfo
                connected = networkInfo != null && networkInfo.isAvailable &&
                        networkInfo.isConnected
                return connected
            } catch (e: Exception) {
//            System.out.println("CheckConnectivity Exception: " + e.getMessage());
//            Log.v("connectivity", e.toString());
            }
            return connected
        }

    companion object {
        val instance = AppStatus()
        var context: Context? = Application.applicationContext()
    }
}