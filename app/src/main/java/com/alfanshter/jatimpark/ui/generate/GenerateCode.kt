@file:Suppress("DEPRECATION")

package com.alfanshter.jatimpark.ui.generate

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.alfanshter.jatimpark.Model.ModelBaru

import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.util.*

class GenerateCode : Fragment(),AnkoLogger {

    lateinit var user: FirebaseUser
    lateinit var reference: DatabaseReference
    lateinit var referencebaru: DatabaseReference
    lateinit var progressdialog: ProgressDialog
    lateinit var userID:String
    private lateinit var auth : FirebaseAuth

    private lateinit var sessionManager: SessionManager

    private lateinit var generate: Button
    private lateinit var textCode: TextView

    val myDate = Date()
    val format1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
    val date :String = format1.format(myDate)
    val random = Random()
    val n:Int = 100000 + random.nextInt(900000)
    private val code = n.toString()




    private lateinit var viewModel: GenerateCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(GenerateCodeViewModel::class.java)

        val root = inflater.inflate(R.layout.generate_code_fragment, container, false)


        reference = FirebaseDatabase.getInstance().reference.child("Selecta/Users")
        referencebaru = FirebaseDatabase.getInstance().reference.child("Selecta")

        auth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(context!!.applicationContext)

        generate = root.find(R.id.btn_invitefragment)
        textCode = root.find(R.id.kodefragment)

        generate.setOnClickListener {
            textCode.text= code
            user = auth.currentUser!!
            userID = user.uid
            reference.child(userID).child("key").setValue(code)
            reference.child(userID).child("date").setValue(date)
            referencebaru.child("sharing").child(code).child(userID).setValue(ModelBaru("","-7.8178233","112.525445"))

                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        toast("berhasil")
                    }

                    else
                    {
                        toast("gagal")
                        info("Gagal : ${task.exception?.message}")
                    }

                }


        }

        return root

    }


}
