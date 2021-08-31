package com.example.dataclasses

import android.graphics.Picture
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.*
const val DEFAULT_AVATAR_URI = "https://i.pinimg.com/originals/0d/68/d4/0d68d4a12a253c5df2e8e10277754774.jpg"
abstract class User{
    abstract val role : String
    val  observers :ArrayList<Observer>
        get() {
            return this.observers
        }
    val carList : ArrayList<Car>
        get() {
            return this.carList
        }
}

@Parcelize
data class Customer(var name: String = "",
                    var dateOfBirth: String = "",
                    var avatarImageUri: String = DEFAULT_AVATAR_URI,
                    var phone: String = "",
                    var numberOfServiceTime : Int = 0,
                    var rank : String = "Bronze",
                    var role: String = "customer",
                    var email: String = "" // This will be used as username as well if registered with email password
) :Parcelable


data class Technician(var name: String = "", val EID: String = "", val DOB: String = "",val gender : String =""
, var posistion: String = "", var phone : String = "", var email : String = "") : User(){
    override val role: String = "technician"
}

data class Manager(var name: String = "", val EID: String = "", val DOB: String = "",val gender : String =""
                      , var posistion: String = "", var phone : String = "", var email : String = "") : User(){
    override val role: String =  "manager"}