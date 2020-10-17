package com.techbayportal.wataya.mvp.view.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.os.Handler
import javax.inject.Inject
import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
import android.content.Context
import android.location.Location
import android.view.WindowManager
import android.widget.FrameLayout
import com.techbayportal.wataya.R
import android.view.LayoutInflater
import dagger.android.AndroidInjector
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import dagger.android.DispatchingAndroidInjector

abstract class BaseActivity : AppCompatActivity() {

    fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val mHideHandler = Handler()
    private val mShowPart2Runnable = Runnable {
        val actionBar = this@BaseActivity.supportActionBar
        actionBar?.show()
    }

    var base_progress: FrameLayout? = null

    private lateinit var baseContentFrame: FrameLayout

    open var currentLocation: MutableLiveData<Location> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
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
        baseContentFrame = findViewById(R.id.base_content_frame)

        base_progress = findViewById(R.id.base_progress)
    }

    open fun progress(show: Boolean) {
        base_progress?.visibility = base_progress?.let {
            if (show) { View.VISIBLE } else { View.GONE }
        } ?: kotlin.run {
            View.GONE
        }
    }

    open fun loadFragment(fragment: Fragment) {
        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, fragment).addToBackStack(null)
        fragmentTransaction.commit()
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

    override fun onBackPressed() {
        progress(false)
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }

     private fun hideActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
    * so that, we can make other activity implementations looks like normal activity subclasses.
    */
    override fun setContentView(layoutResID: Int) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
//        activityNavBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        val stubView = inflater.inflate(layoutResID, baseContentFrame, false)
        baseContentFrame.removeAllViews()
        baseContentFrame.addView(stubView)
    }

    override fun setContentView(view: View) {
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        baseContentFrame.removeAllViews()
        baseContentFrame.addView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        baseContentFrame.removeAllViews()
        baseContentFrame.addView(view, params)
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}