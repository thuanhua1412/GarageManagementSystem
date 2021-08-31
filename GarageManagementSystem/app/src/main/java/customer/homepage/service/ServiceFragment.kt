import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataclasses.R
import com.example.dataclasses.Service
import com.example.dataclasses.databinding.FragmentServiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import customer.homepage.HomeScreenActivity
import customer.homepage.service.*
import customer.homepage.service.detail.PAYMENT_REQUEST
import customer.homepage.service.detail.ServiceDetailActivity
import customer.homepage.service.detail.ServiceDetailViewModel
import customer.homepage.service.detail.ServiceDetailViewModelFactory
import payment.MomoPaymentActivity
import payment.MomoPaymentStarterActivity


private const val FROM_CLIENT_PARAM = "fromClientIndicator"
private const val SERVICE_HOST_PARAM_2 = "param2"

class ServiceFragment : Fragment() {
    private lateinit var serviceViewModel: ServiceViewModel

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore

    private lateinit var adapter: ServiceListAdapter
    private lateinit var binding: FragmentServiceBinding


    private var fromClientParam: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fromClientParam = it.getString(FROM_CLIENT_PARAM)
            param2 = it.getString(SERVICE_HOST_PARAM_2)
        }
        Log.d("FRAMENT_SERV:","ON VIEW CREAT with source: $fromClientParam")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)

        val viewModelFactory = ServiceViewModelFactory(fromClientParam!!)
        serviceViewModel = ViewModelProvider(this,viewModelFactory).get(ServiceViewModel::class.java)
        val layoutManager = LinearLayoutManager(this.requireContext())

        createNotificationChannel()

        serviceViewModel.getMergedLiveData().observe(viewLifecycleOwner, Observer { pair ->
            adapter = ServiceListAdapter(
                pair.first,
                pair.second,
                ServiceClickListener { serviceStringPair ->
                    val serviceItem = serviceStringPair.first
                    val viewType = serviceStringPair.second
                    when (viewType) {
                        "payButton" -> {
                            navigateToMomoPayment(serviceItem)
                        }
                        "detailButton" -> {
                            Log.d("SERVICE_FRAGMENT:", "detailButton Clicked")
                            navigateToServiceDetailFragment(serviceItem)
                        }
                        "card" -> {

                        }
                        else -> {

                        }
                    }
//                Toast.makeText(this.requireContext(), "$it is clicked", Toast.LENGTH_SHORT).show()
//            //TODO: REMOVE THIS LINE AFTER TESTING MOMO
////                navigateToMomoPayment()
                },
                serviceViewModel
            )
            binding.serviceRecyclerView.adapter = adapter
            binding.serviceRecyclerView.layoutManager = layoutManager

        })
        binding.lifecycleOwner = this
        Log.d("FRAMENT_SERV:","ON CREATE VIEW")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FRAMENT_SERV:","ON VIEW CREATED")
    }

    //TODO: Set notification when service state changed
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SERVICE STATUS"
            val descriptionText = "TEST CHANNEL DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TEST_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        Log.d("NOTIFICATION SERV_FRAG", "Successfully Created a chanel")
    }

    private fun navigateToServiceDetailFragment(service: Service) {
        val intent = Intent(this.activity, ServiceDetailActivity::class.java)
        intent.putExtra("service", service)
        intent.putExtra("carRegNum", service.carServiced)
        startActivity(intent)
    }

    private fun createAndSendNotification() {
        val notificationManager: NotificationManager =
            this.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this.context, HomeScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0)
        var builder = NotificationCompat.Builder(this.requireContext(), "TEST_CHANNEL_ID")
            .setSmallIcon(R.drawable.info_icon)
            .setContentTitle("CAR SERVICE")
            .setContentText("CAR SERVICE DESCRIPTION")
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        Log.d("CREATE NOTIFICATION", "Successfully")
//        notificationManager.cancelAll()
        notificationManager.notify(1, builder.build())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PAYMENT_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val result = data?.getStringExtra("result")
                if (result != null) {
                    if (result.contains("successful")) {
                        if (data.getStringExtra("serviceID") != null) {
                            val serviceID = data.getStringExtra("serviceID")
                            if (serviceID != null && serviceID != "Nothing") {
                                Log.d("SERVICE_FRAG_ONRESULT:","$serviceID")
                                serviceViewModel.paidService(serviceID = serviceID)
                            }
                        }
                        Log.d("SERVICE_FRAG_ONRESULT2:","Null return")

                    }
                }
                Log.d("SERVICE_FRAG_ONRESULT3:","Nthing")

            }
            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                // Do Nothing since no payment happended
                Toast.makeText(this.context, "Failed To Pay for the service", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //TODO: FUNCTION TO OPEN MOMO FOR PAYMENT
    private fun navigateToMomoPayment(serviceToPay: Service) {
        val intent = Intent(this.requireActivity(), MomoPaymentActivity::class.java)
        Log.d("SERVICE_PASS_COST","${serviceToPay.cost}")
        intent.putExtra("cost", serviceToPay.cost)
        intent.putExtra("serviceID",serviceToPay.serviceID)
        startActivityForResult(intent, PAYMENT_REQUEST)
    }

    // TODO: FUNCTION TO SHOW SERVICE DETAIL
    private fun navigateToServiceDetail(service: Service) {
        // TODO: IMPLEMENT ACTIVITY TO SHOW DETAIL
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ServiceHostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServiceFragment().apply {
                arguments = Bundle().apply {
                    putString(FROM_CLIENT_PARAM, param1)
                    putString(SERVICE_HOST_PARAM_2, param2)
                }
            }
    }
}

