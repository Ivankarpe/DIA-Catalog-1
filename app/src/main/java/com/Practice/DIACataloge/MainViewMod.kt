package com.Practice.DIACataloge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class MainViewMod : ViewModel() {
    private val firestoreDatabase = FirebaseFirestore.getInstance()
    private val _category = MutableLiveData<MutableList<Category>>()
    private val _recommended = MutableLiveData<MutableList<ItemsModel>>()
    private val _allItems = MutableLiveData<List<ItemsModel>>()
    val category: LiveData<MutableList<Category>> = _category
    val recommended: LiveData<MutableList<ItemsModel>> = _recommended
    val allItems: LiveData<List<ItemsModel>> get() = _allItems


    fun filterItemsByTitle(title: String) {
        val ref = firestoreDatabase.collection("Items")
        val query = ref.orderBy("title").orderBy("rating", Query.Direction.DESCENDING)
        query.get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                val filteredItems = mutableListOf<ItemsModel>()
                for (document in documents) {
                    val item = document.toObject(ItemsModel::class.java)
                    item.iditeam=document.id
                    if (item.title.lowercase().contains(title.lowercase())) {
                        filteredItems.add(item)

                    }

                }
                _allItems.value = filteredItems
            }
            .addOnFailureListener {exception ->
                Log.i("INFO", "fail" + exception.localizedMessage.toString())

            }
    }

    fun loadFiltered(id: String) {
        val ref = firestoreDatabase.collection("Items")
        ref.whereEqualTo("categoryId", id).orderBy("rating", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val lists = mutableListOf<ItemsModel>()
                for (document in documents) {
                    val item = document.toObject(ItemsModel::class.java)
                    item.iditeam=document.id
                    lists.add(item)
                }
                _recommended.value = lists
            }
            .addOnFailureListener {exception ->
                Log.i("INFO", "fail" + exception.localizedMessage.toString())

            }
    }

    fun loadRecommended() {
        Log.i("INFO", "wos")
        val ref = firestoreDatabase.collection("Items").whereEqualTo("showRecommended", true).orderBy("rating", Query.Direction.DESCENDING).limit(14)
        ref.get()
            .addOnSuccessListener { documents ->
                val lists = mutableListOf<ItemsModel>()
                for (document in documents) {
                    val item = document.toObject(ItemsModel::class.java)
                   item.iditeam=document.id
                    lists.add(item)
                }
                _recommended.value = lists
                Log.i("INFO", "easdad")
            }
            .addOnFailureListener {exception ->
                Log.i("INFO", "fail" + exception.localizedMessage.toString())
            }
    }

    fun loadCategory() {
        val ref = firestoreDatabase.collection("Category")
        ref.get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                val lists = mutableListOf<Category>()
                for (document in documents) {
                    val category = document.toObject(Category::class.java)
                    lists.add(category)
                }
                _category.value = lists
            }
            .addOnFailureListener {exception ->
                Log.i("INFO", "fail" + exception.localizedMessage.toString())

            }

    }

    fun loadAllItems() {
        val ref = firestoreDatabase.collection("Items").orderBy("rating", Query.Direction.DESCENDING)
        ref.get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                val lists = mutableListOf<ItemsModel>()
                for (document in documents) {
                    val item = document.toObject(ItemsModel::class.java)
                    item.iditeam=document.id
                    lists.add(item)
                }
                _allItems.value = lists
            }
            .addOnFailureListener {exception ->
                Log.i("INFO", "fail" + exception.localizedMessage.toString())
            }

    }


}