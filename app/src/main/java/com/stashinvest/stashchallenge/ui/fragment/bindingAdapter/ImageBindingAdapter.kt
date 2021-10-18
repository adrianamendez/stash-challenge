package com.stashinvest.stashchallenge.ui.fragment.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.stashinvest.stashchallenge.R

@BindingAdapter("loadImageUrl")
fun ImageView.loadImage(url: String?) {
    url?.let {
    Picasso.get()
        .load(url)
        .into(this)
    }
}