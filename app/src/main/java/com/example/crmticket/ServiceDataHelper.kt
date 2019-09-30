package com.example.crmticket

class ServiceDataHelper{
    companion object{
        var id:Long?=null
        fun setId(lid:Long){
            id = lid
        }
        fun getId():Long{
            return id!!
        }
    }
}
