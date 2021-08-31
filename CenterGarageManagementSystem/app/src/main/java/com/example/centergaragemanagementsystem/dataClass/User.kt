package com.example.centergaragemanagementsystem.dataClass

import java.util.*

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

data class UserCustomer(var name: String = "",
                    var dateOfBirth: String = "",
                    var avatarURI: String? = null,
                    var phone: String = "",
                    var email: String = "" // This will be used as username as well if registered with email password
) : User() {
    override val role: String = "customer"
}

data class Technician(var name: String = "", val EID: String = "", val dateOfBirth: String = "",val gender : String =""
, var position: String = "", var phone : String = "", var email : String = "") : User(){
    override val role: String = "technician"
}

data class Manager(var userID:String = "",var avatarURI: String? = null, var name: String = "", val EID: String = "", val dateOfBirth: String = "",val gender : String =""
                      , var position: String = "", var phone : String = "", var email : String = "") : User(){
    override val role: String =  "manager"}