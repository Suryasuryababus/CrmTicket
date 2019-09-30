package com.example.crmticket.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.crmticket.R
import com.example.crmticket.TicketDatahandler
import com.zoho.crm.sdk.android.api.handler.DataCallback
import com.zoho.crm.sdk.android.api.handler.RecordsCallback
import com.zoho.crm.sdk.android.api.response.APIResponse
import com.zoho.crm.sdk.android.authorization.ZCRMSDKClient
import com.zoho.crm.sdk.android.common.CommonUtil
import com.zoho.crm.sdk.android.configuration.ZCRMSDKConfigs
import com.zoho.crm.sdk.android.crud.ZCRMModule
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import com.zoho.crm.sdk.android.exception.ZCRMException
import com.zoho.crm.sdk.android.exception.ZCRMLogger
import com.zoho.crm.sdk.android.setup.sdkUtil.ZCRMSDKUtil
import kotlinx.android.synthetic.main.activity_accepted.*
import java.util.logging.Level

class Accepted : AppCompatActivity() {
    var id:Long?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted)
        shimmer.startShimmerAnimation()
        id = intent?.getLongExtra("id",1111)
        if(id!!.compareTo(1111) !=0 ){
            fetchTicketdata()
        }
    }
    fun acceptButtonClicked(v:View){
        acceptTicket()
    }
    fun acceptTicket(){
        shimmer.startShimmerAnimation()
        val context = this
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
        val sdkConfigs = ZCRMSDKConfigs()
        sdkConfigs.setClientDetails("1000.3YCSFRTKBHEU229426T3NIXHLQD9CH", "f57458a4bed20c18eda631e31a32cf64e80ecf96ea")
        sdkConfigs.setClientDetails("1000.PJ40TGO181FG314104GCMED469UK96", "b0c6eef2272dc313375ffcf4b38d517d997d9d6b6a")
        sdkConfigs.appType = CommonUtil.AppType.ZCRM
        sdkConfigs.oauthScopes =
            "Aaaserver.profile.Read,ZohoCRM.settings.ALL,ZohoCRM.modules.ALL,ZohoCRM.users.ALL,ZohoCRM.org.ALL,ZohoCRM.settings.stages.ALL,ZohoCRM.modules.emails.READ,ZohoCRM.Contacts.send_mail.CREATE,ZohoCRM.modules.emails.READ,ZohoCRM.settings.org_emails.ALL,profile.orguserphoto.READ,profile.orguserphoto.UPDATE,profile.orguserphoto.READ,ZohoCRM.settings.variable_groups.ALL,ZohoCRM.settings.variables.ALL,ZohoCRM.signals.ALL,ZohoCRM.settings.extensions.ALL,ZohoCRM.uns.READ,ZohoCRM.uns.ALL,ZohoCRM.quotes.send_mail.CREATE"
        sdkConfigs.oauthScopes = "ZohoCRM.settings.all,ZohoCRM.Modules.ALL,ZohoCRM.org.ALL,ZohoCRM.users.ALL"
        sdkConfigs.setLoggingPreferences(Level.ALL, true)
        val mobileSDKClient = ZCRMSDKClient.getInstance(this)
        mobileSDKClient.init(sdkConfigs, object : ZCRMSDKClient.Companion.ZCRMInitCallback {
            override fun onFailed(ex: ZCRMException) {
                Log.println(Log.ERROR, "Login_Failure", "Error in zoho login Accept service")
                runOnUiThread {
                    startActivity(
                        Intent(
                            this@Accepted,
                            Error::class.java
                        ))
                    finish()
                }
            }

            override fun onSuccess() {
                ZCRMSDKUtil.getModuleDelegate("Ticket").getRecord(id!!, object :
                    RecordsCallback<APIResponse, ZCRMRecord, Boolean> {
                    override fun fromCache(
                        response: APIResponse?,
                        zcrmentity: ZCRMRecord?,
                        waitForServer: Boolean
                    ) {
                    }

                    override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                        zcrmentity!!.setFieldValue("Status", "Accepted")
                        zcrmentity!!.update(object : RecordsCallback<APIResponse, ZCRMRecord, Boolean> {
                            override fun fromCache(
                                response: APIResponse?,
                                zcrmentity: ZCRMRecord?,
                                waitForServer: Boolean
                            ) {
                            }

                            override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                                println(">> $id accepted")
                                runOnUiThread {
                                    val intent = Intent(this@Accepted, AcceptedTicketDisplay::class.java)
                                    var aoc = ActivityOptionsCompat.makeSceneTransitionAnimation(this@Accepted,imageView3,"acceptimage")
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent,aoc.toBundle())
                                    shimmer.stopShimmerAnimation()
                                     finish()
                                }

                            }


                            override fun failed(exception: ZCRMException) {
                                println(">>update failed ex : $exception")
                                runOnUiThread {
                                    startActivity(
                                        Intent(
                                            this@Accepted,
                                            Error::class.java
                                        ))
                                    finish()
                                }

                            }

                        })
                    }

                    override fun failed(exception: ZCRMException) {
                        runOnUiThread {
                            startActivity(
                                Intent(
                                    this@Accepted,
                                    Error::class.java
                                ))
                            finish()
                        }
                    }

                })

            }
        })



    }
    fun fetchTicketdata(){
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
                ZCRMSDKUtil.getModule("Ticket", object : DataCallback<APIResponse, ZCRMModule> {
                    override fun completed(response: APIResponse, zcrmentity: ZCRMModule) {
                        zcrmentity.getRecord(id!!,object:RecordsCallback<APIResponse,ZCRMRecord,Boolean>{
                            override fun failed(exception: ZCRMException) {
                                runOnUiThread {
                                    startActivity(
                                        Intent(
                                            this@Accepted,
                                            Error::class.java
                                        ))
                                    finish()
                                }

                            }

                            override fun fromCache(
                                response: APIResponse?,
                                zcrmentity: ZCRMRecord?,
                                waitForServer: Boolean
                            ) {

                               }

                            override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                                runOnUiThread{
                                    var title= zcrmentity!!.getFieldValue("Name").toString()
                                    var desc = zcrmentity!!.getFieldValue("Description").toString()
                                    tickettitle.text = title
                                    ticketdesc.text = desc
                                    shimmer.stopShimmerAnimation()
                                }


                             }

                        })

                    }
                    override fun failed(exception: ZCRMException) {
                        runOnUiThread {
                            startActivity(
                                Intent(
                                    this@Accepted,
                                    Error::class.java
                                ))
                            finish()
                        }
                    }

                })
            }

            override fun onFailed(e: ZCRMException) {
                ZCRMLogger.logError(">> Login Failed ::: \$ex")
            }
        })
    }

}
