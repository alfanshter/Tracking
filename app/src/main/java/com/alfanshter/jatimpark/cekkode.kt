package com.alfanshter.jatimpark

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.jatimpark.Model.ModelBaru
import com.alfanshter.jatimpark.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cekkode.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class cekkode : AppCompatActivity(), AnkoLogger {
    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var referencebaru: DatabaseReference
    lateinit var progressdialog: ProgressDialog
    lateinit var userID: String
    lateinit var nilaikode: String
    lateinit var referencesharing: DatabaseReference

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cekkode)
        referencebaru = FirebaseDatabase.getInstance().reference.child("Selecta")
        referencesharing =
            FirebaseDatabase.getInstance().reference.child("Selecta").child("sharing")

        auth = FirebaseAuth.getInstance()

        sessionManager = SessionManager(this)

        user = auth.currentUser!!
        userID = user.uid

        tombolver.setOnClickListener {
            referencesharing.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
//                reference.child("Selecta/Users").child(userID)
                    p0.children.forEach {
                        info("informasi : ${it.key}")
                        if (it.key.toString().equals(812345)) {

                        }

                    }
                }
            })
        }



    }
}
