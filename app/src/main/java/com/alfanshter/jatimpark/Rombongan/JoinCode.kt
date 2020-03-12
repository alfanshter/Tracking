package com.alfanshter.jatimpark.Rombongan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alfanshter.jatimpark.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_join_code.*
import org.jetbrains.anko.toast

class JoinCode : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_code)


    butonkode.setOnClickListener {
        toast(pinview.value.toString())
    }

    }


}
