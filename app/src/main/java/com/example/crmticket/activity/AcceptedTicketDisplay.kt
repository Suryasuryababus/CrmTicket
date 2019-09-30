package com.example.crmticket.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crmticket.R
import kotlinx.android.synthetic.main.activity_accepted_ticket_display.*

class AcceptedTicketDisplay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted_ticket_display)
        parentL.setOnClickListener {
            startActivity(Intent(this,Tickets::class.java))
            finish()
        }
    }


}
