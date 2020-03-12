package com.alfanshter.jatimpark.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alfanshter.jatimpark.BannerFragment
import com.alfanshter.jatimpark.Model.BannerPromo

class BannerAdapter(fragmentManager: FragmentManager,
                    private val banners: List<BannerPromo>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(pos: Int): Fragment {
        return BannerFragment.newInstance(banners[pos].image)
    }

    override fun getCount(): Int = banners.size

}