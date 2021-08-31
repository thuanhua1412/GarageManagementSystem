package customer.homepage.service

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.databinding.FragmentServiceHostBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import customer.homepage.clientinfo.carinfo.cardetail.CarInfoFragment
import customer.homepage.clientinfo.carinfo.cardetail.CarSpecificServiceFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

const val BOOKED_BY_CLIENT = "This service is from CLIENT"
const val SUGGESTED_BY_CENTER = "This service source is recommended by Center"
/**
 * A simple [Fragment] subclass.
 * Use the [ServiceHostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceHostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pager2: ViewPager2
    private lateinit var tabLayout:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d("HOST_FRAMENT_SERV:","ON CREATE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pager2 = view.findViewById(R.id.pager)
        var serviceAdapter  = ServicePagerAdapter(this)
        pager2.adapter = serviceAdapter
        tabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, pager2) { tab, position ->
            when(position){
                0-> tab.text = "Service Booked"
                1-> tab.text = "Maintenace Suggestion"
            }
        }.attach()
        Log.d("HOST_FRAMENT_SERV:","ON VIEW CREATED")
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
            ServiceHostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private inner class ServicePagerAdapter(fa: Fragment) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ServiceFragment.newInstance(BOOKED_BY_CLIENT, "Not Used")
                }
                1 -> {
                    ServiceFragment.newInstance(SUGGESTED_BY_CENTER,"Not Used")
                }
                else -> {
                    ServiceFragment.newInstance(BOOKED_BY_CLIENT,"Not Used")
                }
            }
        }

    }
}