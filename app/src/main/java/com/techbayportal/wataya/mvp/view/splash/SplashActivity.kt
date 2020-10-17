package com.techbayportal.wataya.mvp.view.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import android.os.Handler
import javax.inject.Inject
import android.app.Activity
import android.graphics.Color
import com.techbayportal.wataya.R
import android.view.WindowManager
import dagger.android.AndroidInjector
import androidx.fragment.app.Fragment
import com.techbayportal.wataya.helper.Helper
import androidx.appcompat.app.AppCompatActivity
import dagger.android.DispatchingAndroidInjector
import com.techbayportal.wataya.mvp.view.main.MainActivity
import com.techbayportal.wataya.mvp.data.local.PreferencesHelper

class SplashActivity : AppCompatActivity(), SplashInterfaces.SplashView {

    fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mMainPresenter: SplashActivityPresenter<SplashInterfaces.SplashView>

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val mHideHandler = Handler()
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        val actionBar = this@SplashActivity.supportActionBar
        actionBar?.show()
    }
    private val mHideRunnable = { hideActionBar() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideActionBar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        mMainPresenter.onAttach(this)

        Helper.makeDelayJump(this, MainActivity::class, 1200)
    }

    override fun hideActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
    }

    override fun delayedHide(delayMillis: Int) {
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {

        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}


