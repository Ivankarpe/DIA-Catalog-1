package com.Practice.DIACataloge


import android.os.Parcel
import android.os.Parcelable

data class ItemsModel  (
    var title:String="",
    var description:String="",
    var picUrl:ArrayList<String> =ArrayList(),
    var model:ArrayList<String> =ArrayList(),
    var price:Double=0.0,
    var rating:Double=0.0,
    var numberInCart:Int=0,
    var showRecommended:Boolean=false,
    var categoryId:String="",
    var siteUrl:ArrayList<String> =ArrayList(),
    var characteristics: Map<String, String> = emptyMap(),
    var iditeam:String="",
    var Ratings:Map<String, Float> = emptyMap()
):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",  // Safely handle potential null
        parcel.readString() ?: "",  // Safely handle potential null
        parcel.createStringArrayList() ?: arrayListOf(),  // Handle potential null
        parcel.createStringArrayList() ?: arrayListOf(),  // Handle potential null
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),  // Convert byte to boolean
        parcel.readString() ?: "",  // Safely handle potential null
        parcel.createStringArrayList() ?: arrayListOf(),  // Handle potential null
        parcel.readHashMap(String::class.java.classLoader) as? Map<String, String> ?: mapOf(),  // Safe casting
        parcel.readString() ?: "",  // Safely handle potential null
        parcel.readHashMap(String::class.java.classLoader) as? Map<String, Float> ?: mapOf()  // Corrected to Float
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(picUrl)
        parcel.writeStringList(model)
        parcel.writeDouble(price)
        parcel.writeDouble(rating)
        parcel.writeInt(numberInCart)
        parcel.writeByte(if (showRecommended) 1 else 0)  // Convert boolean to byte
        parcel.writeString(categoryId)
        parcel.writeStringList(siteUrl)
        parcel.writeMap(characteristics)  // Write characteristics map
        parcel.writeString(iditeam)
        parcel.writeMap(Ratings)  // Write ratings map
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel {
            return ItemsModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }
}