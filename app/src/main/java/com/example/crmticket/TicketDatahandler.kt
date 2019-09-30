package com.example.crmticket

import android.content.Context
import com.zoho.crm.sdk.android.api.handler.DataCallback
import com.zoho.crm.sdk.android.api.response.APIResponse
import com.zoho.crm.sdk.android.api.response.BulkAPIResponse
import com.zoho.crm.sdk.android.crud.ZCRMField
import com.zoho.crm.sdk.android.crud.ZCRMModule
import com.zoho.crm.sdk.android.crud.ZCRMQuery
import com.zoho.crm.sdk.android.crud.ZCRMRecord
import com.zoho.crm.sdk.android.exception.ZCRMException
import com.zoho.crm.sdk.android.exception.ZCRMLogger
import com.zoho.crm.sdk.android.setup.sdkUtil.ZCRMSDKUtil

class TicketDatahandler {
    var lfields : List<ZCRMField>? = null
    var lrecords : List<ZCRMRecord>? = null
    var updated: Boolean = false
    companion object {
        private var  myObj: TicketDatahandler? = null
        fun setDataObject(obj: TicketDatahandler){
            myObj = obj
        }
        fun getInstance(): TicketDatahandler? {
            return myObj
        }
    }

    fun getFields() :List<ZCRMField>?{
        return lfields
    }
    fun getRecords() : List<ZCRMRecord>?{
        return lrecords
    }

}
