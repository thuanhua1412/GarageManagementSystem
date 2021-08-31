package com.example.dataclasses
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kotlinx.parcelize.Parcelize


@Parcelize
data class Car(
    var registrationPlate: String = "",
    var type: String = "",
//    var components : ArrayList<CarComponent> = ArrayList<CarComponent>(),
    var ownerID: String = "",
    var carImageURI: String = ""
) : Parcelable{
    // Creat a car with basic components
//    init {
//        components.add(GearBox(state = "Bad"))
//        components.add(Wheel(state = "Bad"))
//        components.add(Engine(state = "Bad"))
//        components.add(WindShield(state="Bad"))
//    }
}

@BindingAdapter("android:src","error")
fun setImageUrl(view: ImageView, url: String?,error: Drawable) {
    Log.d("IMAGE_BINDING:","Binding Image View with: $url")
    Glide.with(view.context).load(url).error(error).into(view)
}