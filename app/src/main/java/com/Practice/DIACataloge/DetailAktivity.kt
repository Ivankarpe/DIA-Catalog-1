package com.Practice.DIACataloge

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.Practice.DIACataloge.R
import com.Practice.DIACataloge.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DetailAktivity : BaseActivity() {
    private  lateinit var binding: ActivityDetailBinding
    private lateinit var  item:ItemsModel
    private lateinit var compareButtonHandler: CompareButtonHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        compareButtonHandler = CompareButtonHandler(this, binding.compareBtn)
        getBundle()
        compareButtonHandler.setInitialState(item)

        binding.compareBtn.setOnClickListener {
            compareButtonHandler.toggleCompare(item)
        }


        getBundle()
        val imageView:ImageView=findViewById(R.id.favBtn)


        imageView.setOnClickListener{
            TogleFavorites(item.iditeam,imageView)

        }
        InFavorities(item.iditeam,imageView)
        initList()
        initSiteList()
        initCharacteristicsList()
    }

    private fun initCharacteristicsList() {
            binding.characteristicList.layoutManager = LinearLayoutManager(this)
            binding.characteristicList.adapter = CharacteristicsAdapter(item.characteristics)
        
    }

    private fun initSiteList() {
        val siteList = ArrayList<String>()
        for (siteUrl in item.siteUrl) {
            siteList.add(siteUrl)
        }
        binding.saitList.adapter = SiteAdapter(siteList)
        binding.saitList.layoutManager = LinearLayoutManager(this)
    }
    private fun TogleFavorites(id: String, favBtn:ImageView) {
        if(FirebaseAuth.getInstance().currentUser == null){
            Toast.makeText(this, "Увійдіть в акаунт, щоб мати можливість зберігати товари у вподобаному", Toast.LENGTH_SHORT).show()
            return
        }
        val doc = FirebaseFirestore.getInstance().collection("users").document(
            FirebaseAuth.getInstance().currentUser!!.uid.toString()
        )
        doc.get().addOnSuccessListener { documentSnapshot ->
            val favorites = documentSnapshot.toObject(
                UserDataClass::class.java
            )!!.Favorites
            if (favorites?.contains(id) == true) {
                favorites?.remove(id)
                Toast.makeText(this, "Товар видалено з вподобаного", Toast.LENGTH_SHORT).show()
            } else {
                favorites?.add(id)
                Toast.makeText(this, "Товар додано до вподобаного", Toast.LENGTH_SHORT).show()

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
            FirebaseAuth.getInstance().currentUser!!.uid.toString()
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

    private fun initList() {
        val modelList = arrayListOf<String>()
        for (models in item.model) {
            modelList.add(models)
        }
        binding.modelList.adapter=SelectModelAdapter(modelList)
        binding.modelList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val picList=ArrayList<String>()
        for(imageUrl in item.picUrl){
            picList.add(imageUrl)
        }
        Glide.with(this)
            .load(picList[0])
            .into(binding.img)


        binding.picList.adapter=PicAdapter(picList){selectedImageUrl->
            Glide.with(this)
                .load(selectedImageUrl)
                .into(binding.img)

        }

    binding.picList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

    }
    private fun updateRating(){
        val doc = FirebaseFirestore.getInstance().collection("Items").document(item.iditeam)
        var rating = 0f


        item.Ratings.forEach { entry ->
            rating+=entry.value
        }



        rating/=item.Ratings.size;


        doc.update("rating", Math.round(rating*10f)/10f)


        binding.ratingTxt.text="${Math.round(rating*10f)/10f}Rating"
    }
    private  fun getBundle(){
        item = intent.getParcelableExtra("object")!!
        val id = intent.getStringExtra("id")!!
        var ratingsNew: MutableMap<String,Float> = item.Ratings.toMutableMap()
        item.iditeam = id
        binding.titleTxt.text=item.title
        binding.descriptionTxt.text=item.description
        binding.priceTxt.text="$"+item.price
        binding.ratingTxt.text="${Math.round(item.rating*10)/10}Rating"
        binding.backBtn.setOnClickListener{ finish()}
        binding.ratingBar.rating = ratingsNew[FirebaseAuth.getInstance().currentUser?.uid.toString()] ?: 0f;
        binding.ratingBar.stepSize = 0.5f
        binding.ratingBar.setOnRatingBarChangeListener{ratingBar, rating, fromUser ->
            if(FirebaseAuth.getInstance().currentUser != null){
                val doc = FirebaseFirestore.getInstance().collection("Items").document(item.iditeam)
                var ratingsNew: MutableMap<String,Float> = item.Ratings.toMutableMap()
                ratingsNew[FirebaseAuth.getInstance().currentUser?.uid.toString()] = binding.ratingBar.rating

                item.Ratings = ratingsNew.toMap()
                updateRating();
                doc.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Document found in the offline cache
                        val document = task.result

                        var ratingsNew: MutableMap<String,Float> = document.toObject(ItemsModel::class.java)?.Ratings!!.toMutableMap()
                        ratingsNew[FirebaseAuth.getInstance().currentUser?.uid.toString()] = binding.ratingBar.rating
                        doc.update("ratings", ratingsNew)
                        item.Ratings = ratingsNew.toMap()
                        updateRating();
                    }
                }

            }
            else{
                Toast.makeText(this, "Увійдіть в акаунт, щоб оцінювати товари", Toast.LENGTH_SHORT).show()
            }

        }

    }

}