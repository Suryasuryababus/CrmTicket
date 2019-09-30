package com.example.crmticket.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crmticket.Adopter.DetailsAdopterView
import com.example.crmticket.R
import com.example.crmticket.TicketDatahandler
import com.zoho.crm.sdk.android.crud.ZCRMField
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import kotlinx.android.synthetic.main.activity_ticket_details.*

class TicketDetails : AppCompatActivity() {
    var id:Long?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_details)
        val tdh: TicketDatahandler? = TicketDatahandler.getInstance()
        var fileds:List<ZCRMField>? = tdh!!.getFields()
        var records:List<ZCRMRecord>? = tdh!!.getRecords()
        var intent:Intent = intent
        var record:ZCRMRecord?=null
        id =  intent.getLongExtra("id",1111111)
        for(lrecord:ZCRMRecord in records!!){
            if(lrecord.id ==id ){
                record = lrecord
                break
            }
        }
        if(record!=null) {
            val av = DetailsAdopterView(this, fileds!! as MutableList<ZCRMField>, record)
            rv.adapter = av
            rv.layoutManager = LinearLayoutManager(applicationContext)!!
        }else{

        }

    }


fun backButtonPressed(v:View){
   super.onBackPressed()
    finishAfterTransition()
}
    fun sendButtonPressed(V:View){
        val app = Intent(Intent.ACTION_EDIT).apply {
            // The intent does not have a URI, so declare the "text/plain" MIME type
            type = "Zcrm/ticket"
            putExtra("id",id)

            // You can also attach multiple items by passing an ArrayList of Uris
        }
        startActivity(app)
    }
}
