package com.Practice.DIACataloge

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Practice.DIACataloge.databinding.ViewholderPicBinding

class PicAdapter(val items: MutableList<String>, private val onImageSelected: (String) -> Unit) :
    RecyclerView.Adapter<PicAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class Viewholder(val binding: ViewholderPicBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewholderPicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        holder.binding.pic.loadImage(item)

        holder.binding.root.setOnClickListener {

            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            onImageSelected(item)
        }



        if (selectedPosition==position) {
            holder.binding.picLayout.setBackgroundResource(R.drawable.green_bg_selected)
        } else {
            holder.binding.picLayout.setBackgroundResource(R.drawable.grey_bg)
        }
    }

    override fun getItemCount(): Int = items.size


    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}