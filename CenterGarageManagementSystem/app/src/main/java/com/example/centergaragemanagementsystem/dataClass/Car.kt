package com.example.centergaragemanagementsystem.dataClass

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


data class Car(
    var registrationPlate: String = "",
    var type: String = "",
//    var components : ArrayList<CarComponent> = ArrayList<CarComponent>(),
    var ownerID: String = "",
    var carImageURI: String = ""
)

@BindingAdapter("android:src","error")
fun setImageUrl(view: ImageView, url: String?, error: Drawable) {
    Glide.with(view.context).load(url).error(error).into(view)
}
