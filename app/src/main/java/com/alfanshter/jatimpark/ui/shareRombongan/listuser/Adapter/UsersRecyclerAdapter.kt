package com.alfanshter.jatimpark.ui.shareRombongan.listuser.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.ui.shareRombongan.listuser.Model.Users
import com.alfanshter.jatimpark.ui.shareRombongan.listuser.SendActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_list_item.view.*


class UsersRecyclerAdapter(
    context: Context,
    usersList: List<Users>
) :
    RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder?>() {
    private val usersList: List<Users>
    private val context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user_name: String = usersList[position].nama!!
        val dataItem = usersList[position]
        holder.user_name_view.text = user_name
        val user_image_view = holder.user_image_view
        Glide.with(context).load(usersList[position].image).into(user_image_view)

        val user_id = usersList[position].userId
        holder.setData(dataItem,position)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show()
            val sendIntent = Intent(context, SendActivity::class.java)
            sendIntent.putExtra("user_id", user_id)
            sendIntent.putExtra("user_name", user_name)
            context.startActivity(sendIntent)
        }
//        val user_id: String = usersList[position].userId

        /* holder.mView.setOnClickListener {
            val sendIntent = Intent(context, SendActivity::class.java)
            sendIntent.putExtra("user_id", user_id)
            sendIntent.putExtra("user_name", user_name)
            context.startActivity(sendIntent)
        }*/
    }
    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var user_image_view: CircleImageView
         var user_name_view: TextView
        var currentData: Users? = null
        var currentPosisi : Int = 0

         init {
//                itemView.setOnClickListener {
//                    Toast.makeText(context, currentData!!.nama + "click", Toast.LENGTH_SHORT).show()
//                }
            user_image_view = itemView.findViewById<View>(R.id.user_list_image) as CircleImageView
            user_name_view = itemView.findViewById<View>(R.id.user_list_name) as TextView
        }
        fun setData (itemdata : Users? , position: Int){
                itemView.user_list_name.text =itemdata!!.nama
                this.currentData = itemdata
                this.currentPosisi = position
        }
    }


    init {
        this.usersList = usersList
        this.context = context
    }
}
