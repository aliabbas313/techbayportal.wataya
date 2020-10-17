package com.techbayportal.wataya.mvp.view.main

import android.Manifest
import android.os.Bundle
import android.view.View
import android.app.Activity
import android.location.Location
import com.techbayportal.wataya.R
import android.content.IntentSender
import androidx.fragment.app.Fragment
import android.annotation.SuppressLint
import android.location.LocationManager
import android.location.LocationListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.GoogleMap
import com.kotlinpermissions.KotlinPermissions
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import com.techbayportal.wataya.util.VectorDrawableUtils
import com.techbayportal.wataya.mvp.view.base.BaseActivity
import com.google.android.gms.location.LocationSettingsRequest
import com.techbayportal.wataya.mvp.view.addaddress.AddAddress
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData

class MainActivity : BaseActivity(), MainInterfaces.MainView, OnMapReadyCallback {

    private var activity: Activity? = null

    private var mapFragment: SupportMapFragment? = null

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var googleApiClient: GoogleApiClient
    private var locationManager: LocationManager? = null

    private val onMapReady: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var lifecycleOwner: LifecycleOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this
        lifecycleOwner = this

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        onMapReady.observe(this, androidx.lifecycle.Observer {
            checkLocationPermission()
        })

        fab_add.setOnClickListener {
            fab_add.visibility = View.GONE
            main_content.visibility = View.VISIBLE
            val addAddress: Fragment = AddAddress(lifecycleOwner)
            loadFragment(addAddress)
        }

    }

    override fun showData(data: BaseModel<UserData>) {
    }

    override fun showError(error: String) {
    }

    override fun showLoading() {
        progress(true)
    }

    override fun hideLoading() {
        progress(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        onMapReady.value = true
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager!!.removeUpdates(loctionListener)
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        KotlinPermissions.with(this).permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
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
                            builder.setAlwaysShow(true)
                            //**************************
                            val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

                            result.setResultCallback { result ->
                                val status = result.status
                                val state = result.locationSettingsStates
                                when (status.statusCode) {
                                    LocationSettingsStatusCodes.SUCCESS -> {
                                        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, loctionListener)
                                        mGoogleMap.isMyLocationEnabled = true
                                        mGoogleMap.setOnMyLocationClickListener(onMyLocationClickListener)
                                    }
                                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                        try { status.startResolutionForResult(activity, 1000) }
                                        catch (e: IntentSender.SendIntentException) { // Ignore the error.
                                        }
                                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }
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

        fab_add.visibility = View.VISIBLE
        currentLocation.value = location

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
    }

    private var currentLocFound: Boolean = true

    private val loctionListener = object : LocationListener {

        override fun onLocationChanged(changeLoc: Location) {
            if (currentLocFound) {
                currentLocFound = false
                fab_add.visibility = View.VISIBLE

                currentLocation.value = changeLoc
                changeLoc?.let {

                    val marker = MarkerOptions()
                        .position(LatLng(it.latitude, it.longitude))
                        .title("Your Location")
                    mGoogleMap.addMarker(marker)
                    mGoogleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                it.latitude,
                                it.longitude
                            ), 18f
                        )
                    )

                }
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fab_add.visibility = View.VISIBLE

    }
}