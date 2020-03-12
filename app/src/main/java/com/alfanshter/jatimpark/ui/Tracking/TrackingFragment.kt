@file:Suppress("UNREACHABLE_CODE")

package com.alfanshter.jatimpark.ui.Tracking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngQuad
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.*
import com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.ImageSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationView
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*


@Suppress(
    "CAST_NEVER_SUCCEEDS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "SENSELESS_COMPARISON", "DEPRECATION"
)
class TrackingFragment : Fragment(), OnMapReadyCallback, PermissionsListener,
    MapboxMap.OnMapClickListener {

    private lateinit var trackingViewModel: TrackingViewModel


    var reference: DatabaseReference? = null

    var user: FirebaseUser? = null
    var userID: String? = null
    var auth: FirebaseAuth? = null


    private var permissionsManager: PermissionsManager = PermissionsManager(this)

    private lateinit var mapboxMap: MapboxMap
    private lateinit var startnavigasi: Button
    private lateinit var locationComponent: LocationComponent
    //variabel menggambar rute
    private var currentRoute: DirectionsRoute? = null
    private var navigationMapRoute: NavigationMapRoute? = null
    //=====================
    private var navigationView: NavigationView? = null
    var origin = Point.fromLngLat( 112.5265,-7.8196)


    private val ID_IMAGE_SOURCE = "animated_image_source"
    private val ID_IMAGE_SOURCEdua = "animated_image_source2"
    private val ID_IMAGE_LAYER = "animated_image_layer"
    private val ID_IMAGE_LAYERdua = "animated_image_layer2"

    private var locationEngine: LocationEngine? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    lateinit var butonnavigasi : Button
    private var callback = MainActivityLocationCallback(this)

    var lastlatitude: String? = null

    //==============VARIABEL RECYCLERVIEW=================================
    private val TAG = "RVDirectionsActivity"
    private val SYMBOL_ICON_ID = "SYMBOL_ICON_ID"
    private val PERSON_ICON_ID = "PERSON_ICON_ID"
    private val MARKER_SOURCE_ID = "MARKER_SOURCE_ID"
    private val PERSON_SOURCE_ID = "PERSON_SOURCE_ID"
    private val DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID = "DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID"
    private val LAYER_ID = "LAYER_ID"
    private val PERSON_LAYER_ID = "PERSON_LAYER_ID"
    private val DASHED_DIRECTIONS_LINE_LAYER_ID = "DASHED_DIRECTIONS_LINE_LAYER_ID"
    private val directionsOriginPoint = Point.fromLngLat( 112.5265,-7.8196)
    private val possibleDestinations = arrayOf(
        LatLng(-7.818716, 112.525346),
        LatLng(-7.818360, 112.525417),
        LatLng(-7.818039, 112.525205),
        LatLng(-7.818310, 112.524731),
        LatLng(-7.817992, 112.524632),
        LatLng(-7.817648, 112.524571),
        LatLng(-7.817184, 112.524829),
        LatLng(-7.816620, 112.524659),
        LatLng(-7.816306, 112.524600)
    )

    private var dashedLineDirectionsFeatureCollection: FeatureCollection? = null
    private val directionsRouteList: MutableList<DirectionsRoute> = ArrayList()
    lateinit var recyclerView: RecyclerView
    //====================================================================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        Mapbox.getInstance(context!!.applicationContext, getString(R.string.access_token))
        trackingViewModel = ViewModelProviders.of(this).get(TrackingViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val context: Context = inflater.context
        val mapView = view.findViewById(R.id.mapbox) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        recyclerView = view.find(R.id.rv_on_top_of_map)
        butonnavigasi = view.find(R.id.navigasi)
        butonnavigasi.setOnClickListener {
            var simulateRoute = true
            var options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
/*
                    .shouldSimulateRoute(simulateRoute)
*/
                .build()
            NavigationLauncher.startNavigation(view.context as Activity?, options)

        }
        butonnavigasi.isEnabled = true
        butonnavigasi.setBackgroundResource(R.color.mapboxBlue)



        return view

    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(

            Style.Builder().fromUri(Style.LIGHT) // Set up the image, source, and layer for the person icon,
// which is where all of the routes will start from
                .withImage(
                    PERSON_ICON_ID,
                    BitmapUtils.getBitmapFromDrawable(
                        resources.getDrawable(R.drawable.ic_person)
                    )!!
                )
                .withSource(
                    GeoJsonSource(
                        PERSON_SOURCE_ID,
                        Feature.fromGeometry(directionsOriginPoint)
                    )
                )
                .withLayer(
                    SymbolLayer(
                        PERSON_LAYER_ID,
                        PERSON_SOURCE_ID
                    ).withProperties(
                        iconImage(PERSON_ICON_ID),
                        iconSize(2f),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                    )
                ) // Set up the image, source, and layer for the potential destination markers
                .withImage(
                    SYMBOL_ICON_ID,
                    BitmapFactory.decodeResource(
                        this.resources, R.drawable.red_marker
                    )

                )
                .withSource(
                    GeoJsonSource(
                        MARKER_SOURCE_ID,
                        initDestinationFeatureCollection()
                    )
                )
                .withLayer(
                    SymbolLayer(
                        LAYER_ID,
                        MARKER_SOURCE_ID
                    ).withProperties(
                        iconImage(SYMBOL_ICON_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.2f)
                    )
                ) // Set up the source and layer for the direction route LineLayer
                .withSource(GeoJsonSource(DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID))
                .withLayerBelow(
                    LineLayer(
                        DASHED_DIRECTIONS_LINE_LAYER_ID,
                        DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID
                    )
                        .withProperties(
                            lineWidth(7f),
                            lineJoin(LINE_JOIN_ROUND),
                            lineColor(
                                Color.parseColor(
                                    "#2096F3"
                                )
                            )
                        ),
                    PERSON_LAYER_ID
                )
        ) {
            getRoutesToAllPoints()
            initRecyclerView()

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
            LoadGeoJson(this).execute()
            enableLocationComponent(it)
            addDestinationIconSymbolLayer(it)
            mapboxMap.addOnMapClickListener(this)


        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val preferences = context.getSharedPreferences("pref", 0)
    }

    override fun onStart() {
        super.onStart()
        mapbox.onStart()
    }


    override fun onResume() {
        super.onResume()
        mapbox.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapbox.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapbox.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapbox.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapbox.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()



        if (locationEngine != null) {
            locationEngine!!.removeLocationUpdates(callback)
        }

        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this)
        }
        mapbox.onDestroy()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    //==================Recycler View================


    /**
     * Create data fro the RecyclerView.
     *
     * @return a list of {@link SingleRecyclerViewLocation} objects for the RecyclerView.
     */
    private fun createRecyclerViewLocations(): List<SingleRecyclerViewLocation> {
        var locationList: ArrayList<SingleRecyclerViewLocation> = ArrayList()
        for (x in 0 until possibleDestinations.size) {
            val singleLocation = SingleRecyclerViewLocation()
            singleLocation.name =
                String.format(getString(R.string.rv_directions_route_card_name), x)
            singleLocation.availableTables = String.format(
                getString(R.string.rv_directions_route_available_table_info),
                Random().nextInt(possibleDestinations.size)
            )
            locationList.add(singleLocation)
        }
        return locationList
    }

    /**
     * Loop through the possible destination list of LatLng locations and get
     * the route for each destination.
     */
    private fun getRoutesToAllPoints() {
        for (singleLatLng in possibleDestinations) {
            getRouterecycler(Point.fromLngLat(singleLatLng.longitude, singleLatLng.latitude))
        }
    }


    /**
     * Create a FeatureCollection to display the possible destination markers.
     *
     * @return a {@link FeatureCollection}, which represents the possible destinations.
     */

    private fun getRouterecycler(destination: Point) {
        //ambil function lokasi sekarnag
       // origin = Point.fromLngLat( locationComponent.lastKnownLocation!!.longitude,locationComponent.lastKnownLocation!!.latitude)
        val client = MapboxDirections.builder()
            .origin(origin)
            .destination(destination)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .accessToken(getString(R.string.access_token))
            .build()
        client.enqueueCall(object : Callback<DirectionsResponse> {
            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                if (response.body() == null) {
                    Log.d(
                        TAG,
                        "No routes found, make sure you set the right user and access token."
                    )

                    return
                } else if (response.body()!!.routes().size < 1) {
                    Log.d(TAG, "No routes found")
                    return
                }
                directionsRouteList.add(response.body()!!.routes().get(0))


                //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    /**
     * Create a FeatureCollection to display the possible destination markers.
     *
     * @return a {@link FeatureCollection}, which represents the possible destinations.
     */
    //Create a FeatureCollection to display the possible destination markers.
    private fun initDestinationFeatureCollection(): FeatureCollection? {
        val featureList: MutableList<Feature> = ArrayList()
        for (latLng in possibleDestinations) {
            featureList.add(
                Feature.fromGeometry(
                    Point.fromLngLat(latLng.longitude, latLng.latitude)
                )
            )
        }
        return FeatureCollection.fromFeatures(featureList)
    }

    /**
     * Set up the RecyclerView.
     */
    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(
            getApplicationContext(),
            LinearLayoutManager.HORIZONTAL, true
        )
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = LocationRecyclerViewAdapter(
            this,
            createRecyclerViewLocations(), mapboxMap
        )
        LinearSnapHelper().attachToRecyclerView(recyclerView)
    }


    /**
     * Update the GeoJSON data for the direction route LineLayer.
     *
     * @param route The route to be drawn in the map's LineLayer that was set up above.
     */

    private fun drawNavigationPolylineRoute(route: DirectionsRoute) {
        if (mapboxMap != null) {

//            var destinationPoint = Point.fromLngLat(point.longitude, point.latitude)
//            var originPoint = Point.fromLngLat(
//                locationComponent.lastKnownLocation!!.longitude,
//                locationComponent.lastKnownLocation!!.latitude
//            )
//            mapboxMap.style?.sources
//                    getRoute(originPoint, destinationPoint)
            mapboxMap.getStyle { style ->
                val directionsRouteFeatureList: MutableList<Feature> = ArrayList()
                val lineString = LineString.fromPolyline(route.geometry().toString(), PRECISION_6)
                val lineStringCoordinates = lineString.coordinates()
                for (i in 0 until lineStringCoordinates.size) {
                    directionsRouteFeatureList.add(
                        Feature.fromGeometry(
                            LineString.fromLngLats(lineStringCoordinates)
                        )
                    )
                }

                dashedLineDirectionsFeatureCollection =
                    FeatureCollection.fromFeatures(directionsRouteFeatureList)
                val source: GeoJsonSource =
                    style.getSourceAs(DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID)!!

                if (source != null) {
                    source.setGeoJson(dashedLineDirectionsFeatureCollection)
                }
            }
        }
    }


    internal class LocationRecyclerViewAdapter internal constructor(
        activity: TrackingFragment,
        locationList: List<SingleRecyclerViewLocation>,
        mapBoxMap: MapboxMap
    ) : RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder>() {
        private val locationList: List<SingleRecyclerViewLocation>
        private val map: MapboxMap
        private val weakReference: WeakReference<TrackingFragment>

        init {
            this.locationList = locationList
            this.map = mapBoxMap
            this.weakReference = WeakReference(activity)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_directions_card, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val singleRecyclerViewLocation = locationList.get(position)
            holder.name.text = singleRecyclerViewLocation.name
            holder.numOfAvailableTables.text = singleRecyclerViewLocation.availableTables
            holder.setClickListener(object : ItemClickListener {

                override fun onClick(view: View?, position: Int) {

                    weakReference.get()
                        ?.drawNavigationPolylineRoute(

                            weakReference.get()!!.directionsRouteList[position]
                        )                }

            })
        }

        internal class MyViewHolder(view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            var name: TextView
            var numOfAvailableTables: TextView
            var singleCard: CardView
            private var clickListener: ClickListener? = null

            init {
                name = view.findViewById(R.id.location_title_tv)
                numOfAvailableTables = view.findViewById(R.id.location_num_of_beds_tv)
                singleCard = view.findViewById(R.id.single_location_cardview)
                singleCard.setOnClickListener(this)
            }

            fun setClickListener(itemClickListener: ItemClickListener) {
                this.clickListener = itemClickListener
            }

            override fun onClick(v: View?) {
                clickListener?.onClick(v, layoutPosition)
            }


        }

        override fun getItemCount(): Int {
            return locationList.size

        }
    }

    /**
     * POJO model class for a single location in the RecyclerView.
     */

    internal class SingleRecyclerViewLocation {
        lateinit var name: String
        lateinit var availableTables: String
    }

    interface ItemClickListener : ClickListener {
        override fun onClick(view: View?, position: Int)
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
    }
    //===================END RECYCLERVIEW===========================

    fun startNavigation() {
        if (currentRoute == null) {
            return
        }

        var options = NavigationViewOptions.builder()
            .directionsRoute(currentRoute)
            .shouldSimulateRoute(true)


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
        var destinationPoint = Point.fromLngLat(point.longitude, point.latitude)
        var originPoint = Point.fromLngLat(
            locationComponent.lastKnownLocation!!.longitude,
            locationComponent.lastKnownLocation!!.latitude
        )
        toast(destinationPoint.toString())
        //meload marker pada saat di click
        mapboxMap.style?.getSourceAs<GeoJsonSource>("destination-source-id")
            ?.setGeoJson(Feature.fromGeometry(destinationPoint))
        //===============================
        getRoute(originPoint, destinationPoint)
        return true
    }


    private class LoadGeoJson internal constructor(activity: TrackingFragment) :
        AsyncTask<Void?, Void?, FeatureCollection?>() {
        private val weakReference: WeakReference<TrackingFragment> = WeakReference(
            activity
        )


        override fun onPostExecute(featureCollection: FeatureCollection?) {
            super.onPostExecute(featureCollection)
            val activity: TrackingFragment? =
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
                val activity: TrackingFragment? =
                    weakReference.get()
                if (activity != null) {

                    val inputStream: InputStream? =
                        activity.context?.assets?.open("example.geojson")
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

    private fun drawLines(featureCollection: FeatureCollection) {
        mapboxMap.getStyle { style: Style ->
            if (featureCollection.features() != null) {
                if (featureCollection.features()!!.size > 0) {
                    style.addSource(GeoJsonSource("line-source", featureCollection))
                    // The layer properties for our line. This is where we make the line dotted, set the
                    // color, etc.
                    style.addLayer(
                        LineLayer("linelayer", "line-source")
                            .withProperties(
                                PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                PropertyFactory.lineOpacity(
                                    .5f
                                ),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(
                                    Color.parseColor("#3bb2d0")
                                )
                            )
                    )
                }
            }
        }
    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    internal class MainActivityLocationCallback internal constructor(activity: TrackingFragment) :
        LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<TrackingFragment> = WeakReference(activity)
        lateinit var sessionManager: SessionManager

        @SuppressLint("SetTextI18n", "InflateParams")
        override fun onSuccess(result: LocationEngineResult?) {
            val activity: TrackingFragment? = activityWeakReference.get()


            if (activity != null) {
                val location: Location? = result?.lastLocation
                if (location == null) {
                    return

                }


                val sessionManager = SessionManager(activity.context)

                sessionManager.setlongitude(result.lastLocation!!.longitude.toString())
                sessionManager.setLatidude(result.lastLocation!!.latitude.toString())

                if (result.lastLocation != null) {
                    activity.mapboxMap.locationComponent.forceLocationUpdate(result.lastLocation)
                }

                activity.lastlatitude = "${result.lastLocation!!.longitude}"
            }
        }

        @SuppressLint("LogNotTimber")
        override fun onFailure(exception: java.lang.Exception) {
            Log.d("LocationChange", exception.localizedMessage)
            val activity: TrackingFragment? = activityWeakReference.get()
            if (activity != null) {
                activity.toast(exception.localizedMessage)

            }

        }
    }

    //fungsi gambar rute
    private fun getRoute(origin: Point, destination: Point) {
        var client = MapboxDirections.builder()


        NavigationRoute.builder(context?.applicationContext)
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
                    //menggabar route di map
                    currentRoute = response.body()!!.routes().get(0)
                    if (navigationMapRoute != null) {
                        navigationMapRoute!!.removeRoute()
                    } else {
                        navigationMapRoute =
                            NavigationMapRoute(null, mapbox, mapboxMap, R.style.NavigationMapRoute)
                    }
                    navigationMapRoute!!.addRoute(currentRoute)
                }
            })


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
            //locationComponent.lastKnownLocation!!.longitude
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
/*
        locationEngine.removeLocationUpdates(request,callback,mainLooper)
*/
/*
        locationEngine.getLastLocation(callback)
*/
    }

    //penanda marker
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
            iconIgnorePlacement(true),
            iconSize(1f)
        )
        loadedMapStyle.addLayer(destinationSymbolLayer)
    }


}