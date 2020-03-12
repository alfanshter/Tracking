package com.alfanshter.jatimpark

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.alfanshter.jatimpark.Session.SessionManager
import com.alfanshter.jatimpark.auth.Login
import com.alfanshter.jatimpark.ui.Tracking.TrackingFragment
import com.alfanshter.jatimpark.ui.dashboard.DashboardFragment
import com.alfanshter.jatimpark.ui.generate.GenerateCode
import com.alfanshter.jatimpark.ui.shareRombongan.ShareRombongan
import com.alfanshter.jatimpark.ui.shareRombongan.listuser.UsersFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_tracking__rombongan.*
import kotlinx.android.synthetic.main.drawer_header.*
import org.jetbrains.anko.startActivity
import java.util.*

class Tracking_Rombongan : AppCompatActivity() {
    val PERMISSIONS_REQUEST = 1

    private var mAuth: FirebaseAuth? = null

    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var referencebaru: DatabaseReference
    lateinit var userID: String
    private lateinit var mainPresenter: MainPresenter
    private lateinit var sessionManager: SessionManager
    private var mFirestore: FirebaseFirestore? = null
    private var mUserId: String? = null

    var namaprofil = ""
    var emailprofil = ""
    var gambarprofil = ""
        var nilai = false
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                mainPresenter.changeFragment(supportFragmentManager,
                    TrackingFragment(),R.id.nav_host_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                mainPresenter.changeFragment(supportFragmentManager,
                    TrackingFragment(),R.id.nav_host_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sharingmaps -> {
                mainPresenter.changeFragment(supportFragmentManager,
                    ShareRombongan(),R.id.nav_host_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications ->{
                mainPresenter.changeFragment(supportFragmentManager,
                    UsersFragment(),R.id.nav_host_fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking__rombongan)
    /*    registerPushToken()
        if (nilai ==true)
        {
            FcmPush.instance.sendMessage("Cr8miBro1QP018K80U6qXUHkUNC2","hi","emboh kah ")

        }
*/



        //upload info
        /*referencebaru.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
            namaprofil = p0.child(auth.uid.toString()+"/nama").value.toString()
                gambarprofil = p0.child(auth.uid.toString()+"/gambar").value.toString()
                emailprofil = p0.child(auth.uid.toString()+"/email").value.toString()
                nama_drawer.text = namaprofil
                Picasso.get().load(gambarprofil)
                    .into(gambardrawer)
                email_drawer.text = emailprofil
            }
        })*/
        //========

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
        container,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ){
            override fun onDrawerClosed(view: View){
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        container.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.navigation_generate -> {
                    mainPresenter.changeFragment(supportFragmentManager,
                        GenerateCode(),R.id.nav_host_fragment)
                }
                R.id.nav_logout -> {

                    val tokenMapRemove: MutableMap<String, Any> =
                        HashMap()
                    tokenMapRemove["token_id"] = FieldValue.delete()

                    mFirestore!!.collection("Users").document(mUserId!!).update(tokenMapRemove)
                        .addOnSuccessListener {
                            mAuth!!.signOut()
                            sessionManager.setLogin(false)
                            sessionManager.setNama(false)
                            startActivity<Login>()
                            finish()

                        }

                }
            }
            container.closeDrawer(GravityCompat.START)
            true

        }
        mainPresenter = MainPresenter()
        mainPresenter.changeFragment(supportFragmentManager,
            TrackingFragment(),R.id.nav_host_fragment)
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


 /*   fun registerPushToken()
    {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                task ->
            val token = task.result?.token
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val map = mutableMapOf<String,Any>()
            map["pushToken"] = token!!

            FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
        }
        lateinit var databaseReference: DatabaseReference

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener{
            task ->
            val token = task.result?.token
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val map = mutableMapOf<String,Any>()
            map["pushToken"] = token!!
            databaseReference = FirebaseDatabase.getInstance().reference.child("Selecta").child("pushtokens").child(uid.toString())
            databaseReference.setValue(map)
        }
        nilai = true
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        fragment?.onActivityResult(requestCode,resultCode,data)
    }

    fun checkPermission(){

        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)

//If the location permission has been granted, then start the TrackerService//

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startService(Intent(this, trackingservice::class.java))
        } else {

            //If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),PERMISSIONS_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        //If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.size == 1
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//

            startService(Intent(this, trackingservice::class.java))

        } else {

            //If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show()
        }
    }


    }
