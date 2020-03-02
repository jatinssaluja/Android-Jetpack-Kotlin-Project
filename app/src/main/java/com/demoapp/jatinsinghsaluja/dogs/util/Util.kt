package com.demoapp.jatinsinghsaluja.dogs.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.demoapp.jatinsinghsaluja.dogs.R

const val PERMISSION_SEND_SMS = 234

// will give little spinner image while the actual image is being loaded
fun getProgressDrawable(context: Context):CircularProgressDrawable {

    return CircularProgressDrawable(context).apply {

        strokeWidth = 10f
        centerRadius = 50f
        start()
    }

}

    //Extension Function for ImageView Element
    fun ImageView.loadImage(uri:String?, progressDrawable: CircularProgressDrawable){

        val options = RequestOptions()
            .placeholder(progressDrawable)
                // in case of an error, load the defualt image
            .error(R.mipmap.ic_dog_icon)

        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)

    }

// BindingAdapter annotation makes function available to a layout
@BindingAdapter("android:imageUrl")
fun loadImage(view:ImageView , url:String?){
    view.loadImage(url, getProgressDrawable(view.context))
}
