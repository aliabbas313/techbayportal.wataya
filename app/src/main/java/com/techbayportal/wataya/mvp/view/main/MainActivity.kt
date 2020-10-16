package com.techbayportal.wataya.mvp.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kotlinpermissions.KotlinPermissions
import com.techbayportal.wataya.Application.Companion.context
import com.techbayportal.wataya.R
import com.techbayportal.wataya.helper.Helper
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import com.techbayportal.wataya.util.VectorDrawableUtils
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
    lateinit var mGoogleMap: GoogleMap
    lateinit var  mapWrapperLayout :MapWrapperLayout

    private var locationManager: LocationManager? = null

    private var activity: Activity? = null
    lateinit var googleApiClient: GoogleApiClient
    var currentLocFound: Boolean = true
    lateinit var mLastLocation: Location
    lateinit var currentMarker: Marker
    private var mapFragment: SupportMapFragment? = null

    private val onMapReady: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this
        mMainPresenter.onAttach(this)
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        onMapReady.observe(this, androidx.lifecycle.Observer {
            checkLocationPermission()
        })

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
        mGoogleMap = googleMap
        onMapReady.value = true
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        KotlinPermissions.with(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .onAccepted {
                googleApiClient = GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                        override fun onConnected(bundle: Bundle?) {
                            val locationRequest = LocationRequest.create()
                            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            locationRequest.interval = 30 * 1000.toLong()
                            locationRequest.fastestInterval = 5 * 1000.toLong()
                            val builder = LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                            //**************************
                            builder.setAlwaysShow(true) //this is the key ingredient
                            //**************************
                            val result = LocationServices.SettingsApi.checkLocationSettings(
                                googleApiClient,
                                builder.build()
                            )
                            result.setResultCallback { result ->
                                val status = result.status
                                val state = result.locationSettingsStates
                                when (status.statusCode) {
                                    LocationSettingsStatusCodes.SUCCESS -> {
                                        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0L,0f, loctionListener)
//                                      mGoogleMap.isMyLocationEnabled = true
                                        mGoogleMap.setOnMyLocationClickListener(onMyLocationClickListener)
                                    }
                                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                        try {
                                            status.startResolutionForResult(activity, 1000)
                                        } catch (e: IntentSender.SendIntentException) { // Ignore the error.
                                        }
                                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                    }
                                }
                            }

                        }

                        override fun onConnectionSuspended(i: Int) {

                        }
                    })
                    .addOnConnectionFailedListener { connectionResult: ConnectionResult? -> }.build()
                googleApiClient.connect()
            }
            .onDenied {

            }
            .onForeverDenied {

            }
            .ask()
    }
    private val onMyLocationClickListener = GoogleMap.OnMyLocationClickListener { location ->

        var marker = MarkerOptions()
            .position(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            )
            .title("Your Location")
            .icon(VectorDrawableUtils.getBitmapDescriptorFromVector(this, R.drawable.ic_location))
        mGoogleMap.addMarker(marker)

//        val circleOptions = CircleOptions()
//        circleOptions.center(LatLng(location.latitude,
//                location.longitude))
//        circleOptions.radius(200.0)
//        circleOptions.fillColor(Color.RED)
//        circleOptions.strokeWidth(6f)
//        mGoogleMap.addCircle(circleOptions)
    }

    private val loctionListener = object : LocationListener {

        override fun onLocationChanged(changeLoc: Location) {
            if (currentLocFound) {
                currentLocFound = false
                changeLoc?.let {
//                    mMainPresenter.getPointofInterests(it.latitude.toString(), it.longitude.toString())
                    mMainPresenter.getUserAddresses(123, "en")

                    mLastLocation = changeLoc

                    val marker = MarkerOptions()
                        .position(LatLng(it.latitude, it.longitude))
                        .title("Your Location")
                    currentMarker = mGoogleMap.addMarker(marker)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 18f))

                }
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        locationManager!!.removeUpdates(loctionListener)
    }
}