package customer.homepage.clientinfo.carinfo.cardetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.Service
import com.example.dataclasses.databinding.FragmentCarSpecificListBinding
import customer.homepage.clientinfo.carinfo.cardetail.placeholder.CarSpecificServiceViewModel
import customer.homepage.service.detail.ServiceDetailActivity

/**
 * A fragment representing a list of Items.
 */
class CarSpecificServiceFragment : Fragment() {

    private var columnCount = 1
    private lateinit var car : Car
    private lateinit var binding: FragmentCarSpecificListBinding
    private lateinit var adapter : CarSpecificServiceRecyclerViewAdapter
    private lateinit var serviceViewModel : CarSpecificServiceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FRAGMENT_SERVICE_LIST:","CREATED SERVICE LIST FOR CAR")
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            car = it.getParcelable(CAR_PARAM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_car_specific_list,container,false)

        serviceViewModel = ViewModelProvider(this).get(CarSpecificServiceViewModel::class.java)
        val layoutManager = LinearLayoutManager(this.requireContext())


        serviceViewModel.getServiceListLiveData().observe(viewLifecycleOwner, Observer { serviceList ->
            adapter = CarSpecificServiceRecyclerViewAdapter(serviceList, car, CarSpecificServiceClickListener { serviceStringPair ->
                val serviceItem = serviceStringPair.first
                when(serviceStringPair.second){
                    "payButton" -> {
//                        navigateToMomoPayment(serviceItem)
                        Log.d("CAR_SPECIFIC","Pay Button Clicked")
                    }
                    "detailButton" ->{
                        Log.d("CAR_SPECIFIC","detailButton Clicked")
                        navigateToServiceDetailFragment(serviceItem)
                    }
                    "card" ->{
                        Log.d("CAR_SPECIFIC","Card Clicked")
                    }
                    else->{
                        Log.d("CAR_SPECIFIC","Something else Clicked")
                    }
                }
//                Toast.makeText(this.requireContext(), "$it is clicked", Toast.LENGTH_SHORT).show()
//            //TODO: REMOVE THIS LINE AFTER TESTING MOMO
////                navigateToMomoPayment()
            },serviceViewModel)
            binding.list.adapter = adapter
            binding.list.layoutManager = layoutManager
        })
        binding.lifecycleOwner = this
        return binding.root
    }
    private fun navigateToServiceDetailFragment(service : Service){
        val intent = Intent(this.activity,ServiceDetailActivity:: class.java)
        intent.putExtra("service",service)
        intent.putExtra("carRegNum",car.registrationPlate)
        startActivity(intent)
    }
    private fun setUpRecyclerView(){
//        Log.d("Get Service List Of Car ", currentUser!!.email!!)
//        val carListQuery : Query = db.collection("Car")
//            .whereEqualTo("ownerID",currentUser.email)
//
//        val options = FirestoreRecyclerOptions.Builder<Car>()
//            .setQuery(carListQuery, Car::class.java)
//            .build()
//        // TODO: Connect Firebase Adapter to Recycler View
//        adapter = CarListRecyclerViewAdapter(options, CarClickListener { car->
//            navigateToCarDetailActivity(car)
//        })
//        val layoutManager = LinearLayoutManager(this)
//
//        binding.carRecyclerView.adapter = adapter
//        binding.carRecyclerView.layoutManager = layoutManager
    }


    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val CAR_PARAM = "car"
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(car: Car,columnCount: Int) =
            CarSpecificServiceFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putParcelable(CAR_PARAM, car)
                }
            }
    }
}