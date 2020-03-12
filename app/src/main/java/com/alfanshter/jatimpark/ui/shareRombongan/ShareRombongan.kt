package com.alfanshter.jatimpark.ui.shareRombongan

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
import com.alfanshter.jatimpark.Model.Util.FcmPush
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.alfanshter.jatimpark.ui.dashboard.DashboardFragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoQuery
import com.goodiebag.pinview.Pinview
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


class ShareRombongan : Fragment(),  AnkoLogger {

    private lateinit var viewModel: ShareRombonganViewModel
    lateinit var user: FirebaseUser
    lateinit var userID: String
    private lateinit var butonkode: Button
    lateinit var sessionManager: SessionManager
    private lateinit var referencejoin: DatabaseReference
    private lateinit var pin : Pinview
    var nama = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        Mapbox.getInstance(context!!.applicationContext, getString(R.string.access_token))
        viewModel = ViewModelProviders.of(this).get(ShareRombonganViewModel::class.java)
        val root = inflater.inflate(R.layout.share_rombongan_fragment, container, false)
        FcmPush.instance.sendMessage("Cr8miBro1QP018K80U6qXUHkUNC2","Howlstagram","ayolah please")

        sessionManager = SessionManager(context)
        butonkode = root.find(R.id.sharingbuton)
        pin = root.find(R.id.pinsharing)
        butonkode.setOnClickListener {

            FcmPush.instance.sendMessage("Cr8miBro1QP018K80U6qXUHkUNC2","Howlstagram","ayolah please")
            referencejoin = FirebaseDatabase.getInstance().reference.child("Selecta").child("sharing")
            var pinjoin = pin.value.toString()
            referencejoin.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        info("informasi : ${it.key}")
                        if (it.key.toString().equals(pinjoin)) {
                            sessionManager.setKunci(pin.value.toString())
                            sessionManager.setIDStatusUser("1")
                            val fr = fragmentManager?.beginTransaction()
                            fr?.replace(R.id.nav_host_fragment,sharerombongandua())
                            fr?.commit()
                        }
                    }
                }
            })

        }

        if (sessionManager.getIDStatusUser().equals("1"))
        {
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.nav_host_fragment,sharerombongandua())
            fr?.commit()
        }


        return root
    }








}
