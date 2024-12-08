package com.Practice.DIACataloge


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.Practice.DIACataloge.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewMod()
    private lateinit var navigationView: BottomNavigationView
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        initCategory()
        initRecommended()
        setupSearchView()

    }

    private fun setupSearchView() {
        binding.searchView1.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {

                    if (it.isNotEmpty()) {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java).apply {
                            putExtra("query", it)
                        }
                        startActivity(intent)
                        isSearching = false
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupBottomNavigation() {
        navigationView = binding.navigationBar
        navigationView.setSelectedItemId(R.id.home)

        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> true
                R.id.compare -> {
                    startActivity(Intent(this, Compare_Acrivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.favorits -> {
                    startActivity(Intent(this, Favorities_Activity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile -> {
                    if (FirebaseAuth.getInstance().currentUser == null) {
                        startActivity(Intent(this, Login_Activity::class.java))
                    } else {
                        startActivity(Intent(this, Account_Activity::class.java))
                    }
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecommended() {
        binding.progressBarRecommend.visibility = View.VISIBLE
        viewModel.recommended.observe(this, Observer {
            binding.viewRecommendation.layoutManager = GridLayoutManager(this, 2)
            binding.viewRecommendation.adapter = RecommendedAdapt(it, this)
            binding.progressBarRecommend.visibility = View.GONE
        })
        viewModel.loadRecommended()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.recyclerViewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCategory.adapter = AdaptCategory(it)
            binding.progressBarCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }
    override fun onResume() {
        super.onResume()
        initRecommended()
        initCategory()
    }
}