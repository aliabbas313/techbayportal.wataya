package com.techbayportal.wataya.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object Helper {
    fun makeDelayJump(activity: Activity, aClass: KClass<*>, millSec: Int) {
        val handler = Handler()
        handler.postDelayed({
            activity.finish()
            activity.startActivity(Intent(activity, aClass.java))
        }, millSec.toLong())
    }

    fun  getPixelsFromDp(context: Context, dp : Int):Int {
        var scale = context.resources.displayMetrics.density
        return  (dp * scale + 0.5f).toInt()
    }
}
