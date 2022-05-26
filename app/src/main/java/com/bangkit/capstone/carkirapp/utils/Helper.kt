package com.bangkit.capstone.carkirapp.utils

import android.content.Context
import android.widget.ImageView
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