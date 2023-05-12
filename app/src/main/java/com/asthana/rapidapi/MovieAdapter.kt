package com.asthana.rapidapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val context: Context,private val ottList: MutableList<OTTDetails>) :
    RecyclerView.Adapter<MovieAdapter.OTTViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OTTViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemcard, parent, false)
        return OTTViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OTTViewHolder, position: Int) {
        val currentOTT = ottList[position]
        holder.setDataa(currentOTT)
    }

    override fun getItemCount(): Int {
        return ottList.size
    }

    fun setData(data: List<OTTDetails>?) {
        ottList.clear()
        if (data != null) {
            ottList.addAll(data)
        }
        notifyDataSetChanged()
    }


    inner class OTTViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otttitle: TextView = itemView.findViewById(R.id.movie_title)
        val ottrelease: TextView = itemView.findViewById(R.id.movie_released)
        val ottrating: TextView = itemView.findViewById(R.id.movie_rating)
        val imageurls: ImageView = itemView.findViewById(R.id.movieimg)
        // Declare other views for other data fields

        fun setDataa(ottDetails: OTTDetails) {
            otttitle.text = ottDetails.title
            ottrelease.text = ottDetails.released.toString()
            ottrating.text = ottDetails.imdbrating.toString()
            Glide.with(context).load(ottDetails.imageurl).into(imageurls)
            // Set other data fields here
        }
    }
}