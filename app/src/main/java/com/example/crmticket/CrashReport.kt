package com.example.crmticket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crmticket.Adopter.CrashReportAdopter
import com.example.crmticket.Adopter.TicketAdopterView
import kotlinx.android.synthetic.main.activity_ticket_details.*

class CrashReport : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_report)
        val av = CrashReportAdopter(applicationContext)
        rv.adapter = av
        rv.layoutManager = LinearLayoutManager(applicationContext)
    }
}
