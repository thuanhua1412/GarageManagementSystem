package customer.homepage.clientinfo

import android.content.Context
import customer.homepage.clientinfo.carinfo.CarListActivity
import customer.homepage.clientinfo.customerinfo.EditInfoActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.dataclasses.R
import com.example.dataclasses.databinding.FragmentClientInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import login.LoginActivity
import repository.Repository


class ClientInfoFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore

    private lateinit var binding : FragmentClientInfoBinding

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_client_info,container,false)

        //Set up UI:
        binding.viewCarListButton.setOnClickListener {
            navigateToCarListActivity()
        }

        binding.editPersonalInfoButton.setOnClickListener {
            navigateToEditInfoActivity()
        }

        // Bind Customer
//        binding.customer = Repository.customer
        Log.d("REPO_CLIENT:","${Repository.customer}")
        binding.signOutButton.setOnClickListener {
            signOut()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d("FRAGMENT_LIFECYCLE:","I AM STARTED")
    }

    override fun onResume() {
        super.onResume()
        Log.d("FRAGMENT_LIFECYCLE:","I AM RESUMED")
        binding.customer = Repository.customer


    }
    private fun signOut(){
        auth.signOut()
        val intent = Intent(this.requireContext(),LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val userName = view.findViewById<TextView>(R.id.textView13)
//        val birth = view.findViewById<TextView>(R.id.textView12)
//        val Phonenumber = view.findViewById<TextView>(R.id.textView14)
//
//        val docRef = db.collection("Customer").document("LFEQqNoNVPBFguRwg5wH")
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d("exit", "DocumentSnapshot data: ${document.data}")
//                    userName.text = document.getString("name")
//                    birth.text = document.getString("dateOfBirth")
//                    Phonenumber.text = document.getString("phone")
//                } else {
//                    Log.d("noexit", "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->

//
//        val changeInfoBtn = view.findViewById<Button>(R.id.button5)
//        changeInfoBtn.setOnClickListener{
//            val intent = Intent (getActivity(), changeInfo::class.java)
//            startActivity(intent)
//        }
//        val viewCarListBtn = view.findViewById<Button>(R.id.button8)
//        viewCarListBtn.setOnClickListener{
//            val intent = Intent (getActivity(), CarListActivity::class.java)
//            startActivity(intent)
//        }
         binding.customer = Repository.customer
    }
    private fun navigateToCarListActivity(){
        val intent = Intent (activity, CarListActivity::class.java)
            startActivity(intent)
    }
    private fun navigateToEditInfoActivity(){
        val intent = Intent (activity, EditInfoActivity::class.java)
        startActivity(intent)
    }
    companion object {
        fun newInstance(): ClientInfoFragment = ClientInfoFragment()
    }
}