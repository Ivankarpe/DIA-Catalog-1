package com.Practice.DIACataloge

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat

class CompareButtonHandler(
    private val context: Context,
    private val compareBtn: ImageView
) {
    private var isInComparison = false

    fun toggleCompare(item: ItemsModel) {
        isInComparison = !isInComparison

        if (isInComparison) {

            if (!ComparisonManager.isItemInComparison(item)) {
                ComparisonManager.addToComparison(item)
                compareBtn.setColorFilter(ContextCompat.getColor(context, R.color.green))
                Toast.makeText(context, "Додано до порівняння", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Товар вже в списку порівняння", Toast.LENGTH_SHORT).show()
            }
        } else {

            ComparisonManager.removeFromComparison(item)
            compareBtn.setColorFilter(ContextCompat.getColor(context, R.color.grey))
            Toast.makeText(context, "Видалено з порівняння", Toast.LENGTH_SHORT).show()
        }
    }


    fun setInitialState(item: ItemsModel) {
        isInComparison = ComparisonManager.isItemInComparison(item)
        compareBtn.setColorFilter(
            if (isInComparison) ContextCompat.getColor(context, R.color.green)
            else ContextCompat.getColor(context, R.color.grey)
        )
    }
}