package com.alfanshter.jatimpark.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.R

class LokasiAdapter(private val place:IntArray, private val name: Array<String>, private val mContext: Context)
    :RecyclerView.Adapter<LokasiHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LokasiHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_lokasi,parent,false)
        return LokasiHolder(v,mContext)
    }

    override fun getItemCount(): Int {
        return place.size
    }

    override fun onBindViewHolder(holder: LokasiHolder, position: Int) {
        holder.index(place[position],name[position])
    }
}