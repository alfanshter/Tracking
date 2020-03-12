package com.alfanshter.jatimpark

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout.HORIZONTAL
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.Session.SessionManager
import com.alfanshter.jatimpark.adapter.LokasiAdapter
import com.firebase.geofire.GeoFire
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngQuad
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.ImageSource
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.activity_tracking.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*
import com.mapbox.android.core.location.*
import com.mapbox.api.directions.v5.DirectionsCriteria


@Suppress("DEPRECATION")
class Tracking : AppCompatActivity(),OnMapReadyCallback, PermissionsListener,
MapboxMap.OnMapClickListener {

    var reference: DatabaseReference? = null
    var Currentreference: DatabaseReference? = null
    var Circlereference: DatabaseReference? = null

    var user: FirebaseUser? = null
    var userID: String? = null
    var auth: FirebaseAuth? = null

    private val tempatdestinasi = arrayOf(
        LatLng(-7.818474, 112.525348),
        LatLng(-7.818836, 112.525348)
    )
    private var permissionsManager: PermissionsManager = PermissionsManager(this)

    lateinit var mapboxMap: MapboxMap
    private lateinit var startnavigasi: Button
    private lateinit var locationComponent: LocationComponent

    private var currentRoute: DirectionsRoute? = null
    private var navigationMapRoute: NavigationMapRoute? = null
    private val TAG = "DirectionsActivity"

    private val ID_IMAGE_SOURCE = "animated_image_source"
    private val ID_IMAGE_SOURCEdua = "animated_image_source2"
    private val ID_IMAGE_LAYER = "animated_image_layer"
    private val ID_IMAGE_LAYERdua = "animated_image_layer2"

    private var locationEngine: LocationEngine? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

    private var callback = MainActivityLocationCallback(this)

     var lastlatitude : String? = null


    @SuppressLint("WrongConstant", "LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_tracking)
        mapview.onCreate(savedInstanceState)
        mapview.getMapAsync(this)
        val recyclerview = findViewById<View>(R.id.recyclerlokasi) as RecyclerView

        val tempat = intArrayOf(
            R.drawable.selectasatu,
            R.drawable.selectadua,
            R.drawable.selectatiga,
            R.drawable.selectaempat
        )

        val nama = arrayOf("kolamrenang", "tempat ayun", "outbond", "gulat")
        val layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = LokasiAdapter(tempat, nama, this)

        reference = FirebaseDatabase.getInstance().reference.child("Selecta/Users")

        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser
        userID = user!!.uid

        /*val toolbar : Toolbar = findViewById(R.id.toolbar)
        val drawer :DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle  = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open
            ,R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
*/
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.setStyle(Style.LIGHT) {

            // Set the latitude and longitude values for the image's four corners
            val quad = LatLngQuad(
                LatLng(-7.817532, 112.525955),
                LatLng(-7.817291, 112.525754),
                LatLng(-7.817399, 112.525500),
                LatLng(-7.817588, 112.525624)
            )
            val kolamanak = LatLngQuad(
                LatLng(-7.81818, 112.52519),
                LatLng(-7.81812, 112.52536),
                LatLng(-7.81786, 112.52517),
                LatLng(-7.81790, 112.52506)
            )
            it.addSource(ImageSource(ID_IMAGE_SOURCE, quad, R.drawable.selecta))
            it.addSource(ImageSource(ID_IMAGE_SOURCEdua, kolamanak, R.drawable.pool))
            it.addLayer(RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE))
            it.addLayer(RasterLayer(ID_IMAGE_LAYERdua, ID_IMAGE_SOURCEdua))
            LoadGeoJson(this@Tracking).execute()
            enableLocationComponent(it)
            addDestinationIconSymbolLayer(it)
            Toast.makeText(this, "Get Location", Toast.LENGTH_LONG).show()
            mapboxMap.addOnMapClickListener(this)

            navigasia.setOnClickListener {
                var simulateRoute = true
                var options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
/*
                    .shouldSimulateRoute(simulateRoute)
*/
                    .build()
                NavigationLauncher.startNavigation(this, options)


            }


        }

    }


    private fun addDestinationIconSymbolLayer(@NonNull loadedMapStyle: Style) {
        loadedMapStyle.addImage(
            "destination-icon-id",
            BitmapFactory.decodeResource(this.resources, R.drawable.mapbox_marker_icon_default)
        )
        val geoJsonSource = GeoJsonSource("destination-source-id")
        loadedMapStyle.addSource(geoJsonSource)
        val destinationSymbolLayer =
            SymbolLayer("destination-symbol-layer-id", "destination-source-id")
        destinationSymbolLayer.withProperties(
            iconImage("destination-icon-id"),
            iconAllowOverlap(true),
            iconIgnorePlacement(true)
        )
        loadedMapStyle.addLayer(destinationSymbolLayer)
    }


    private class LoadGeoJson internal constructor(activity: Tracking) :
        AsyncTask<Void?, Void?, FeatureCollection?>() {
        private val weakReference: WeakReference<Tracking> = WeakReference(
            activity
        )


        override fun onPostExecute(featureCollection: FeatureCollection?) {
            super.onPostExecute(featureCollection)
            val activity: Tracking? =
                weakReference.get()
            if (activity != null && featureCollection != null) {
                activity.drawLines(featureCollection)
            }
        }

        companion object {
            fun convertStreamToString(`is`: InputStream?): String {
                val scanner = Scanner(`is`).useDelimiter("\\A")
                return if (scanner.hasNext()) scanner.next() else ""
            }
        }

        override fun doInBackground(vararg params: Void?): FeatureCollection? {
            try {
                val activity: Tracking? =
                    weakReference.get()
                if (activity != null) {
                    val inputStream: InputStream =
                        activity.assets.open("example.geojson")
                    return FeatureCollection.fromJson(
                        convertStreamToString(
                            inputStream
                        )
                    )
                }
            } catch (exception: Exception) {
                Timber.e("Exception Loading GeoJSON: %s", exception.toString())
            }
            return null
        }

    }

    internal fun drawLines(featureCollection: FeatureCollection) {
        mapboxMap.getStyle { style: Style ->
            if (featureCollection.features() != null) {
                if (featureCollection.features()!!.size > 0) {
                    style.addSource(GeoJsonSource("line-source", featureCollection))
                    // The layer properties for our line. This is where we make the line dotted, set the
                    // color, etc.
                    style.addLayer(
                        LineLayer("linelayer", "line-source")
                            .withProperties(
                                lineCap(Property.LINE_CAP_SQUARE),
                                lineJoin(Property.LINE_JOIN_MITER),
                                lineOpacity(
                                    .5f
                                ),
                                lineWidth(5f),
                                lineColor(
                                    Color.parseColor("#3bb2d0")
                                )
                            )
                    )
                }
            }
        }
    }


