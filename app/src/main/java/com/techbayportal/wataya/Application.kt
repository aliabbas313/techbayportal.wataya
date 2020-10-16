package com.techbayportal.wataya

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.techbayportal.wataya.di.injection.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class Application : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        instance = this
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any>? = dispatchingAndroidInjector

    companion object {
        private var instance: Application? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        @JvmStatic
        fun getAppContext(): Application? {
            return instance
        }
    }

}