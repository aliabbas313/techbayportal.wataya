package com.techbayportal.wataya.mvp.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.techbayportal.wataya.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    var base_progress: FrameLayout? = null

    private lateinit var baseContentFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)

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

    override fun onBackPressed() {
        progress(false)
        super.onBackPressed()
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