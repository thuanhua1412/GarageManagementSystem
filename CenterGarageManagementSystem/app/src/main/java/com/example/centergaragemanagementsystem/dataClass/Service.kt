package com.example.centergaragemanagementsystem.dataClass

const val SERVICE_WAITING = "Đang chờ"
const val SERVICE_EXECUTING = "Đang thực hiện"
const val SERVICE_FINISHED = "Đã xong"
const val SERVICE_CONFIRMATION_WAITING = "Đang chờ duyệt"

const val SERVICE_CANCEL_ACTION  = "Hủy dịch vụ"
const val SERVICE_REWORK_REQUEST_ACTION  = "Làm lại hộ cái"
//const val SERVICE_


data class Service(
        var serviceID: String = "",
        var carImage: ArrayList<String> = ArrayList(),
        val name: String = "",
        var ownerID : String ="",
        var carServiced : String = "", // This will be the registration number entered by the user
        var status : String = SERVICE_CONFIRMATION_WAITING,
        var pickUpLocation: String = "",
        var cartID : String = "",
        var timeFinish: String = "",
        var description: String = "",
        var paid: Boolean = false,
        var reworkRequested: Boolean = false,
        var cost: Int = 0,
        var carArriveTime: String="",
        var timeLimit: String ="",
        var proofImageURI : String = "",
        var fromClient:Boolean = true,
        var speedometer: String = "",
        var vatDung: String = ""
)

data class ServiceItem(
        var name: String = "",
        var description: String  ="",
        var cost : Int = 0,
        var timeLimit : String ="",
        var type : String = "",
)
