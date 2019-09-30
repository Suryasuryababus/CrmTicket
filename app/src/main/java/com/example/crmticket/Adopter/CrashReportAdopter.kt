package com.example.crmticket.Adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crmticket.Loghelper
import com.example.crmticket.R
import com.example.crmticket.crashReportDatabase

class CrashReportAdopter () :RecyclerView.Adapter<CrashReportAdopter.ViewHolder>(){
    private var logs:List<Loghelper>?=null
    constructor(context: Context) : this() {
        logs = crashReportDatabase(context).getLogs()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.crash_report_adopter, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

    return logs!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text =logs!![position].date
        holder.log.text= logs!![position].log

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var date: TextView = itemView.findViewById(R.id.crash_report_date)
        internal var log: TextView = itemView.findViewById(R.id.crash_log)

    }
}
