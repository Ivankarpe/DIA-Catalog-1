package com.Practice.DIACataloge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Practice.DIACataloge.databinding.ActivityCompareAcrivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth

class Compare_Acrivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompareAcrivityBinding
    private val viewModel = MainViewMod()
    private var navigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareAcrivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val comparisonList = ComparisonManager.comparisonList
        val comparisonRecyclerView: RecyclerView = findViewById(R.id.comparisonRecyclerView)

        comparisonRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var category:Category = Category()
        category.id = -1




        navigationView = binding.navigationBar
        navigationView?.setSelectedItemId(R.id.compare)

        navigationView?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@Compare_Acrivity, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.compare -> true
                R.id.favorits -> {
                    startActivity(Intent(this@Compare_Acrivity, Favorities_Activity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile -> {
                    val intent = if (FirebaseAuth.getInstance().currentUser == null) {
                        Intent(this@Compare_Acrivity, Login_Activity::class.java)
                    } else {
                        Intent(this@Compare_Acrivity, Account_Activity::class.java)
                    }
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        })

        initCategoryCompare()
        filterComparisonByCategory(category)
    }

    private fun initCategoryCompare() {
        binding.textView14.visibility = View.INVISIBLE
        binding.progressBarCompareCategory.visibility = View.VISIBLE
        binding.progressBarComareItems.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer { categories ->
            binding.recyyclerViewCompareCategory.layoutManager = LinearLayoutManager(
                this@Compare_Acrivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )


            binding.recyyclerViewCompareCategory.adapter = AdaptCategoryCompare(categories.toMutableList()) { selectedCategory ->
                filterComparisonByCategory(selectedCategory)
            }
            binding.progressBarCompareCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }
    private fun filterComparisonByCategory(category: Category) {
        val filteredComparisonList = ComparisonManager.comparisonList.filter { item ->
            (item.categoryId == category.id.toString() ) || (category.id.toString() == "-1")
        }


        binding.progressBarComareItems.visibility = View.GONE
        Log.i("INFO", "list is " + filteredComparisonList.isEmpty())
        if (filteredComparisonList.isEmpty()){
            binding.comparisonRecyclerView.visibility = View.INVISIBLE
            binding.textView14.visibility =View.VISIBLE
        }else{
            binding.comparisonRecyclerView.visibility = View.VISIBLE
            binding.textView14.visibility =View.INVISIBLE

            val comparisonRecyclerView: RecyclerView = findViewById(R.id.comparisonRecyclerView)
            comparisonRecyclerView.adapter = ComparisonAdapter(filteredComparisonList)

        }


    }
}