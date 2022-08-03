package com.techfii.bustracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.techfii.bustracking.databinding.ActivityMapBinding
import com.techfii.bustracking.models.MapData
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.DriverViewModel
import okhttp3.OkHttpClient
import okhttp3.Request


class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    var isMapReady = false
    val zoomLevel = 12.0f
    lateinit var preferences: SavePreferences
    lateinit var driverModel: DriverViewModel
    var polylineFinal: Polyline? = null
    private var currentLocation: Location? = null
    lateinit var locationManager: LocationManager
    lateinit var locationByGps: Location
    lateinit var locationByNetwork: Location
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var driverLatitude = "0.0"
    var driverLongitude = "0.0"
    var driver_id = "6"
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 60000//1 minutes 5000//SECONDS // 120000 2 MINUTES
    private var zoomed = false
    var yourMarker: ArrayList<LatLng>? = ArrayList()
    private var mOrigin: LatLng? = null
    private var mDestination: LatLng? = null
    lateinit var duration: String
    lateinit var distance: String

    companion object {
        fun startIntent(context: Context) {
            val i = Intent(context, MapActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        driverLatitude = intent.getStringExtra("latitude").toString()
        driverLongitude = intent.getStringExtra("longitude").toString()
        driver_id = intent.getStringExtra("driver_id").toString()
        // 2 // first in manifest
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        preferences = SavePreferences(this)
        driverModel = ViewModelProvider(this).get(DriverViewModel::class.java)

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


        }


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
                if (mMap != null) {
                    if (yourMarker!!.size > 1) {
                        yourMarker!!.clear();
                        mMap.clear();
                    }
                    yourMarker!!.add(city)
                    val options = MarkerOptions()
                    options.position(city)
                    options.title("Your Location").icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                    )
                    mMap.addMarker(options)

                    if (!zoomed) {
                        //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, zoomLevel))
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 2000, null);
                        zoomed = true
                    }

                    mMap.uiSettings.isZoomControlsEnabled = true
                    var driver = LatLng(driverLatitude.toDouble(), driverLongitude.toDouble())
                    mMap.addMarker(
                        MarkerOptions().position(driver).title("Driver")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_bus))
                    )
                    mOrigin = city
                    mDestination = driver

                    val route = PolylineOptions()
                    route.color(Color.RED)
                    route.width(5f)
                    val url = getDirectionURL(mOrigin!!, mDestination!!)
                    GetDirection(url).execute()
                }
            }
        }


    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng): String {
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()


        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$apiKey"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                distance = respObj.routes[0].legs[0].distance.text
                duration = respObj.routes[0].legs[0].duration.text

                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {

            if (polylineFinal != null) {
                polylineFinal!!.remove()
            }
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(8f)
                lineoption.color(Color.BLACK)
                lineoption.geodesic(true)
            }

            polylineFinal = mMap.addPolyline(lineoption)
            binding.distance.text = "Distance: ${distance}"
            binding.duration.text = "Duration: ${duration}"
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (data != null) {


                //     getLocation()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
    }

    override fun onResume() {

        super.onResume()
        getDriverLocation()
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            getDriverLocation()
        }.also { runnable = it }, delay.toLong())

    }

    override fun onMapReady(googleMap: GoogleMap) {
        isMapReady = true
        //3
        mMap = googleMap


        var city = LatLng(-34.0, 151.0)
        if (currentLocation != null) {
            city = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)


            mMap.addMarker(
                MarkerOptions().position(city).title("Your Location").icon(
                    BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                )
            )
            //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, zoomLevel))

            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 2000, null);


            mMap.uiSettings.isZoomControlsEnabled = true
        }
        var driver = LatLng(driverLatitude.toDouble(), driverLongitude.toDouble())
        mMap.addMarker(
            MarkerOptions().position(driver).title("Driver")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_bus))
        )

    }

    fun getDriverLocation() {
        driverModel.driverLocation(this, driver_id)!!.observe(this,
            Observer {
                if (it != null) {

                    if (it.status) {
                        if (!it.latitude.isNullOrEmpty())
                            driverLatitude = it.latitude
                        if (!it.longitude.isNullOrEmpty())
                            driverLongitude = it.longitude
                        getLocation()
                    } else {
                        /* Toast.makeText(
                             this,
                             "Sorry,Could not load Driver location",
                             Toast.LENGTH_LONG
                         ).show()*/
                    }

                }
            })
    }

    override fun onMapClick(p0: LatLng) {

    }

}