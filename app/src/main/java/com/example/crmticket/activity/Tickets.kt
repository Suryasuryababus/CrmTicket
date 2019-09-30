package com.example.crmticket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crmticket.Adopter.TicketAdopterView
import com.example.crmticket.R
import com.example.crmticket.TicketDatahandler
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
import kotlinx.android.synthetic.main.activity_ticket_details.rv
import kotlinx.android.synthetic.main.activity_tickets.*
import java.util.logging.Level


class Tickets : AppCompatActivity() {

    var v:View?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)
        shimmer.startShimmerAnimation()
        if(TicketDatahandler.getInstance()!=null){
            refreshTicketsView()
        }else {
            fetchTicketData()
        }
    }

    fun refreshTicketsView(){
        val tdh: TicketDatahandler? = TicketDatahandler.getInstance()
        val records:List<ZCRMRecord>? = tdh!!.getRecords()
        val av = TicketAdopterView(this, records!!)
        rv.adapter = av
        rv.layoutManager = LinearLayoutManager(applicationContext)!!
        shimmer.stopShimmerAnimation()
    }

    override fun onBackPressed() {
        backButtonPressed(View(applicationContext))
    }

    fun backButtonPressed(view: View){

        val layoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = layoutInflater.inflate(R.layout.popup, null)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        //instantiate popup window
        val popupWindow = PopupWindow(customView, width-150, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        shimmer.visibility = INVISIBLE
        popupWindow.animationStyle = R.style.Animation
        //display the popup window
        popupWindow.showAtLocation(playout, Gravity.CENTER, 0, 0)


        val exit = customView.findViewById(R.id.popupexit) as Button
        exit.setOnClickListener {
            finishAffinity()
           finish()
            System.exit(0)
        }
        val cancel = customView.findViewById(R.id.popcancel) as Button
        cancel.setOnClickListener {
            shimmer.visibility = VISIBLE
            popupWindow.dismiss()

        }

    }
    fun refreshButtonClicked(V:View){
        shimmer.startShimmerAnimation()
        fetchTicketData()


    }
    fun fetchTicketData(){
        val sdkConfigs = ZCRMSDKConfigs()
        sdkConfigs.setClientDetails("1000.3YCSFRTKBHEU229426T3NIXHLQD9CH", "f57458a4bed20c18eda631e31a32cf64e80ecf96ea")
        sdkConfigs.setClientDetails("1000.PJ40TGO181FG314104GCMED469UK96", "b0c6eef2272dc313375ffcf4b38d517d997d9d6b6a")
        sdkConfigs.appType = CommonUtil.AppType.ZCRM
        sdkConfigs.oauthScopes = "Aaaserver.profile.Read,ZohoCRM.settings.ALL,ZohoCRM.modules.ALL,ZohoCRM.users.ALL,ZohoCRM.org.ALL,ZohoCRM.settings.stages.ALL,ZohoCRM.modules.emails.READ,ZohoCRM.Contacts.send_mail.CREATE,ZohoCRM.modules.emails.READ,ZohoCRM.settings.org_emails.ALL,profile.orguserphoto.READ,profile.orguserphoto.UPDATE,profile.orguserphoto.READ,ZohoCRM.settings.variable_groups.ALL,ZohoCRM.settings.variables.ALL,ZohoCRM.signals.ALL,ZohoCRM.settings.extensions.ALL,ZohoCRM.uns.READ,ZohoCRM.uns.ALL,ZohoCRM.quotes.send_mail.CREATE"
        sdkConfigs.oauthScopes = "ZohoCRM.settings.all,ZohoCRM.Modules.ALL,ZohoCRM.org.ALL,ZohoCRM.users.ALL"
        sdkConfigs.setLoggingPreferences(Level.ALL, true)
         val mobileSDKClient = ZCRMSDKClient.getInstance(this)
        mobileSDKClient.init(sdkConfigs, object : ZCRMSDKClient.Companion.ZCRMInitCallback {
            override fun onSuccess() {
                ZCRMLogger.logInfo(">> Login Success.")
                Log.println(Log.INFO,"login","Login-Success")
                ZCRMSDKUtil.getModule("Ticket", object : DataCallback<APIResponse, ZCRMModule> {
                    override fun completed(response: APIResponse, zcrmentity: ZCRMModule) {
                        val params: ZCRMQuery.Companion.GetRecordParams = ZCRMQuery.Companion.GetRecordParams()
                        params.page = 1
                        params.perPage = 200
                        zcrmentity.getRecords(params, object :
                            RecordsCallback<BulkAPIResponse, List<ZCRMRecord>, Boolean> {
                            override fun failed(exception: ZCRMException) {
                                runOnUiThread {
                                    startActivity(
                                        Intent(
                                            this@Tickets,
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
                                zcrmentity.getFields(object : DataCallback<BulkAPIResponse, List<ZCRMField>> {
                                    override fun completed(response: BulkAPIResponse, fields: List<ZCRMField>) {
                                        runOnUiThread {
                                            var tdh = TicketDatahandler()
                                            tdh.lfields=fields
                                            tdh.lrecords=records
                                            TicketDatahandler.setDataObject(tdh)
                                            refreshTicketsView()
                                        }

                                    }

                                    override fun failed(exception: ZCRMException) {
                                        runOnUiThread {
                                            startActivity(
                                                Intent(
                                                    this@Tickets,
                                                    Error::class.java
                                                ))
                                            finish()
                                        }
                                        Toast.makeText(applicationContext,"something went wrong", Toast.LENGTH_LONG).show()
                                    }

                                })

                            }

                        })
                    }
                    override fun failed(exception: ZCRMException) {
                        runOnUiThread {
                            startActivity(
                                Intent(
                                    this@Tickets,
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
                            this@Tickets,
                            Error::class.java
                        ))
                    finish()
                }
            }
        })

    }

}
