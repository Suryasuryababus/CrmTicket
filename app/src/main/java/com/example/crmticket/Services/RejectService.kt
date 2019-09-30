package com.example.crmticket.Services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.crmticket.ServiceDataHelper
import com.example.crmticket.TicketDatahandler
import com.zoho.crm.sdk.android.api.handler.RecordsCallback
import com.zoho.crm.sdk.android.api.response.APIResponse
import com.zoho.crm.sdk.android.authorization.ZCRMSDKClient
import com.zoho.crm.sdk.android.common.CommonUtil
import com.zoho.crm.sdk.android.configuration.ZCRMSDKConfigs
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import com.zoho.crm.sdk.android.exception.ZCRMException
import com.zoho.crm.sdk.android.setup.sdkUtil.ZCRMSDKUtil
import java.util.logging.Level

class RejectService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)

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
                ZCRMSDKUtil.getModuleDelegate("Ticket").getRecord(ServiceDataHelper.getId(), object:
                    RecordsCallback<APIResponse, ZCRMRecord, Boolean> {
                    override fun fromCache(
                        response: APIResponse?,
                        zcrmentity: ZCRMRecord?,
                        waitForServer: Boolean
                    ) {
                    }
                    override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                        zcrmentity!!.setFieldValue("Status", "Rejected")
                        zcrmentity!!.update(object : RecordsCallback<APIResponse, ZCRMRecord, Boolean> {
                            override fun fromCache(
                                response: APIResponse?,
                                zcrmentity: ZCRMRecord?,
                                waitForServer: Boolean
                            ) { }

                            override fun fromServer(response: APIResponse?, zcrmentity: ZCRMRecord?) {
                                println(">> ${ServiceDataHelper.getId()} Rejected")

                            }


                            override fun failed(exception: ZCRMException) {
                                println(">>update failed ex : $exception")
                            }

                        })
                    }

                    override fun failed(exception: ZCRMException) {

                    }

                })

            }
        })





        return super.onStartCommand(intent, flags, startId)
    }
}

