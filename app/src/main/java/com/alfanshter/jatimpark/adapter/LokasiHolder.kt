package com.alfanshter.jatimpark.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.R
import com.bumptech.glide.Glide


class LokasiHolder (itemView : View, private val context: Context) : RecyclerView.ViewHolder(itemView){
    private val iview : ImageView = itemView.findViewById<View>(R.id.gambarlokasi) as ImageView
    private val tview : TextView = itemView.findViewById<View>(R.id.namalokasi) as TextView

    fun index(item:Int, s: String){
        Glide.with(context).load(item).into(iview)
        tview.text = s
    }
}

