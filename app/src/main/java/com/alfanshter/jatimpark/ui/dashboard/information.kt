package com.alfanshter.jatimpark.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alfanshter.jatimpark.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_information.*

class information : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val bundle: Bundle? = intent.extras
        val image = bundle!!.getString("Firebase_Image")
        val title = bundle.getString("Firebase_title")
        var isi = bundle.getString("Firebase_isi")
        textinfo.setText(title)
        textisi.setText(isi)
        Picasso.get().load(image).into(gambarinfo)

    }
}
