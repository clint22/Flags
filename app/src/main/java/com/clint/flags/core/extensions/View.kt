package com.clint.flags.core.extensions

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.clint.flags.core.Constants.GLIDE_ROUNDED_CORNER_SIZE

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(url: String?) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .transform(CenterCrop(), RoundedCorners(GLIDE_ROUNDED_CORNER_SIZE))
        .into(this)

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.updateTextColorAndSize(
    startIndex: Int,
    endIndex: Int,
    textColor: Int
): Spannable {
    val spannable = SpannableString(this)
    spannable.setSpan(
        RelativeSizeSpan(2f),
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    spannable.setSpan(
        ForegroundColorSpan(textColor), startIndex, endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}