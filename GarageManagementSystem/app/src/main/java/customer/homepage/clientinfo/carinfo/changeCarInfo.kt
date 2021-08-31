package customer.homepage.clientinfo.carinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.dataclasses.R
import customer.homepage.clientinfo.carinfo.cardetail.CarInfoActivity

class changeCarInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_car_info)
        val changeCarInfoBtn = findViewById<Button>(R.id.button3)
        changeCarInfoBtn.setOnClickListener{
            val intent = Intent (this, CarInfoActivity::class.java)
            startActivity(intent)
        }
    }
}