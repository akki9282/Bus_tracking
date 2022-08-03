package com.techfii.bustracking

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.techfii.bustracking.databinding.ActivityDriverTrackingBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.DriverViewModel
import android.os.Handler
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.techfii.bustracking.viewmodels.LoginViewModel

class DriverTrackingActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDriverTrackingBinding
    val zoomLevel = 12.0f
    var isMapReady = false
    private var zoomed = false
    var yourMarker: ArrayList<LatLng>? = ArrayList()

    lateinit var preferences: SavePreferences
    lateinit var driverModel: DriverViewModel
    lateinit var loginModel: LoginViewModel
    var journeyid = 0
    private var currentLocation: Location? = null
    lateinit var locationManager: LocationManager
    lateinit var locationByGps: Location
    lateinit var locationByNetwork: Location
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000// 120000 - >2 minutes

    companion object {
        fun startIntent(context: Context) {
            val i = Intent(context, DriverTrackingActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDriverTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2 // first in manifest
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        preferences = SavePreferences(this)
        driverModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        loginModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        locationByGps =
            Location(LocationManager.NETWORK_PROVIDER) // OR GPS_PROVIDER based on the requirement
        locationByGps.latitude = 42.125
        locationByGps.longitude = 55.123

        locationByNetwork =
            Location(LocationManager.NETWORK_PROVIDER) // OR GPS_PROVIDER based on the requirement
        locationByNetwork.latitude = 42.125
        locationByNetwork.longitude = 55.123

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (isLocationPermissionGranted()) {
            if (preferences.getJourneyid().isNullOrEmpty()) {
                getLastJourney()
            }

            getLocation()
        }
        setListeners()

    }

    fun setListeners() {
        binding.ivLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setMessage("After logout location update service will stop")
                .setPositiveButton("Logout",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        logout()
                    })
                .setNegativeButton("Cancel", null)
                .show()


        }
        binding.ivChangePwd.setOnClickListener {
            ChangePasswordActivity.startIntent(this)
        }

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            getLocation()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    fun createJourney() {
        journeyid = journeyid + 1

        binding.pg.visibility = View.VISIBLE
        driverModel.newJourney(
            this,
            preferences!!.getUserid().toString(),
            journeyid.toString(),
            currentLocation!!.latitude.toString(),
            currentLocation!!.longitude.toString(),
            "1",
            "0"
        )!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    preferences.setJourneyid(journeyid.toString())

                }
            })

    }

    fun updateDriver() {
        driverModel.newJourney(
            this,
            preferences!!.getUserid().toString(),
            preferences!!.getJourneyid().toString(),
            currentLocation!!.latitude.toString(),
            currentLocation!!.longitude.toString(),
            "1",
            "0"
        )!!.observe(this,
            Observer {
                if (it != null) {

                }
            })

    }

    fun logout() {
        loginModel.userlogout(this, preferences.getUserid().toString(), "driver")!!.observe(this,
            Observer {
                if (it != null) {
                }
            })
        binding.pg.visibility = View.VISIBLE
        driverModel.driverLoagout(
            this,
            preferences!!.getUserid().toString(),
            journeyid.toString()
        )!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE

                    preferences.setLogin(false)
                    preferences.setJourneyid("")
                    UserSelectionActivity.startIntent(this)
                }
            })
    }

    fun getLastJourney() {
        binding.pg.visibility = View.VISIBLE
        driverModel.getLastJourney(this)!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    journeyid = it.journey_id.toInt()
                    if (currentLocation != null) {
                        createJourney()
                    } else {
                        Toast.makeText(this, "Please turn on GPS location ", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
            false
        } else {

            true
        }
    }

    fun getLocation() {
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val gpsLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationByGps = location
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val networkLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationByNetwork = location
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        if (hasGps) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                gpsLocationListener
            )
        }
        if (hasNetwork) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0F,
                networkLocationListener
            )
        }

        if (!hasGps && !hasNetwork) {
            // notify user
            AlertDialog.Builder(this)
                .setMessage("Turn on location and restart your app")
                .setPositiveButton("Open Location Setting",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
            return
        }
        val lastKnownLocationByGps =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            locationByGps = lastKnownLocationByGps
        }

        val lastKnownLocationByNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocationByNetwork?.let {
            locationByNetwork = lastKnownLocationByNetwork
        }

        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps.accuracy > locationByNetwork!!.accuracy) {
                currentLocation = locationByGps
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
                // use latitude and longitude as per your need
            } else {
                currentLocation = locationByNetwork
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
                // use latitude and longitude as per your need
            }
        }
        if (currentLocation != null) {
            var city = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            if (isMapReady) {
                if (yourMarker!!.size > 1) {
                    yourMarker!!.clear();
                    mMap.clear();
                }
                yourMarker!!.add(city)
                val options = MarkerOptions()
                options.position(city)
                options.title("Driver Location").icon(
                    BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
                mMap.addMarker(options)
                if (!zoomed) {
                    //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, zoomLevel))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 2000, null);
                    zoomed = true
                }
                mMap.uiSettings.isZoomControlsEnabled = true
            }
        }
        updateDriver()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (data != null) {
                if (preferences.getJourneyid().isNullOrEmpty()) {
                    getLastJourney()
                }

                getLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        isMapReady = true
        //3
        mMap = googleMap


        var city = LatLng(-34.0, 151.0)
        if (currentLocation != null) {
            city = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)


            mMap.addMarker(MarkerOptions().position(city).title("Driver in kolhapur"))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 2000, null);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, zoomLevel))

            mMap.uiSettings.isZoomControlsEnabled = true
        }
    }

    override fun onMapClick(p0: LatLng) {
        // TODO("Not yet implemented")
    }
}