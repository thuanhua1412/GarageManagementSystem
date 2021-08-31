package com.example.dataclasses

// Service List: Remember to pass in a Service List to calculate cost upon creation of Bill
data class Bill (
    var totalBill : Int = 0,
    var timeCreated : String = "",
    var billApproved : Boolean = false
)