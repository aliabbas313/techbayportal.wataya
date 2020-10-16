package com.techbayportal.wataya.mvp.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
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
import com.techbayportal.wataya.R
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import com.techbayportal.wataya.mvp.view.base.BaseActivity
import com.techbayportal.wataya.util.VectorDrawableUtils
import com.techbayportal.wataya.util.widget.MapWrapperLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainInterfaces.MainView, OnMapReadyCallback {

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

    private lateinit var lifecycleOwner: LifecycleOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this
        lifecycleOwner = this

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

        var cities: ArrayList<Int> = ArrayList()
        var areas: List<UserData.AreasOfCity> = ArrayList()
        var userAddresses: List<UserData.UserAddres> = ArrayList()

        var citiesList: ArrayList<String> = ArrayList()
        var areasList: ArrayList<String> = ArrayList()

        var city_selected_id: Int = 0

        val citySelected: MutableLiveData<Boolean> = MutableLiveData()

        data.data!!.cities.forEach {
            citiesList.add(it.name)
            cities.add(it.cityId)
        }

        var cityAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citiesList)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        city_spinner.adapter = cityAdapter

        city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                city_selected_id = cities[position]
                citySelected.value = true
            }
        }

        citySelected.observe(lifecycleOwner, androidx.lifecycle.Observer {
            areasList.clear()
            data.data!!.areasOfCities.forEach {
                if(it.cityId == city_selected_id) {
                    areasList.add(it.name)
                }
            }

            var areaAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areasList)
            areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            area_spinner.adapter = areaAdapter

            area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                }
            }
        })


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
                                        locationManager?.requestLocationUpdates(
                                            LocationManager.NETWORK_PROVIDER,
                                            0L,
                                            0f,
                                            loctionListener
                                        )
//                                      mGoogleMap.isMyLocationEnabled = true
                                        mGoogleMap.setOnMyLocationClickListener(
                                            onMyLocationClickListener
                                        )
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


    override fun onDestroy() {
        super.onDestroy()
        locationManager!!.removeUpdates(loctionListener)
    }
}