//package com.example.dataclasses
//
//import android.os.Bundle
//import androidx.appcompat.app.ActionBar
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.snackbar.Snackbar
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import customer.homepage.home.HomeFragment
//
//class AppCompatActivityWithToolBar : AppCompatActivity() {
//    private lateinit var toolbar: Toolbar
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_app_compat_with_tool_bar)
//        toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        var actionbar: ActionBar? = supportActionBar
//        val backArrowIcon = R.drawable.ic_baseline_arrow_back_24
//        actionbar?.setDisplayHomeAsUpEnabled(true)
//        actionbar?.setDisplayShowHomeEnabled(true)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, HomeFragment()).commit()
//        }
////        toolbar.setOnMenuItemClickListener{
////            when (it.itemId){
////                R.id.notification_menu_item -> {
////                    openNotificationActivity()
////                    return@setOnMenuItemClickListener true
////                }
////
////                else -> return@setOnMenuItemClickListener false
////            }
////        }
////        var bellIcon = this.findViewById<Button>(R.id.notification_menu_item)
////        bellIcon?.setOnClickListener{
////            openNotificationActivity()
////        }
//    }
//    }
//}