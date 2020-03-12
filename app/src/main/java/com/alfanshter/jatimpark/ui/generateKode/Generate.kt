@file:Suppress("UNREACHABLE_CODE")

package com.alfanshter.jatimpark.ui.generateKode

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria
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
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.style.layers.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.ImageSource
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.navigasi
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*


@Suppress("CAST_NEVER_SUCCEEDS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "SENSELESS_COMPARISON", "DEPRECATION"
)
class Generate : Fragment() {

    private lateinit var trackingViewModel: GenerateViewModel


    var reference: DatabaseReference? = null

    var user: FirebaseUser? = null
    var userID: String? = null
    var auth: FirebaseAuth? = null



    private lateinit var mapboxMap: MapboxMap
    private lateinit var startnavigasi: Button
    private lateinit var locationComponent: LocationComponent
    private lateinit var lokasi:TextView
    private lateinit var markerViewManager: MarkerViewManager
    private lateinit var markerView: MarkerView
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
    private lateinit var sessionManager: SessionManager


    var lastlatitude : String? = null
    var lat = 0.00
    var long = 0.00
    private lateinit var referencebaru: DatabaseReference
     var paint: Paint? = null
    var i = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.activity_cekkode, container, false)


        trackingViewModel = ViewModelProviders.of(this).get(GenerateViewModel::class.java)
        return view

    }







}