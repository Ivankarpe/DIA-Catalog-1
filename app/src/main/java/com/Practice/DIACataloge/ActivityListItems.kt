package com.Practice.DIACataloge

import android.os.Bundle
import android.view.View
import android.widget.ImageView

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.Practice.DIACataloge.R
import com.Practice.DIACataloge.databinding.ActivityListItemsBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ActivityListItems : BaseActivity() {
    private lateinit var binding: ActivityListItemsBinding
    private val viewModel = MainViewMod()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getBundle()
        initList()
    }
    private fun TogleFavorites(id: String) {
        val doc = FirebaseFirestore.getInstance().collection("users").document(
            FirebaseAuth.getInstance().currentUser!!.uid.toString()
        )
        doc.get().addOnSuccessListener { documentSnapshot ->
            val favorites = documentSnapshot.toObject(
                UserDataClass::class.java
            )!!.Favorites
            if (favorites.contains(id)) {
                favorites.remove(id)
            } else {
                favorites.add(id)
            }
            doc.update("Favorites", favorites)
        }
    }

    private fun InFavorities(id: String, imageView: ImageView) {
        val doc = FirebaseFirestore.getInstance().collection("users").document(
            FirebaseAuth.getInstance().currentUser!!.uid.toString()
        )

        doc.get().addOnSuccessListener { documentSnapshot ->
            val favorities = documentSnapshot.toObject(
                UserDataClass::class.java
            )!!.Favorites
            if (favorities.contains(id)) {
                imageView.setImageResource(R.drawable.btn_3on)
            } else {
                imageView.setImageResource(R.drawable.btn_3)
            }
        }
    }

    private fun initList() {
        binding.apply {
            progressBarlist.visibility = View.VISIBLE
            viewModel.recommended.observe(this@ActivityListItems, Observer {
                viewList.layoutManager = GridLayoutManager(this@ActivityListItems, 2)
                viewList.adapter = ListItemAdapt(it)
                progressBarlist.visibility = View.GONE

            })
            viewModel.loadFiltered(id)
        }
    }

    private fun getBundle() {
        id=intent.getStringExtra("id")!!
        title=intent.getStringExtra("title")!!

        binding.categoryTxt.text = title
        binding.backBtn.setOnClickListener{ finish()}
    }
}
