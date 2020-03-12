//package com.alfanshter.jatimpark.Rombongan
//
//import android.app.ProgressDialog
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.alfanshter.jatimpark.Model.ModelBaru
//import com.alfanshter.jatimpark.R
//import com.alfanshter.jatimpark.Session.SessionManager
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import kotlinx.android.synthetic.main.activity_invite_code.*
//import org.jetbrains.anko.AnkoLogger
//import org.jetbrains.anko.info
//import org.jetbrains.anko.toast
//
//class InviteCode : AppCompatActivity(),AnkoLogger {
//
//    lateinit var user: FirebaseUser
//    lateinit var reference: DatabaseReference
//    lateinit var referencebaru: DatabaseReference
//    lateinit var progressdialog: ProgressDialog
//    lateinit var userID:String
//    private lateinit var auth : FirebaseAuth
//
//    private lateinit var sessionManager: SessionManager
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?){
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_invite_code)
//
//        reference = FirebaseDatabase.getInstance().reference.child("Selecta/Users")
//        referencebaru = FirebaseDatabase.getInstance().reference.child("Selecta")
//
//        auth = FirebaseAuth.getInstance()
//
//        val bundle :Bundle ?=intent.extras
//
//        val code = bundle!!.getString("code") // 1
//
//        val nama = bundle.getString("name") // 2
//        val date = bundle.getString("date") // 2
//
///*
//        var createUser = CreateUser(nama.toString(),email.toString(),password.toString(),code.toString(),"","","")
//*/
//
//        sessionManager = SessionManager(this)
//        btn_invite.setOnClickListener {
//            user = auth.currentUser!!
//            userID = user.uid
//            reference.child(userID).child("name").setValue(nama)
//            reference.child(userID).child("key").setValue(code)
//            reference.child(userID).child("date").setValue(date)
//            referencebaru.child("sharing").child(code!!).child(userID).setValue(ModelBaru("",0.00,sessionManager.getlongitude()!!.toDouble()))
//
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//                        toast("berhasil")
//                    }
//
//                    else
//                    {
//                        toast("gagal")
//                        info("Gagal : ${task.exception?.message}")
//                    }
//                }
//
//        }
//
//
//
///*
//        val imageUri:Uri = bundle.getParcelable("uri")!! // 2
//*/
//        kode.text = code
//
//
//
//
//
//
//
//
///*        var myIntent : Intent? = null
//        if (myIntent!=null){
//            name = myIntent.getStringExtra("name")
//            email = myIntent.getStringExtra("email")
//            password = myIntent.getStringExtra("password")
//            date = myIntent.getStringExtra("date")
//            issharing = myIntent.getStringExtra("isSharing")
//            code = myIntent.getStringExtra("code")
//            imageUri = myIntent.getParcelableExtra("uri")
//        }
//        kode.setText(code)*/
//    }
//}
