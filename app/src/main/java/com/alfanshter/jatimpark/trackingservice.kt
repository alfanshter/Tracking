package com.alfanshter.jatimpark

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.alfanshter.jatimpark.Model.Contants
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.jar.Manifest

class trackingservice : Service() {

    private var mAuth: FirebaseAuth? = null
    protected var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            //Unregister the BroadcastReceiver when the notification is tapped//

            unregisterReceiver(this)

            //Stop the Service//

            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        requestLocationUpdates()
    }

    private fun requestLocationUpdates() {

        val ref = FirebaseDatabase.getInstance().getReference(Contants.tb_Uaser)

        val query = ref.orderByChild("uid").equalTo(mAuth?.currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (issue in p0.children) {
                    val key = issue.key

                    val request = LocationRequest()
                    request.interval = 1000
                    request.fastestInterval = 1000
                    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val client =
                        LocationServices.getFusedLocationProviderClient(this@trackingservice)
                    val permission = ContextCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    if (permission == PackageManager.PERMISSION_GRANTED) {

                        client.requestLocationUpdates(request, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                val location = locationResult?.lastLocation
                                if (location != null) {
                                    key?.let {
                                        ref.child(it).child("latitude")
                                            .setValue(location.latitude.toString())
                                    }
                                    key?.let {
                                        ref.child(it).child("longitude")
                                            .setValue(location.longitude.toString())
                                    }


                                }
                            }
                        }, null)
                    }

                }

            }
        })

    }


}