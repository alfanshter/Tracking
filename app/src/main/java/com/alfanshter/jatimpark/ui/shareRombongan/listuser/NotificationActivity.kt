package com.alfanshter.jatimpark.ui.shareRombongan.listuser

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.jatimpark.R
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val dataMessage = intent.getStringExtra("message")
        val dataFrom = intent.getStringExtra("from_user_id")


        notif_text.text=(" FROM : $dataFrom | MESSAGE : $dataMessage")


    }
}
