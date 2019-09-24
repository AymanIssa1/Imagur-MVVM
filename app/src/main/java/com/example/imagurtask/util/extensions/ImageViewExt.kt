package com.example.imagurtask.util.extensions

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.imagurtask.R

fun ImageView.loadUrl(url: String?) {
    if (!url.isNullOrBlank()) {
        if (url.endsWith("gif")) {
            Glide.with(context)
                .asGif()
                .load(url)
                .into(this)
        } else {
            Glide.with(context)
                .load(url)
                .placeholder(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this.context,
                            R.color.imagePlaceholder
                        )
                    )
                )
                .into(this)
        }
    } else {
        this.setImageDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this.context,
                    R.color.imagePlaceholder
                )
            )
        )
    }
}