/*
    fun lokasiterkini(){
        //Menentukan titik koordinat
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(this
        ) { location ->
            Log.d("Lokasi saat ini", "Lat : ${location?.latitude} Long : ${location?.longitude}")
            lokasi.text = "Lat : ${location?.latitude} Long : ${location?.longitude}"
        }
    }
*/


    //Mengatur pembaruan lokasi
    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()

        locationEngine!!.requestLocationUpdates(request, callback, mainLooper)
        locationEngine!!.getLastLocation(callback)
/*
        locationEngine.removeLocationUpdates(request,callback,mainLooper)
*/
/*
        locationEngine.getLastLocation(callback)
*/
    }


    @SuppressLint("MissingPermission", "WrongConstant")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(this, loadedMapStyle)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
            initLocationEngine()


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }


    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            if (mapboxMap.style != null) {
                enableLocationComponent(mapboxMap.style!!)
            }
        } else {
            toast(R.string.user_location_permission_not_granted)
            finish()
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    internal class MainActivityLocationCallback internal constructor(activity: Tracking) :
        LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<Tracking> = WeakReference(activity)

        @SuppressLint("SetTextI18n")
        override fun onSuccess(result: LocationEngineResult?) {
            val activity: Tracking? = activityWeakReference.get()
            if (activity != null) {
                val location: Location? = result?.lastLocation
                if (location == null) {
                    return

                }

                activity.lokasi.text =
                    "Latitude: ${result.lastLocation!!.latitude}, Longitude ${result.lastLocation!!.longitude}"
                activity.reference!!.child(activity.userID!!).child("Longitude")
                    .setValue(result.lastLocation!!.longitude)
                activity.reference!!.child(activity.userID!!).child("Latitude")
                    .setValue(result.lastLocation!!.latitude)


                val sessionManager  = SessionManager(activity)

                sessionManager.setlongitude(result.lastLocation!!.longitude.toString())

                if (result.lastLocation != null) {
                    activity.mapboxMap.locationComponent.forceLocationUpdate(result.lastLocation)
                }

                activity.lastlatitude = "${result.lastLocation!!.longitude}"
                activity.toast(activity.lastlatitude!!)
            }
        }

        @SuppressLint("LogNotTimber")
        override fun onFailure(exception: java.lang.Exception) {
            Log.d("LocationChange", exception.localizedMessage)
            val activity: Tracking? = activityWeakReference.get()
            if (activity != null) {
                Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onStart() {
        super.onStart()
        mapview.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapview.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapview.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapview.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (locationEngine != null) {
            locationEngine!!.removeLocationUpdates(callback)
        }
        mapview.onDestroy()
    }

    override fun onMapClick(point: LatLng): Boolean {
        var destinationPoint = Point.fromLngLat(point.longitude, point.latitude)
        var originPoint = Point.fromLngLat(
            locationComponent.lastKnownLocation!!.longitude,
            locationComponent.lastKnownLocation!!.latitude
        )
        mapboxMap.style?.getSourceAs<GeoJsonSource>("destination-source-id")
            ?.setGeoJson(Feature.fromGeometry(destinationPoint))
        getRoute(originPoint, destinationPoint)
        return true
    }

    private fun getRoute(origin: Point, destination: Point) {
        var client = MapboxDirections.builder()
        NavigationRoute.builder(applicationContext)
            .accessToken(getString(R.string.access_token))
            .origin(origin)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .destination(destination)
            .build()

            .getRoute(object : Callback<DirectionsResponse> {
                @SuppressLint("LogNotTimber")
                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Log.e(TAG, "Error" + t.message)
                }

                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    @NonNull
                    call: Call<DirectionsResponse>, @NonNull
                    response: Response<DirectionsResponse>
                ) {
                    Log.d(TAG, " Response Codde :" + response.code())
                    if (response.body() == null) {
                        Log.e(TAG, "Tidak ada Route, pastikan menggunakan token yang tepat")
                        return
                    } else if (response.body()!!.routes().size < 1) {
                        Log.e(TAG, "Tidak ada Route")
                        return
                    }

                    currentRoute = response.body()!!.routes().get(0)
                    if (navigationMapRoute != null) {
                        navigationMapRoute!!.removeRoute()
                    } else {
                        navigationMapRoute =
                            NavigationMapRoute(null, mapview, mapboxMap, R.style.NavigationMapRoute)
                    }
                    navigationMapRoute!!.addRoute(currentRoute)
                }
            })


    }

    fun getuserall() {
        var getuserall = FirebaseDatabase.getInstance().reference.child("Selecta/userall")
        var geofire = GeoFire(getuserall)

        //menuju destinasi marker
        /*  private fun pergikerute(point: LatLng):Boolean{
        var destinationPoint = Point.fromLngLat(point.longitude,point.latitude)
        var originPoint = Point.fromLngLat(locationComponent.lastKnownLocation!!.longitude,
            locationComponent.lastKnownLocation!!.latitude
        )

        for (singleLatLng in tempatdestinasi){
            getRoute()
        }
        return true
    }*/


    }
}
