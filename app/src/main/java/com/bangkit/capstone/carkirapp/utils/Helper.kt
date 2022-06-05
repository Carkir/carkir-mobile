package com.bangkit.capstone.carkirapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Create extension function to ImageView to
 * load image from network source or Any other type using glide library
 * */
fun ImageView.loadImage(context: Context, source: Any) {
    Glide.with(context)
        .load(source)
        .into(this)
}

/**
 * Create extension function String for
 * Decode Base64 string into a Bitmap Image
 * */
fun String.decodeBase64ToBitmap(): Bitmap {
    val decodedString = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

/**
 * Calculate number of columns based width of items
 * @return number of columns
 */
fun calculateAutoColumns(rv: RecyclerView, columnWidth: Int): Int {
    val displayMetrics = rv.context.resources.displayMetrics
    return ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
}