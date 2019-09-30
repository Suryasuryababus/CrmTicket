package com.example.crmticket.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crmticket.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.zoho.crm.sdk.android.api.handler.DataCallback
import com.zoho.crm.sdk.android.api.handler.RecordsCallback
import com.zoho.crm.sdk.android.api.response.APIResponse
import com.zoho.crm.sdk.android.api.response.BulkAPIResponse
import com.zoho.crm.sdk.android.authorization.ZCRMSDKClient
import com.zoho.crm.sdk.android.common.CommonUtil
import com.zoho.crm.sdk.android.configuration.ZCRMSDKConfigs
import com.zoho.crm.sdk.android.crud.ZCRMField
import com.zoho.crm.sdk.android.crud.ZCRMModule
import com.zoho.crm.sdk.android.crud.ZCRMQuery
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import com.zoho.crm.sdk.android.exception.ZCRMException
import com.zoho.crm.sdk.android.exception.ZCRMLogger
import com.zoho.crm.sdk.android.setup.sdkUtil.ZCRMSDKUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Level
import android.media.RingtoneManager




class MainActivity : AppCompatActivity()  {


    var lfields : List<ZCRMField>? = null
    var lrecords : List<ZCRMRecord>? = null
    var updated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (logo_anim.drawable as AnimatedVectorDrawable).start()
        Handler().postDelayed({
           logo_anim.setImageResource(R.drawable.icon)
        }, 2000)

        fetchTicketData()
        registerInFirebase()
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_Name, NotificationManager.IMPORTANCE_HIGH)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(true)
            channel.enableLights(true)
            var a = AudioAttributes.Builder()
            channel.setSound(alarmSound,a.build())
            channel.description = CHANNEL_DESCRIPTION
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

}


    fun fetchTicketData(){
        val sdkConfigs = ZCRMSDKConfigs()
        sdkConfigs.setClientDetails("1000.3YCSFRTKBHEU229426T3NIXHLQD9CH", "f57458a4bed20c18eda631e31a32cf64e80ecf96ea")
        sdkConfigs.setClientDetails("1000.PJ40TGO181FG314104GCMED469UK96", "b0c6eef2272dc313375ffcf4b38d517d997d9d6b6a")
        sdkConfigs.appType = CommonUtil.AppType.ZCRM
        sdkConfigs.oauthScopes = "Aaaserver.profile.Read,ZohoCRM.settings.ALL,ZohoCRM.modules.ALL,ZohoCRM.users.ALL,ZohoCRM.org.ALL,ZohoCRM.settings.stages.ALL,ZohoCRM.modules.emails.READ,ZohoCRM.Contacts.send_mail.CREATE,ZohoCRM.modules.emails.READ,ZohoCRM.settings.org_emails.ALL,profile.orguserphoto.READ,profile.orguserphoto.UPDATE,profile.orguserphoto.READ,ZohoCRM.settings.variable_groups.ALL,ZohoCRM.settings.variables.ALL,ZohoCRM.signals.ALL,ZohoCRM.settings.extensions.ALL,ZohoCRM.uns.READ,ZohoCRM.uns.ALL,ZohoCRM.quotes.send_mail.CREATE"
        sdkConfigs.oauthScopes = "ZohoCRM.settings.all,ZohoCRM.Modules.ALL,ZohoCRM.org.ALL,ZohoCRM.users.ALL"
        sdkConfigs.setLoggingPreferences(Level.ALL, true)
        var tdh = TicketDatahandler()
        val mobileSDKClient = ZCRMSDKClient.getInstance(this)
        mobileSDKClient.init(sdkConfigs, object : ZCRMSDKClient.Companion.ZCRMInitCallback {
            override fun onSuccess() {
                ZCRMLogger.logInfo(">> Login Success.")
                Log.println(Log.INFO,"login","Login-Success")
                ZCRMSDKUtil.getModule("Ticket", object : DataCallback<APIResponse,ZCRMModule>{
                    override fun completed(response: APIResponse, zcrmentity: ZCRMModule) {
                        val params: ZCRMQuery.Companion.GetRecordParams = ZCRMQuery.Companion.GetRecordParams()
                        params.page = 1
                        params.perPage = 200
                        zcrmentity.getRecords(params, object : RecordsCallback<BulkAPIResponse, List<ZCRMRecord>, Boolean> {
                            override fun failed(exception: ZCRMException) {
                                runOnUiThread {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            Error::class.java
                                        ))
                                    finish()
                                }

                            }

                            override fun fromCache(
                                response: BulkAPIResponse?,
                                zcrmentity: List<ZCRMRecord>?,
                                waitForServer: Boolean
                            ) {

                            }

                            override fun fromServer(response: BulkAPIResponse?, records: List<ZCRMRecord>?) {
                                for(rec:ZCRMRecord in records!!.iterator()){

                                    Log.println(Log.INFO,"field",rec.getFieldValueAsString("Name")!!)
                                }
                                zcrmentity.getFields(object :DataCallback<BulkAPIResponse,List<ZCRMField>>{
                                    override fun completed(response: BulkAPIResponse, fields: List<ZCRMField>) {

                                        tdh.lfields=fields
                                        tdh.lrecords=records
                                        TicketDatahandler.setDataObject(tdh)
                                        startActivity(Intent(applicationContext,Tickets::class.java))
                                        finish()
                                    }

                                    override fun failed(exception: ZCRMException) {
                                        runOnUiThread {
                                            startActivity(
                                                Intent(
                                                    this@MainActivity,
                                                    Error::class.java
                                                ))
                                            finish()
                                        }
                                        Toast.makeText(applicationContext,"something went wrong",Toast.LENGTH_LONG).show()
                                    }

                                })

                            }

                        })
                    }
                    override fun failed(exception: ZCRMException) {
                        runOnUiThread {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    Error::class.java
                                ))
                            finish()
                        }
                    }

                })
            }

            override fun onFailed(e: ZCRMException) {
                ZCRMLogger.logError(">> Login Failed ::: \$ex")
                runOnUiThread {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            Error::class.java
                        ))
                    finish()
                }
            }
        })

    }
    fun registerInFirebase(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token

                Log.println(Log.INFO,">> token ", token)
                //Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
            })

    }

    companion object {


        private val TAG = "mainactivity"
        private val CHANNEL_ID = "ButterFly_Id1"
        private val CHANNEL_Name = "ButterFly"
        private val CHANNEL_DESCRIPTION = "ButterFly Notification"
    }
}
