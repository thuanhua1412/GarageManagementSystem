package customer.homepage.booking

import UtilityClass
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dataclasses.*
import com.example.dataclasses.databinding.FragmentBookingBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import customer.homepage.HomeScreenActivity
import customer.homepage.ServiceCartViewModel
import customer.homepage.service.ServiceItemAdapter
import payment.PaymentActivity
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

//import payment.PaymentActivity

//TODO: Modify the FAB so after pushing the database we either
// : Notify the ViewModel to fetch from database again
// or We push the added service(s) directly in to the ViewModel --- Thinh on 5th, April - 2021


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingFragment : Fragment() {

    private lateinit var spinnerOptionViewModel: SpinnerOptionViewModel
    private val serviceViewModel: ServiceCartViewModel by activityViewModels<ServiceCartViewModel>();
    private lateinit var binding: FragmentBookingBinding

    private lateinit var fab: FloatingActionButton

    private val serviceItemList: ArrayList<ServiceItem> = ArrayList<ServiceItem>();
    private lateinit var availableServiceSpinnerAdapter: AvailableServiceSpinnerAdapter
    private lateinit var carSpinnerAdapter: CarSpinnerAdapter

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore

    private lateinit var pickedDateTime : Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false)

        spinnerOptionViewModel = ViewModelProvider(this).get(SpinnerOptionViewModel::class.java)
        Log.d("CREATED VIEWMODEL:", "Successfuly")

        binding.pickCarArriveTimeEditText.setOnClickListener {
            pickDateTime()
        }
        spinnerOptionViewModel.getCarListLiveData().observe(viewLifecycleOwner, Observer {
            carSpinnerAdapter = CarSpinnerAdapter(
                this.requireContext(),
                it
            )
            binding.pickCarSpinner.adapter = carSpinnerAdapter
            binding.pickCarSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val chosenCar: Car = parent.getItemAtPosition(position) as Car
                        Log.d("Serice chosen", "$chosenCar")
                        binding.pickedCar = chosenCar
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
        })
        spinnerOptionViewModel.getAvailableServiceLiveData().observe(viewLifecycleOwner, Observer {
            availableServiceSpinnerAdapter = AvailableServiceSpinnerAdapter(
                this.requireContext(), it
            )
            binding.pickServiceSpinner.adapter = availableServiceSpinnerAdapter
            binding.pickServiceSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val chosenService: ServiceItem = parent.getItemAtPosition(position) as ServiceItem
                        binding.pickedService = chosenService
                        Log.d("Serice chosen", "$chosenService")

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

        })
        return binding.root

    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                    binding.pickCarArriveTimeEditText.setText(pickedDateTime.time.toString())
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishFab.setOnClickListener {
            addChosenServices()
        }
    }

    private fun addChosenServices() {
        val chosenServices = availableServiceSpinnerAdapter.getSelectedServices()
        Log.d("CHOSEN_SERVICES:",chosenServices.toString())
        if(chosenServices.isEmpty()){
            Toast.makeText(this.context, "No service chosen", Toast.LENGTH_SHORT).show()
        }
        // Creat a bill containing all the services chosen
        val totalCost = chosenServices.sumBy {
            it.cost
        }
        val bill = Bill(timeCreated = System.currentTimeMillis().toString(),totalBill = totalCost)
        db.collection("Bill").add(bill)
            .addOnSuccessListener {

                for (serviceItem in chosenServices){
                    val service =  Service(name = serviceItem.name,
                        ownerID = currentUser.email,
                        carServiced = binding.pickedCar!!.registrationPlate,cartID = it.id,
                        cost = serviceItem.cost,timeLimit = serviceItem.timeLimit,
                        carArriveTime = binding.pickCarArriveTimeEditText.text.toString()
                    )
                    db.collection("Service").add(service)
                        .addOnSuccessListener {
                            Log.d("SERVICE_ADDED:","${service.name} by ${service.ownerID}")
                            Toast.makeText(this.requireContext(), "Successfully added the Selected Services", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("SERVICE_ADDED:",exception.toString())
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this.context, "Failed to create Bill. ${it.toString()}", Toast.LENGTH_SHORT).show()
            }
    }

//

    private fun requiredInformationFilled(): Boolean {
        //TODO: Get data from the views --> Create an Service object to store the created data--> Push it to firebase
        if (UtilityClass.checkEmptyString(binding.pickedCar!!.registrationPlate)) {
            Toast.makeText(this.context, "Chưa chọn biển xe. Kiểm tra lại", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (binding.pickedService!!.name.trim().isEmpty()) {
            Toast.makeText(this.context, "Chưa chọn dịch vụ", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (binding.pickCarArriveTimeEditText.text.toString().trim().isEmpty()) {
            Toast.makeText(this.context, "Chưa chọn thời gian nhận xe", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    companion object {
        fun newInstance(): BookingFragment = BookingFragment()
    }
}




//private fun addNewService() {
//        if (requiredInformationFilled()) {
//            // Push the service onto the Firebase FireStore
//            val serviceFromVies = Service(
//                ownerID = currentUser.email,
//                name = binding.pickedService!!.name,
//                carServiced = binding.pickedCar!!.registrationPlate,
//                description = if (binding.addtionalNotesEditText.text.toString().trim()
//                        .isEmpty()
//                ) "Không có mô tả thêm" else binding.addtionalNotesEditText.text.toString(),
//                status = "Đang chờ",
//                pickUpLocation = "Trung tâm",
//                paid = false,
//                reworkRequested = false,
//                timeLimit = binding.pickedService!!.timeLimit,
//                carArriveTime = binding.pickCarReturnTimeEditText.text.toString(),
//                cost = binding.pickedService!!.cost
//            )
//            serviceViewModel.addNewUnpaidServerToFirebase(serviceFromVies)
//            val intent = Intent(this.context, PaymentActivity::class.java)
//            startActivity(intent)
//        } else {
//            Toast.makeText(
//                this.context,
//                "Missing Some Information. Please Check again",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }