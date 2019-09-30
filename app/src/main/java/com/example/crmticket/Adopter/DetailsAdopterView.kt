package com.example.crmticket.Adopter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crmticket.R
import com.zoho.crm.sdk.android.crud.ZCRMField
import com.zoho.crm.sdk.android.crud.ZCRMRecord



class DetailsAdopterView(
    private val context: Context,
    private var fields: MutableList<ZCRMField>,
    private val data: ZCRMRecord
) : RecyclerView.Adapter<DetailsAdopterView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticketdetailslistview, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.field.text = fields[position].displayName + ":"
            if (!data.getFieldValueAsString(fields[position].apiName).isNullOrEmpty()) {
                holder.data.text = data.getFieldValueAsString(fields[position].apiName)
            } else {
                holder.data.text = "-"
                holder.data.setTextColor(Color.RED)
            }

        } catch (e: Exception) {
            Log.println(Log.ERROR, "ERROR", e.toString())


        }


    }

    override fun getItemCount(): Int {
        return fields.size - 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var field: TextView
        internal var data: TextView
        internal var rl: RelativeLayout

        init {
            field = itemView.findViewById(R.id.FieldName)
            data = itemView.findViewById(R.id.FieldData)
            rl = itemView.findViewById(R.id.rllv)
        }
    }
}
