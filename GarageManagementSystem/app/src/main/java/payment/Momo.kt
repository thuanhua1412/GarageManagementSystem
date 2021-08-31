package payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.dataclasses.R

class Momo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_momo)
        val button = findViewById<Button>(R.id.ratebutton)
        button.setOnClickListener {
            val intent = Intent (this, Review_rate::class.java)

            startActivity(intent)
        }
    }
}