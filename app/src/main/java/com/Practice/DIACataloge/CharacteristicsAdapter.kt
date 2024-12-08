package com.Practice.DIACataloge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CharacteristicsAdapter(private val characteristics: Map<String, String>) :
    RecyclerView.Adapter<CharacteristicsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyTextView: TextView = itemView.findViewById(R.id.keyTextView)
        val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_characteristic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = characteristics.keys.elementAt(position)
        val value = characteristics[key]
        holder.keyTextView.text = key
        holder.valueTextView.text = value
    }

    override fun getItemCount(): Int = characteristics.size
}