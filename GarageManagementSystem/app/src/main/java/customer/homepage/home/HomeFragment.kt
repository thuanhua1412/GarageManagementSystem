package customer.homepage.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dataclasses.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val carInsurance = view.findViewById<Button>(R.id.car_insurance_button)
        val Guide = view.findViewById<Button>(R.id.guide_line_homescreen)
        val Sale = view.findViewById<Button>(R.id.sale_button)
        val Timeline = view.findViewById<Button>(R.id.garage_timeline)
        val infoService = view.findViewById<Button>(R.id.service_info_button)
        val Franchise = view.findViewById<Button>(R.id.franchise_button)
        carInsurance.setOnClickListener{
            val intent = Intent (getActivity(), InsuranceActivity::class.java)
            startActivity(intent)
        }
        Guide.setOnClickListener{
            val intent = Intent (getActivity(), GuideActivity::class.java)
            startActivity(intent)
        }
        Sale.setOnClickListener{
            val intent = Intent (getActivity(), SaleActivity::class.java)
            startActivity(intent)
        }
        Timeline.setOnClickListener{

        }
        infoService.setOnClickListener{
            val intent = Intent (getActivity(), InfoServiceActivity::class.java)
            startActivity(intent)
        }

        Franchise.setOnClickListener{
            val intent = Intent (getActivity(), FranchiseActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}