package com.example.newsfeed

import android.graphics.Bitmap

data class Item(
    val imageBitmap: Bitmap?,
    val title: String,
    val description: String
)
