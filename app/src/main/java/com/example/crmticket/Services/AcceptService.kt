package com.example.crmticket.Services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.crmticket.ServiceDataHelper
import com.example.crmticket.TicketDatahandler
import com.example.crmticket.activity.Accepted
import com.example.crmticket.activity.MainActivity
import com.example.crmticket.activity.TicketDetails
import com.zoho.crm.sdk.android.api.handler.RecordsCallback
import com.zoho.crm.sdk.android.api.response.APIResponse
import com.zoho.crm.sdk.android.authorization.ZCRMSDKClient
import com.zoho.crm.sdk.android.common.CommonUtil
import com.zoho.crm.sdk.android.configuration.ZCRMSDKConfigs
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import com.zoho.crm.sdk.android.exception.ZCRMException
import com.zoho.crm.sdk.android.setup.sdkUtil.ZCRMSDKUtil
import java.util.logging.Level


class AcceptService : Service() {
    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    fun showtoast(msg: String) {

        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
        if(intent!=null) {
            startActivity(Intent(this, Accepted::class.java).apply {
                putExtra("id", intent!!.getLongExtra("id", 1111))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }


        return super.onStartCommand(intent, flags, startId)
    }
}
