package com.techbayportal.wataya.helper

import android.app.Activity
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
}
