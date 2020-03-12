package com.alfanshter.jatimpark.item

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alfanshter.jatimpark.Model.BannerPromo
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.adapter.BannerAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_carousel_banner.view.*


interface BannerListener {
    fun onSeeAllPromoClick()
    fun onBannerClick(promo: BannerPromo)
}

private val myContext: FragmentActivity? = null

class BannerCarouselItem(private val banners: List<BannerPromo>,
                         private val supportFragmentManager: FragmentManager,
                         private val listener: BannerListener) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val viewPagerAdapter = BannerAdapter(supportFragmentManager, banners)
        viewHolder.itemView.viewPagerBanner.adapter = viewPagerAdapter
        viewHolder.itemView.indicator.setViewPager(viewHolder.itemView.viewPagerBanner)

        viewHolder.itemView.btnSemuaPromo.setOnClickListener {
            listener.onSeeAllPromoClick()
        }

    }

    override fun getLayout(): Int = R.layout.item_carousel_banner

}