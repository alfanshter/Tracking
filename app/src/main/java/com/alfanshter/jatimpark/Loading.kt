package com.alfanshter.jatimpark

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.alfanshter.jatimpark.Rombongan.JoinCode
import com.alfanshter.jatimpark.auth.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_loading.*
import org.jetbrains.anko.startActivity


class Loading : AppCompatActivity() {
    private var Value = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        //Handler
        //Handler
        val handler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                persentase.text = "$Value%"
                if (Value == loading.max) {
                    Toast.makeText(applicationContext, "Loading Selesai", Toast.LENGTH_SHORT)
                        .show()
                    startActivity<Tracking_Rombongan>()
                    finish()
                }
                Value++
            }
        }

        //thread
        //thread
        val thread = Thread(Runnable {
            try {
                for (w in 0..loading.getMax()) {
                    loading.setProgress(w)
                    handler.sendMessage(handler.obtainMessage())
                    Thread.sleep(20)
                }
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        })
        thread.start()
    }
}
