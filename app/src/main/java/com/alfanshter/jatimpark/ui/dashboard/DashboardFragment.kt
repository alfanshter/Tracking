package com.alfanshter.jatimpark.ui.dashboard

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.Model.BannerPromo
import com.alfanshter.jatimpark.Model.UsersInfo
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.item.BannerCarouselItem
import com.alfanshter.jatimpark.item.BannerListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


@Suppress("UNREACHABLE_CODE")
class DashboardFragment : Fragment(), BannerListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var banner: RecyclerView
    private var groupAdapter = GroupAdapter<ViewHolder>()
    lateinit var databaseReference: DatabaseReference
    lateinit var refinfo : DatabaseReference
    lateinit var show_progress: ProgressBar
    var bannersatu = ""
    var promos: List<BannerPromo> = listOf()
    var status = ""
    @SuppressLint("StringFormatInvalid")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        banner = root.find(R.id.rvMain)
        databaseReference = FirebaseDatabase.getInstance().reference.child("Selecta").child("Home")
        databaseReference.addValueEventListener(object : ValueEventListener, BannerListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                bannersatu = p0.child("banner/postimage").value.toString()
                promos = listOf(
                    BannerPromo(
                        name = "Puncak badai uang",
                        image = bannersatu
                    ),
                    BannerPromo(
                        name = "hati hati ada guncangan badai uang",
                        image = "https://s4.bukalapak.com/uploads/promo_partnerinfo_bloggy/5042/Bloggy_1.jpg"
                    ),
                    BannerPromo(
                        name = "Puncak badai uang",
                        image = "https://s2.bukalapak.com/uploads/promo_partnerinfo_bloggy/2842/Bloggy_1_puncak.jpg"
                    ),
                    BannerPromo(
                        name = "hati hati ada guncangan badai uang",
                        image = "https://s4.bukalapak.com/uploads/promo_partnerinfo_bloggy/5042/Bloggy_1.jpg"
                    ),
                    BannerPromo(
                        name = "Puncak badai uang",
                        image = "https://s2.bukalapak.com/uploads/promo_partnerinfo_bloggy/2842/Bloggy_1_puncak.jpg"
                    ),
                    BannerPromo(
                        name = "hati hati ada guncangan badai uang",
                        image = "https://s4.bukalapak.com/uploads/promo_partnerinfo_bloggy/5042/Bloggy_1.jpg"
                    )
                )

                val bannerCarouselItem = BannerCarouselItem(promos, fragmentManager!!, this)
                groupAdapter.add(bannerCarouselItem)


            }

            override fun onSeeAllPromoClick() {
                toast("see all promo")
            }

            override fun onBannerClick(promo: BannerPromo) {
            }
        })

        var alfan =
            "https://firebasestorage.googleapis.com/v0/b/database-alfan.appspot.com/o/images%2F1581859318952.jpg?alt=media&token=7243a48a-c3ba-4933-81f0-02517297c342"

        banner.apply {
            layoutManager = LinearLayoutManager(context.applicationContext)
            adapter = groupAdapter
        }



        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)



        recyclerView = root.find(R.id.recyclerinfo)

        /*
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = LayoutManager
*/
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("Selecta").child("info")

        val option = FirebaseRecyclerOptions.Builder<UsersInfo>().setQuery(refinfo,UsersInfo::class.java)
            .build()

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<UsersInfo,MyViewHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(context?.applicationContext).inflate(R.layout.list_item,parent,false)
                return MyViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UsersInfo) {
                val refid = getRef(position).key.toString()
                refinfo.child(refid).addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        holder.mtitle.setText(model.nama)
                        Picasso.get().load(model.gambar).fit().centerCrop().into(holder.mimage)
                        holder.itemView.setOnClickListener {
                            startActivity<information>("Firebase_Image" to model.gambar, "Firebase_title" to model.nama, "Firebase_isi" to model.deskripsi)


                        }
                    }

                })
            }
        }

        recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

/*
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = LayoutManager
*/


/*
        val adapter = HobbiesAdapter(context!!.applicationContext, Supplier.hobbies)
        recyclerView.adapter = adapter
*/

        return root

    }


    override fun onSeeAllPromoClick() {
        toast("see all promo")
    }

    override fun onBannerClick(promo: BannerPromo) {
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mtitle : TextView = itemView.findViewById(R.id.txvTitle)
        var mimage : ImageView = itemView.findViewById(R.id.gambar)
    }

}