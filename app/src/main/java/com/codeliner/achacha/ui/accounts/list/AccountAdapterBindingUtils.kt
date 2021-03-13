package com.codeliner.achacha.ui.accounts.list

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.codeliner.achacha.data.accounts.Account
import com.example.helpers.GlideOptions
import timber.log.Timber
import java.util.*

@BindingAdapter("setAccountTitle")
fun TextView.setAccountTitle(item: Account?) {
    item?.let {
        text = it.title
    }
}

@BindingAdapter("setAccountUsername")
fun TextView.setAccountUsername(item: Account?) {
    item?.let {
        text = it.identity
    }
}

@BindingAdapter("setAccountThumbnailAsText")
fun TextView.setThumbnailAsText(item: Account?) {
    item?.let { account ->
        val let = account.title?.let { title ->
            text = title.capitalize(Locale.ROOT).firstOrNull().toString()
        }
        let
    }
}

@BindingAdapter("setAccountThumbnailAsImage")
fun ImageView.setThumbnailAsImage(item: Account?) {
    item?.let { account ->
        account.thumbnail?.let { thumbnail ->
            Glide
                .with(this.context)
                .load(thumbnail.toUri())
                .centerCrop()
                .apply(GlideOptions.centerCropAndRadius(16))
                .into(this)
        }
    }
}