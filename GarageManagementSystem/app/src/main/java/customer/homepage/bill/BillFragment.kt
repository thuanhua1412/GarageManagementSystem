package customer.homepage.bill

//import customer.homepage.ServiceCartViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.dataclasses.R
import com.example.dataclasses.Service
import com.google.android.material.bottomnavigation.BottomNavigationView
import customer.homepage.ServiceCartViewModel
import java.sql.Timestamp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BillFragment : BillRecyclerViewAdapter.ServiceItemClickListener,Fragment() {
    private val serviceViewModel: ServiceCartViewModel by activityViewModels<ServiceCartViewModel>();
    // Things needed for recycler View
    private lateinit var unpaidServiceList : LiveData<ArrayList<Service>>;
    private lateinit var adapter : BillRecyclerViewAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        bottomNavBar?.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val bottomNavBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        bottomNavBar?.visibility = View.VISIBLE
    }
    companion object {
        fun newInstance(): BillFragment = BillFragment()
    }

    override fun onServiceItemClicked(position: Int) {
        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = unpaidServiceList.value?.get(position)
        if (clickedItem != null) {
            clickedItem.description = "Clicked"
        }
        adapter.notifyItemChanged(position)
    }
}

