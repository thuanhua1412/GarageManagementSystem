package customer.homepage.service.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.dataclasses.*
import com.example.dataclasses.databinding.ActivityServiceDetailBinding
import customer.homepage.ServiceCartViewModel
import customer.homepage.service.ServiceViewModel
import payment.MomoPaymentActivity
import payment.MomoPaymentStarterActivity
import payment.MyViewModelFactory


const val PAYMENT_REQUEST = 3

class ServiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var adapter: CarPhotoAdapter


    private lateinit var viewModelFactory: ServiceDetailViewModelFactory
    private lateinit var viewModel : ServiceDetailViewModel
    private lateinit var servicePassed: Service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_detail)
        servicePassed = intent.extras?.get("service") as Service
        val carRegNum = intent.extras!!.getString("carRegNum")
        viewModelFactory = ServiceDetailViewModelFactory(servicePassed)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ServiceDetailViewModel::class.java)
        viewModel.getServiceListLiveData().observe(this, Observer {
            binding.service = it
            binding.carRegistrationEditText.setText(carRegNum)
            val colorPercentPair = getColorFromStatus(it.status)
            binding.progressBar.progress = colorPercentPair.second
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.progressBar.progressTintList =
                    ColorStateList.valueOf(colorPercentPair.first)
            }

            adapter = CarPhotoAdapter(it)
            binding.viewpager.adapter = adapter
            binding.indicator.setViewPager(binding.viewpager)

        })

        binding.payButton.setOnClickListener{
            navigateToMomoPayment(servicePassed)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PAYMENT_REQUEST){
            if(resultCode == RESULT_OK){
                val result = data?.getStringExtra("result")
                if (result != null) {
                    if(result.contains("successful")){
                        viewModel.paidService(service = servicePassed)
                        binding.paidEditText.setText("Paid")
                    }
                }
            }
            if(resultCode == RESULT_CANCELED){
                // Do Nothing since no payment happended
            }
        }
    }
    //TODO: FUNCTION TO OPEN MOMO FOR PAYMENT
    private fun navigateToMomoPayment(serviceToPay: Service) {
        val intent = Intent(this, MomoPaymentActivity::class.java)
        intent.putExtra("cost", serviceToPay.cost)
        intent.putExtra("serviceID",serviceToPay.serviceID)
        startActivityForResult(intent, PAYMENT_REQUEST)
    }

    private fun getColorFromStatus(status: String): Pair<Int, Int> = when (status) {
        SERVICE_CONFIRMATION_WAITING -> Pair(Color.argb(0, 255, 27, 27), 25)
        SERVICE_WAITING -> Pair(Color.argb(0, 255, 191, 27), 50)
        SERVICE_EXECUTING -> Pair(Color.argb(0, 15, 134, 201), 75)
        SERVICE_FINISHED -> Pair(Color.argb(0, 63, 201, 15), 100)
        else -> Pair(Color.argb(0, 255, 27, 27), 25)
    }

}

