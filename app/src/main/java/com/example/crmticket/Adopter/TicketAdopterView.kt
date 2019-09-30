package com.example.crmticket.Adopter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.crmticket.R
import com.example.crmticket.activity.TicketDetails
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TicketAdopterView(
    private val context: Context,
    private val data: List<ZCRMRecord>
) : RecyclerView.Adapter<TicketAdopterView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_list__view, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var time = data[position].createdTime
        Log.println(Log.INFO,"time",time)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time!!.replace("T"," "))
        val dif = Calendar.getInstance().time.time - date.time
        holder.title.text = data[position].getFieldValueAsString("Name")
        if(data[position].getFieldValue("Status")=="Accepted"){
            holder.statuscircle.setImageResource(R.drawable.green_circle)
        }else if(data[position].getFieldValue("Status")=="Rejected"){
            holder.statuscircle.setImageResource(R.drawable.red_circle)
        }else{
            holder.statuscircle.setImageResource(R.drawable.empty_circle)
        }
        holder.ticket.setOnClickListener{
            val intent = Intent(context, TicketDetails::class.java)
            var ac = context as Activity

            var aoc = ActivityOptionsCompat.makeSceneTransitionAnimation(ac,ac.findViewById<View>(R.id.backButton),"backbutton")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id",data[position].id)
            context.startActivity(intent,aoc.toBundle())
        }
        var days = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS).toString()
        if(days == "1") {
            days= "$days day ago"

        }else if(days == "0"){
            days= "today"
        }else {
            days= "$days days ago"
        }
        holder.time.text = days
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView
        internal var ticket:LinearLayout
        internal var statuscircle:ImageView
        internal var time:TextView
        init {
            title = itemView.findViewById(R.id.title)
            ticket = itemView.findViewById(R.id.ticket)
            statuscircle = itemView.findViewById(R.id.StatusCircle)
            time = itemView.findViewById(R.id.time)
        }
    }
}
