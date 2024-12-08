package com.Practice.DIACataloge


object ComparisonManager {

    val comparisonList: MutableList<ItemsModel> = mutableListOf()
    fun addToComparison(item: ItemsModel): Boolean {
        return if (!isItemInComparison(item)) {
            comparisonList.add(item)
            true
        } else {
            false
        }
    }


    fun removeFromComparison(item: ItemsModel) {
        comparisonList.remove(item)
    }
    fun isItemInComparison(item: ItemsModel): Boolean {
        return comparisonList.any { it.title == item.title }
    }
}