package com.Practice.DIACataloge

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Practice.DIACataloge.databinding.ViewRecommendedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListItemAdapt(val items:MutableList<ItemsModel>):RecyclerView.Adapter<ListItemAdapt.Viewholder>() {
    class Viewholder(val binding: ViewRecommendedBinding)  :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemAdapt.Viewholder {
       val binding=ViewRecommendedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            titleTxt.text = item.title
            priceTxt.text = "$${item.price}"
            ratingTxt.text=(Math.round(item.rating*10f)/10f).toString()

            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .into(pic)
            favBtn.setOnClickListener {
                TogleFavorites(item.iditeam, favBtn)
            }
            InFavorities(item.iditeam, favBtn)
            root.setOnClickListener{
val intent= Intent(holder.itemView.context,DetailAktivity::class.java).apply{
    putExtra("object",item)
    putExtra("id",item.iditeam)
}
            ContextCompat.startActivity(holder.itemView.context,intent,null)
                AddToSeen(item.iditeam)
            }
        }
    }
    fun AddToSeen(id: String) {
        if(FirebaseAuth.getInstance().currentUser == null){
            return
        }
        val db = FirebaseFirestore.getInstance()

        val doc = db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid.toString())
        doc.get().addOnSuccessListener { documentSnapshot ->
            val ids = documentSnapshot.toObject(UserDataClass::class.java)!!.LastView
            ids?.remove(id)
            ids?.add(id)
            doc?.update("LastView", ids)
        }
    }
    private fun TogleFavorites(id: String, favBtn:ImageView) {
        if(FirebaseAuth.getInstance().currentUser == null){
            return
        }
        val doc = FirebaseFirestore.getInstance().collection("users").document(
            FirebaseAuth.getInstance().currentUser?.uid.toString()
        )
        doc.get().addOnSuccessListener { documentSnapshot ->
            val favorites = documentSnapshot.toObject(
                UserDataClass::class.java
            )!!.Favorites
            if (favorites?.contains(id) == true) {
                favorites?.remove(id)
            } else {
                favorites?.add(id)
            }
            doc?.update("Favorites", favorites)
            InFavorities(id,favBtn)
        }
    }
    private fun InFavorities(id: String, imageView: ImageView) {
        if(FirebaseAuth.getInstance().currentUser == null){
            return
        }
        val doc = FirebaseFirestore.getInstance().collection("users").document(
            FirebaseAuth.getInstance().currentUser?.uid.toString()
        )

        doc.get().addOnSuccessListener { documentSnapshot ->
            val favorities = documentSnapshot.toObject(
                UserDataClass::class.java
            )!!.Favorites
            if (favorities?.contains(id) == true) {
                imageView.setImageResource(R.drawable.btn_3on)
            } else {
                imageView.setImageResource(R.drawable.btn_3)
            }
        }
    }

    override fun getItemCount(): Int = items.size

}