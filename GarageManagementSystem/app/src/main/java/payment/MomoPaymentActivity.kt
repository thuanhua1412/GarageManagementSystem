package payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclasses.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib
import vn.momo.momo_partner.MoMoParameterNamePayment
import java.util.*
import kotlin.collections.HashMap


class MomoPaymentActivity : AppCompatActivity() {
    private lateinit var tvEnvironment: TextView
    private lateinit var tvMerchantCode : TextView
    private lateinit var tvMerchantName : TextView
    private lateinit var edAmount : EditText
    private lateinit var tvMessage : TextView
    private lateinit var btnPayMomo : Button


    private var amount = "1000000"
    private var fee = "0"
    private var environment = 1

    private var merchantName = "Rửa xe cực mạnh"
    private var merchantCode = "MOMOQYW920210515"
    private var merchantNameLabel = "Cửa hàng 1"
    private var description = "Sử dụng dịch vụ bảo quản xe ở Cửa hàng 1"

    private lateinit var serviceID : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_momo_payment)
        setSupportActionBar(findViewById(R.id.toolbar))

        initializeUI()
        val data : Bundle? = intent.extras
        if(data != null){
//            environment = data.getInt(MoMoConstants.KEY_ENVIRONMENT)
            serviceID = if(data.getString("serviceID") != null) data.getString("serviceID")!! else "Nothing"
            Log.d("AMOUNT__MOMO","${data.getInt("cost")}")
            amount = data.getInt("cost").toString()
        }
        if(environment == 0){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEBUG)
            tvEnvironment.text = "Development Environment"
        }
        else if(environment  == 1){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)
            tvEnvironment.text = "Development Environment"
        }
        else if(environment  == 1){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.PRODUCTION)
            tvEnvironment.text = "PRODUCTION Environment"
        }

        tvMerchantCode.text = "Merchant Code$merchantCode"
        tvMerchantName.text = "Merchant Name:$merchantName"
    }

//    private fun requestPayment(){
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
//        if (edAmount.text.toString()
//                .trim { it <= ' ' }.isNotEmpty()
//        ) amount = edAmount.text.toString().trim { it <= ' ' }
//
//        val eventValue: MutableMap<String, Any> = HashMap()
//        //client Required
//        //client Required
//        eventValue["merchantname"] =
//            merchantName //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//
//        eventValue["merchantcode"] =
//            merchantCode //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//
//        eventValue["amount"] = total_amount //Kiểu integer
//
//        eventValue["orderId"] =
//            "orderId123456789" //uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
//
//        eventValue["orderLabel"] = "Mã đơn hàng" //gán nhãn
//
//
//        //client Optional - bill customer.homepage.clientinfo.info
//
//        //client Optional - bill customer.homepage.clientinfo.info
//        eventValue["merchantnamelabel"] = "Dịch vụ" //gán nhãn
//
//        eventValue["fee"] = total_fee //Kiểu integer
//
//        eventValue["description"] = description //mô tả đơn hàng - short description
//
//
//        //client extra data
//
//        //client extra data
//        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
//        eventValue["partnerCode"] = merchantCode
//        //Example extra data
//        //Example extra data
//        val objExtraData = JSONObject()
//        try {
//            objExtraData.put("site_code", "008")
//            objExtraData.put("site_name", "CGV Cresent Mall")
//            objExtraData.put("screen_code", 0)
//            objExtraData.put("screen_name", "Special")
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
//            objExtraData.put("movie_format", "2D")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        eventValue["extraData"] = objExtraData.toString()
//
//        eventValue["extra"] = ""
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
//
//
//    }

    //example payment
    private fun requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
        if (edAmount.text.toString() != null && edAmount.text.toString()
                .trim { it <= ' ' }.length != 0
        ) amount = edAmount.text.toString().trim { it <= ' ' }
        val eventValue: MutableMap<String, Any> = HashMap()
        //client Required
        eventValue[MoMoParameterNamePayment.MERCHANT_NAME] = merchantName
        eventValue[MoMoParameterNamePayment.MERCHANT_CODE] = merchantCode
        eventValue[MoMoParameterNamePayment.AMOUNT] = amount
        eventValue[MoMoParameterNamePayment.DESCRIPTION] = description
        //client Optional
        eventValue[MoMoParameterNamePayment.FEE] = fee
        eventValue[MoMoParameterNamePayment.MERCHANT_NAME_LABEL] = merchantNameLabel
        eventValue[MoMoParameterNamePayment.REQUEST_ID] =
            merchantCode + "-" + UUID.randomUUID().toString()
        eventValue[MoMoParameterNamePayment.PARTNER_CODE] = "MOMOQYW920210515"
        val objExtraData = JSONObject()
        try {
            objExtraData.put("site_code", "008")
            objExtraData.put("site_name", "CGV Cresent Mall")
            objExtraData.put("screen_code", 0)
            objExtraData.put("screen_name", "Special")
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
            objExtraData.put("movie_format", "2D")
            objExtraData.put(
                "ticket",
                "{\"ticket\":{\"01\":{\"type\":\"std\",\"price\":110000,\"qty\":3}}}"
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        eventValue[MoMoParameterNamePayment.EXTRA_DATA] = objExtraData.toString()
        eventValue[MoMoParameterNamePayment.REQUEST_TYPE] = "payment"
        eventValue[MoMoParameterNamePayment.LANGUAGE] = "vi"
        eventValue[MoMoParameterNamePayment.EXTRA] = ""
        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    tvMessage.text = "message: " + "Get token " + data.getStringExtra("message")
                    if (data.getStringExtra("data") != null && data.getStringExtra("data") != "") {
                        // TODO:
                    } else {
                        tvMessage.text = "message: " + "this.getString(R.string.not_receive_info)"
                    }
                    returnWithMessage("successful")
                } else if (data.getIntExtra("status", -1) == 1) {
                    val message =
                        if (data.getStringExtra("message") != null) data.getStringExtra("message") else "Thất bại"
                    tvMessage.text = "message: $message"
                } else if (data.getIntExtra("status", -1) == 2) {
                    tvMessage.text = "message: " + "this.getString(R.string.not_receive_info)"
                } else {
                    tvMessage.text = "message: " + "this.getString(R.string.not_receive_info)"
                }
            } else {
                tvMessage.text = "message: " + "this.getString(R.string.not_receive_info)"
            }
        } else {
            tvMessage.text = "message: " + "this.getString(R.string.not_receive_info_err)"
        }
    }
    private fun returnWithMessage(mess: String){
        val returnIntent = Intent()
        returnIntent.putExtra("result",mess)
        if(serviceID != "Nothing") {
            returnIntent.putExtra("serviceID",serviceID)
        }
        Log.d("MOMO_RETURN","$serviceID")

        setResult(RESULT_OK,returnIntent)
        finish()
    }
    private fun initializeUI(){
        tvEnvironment = findViewById(R.id.tvEnvironment)
        tvMerchantCode = findViewById(R.id.tvMerchantCode)
        tvMerchantName = findViewById(R.id.tvMerchantName)
        edAmount = findViewById(R.id.edAmount)
        tvMessage = findViewById(R.id.tvMessage)
        btnPayMomo = findViewById(R.id.btnPayMoMo)

        btnPayMomo.setOnClickListener {
            requestPayment()
        }
    }
}