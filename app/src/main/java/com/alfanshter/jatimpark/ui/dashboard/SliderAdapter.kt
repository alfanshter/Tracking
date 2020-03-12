package com.alfanshter.jatimpark.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import com.alfanshter.jatimpark.R

class SliderAdapter(val context: Context):PagerAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private var images = arrayOf(R.drawable.banner,R.drawable.bannerdua,R.drawable.bannerbaru)



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return images.size
    }
}