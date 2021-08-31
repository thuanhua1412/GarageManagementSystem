package notification

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dataclasses.Notification
import com.example.dataclasses.R
import payment.PaymentRecyclerViewAdapter

class ActivityNotificationPage : NotificationRecyclerViewAdapter.NotificationItemClickListener,
    AppCompatActivity() {
    private lateinit var notificationList: ArrayList<Notification>
    private lateinit var adapter: NotificationRecyclerViewAdapter
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_page)
        notificationList = fetchDataIntoNotificationList()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Get BACK ARROW
        var actionbar: ActionBar? = supportActionBar
        //val backArrowIcon = R.drawable.ic_baseline_arrow_back_24
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        toolbar.title = "Thông báo"
        val recyclerView = findViewById<RecyclerView>(R.id.notification_recyclerview)

        val layoutManager = LinearLayoutManager(this)
        adapter = NotificationRecyclerViewAdapter(notificationList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNotificationItemClicked(position: Int) {
        Toast.makeText(this, "Notification $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = notificationList[position]
        clickedItem.description = "Clicked"
        adapter.notifyItemChanged(position)
    }

    // TODO: Implement function to load data from firebase into the Notification Array List
    private fun fetchDataIntoNotificationList(): ArrayList<Notification> {
        var noticeList: ArrayList<Notification> = ArrayList<Notification>()
        noticeList.add(
            Notification(
                "We are opening at late hours",
                "Two more hours",
                "We appreciate the customer who waited for our service in hours but not getting" +
                        "serviced due to out of official working time. So we decided to extend the working " +
                        "hours for 2 more hours perday. Thank you for your patience.",
                "April 1st,2021"
            )
        )
        return noticeList
    }

}