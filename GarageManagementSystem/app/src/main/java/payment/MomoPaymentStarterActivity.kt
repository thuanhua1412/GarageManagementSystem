package payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclasses.R

class MomoPaymentStarterActivity : AppCompatActivity(){

    var environment = 1 //developer default - Production environment = 2

//    @BindView(R.id.rdEnvironmentProduction)
//    var rdEnvironmentProduction: RadioButton? = null

//    @BindView(R.id.rdGroupEnvironment)
//    var rdGroupEnvironment: RadioGroup? = null

//    @BindView(R.id.btnPaymentMoMo)
//    var btnPaymentMoMo: Button? = null
    private lateinit var btnPaymentMomo : Button

//    @BindView(R.id.btnMappingMoMo)
//    var btnMappingMoMo: Button? = null
    private lateinit var btnMapppingMomo : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_momo_payment_starter)
        btnPaymentMomo = findViewById(R.id.btnPaymentMoMo)
        btnMapppingMomo = findViewById(R.id.btnMappingMoMo)
        var intent : Intent
        var data : Bundle = Bundle()

        btnPaymentMomo.setOnClickListener {
            intent = Intent(this, MomoPaymentActivity::class.java)
            data.putInt(MoMoConstants.KEY_ENVIRONMENT, environment)
            intent.putExtras(data)
            startActivity(intent)
        }

        btnMapppingMomo.setOnClickListener {
            intent = Intent(this, PaymentActivity::class.java)
            data.putInt(MoMoConstants.KEY_ENVIRONMENT, environment)
            intent.putExtras(data)
            startActivity(intent)
        }
    }


//    private fun returnWithMessage(mess: String){
//        val returnIntent = Intent()
//        returnIntent.putExtra("result",mess)
//        if(serviceID != "Nothing") {
//            returnIntent.putExtra("serviceID",serviceID)
//        }
//        setResult(RESULT_OK,returnIntent)
//        finish()
//    }
//    @OnClick([R.id.btnPaymentMoMo, R.id.btnMappingMoMo])
//    fun onViewClicked(view: View) {
//        val intent: Intent
//        val data = Bundle()
//        when (view.id) {
//            R.id.btnPaymentMoMo -> {
//                intent = Intent(this@MainActivity, PaymentActivity::class.java)
//                data.putInt(MoMoConstants.KEY_ENVIRONMENT, environment)
//                intent.putExtras(data)
//                startActivity(intent)
//            }
//            R.id.btnMappingMoMo -> {
//                intent = Intent(this@MainActivity, MappingActivity::class.java)
//                data.putInt(MoMoConstants.KEY_ENVIRONMENT, environment)
//                intent.putExtras(data)
//                startActivity(intent)
//            }
//        }
//    }
}