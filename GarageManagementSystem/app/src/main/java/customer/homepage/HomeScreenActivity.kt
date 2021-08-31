package customer.homepage

import ServiceFragment
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appinfo.TermOfServicePage
import com.example.dataclasses.R
import com.example.dataclasses.Service
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import customer.homepage.bill.BillFragment
import customer.homepage.booking.BookingFragment
import customer.homepage.clientinfo.ClientInfoFragment
import customer.homepage.home.HomeFragment
import customer.homepage.service.ServiceHostFragment
import login.LoginActivity
import notification.ActivityNotificationPage


class HomeScreenActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigation: BottomNavigationView

    // TODO: Pass the user if through the activities and viewModel to create consistent user profile
    //  --> Check if the user exist on firestore (Yes? Load its data into ViewModel : Create new user
//    val userID = user.uid
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        // Assign a viewModel to the Activity:
        createNotificationChannel()
        val viewModel: ServiceCartViewModel by viewModels()
        viewModel.getServiceListLiveData().observe(this, Observer {
            createAndSendNotification()
        })

        // Set up a toolbar with working functionality
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        val backArrowIcon = R.drawable.ic_baseline_arrow_back_24
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        // Setup Bottom Navigation Bar
        bottomNavigation = findViewById(R.id.bottom_nav_bar)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            Toast.makeText(this, "NULL USER. SCARY", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(
                "USER INFO:",
                "${currentUser.uid} + ${currentUser.displayName} + ${currentUser.email} + ${currentUser.phoneNumber} "
            )
        }
    }

    //private  var previousPage = R.id.navigation_home
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    toolbar.title = "Trang chủ"
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_booking -> {
                    toolbar.title = "Đặt lịch"
                    val bookingFragment = BookingFragment.newInstance()
                    openFragment(bookingFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_services -> {
                    toolbar.title = "Trạng thái dịch vụ"
                    val serviceFragment = ServiceHostFragment.newInstance("Not Used", "Not Used")
                    openFragment(serviceFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_client -> {
                    toolbar.title = "Thông tin cá nhân"
                    val clientInfoFragment = ClientInfoFragment.newInstance()
                    openFragment(clientInfoFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun navigateToServiceList() {
        toolbar.title = "Trạng thái dịch vụ"
        val serviceFragment = ServiceHostFragment.newInstance("Not Used","Not Used")
        openFragment(serviceFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun navigateHome() {
        toolbar.title = "Trang chủ"
        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)
    }

    private fun navigateBooking() {
        toolbar.title = "Đặt lịch"
        val bookingFragment = BookingFragment.newInstance()
        openFragment(bookingFragment)
    }

    private fun navigateServiceState() {
        toolbar.title = "Trạng thái dịch vụ"
        val serviceFragment = ServiceFragment.newInstance("No use", "Nouser")
        openFragment(serviceFragment)
    }

    private fun navigateClientInfo() {
        toolbar.title = "Thông tin cá nhân"
        val clientInfoFragment = ClientInfoFragment.newInstance()
        openFragment(clientInfoFragment)
    }

    // Top toolbar code
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_bar, menu)
        return true
    }

    // Check what item on the action bar is pressed and assign an Listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.notification_menu_item -> {
                Toast.makeText(this, "notification cliekc", Toast.LENGTH_SHORT).show()
                openNotificationActivity()
                return true
            }
            R.id.term_of_service_menu_item -> {
                openTermOfService()
                return true
            }
//            R.id.view_services_cart -> {
//                val billFragment = BillFragment.newInstance()
//                openFragment(billFragment)
//                return true
//            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openNotificationActivity() {
        val intent = Intent(this, ActivityNotificationPage::class.java)
        startActivity(intent)
    }

    private fun openTermOfService() {
        val intent = Intent(this, TermOfServicePage::class.java)
        startActivity(intent)
    }

    private fun getEmojiByUnicode(unicode: Int): String {
        return Character.toChars(unicode).toString()
    }

    override fun onBackPressed() {
        navigateHome()
//        Toast.makeText(
//            this,
//            "back_pressed with ${supportFragmentManager.backStackEntryCount}",
//            Toast.LENGTH_SHORT
//        ).show()
//
//        var fragmentCount = supportFragmentManager.backStackEntryCount
//        if (fragmentCount == 0) {
//            Toast.makeText(this, "stack_empty", Toast.LENGTH_SHORT).show()
//            var confirmLogoutDialog = AlertDialog.Builder(this)
//                .setMessage("Bạn thật sự muốn rời xa chúng tôi ư? " + ("\uD83E\uDD7A"))
//                .setCancelable(false)
//                .setPositiveButton("Đúng vậy") { dialogInterface, i -> navigateToLoginScreen() }
//                .setNegativeButton("Không. Lỡ tay", null)
//                .show()
//        } else {
//            // Set the correct fragment to the preious one
//            if (supportFragmentManager.backStackEntryCount >= 1) {
//                supportFragmentManager.popBackStack()
//                var selectedFragment: Fragment? = null
//                val fragments = supportFragmentManager.fragments
//                for (fragment in fragments) {
//                    if (fragment != null && fragment.isVisible) {
//                        selectedFragment = fragment
//                        break
//                    }
//                }
//                when (selectedFragment) {
//                    is HomeFragment -> {
//                        bottomNavigation.selectedItemId = R.id.navigation_home
//                    }
//                    is BookingFragment -> {
//                        bottomNavigation.selectedItemId = R.id.navigation_booking
//                    }
//                    is ClientInfoFragment -> {
//                        bottomNavigation.selectedItemId = R.id.navigation_client
//                    }
//                    is ServiceFragment -> {
//                        bottomNavigation.selectedItemId = R.id.navigation_services
//                    }
//                    else -> {
//                        Toast.makeText(this, "inner else", Toast.LENGTH_SHORT).show()
//                        super.onBackPressed()
//                    }
//                }
//            }
//            // Then reinflate the last fragment
//            super.onBackPressed()
//        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

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
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        Log.d("NOTIFICATION CHANNEL", "Successfully Created a chanel")
    }

    private fun createAndSendNotification() {
        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, ServiceFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        var builder = NotificationCompat.Builder(this, "TEST_CHANNEL_ID")
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
}