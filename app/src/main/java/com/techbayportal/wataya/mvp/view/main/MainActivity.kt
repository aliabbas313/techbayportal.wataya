package com.techbayportal.wataya.mvp.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.techbayportal.wataya.Application.Companion.context
import com.techbayportal.wataya.R
import com.techbayportal.wataya.helper.Helper
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import com.techbayportal.wataya.util.widget.MapWrapperLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainInterfaces.MainView, OnMapReadyCallback {

    fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mMainPresenter: MainActivityPresenter<MainInterfaces.MainView>
    lateinit var mMap: GoogleMap
    lateinit var  mapWrapperLayout :MapWrapperLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainPresenter.onAttach(this)

        mMainPresenter.getUserAddresses(123, "en")
    }

    override fun showData(data: BaseModel<UserData>) {
    }

    override fun showError(error: String) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapWrapperLayout = findViewById(R.id.map_relative_layout)

        mapWrapperLayout.init(mMap, Helper.getPixelsFromDp(context!!, 39 + 20))


    }
}