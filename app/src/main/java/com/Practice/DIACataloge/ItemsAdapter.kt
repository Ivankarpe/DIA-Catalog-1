package com.Practice.DIACataloge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private var items: List<ItemsModel> = emptyList()
    private var onItemClickListener: ((ItemsModel) -> Unit)? = null
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val rating: TextView = view.findViewById(R.id.itemRating)
        val pic: ImageView = view.findViewById(R.id.pic)

        fun bind(item: ItemsModel) {
            title.text = item.title
            price.text = "$${item.price}"
            rating.text=(Math.round(item.rating*10f)/10f).toString()
            Glide.with(itemView.context).load(item.picUrl.firstOrNull()).into(pic)
            itemView.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<ItemsModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (ItemsModel) -> Unit) {
        onItemClickListener = listener
    }
}