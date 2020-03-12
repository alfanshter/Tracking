package com.alfanshter.jatimpark.auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.alfanshter.jatimpark.Tracking_Rombongan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.karan.churi.PermissionManager.PermissionManager
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.wifiManager
import java.util.*



class Login : AppCompatActivity() {
    lateinit var reference: DatabaseReference
    lateinit var progressdialog: ProgressDialog
    lateinit var userID: String
    lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth

    lateinit var manager: PermissionManager
    private val mProgressBar: ProgressBar? = null
var statuslogin = false
    private val mFirestore: FirebaseFirestore? = null
    lateinit var db : DocumentReference


    lateinit var ip: String
    private lateinit var sessionManager: SessionManager

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {

        manager = object : PermissionManager() {}
        manager.checkAndRequestPermissions(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val manager: WifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager
        ip = android.text.format.Formatter.formatIpAddress(manager.connectionInfo.ipAddress)
        toast(ip)
        val info = wifiManager.connectionInfo
        val alfan = info.ssid
        toast(alfan)
        if (info.equals("AndroidWifi")) {
            toast("uwaw")
        }

        sessionManager = SessionManager(this)

        progressdialog = ProgressDialog(this)

        reference = FirebaseDatabase.getInstance().reference.child("Selecta/Users")

        login.setOnClickListener {
            login()
        }

        if (sessionManager.getLogin()!!) {
            startActivity<Tracking_Rombongan>()
            finish()
        }

        signup.setOnClickListener {
            startActivity<register>()
        }
    }


    fun login() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sedang Login .....")
        progressDialog.show()
        auth = FirebaseAuth.getInstance()

        var userss = users.text.toString()
        var password = pass.text.toString()


        if (!TextUtils.isEmpty(userss) && !TextUtils.isEmpty(password)) {

            auth.signInWithEmailAndPassword(userss, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val token_id = FirebaseInstanceId.getInstance().token
                        val tokenMap: MutableMap<String, Any> =
                            HashMap()
                        tokenMap["token_id"] = token_id!!

                        user = auth.currentUser!!
                        userID = user.uid

                        db = FirebaseFirestore.getInstance().collection("Users").document(userID)
                        db.update(tokenMap).addOnCompleteListener {
                    task ->
                    if (task.isSuccessful)
                    {
                        reference.child(userID).child("email").setValue(userss)
                        reference.child(userID).child("password").setValue(password)
                        reference.child(userID).child("iduser").setValue(userID)

                        sendToMain()
                        progressDialog.dismiss()
                        toast("berhasil")
                        sessionManager.setLogin(true)
                    }
                    else
                    {
                        toast("gagal")
                    }
                }
/*
*/
                    toast("berhasil")
                        progressDialog.dismiss()
                    }
                    else
                    {
                        toast("gagal login")

                    }
                }

        } else {
            toast("masukkan username dan password")

        }

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        manager.checkResult(requestCode, permissions, grantResults)
        val denied_permission: ArrayList<String> = manager.status.get(0).denied
        if (denied_permission.isEmpty()) {
            toast("permission aktif")
        }
    }

    private fun sendToMain() {
        startActivity<Tracking_Rombongan>()
        finish()
    }

    fun realtimedatabase()
    {
        pass.text.toString().trim()
        users.toString().trim()
        var userss = users.text.toString()
        var password = pass.text.toString()
            /*.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressdialog.dismiss()
                    toast("user telah terregistrasi")
                    //menambah sesion
                    sessionManager.setLogin(true)
                    sessionManager.setIduser(userss)
                } else {
                    progressdialog.dismiss()
                    toast("user gagal di registrasi")
                }
            }*/
    }
}

