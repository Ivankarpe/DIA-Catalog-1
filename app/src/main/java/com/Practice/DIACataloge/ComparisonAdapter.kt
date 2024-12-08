package com.Practice.DIACataloge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ComparisonAdapter(private val items: List<ItemsModel>) :
    RecyclerView.Adapter<ComparisonAdapter.ComparisonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comparison, parent, false)
        return ComparisonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComparisonViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ComparisonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val characteristicsContainer: LinearLayout = itemView.findViewById(R.id.characteristicsContainer)

        fun bind(item: ItemsModel) {

            if (item.picUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.picUrl[0])
                    .into(imageView)
            }


            titleTextView.text = item.title
            priceTextView.text = "$${item.price}"


            characteristicsContainer.removeAllViews()


            for ((key, value) in item.characteristics) {

                val characteristicView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.item_characteristic, characteristicsContainer, false)


                val keyTextView: TextView = characteristicView.findViewById(R.id.keyTextView)
                val valueTextView: TextView = characteristicView.findViewById(R.id.valueTextView)

                keyTextView.text = key
                valueTextView.text = value


                characteristicsContainer.addView(characteristicView)
            }
        }
    }
}
