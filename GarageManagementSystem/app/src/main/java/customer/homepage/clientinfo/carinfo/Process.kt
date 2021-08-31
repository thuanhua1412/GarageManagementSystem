package customer.homepage.clientinfo.carinfo//package Info
//
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.ListView
//import android.widget.TextView
//import com.example.dataclasses.R
//
//class Process : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_process)
//        val listView = findViewById<ListView>(R.id.CarInProcess)
//        listView.adapter = CarProcessAdapter(this)
//    }
//    private class CarProcessAdapter(context: Context): BaseAdapter()
//    {
//        private val mContext: Context
//        private val test = arrayListOf<String>( "12345","23456","34567","45678")
//
//        init{
//            mContext = context
//        }
//        override fun getCount(): Int {
//
//            return 4
//        }
//        override fun getItemId(position: Int): Long {
//
//            return position.toLong()
//        }
//        override fun getItem(position: Int): Any {
//
//            return "Test"
//        }
//        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
//
//            val layoutInflater = LayoutInflater.from(mContext)
//            val rowMain = layoutInflater.inflate(R.layout.car_service_item,viewGroup,false)
//
//            val regisplate = rowMain.findViewById<TextView>(R.id.regisplate)
//            regisplate.text = test.get(position)
//            val service = rowMain.findViewById<TextView>(R.id.service)
//            service.text = test.get(position)
//            val place = rowMain.findViewById<TextView>(R.id.place)
//            place.text = test.get(position)
//            val timestart = rowMain.findViewById<TextView>(R.id.timestart)
//            timestart.text = test.get(position)
//            val describe = rowMain.findViewById<TextView>(R.id.describe)
//            describe.text = test.get(position)
//            return rowMain
//        }
//    }
//}