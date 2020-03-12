package com.alfanshter.jatimpark.ui.shareRombongan

import kotlinx.android.synthetic.main.fragment_sharerombongandua.*


import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.jatimpark.Model.ModelBaru
import com.alfanshter.jatimpark.Model.ModelSharing
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.alfanshter.jatimpark.ui.shareRombongan.listuser.UsersFragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoQuery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.share_rombongan_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.internal.InlineMarker

/**
 * A simple [Fragment] subclass.
 */
class sharerombongandua : Fragment(), OnMapReadyCallback, MapboxMap.OnMapClickListener,
    PermissionsListener, AnkoLogger {
    private lateinit var sessionManager: SessionManager
    private lateinit var butonkode: Button
    private lateinit var mapboxMap: MapboxMap
    private var markerViewManager: MarkerViewManager? = null
    private lateinit var locationComponent: LocationComponent
    private var callback = MainActivityLocationCallbackBaru(this)
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private var locationEngine: LocationEngine? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    private lateinit var referencesharing: DatabaseReference
    private lateinit var referencejoin: DatabaseReference
    lateinit var referensehapus: DatabaseReference
    lateinit var userID: String
    private lateinit var marker: MarkerView
    private var setMarker: Boolean = false
    private var hasDraw: Boolean = false
    private var statusjoin: Boolean = false
    private lateinit var customview: View
    private lateinit var animator: ValueAnimator
    private lateinit var geoJsonSource: GeoJsonSource
    var lokasi = LatLng(00.00, 00.00)
    private var currentPosition = LatLng(-7.81958192107, 112.52652012)

    var nama = ""
    @SuppressLint("UseSparseArrays")
    var markerMap: HashMap<Int, MarkerView> = HashMap<Int, MarkerView>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusjoin = true
        Mapbox.getInstance(context!!.applicationContext, getString(R.string.access_token))
        val root = inflater.inflate(R.layout.fragment_sharerombongandua, container, false)
        val mapView = root.findViewById(R.id.mapboxfamily) as MapView
        val butonview : Button = root.find(R.id.viewuser)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        hasDraw = false
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        userID = user.uid
        sessionManager = SessionManager(context)
        butonkode = root.find(R.id.keluarshare)

        butonview.setOnClickListener {
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.nav_host_fragment, UsersFragment())
            fr?.commit()

        }


        // Inflate the layout for this fragment
        butonkode.setOnClickListener {
            sessionManager.setIDStatusUser("")
            statusjoin = false
            referensehapus =
                FirebaseDatabase.getInstance().reference.child("Selecta").child("sharing")
            referensehapus.child(sessionManager.getKunci().toString()).child(userID).removeValue()
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.nav_host_fragment, ShareRombongan())
            fr?.commit()


        }

        return root

    }

    fun ambildata()
    {

        referencesharing =
            FirebaseDatabase.getInstance().reference.child("Selecta").child("sharing")
                .child(sessionManager.getKunci().toString())

        referencesharing =
            FirebaseDatabase.getInstance().reference.child("Selecta")
                .child("sharing")
                .child(sessionManager.getKunci().toString())
        referencesharing.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            @SuppressLint("InflateParams")
            override fun onDataChange(p0: DataSnapshot) {
                for (values in markerMap.values) {
                    markerViewManager?.removeMarker(values)
                }
                                        if (setMarker == true){
                                            mapboxMap.clear()
                                            mapboxMap.markers.clear()
                                            //markerViewManager = null
                                        }
                //   markerViewManager?.removeMarker(marker)
                var counter: Int = 0
                for (i in p0.children) {
                    var user: ModelSharing? =
                        i.getValue(ModelSharing::class.java)
                    var datalongitude = user!!.longitude
                    var datalatitude = user.latidude
                    lokasi = LatLng(datalatitude, datalongitude)
                    nama = user.name
                    /*               activity.updateMarkerPosition(lokasi)
                    */

                    /*customview =
                        LayoutInflater.from(context!!.applicationContext)
                            .inflate(R.layout.marker, null)
                    customview.layoutParams =
                        FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    val titleTextView: TextView =
                        customview.findViewById(R.id.marker_window_title)
                    titleTextView.text = nama
                    marker = MarkerView(lokasi, customview)
                    markerViewManager?.addMarker(marker)
                    markerMap.put(counter, marker)
                    counter++*/

                    //
                                            if (setMarker == true) {
                                                mapboxMap.addMarker(
                                                    MarkerOptions().position(lokasi)
                                                        .title(nama)
                                                )
                                            }

                    //break

                }
                hasDraw = true

            }
        })

    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        setMarker = true

        geoJsonSource = GeoJsonSource(
            "source-id",
            Feature.fromGeometry(
                Point.fromLngLat(
                    currentPosition.longitude,
                    currentPosition.latitude
                )
            )
        )

        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)
            markerViewManager = MarkerViewManager(mapboxfamily, mapboxMap)
            it.addImage(
                ("marker_icon"), BitmapFactory.decodeResource(
                    resources, R.drawable.red_marker
                )
            )
            it.addSource(geoJsonSource)
            it.addLayer(
                SymbolLayer("layer-id", "source-id")
                    .withProperties(
                        PropertyFactory.iconImage("marker_icon"),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconAllowOverlap(true)
                    )
            )

            mapboxMap.addOnMapClickListener(this)

        }

        ambildata()



    }

    @SuppressLint("MissingPermission", "WrongConstant")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(context?.applicationContext)) {
            locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(
                context?.applicationContext!!,
                loadedMapStyle
            )
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
            initLocationEngine()


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(context?.applicationContext as Activity?)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(context?.applicationContext!!)
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()

        locationEngine!!.requestLocationUpdates(request, callback, context!!.mainLooper)
        locationEngine!!.getLastLocation(callback)
    }

    private val latLngEvaluator = object : TypeEvaluator<LatLng> {
        private val latLng = LatLng()
        override fun evaluate(fraction: Float, startValue: LatLng?, endValue: LatLng?): LatLng {
            latLng.latitude =
                (startValue!!.latitude + (endValue!!.getLatitude() - startValue.latitude) * fraction)
            latLng.longitude =
                (startValue.longitude + (endValue.getLongitude() - startValue.longitude) * fraction)
            return latLng
        }

    }

    private val animatorUpdateListener = object : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
            val animatedPosition = valueAnimator!!.animatedValue as LatLng
            geoJsonSource.setGeoJson(
                Point.fromLngLat(
                    animatedPosition.longitude,
                    animatedPosition.latitude
                )
            )

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val preferences = context.getSharedPreferences("pref", 0)
    }

    override fun onStart() {
        super.onStart()
        mapboxfamily.onStart()
    }


    override fun onResume() {
        super.onResume()
        mapboxfamily.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapboxfamily.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapboxfamily.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapboxfamily.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapboxfamily.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()


        if (locationEngine != null) {
            locationEngine!!.removeLocationUpdates(callback)
        }

        mapboxfamily.onDestroy()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        toast(R.string.user_location_permission_explanation)
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            if (mapboxMap.style != null) {
                enableLocationComponent(mapboxMap.style!!)
            }
        } else {
            toast(R.string.user_location_permission_not_granted)
            activity?.finish()
        }

    }

    override fun onMapClick(point: LatLng): Boolean {
        /*     if (animator != null && animator.isStarted)
             {
                 currentPosition = animator.animatedValue as LatLng
                 animator.cancel()
             }
             animator = ObjectAnimator
                 .ofObject(latLngEvaluator, currentPosition, point)
                 .setDuration(2000)
             animator.addUpdateListener(animatorUpdateListener)
             animator.start()
             currentPosition = point
     */
        return true

    }

    @Suppress(
        "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION", "UNREACHABLE_CODE",
        "UNREACHABLE_CODE"
    )
    internal class MainActivityLocationCallbackBaru internal constructor(activity: sharerombongandua) :
        LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<sharerombongandua> =
            WeakReference(activity)
        lateinit var databaseReference: DatabaseReference
        override fun onSuccess(result: LocationEngineResult?) {
            val activity: sharerombongandua? = activityWeakReference.get()


            if (activity != null) {
                val location: Location? = result?.lastLocation
                if (location == null) {
                    return

                }
                if (activity.statusjoin == true)
                {
                    databaseReference =
                        FirebaseDatabase.getInstance().reference.child("Selecta").child("sharing")
                    databaseReference.child(activity.sessionManager.getKunci().toString())
                        .child("${activity.userID}/latidude")
                        .setValue(result.lastLocation!!.latitude)
                    databaseReference.child(activity.sessionManager.getKunci().toString())
                        .child("${activity.userID}/longitude")
                        .setValue(result.lastLocation!!.longitude)
                    databaseReference.child(activity.sessionManager.getKunci().toString())
                        .child("${activity.userID}/name")
                        .setValue(activity.sessionManager.getprofil())


                }

            }

        }


        @SuppressLint("LogNotTimber")
        override fun onFailure(exception: Exception) {
            Log.d("LocationChange", exception.localizedMessage)
            val activity: sharerombongandua? = activityWeakReference.get()
            if (activity != null) {
                activity.toast(exception.localizedMessage)

            }

        }

    }
}
