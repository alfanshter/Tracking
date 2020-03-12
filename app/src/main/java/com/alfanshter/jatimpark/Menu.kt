package com.alfanshter.jatimpark

import android.os.Bundle
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.jatimpark.Rombongan.NamaProfil
import com.alfanshter.jatimpark.ui.Tracking.TrackingFragment.SingleRecyclerViewLocation
import com.karan.churi.PermissionManager.PermissionManager
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class Menu : AppCompatActivity() {
    var flipper : ViewFlipper? = null

    lateinit var manager : PermissionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        manager = object : PermissionManager() {}
        manager.checkAndRequestPermissions(this)

        val images =
            intArrayOf(R.drawable.banner, R.drawable.bannerbaru, R.drawable.bannerdua )
        flipper = findViewById(R.id.slider_menu)
        for (i in images.indices) {
            fliverImages(images[i])
        }
        for (image in images) fliverImages(image)

        tracking.setOnClickListener {
            startActivity<Tracking>()
        }

        rombongan.setOnClickListener {
            startActivity<NamaProfil>()
        }
        panggilandarurat.setOnClickListener {
            startActivity<cekkode>()

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
         manager.checkResult(requestCode,permissions,grantResults)
        val denied_permission : ArrayList<String> = manager.status.get(0).denied
        if (denied_permission.isEmpty())
        {
            toast("permission aktif")
        }
    }
    fun fliverImages(images: Int) {
        val imageView = ImageView(this)
        imageView.setBackgroundResource(images)
        flipper?.addView(imageView)
        flipper?.flipInterval = 2000
        flipper?.isAutoStart = true
        flipper?.setInAnimation(this, android.R.anim.slide_in_left)
        flipper?.setOutAnimation(this, android.R.anim.slide_out_right)
    }



}
