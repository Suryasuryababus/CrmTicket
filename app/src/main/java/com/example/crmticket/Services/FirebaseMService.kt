package com.example.crmticket.Services

import android.app.*
import android.content.Context
import android.content.Intent
import android.drm.DrmStore
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.crmticket.Fullscreennotification
import com.example.crmticket.R
import com.example.crmticket.ServiceDataHelper
import com.example.crmticket.activity.MainActivity
import com.example.crmticket.activity.Tickets

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zoho.crm.sdk.android.api.handler.RecordsCallback
import com.zoho.crm.sdk.android.api.response.APIResponse
import com.zoho.crm.sdk.android.authorization.ZCRMSDKClient
import com.zoho.crm.sdk.android.common.CommonUtil
import com.zoho.crm.sdk.android.configuration.ZCRMSDKConfigs
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import com.zoho.crm.sdk.android.exception.ZCRMException
import com.zoho.crm.sdk.android.setup.sdkUtil.ZCRMSDKUtil
import java.util.logging.Level
import kotlin.random.Random

class FirebaseMService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.println(Log.INFO,"fcm Message",p0.data["id"]!!)


        ServiceDataHelper.setId(p0.data["id"]!!.toLong())

        val accept =  Intent(applicationContext, AcceptService::class.java)
        accept.putExtra("id",p0.data["id"]!!.toLong())
        val acceptpendingIntent = PendingIntent.getService(applicationContext,0,accept,PendingIntent.FLAG_UPDATE_CURRENT)

        val reject =  Intent(applicationContext, RejectService::class.java)
        reject.putExtra("id",p0.data["id"]!!.toLong())
        val rejectpendingIntent = PendingIntent.getService(applicationContext,0,reject,PendingIntent.FLAG_UPDATE_CURRENT)
        var actionAccept = Notification.Action.Builder(Icon.createWithResource(applicationContext,R.drawable.accept),"Accept",acceptpendingIntent)
        var actionReject = Notification.Action.Builder(Icon.createWithResource(applicationContext,R.drawable.reject),"Reject",rejectpendingIntent)

         val builder = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_assignment_turned_in_black_18dp)
            .setContentTitle("CrmTicket")
            .setContentText(p0.data["title"])
            .addAction(actionAccept.build())
            .addAction(actionReject.build())
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder)




    }

    override fun onNewToken(p0: String) {
        Log.d(TAG, "Refreshed token: $p0")
        val sdkConfigs = ZCRMSDKConfigs()
        sdkConfigs.setClientDetails("1000.3YCSFRTKBHEU229426T3NIXHLQD9CH", "f57458a4bed20c18eda631e31a32cf64e80ecf96ea")
        sdkConfigs.setClientDetails("1000.PJ40TGO181FG314104GCMED469UK96", "b0c6eef2272dc313375ffcf4b38d517d997d9d6b6a")
        sdkConfigs.appType = CommonUtil.AppType.ZCRM
        sdkConfigs.oauthScopes = "Aaaserver.profile.Read,ZohoCRM.settings.ALL,ZohoCRM.modules.ALL,ZohoCRM.users.ALL,ZohoCRM.org.ALL,ZohoCRM.settings.stages.ALL,ZohoCRM.modules.emails.READ,ZohoCRM.Contacts.send_mail.CREATE,ZohoCRM.modules.emails.READ,ZohoCRM.settings.org_emails.ALL,profile.orguserphoto.READ,profile.orguserphoto.UPDATE,profile.orguserphoto.READ,ZohoCRM.settings.variable_groups.ALL,ZohoCRM.settings.variables.ALL,ZohoCRM.signals.ALL,ZohoCRM.settings.extensions.ALL,ZohoCRM.uns.READ,ZohoCRM.uns.ALL,ZohoCRM.quotes.send_mail.CREATE"
        sdkConfigs.oauthScopes = "ZohoCRM.settings.all,ZohoCRM.Modules.ALL,ZohoCRM.org.ALL,ZohoCRM.users.ALL"
        sdkConfigs.setLoggingPreferences(Level.ALL, true)
        val mobileSDKClient = ZCRMSDKClient.getInstance(this)
        mobileSDKClient.init(sdkConfigs, object : ZCRMSDKClient.Companion.ZCRMInitCallback {
            override fun onFailed(ex: ZCRMException) {
                Log.println(Log.ERROR,"Login_Failure","Error in zoho login Accept service")
            }

            override fun onSuccess() {
                ZCRMSDKUtil.getModuleDelegate("Agents").getRecord(4065016000000657289, object:
                    RecordsCallback<APIResponse, ZCRMRecord, Boolean> {
                    override fun fromCache(
                        response: APIResponse?,
                        zcrmentity: ZCRMRecord?,
                        waitForServer: Boolean
                    ) {
                    }
                    override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                        zcrmentity!!.setFieldValue("Agentfcmtoken", p0)
                        zcrmentity!!.update(object : RecordsCallback<APIResponse, ZCRMRecord, Boolean> {
                            override fun fromCache(
                                response: APIResponse?,
                                zcrmentity: ZCRMRecord?,
                                waitForServer: Boolean
                            ) { }

                            override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                                println(">> fcm token updated")

                            }


                            override fun failed(exception: ZCRMException) {
                                println(">>fcm token update failed $exception")

                            }

                        })
                    }

                    override fun failed(exception: ZCRMException) {

                    }

                })

            }
        })



    }

    companion object {
        private val TAG = "service"
        private val CHANNEL_ID = "ButterFly_Id1"
        private val CHANNEL_Name = "ButterFly"
        private val CHANNEL_DESCRIPTION = "ButterFly Notification"
    }
}
