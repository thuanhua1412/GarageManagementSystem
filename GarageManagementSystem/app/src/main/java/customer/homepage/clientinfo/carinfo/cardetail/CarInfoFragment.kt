package customer.homepage.clientinfo.carinfo.cardetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.databinding.FragmentCarInfoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CAR_PARAM = "car"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CarInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var carPassed: Car? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCarInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this.requireContext(), "FRAGMENT HAHA", Toast.LENGTH_SHORT).show()
        Log.d("FRAGMENT_CARINFO:","CREATED CAR INFO FERAG")
        arguments?.let {
            carPassed = it.get(CAR_PARAM) as Car
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_car_info,container,false)

        binding.car = carPassed
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CarInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Car, param2: String) =
            CarInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CAR_PARAM, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}