
package com.example.vinilos_app.ui.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy // Importa DiskCacheStrategy
class BindingAdapters {

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Caché para un mejor rendimiento
                .into(view)
        }


    }
}
