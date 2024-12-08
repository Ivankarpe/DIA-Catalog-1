package com.Practice.DIACataloge

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Practice.DIACataloge.databinding.ViewSiteItemBinding

class SiteAdapter(private val sites: List<String>) : RecyclerView.Adapter<SiteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewSiteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewSiteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siteUrl = sites[position]

        when {
            siteUrl.startsWith("https://comfy.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.comfy_icon)
                    .into(holder.binding.siteIcon)
            }
            siteUrl.startsWith("https://rozetka.com.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.rozetka_icon)
                    .into(holder.binding.siteIcon)
            }
            siteUrl.startsWith("https://hard.rozetka.com.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.rozetka_icon)
                    .into(holder.binding.siteIcon)
            }
            siteUrl.startsWith("https://www.foxtrot.com.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.foxtrot_icon)
                    .into(holder.binding.siteIcon)

            }
            siteUrl.startsWith("https://allo.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.allo_icon)
                    .into(holder.binding.siteIcon)

            }
            siteUrl.startsWith("https://www.ctrs.com.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.ctrs_icon)
                    .into(holder.binding.siteIcon)
            }
            siteUrl.startsWith("https://telemart.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.telemart_icon)
                    .into(holder.binding.siteIcon)
            }
            siteUrl.startsWith("https://brain.com.ua/") -> {
                holder.binding.siteUrl.visibility = View.GONE
                holder.binding.siteIcon.visibility = View.VISIBLE
                holder.binding.goToSiteBtn.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load(R.drawable.brain_icon)
                    .into(holder.binding.siteIcon)
            }
            else -> {
                holder.binding.siteUrl.text = siteUrl
                holder.binding.siteUrl.visibility = View.VISIBLE
                holder.binding.siteIcon.visibility = View.GONE
                holder.binding.goToSiteBtn.visibility = View.GONE

                holder.binding.root.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(siteUrl)
                    }
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
            }
        }

        holder.binding.goToSiteBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(siteUrl)
            }
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = sites.size
}
