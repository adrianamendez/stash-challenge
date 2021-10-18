package com.stashinvest.stashchallenge.ui.fragment.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.stashinvest.stashchallenge.R

@BindingAdapter("loadImageUrl")
fun ImageView.loadImage(url: String?) {
    url?.let {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_empty_image)
            .error(R.drawable.ic_empty_image)
            .into(this)
    }
}