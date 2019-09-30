package com.example.crmticket

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues



class crashReportDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CrashReport"
        private val LOG_TABLE = "Logs"
        private val DATE = "date"
        private val LOG = "error"
        private var obj:crashReportDatabase?=null
        fun getInstance():crashReportDatabase{

            return obj!!
        }
    }
    init {
        obj = this
    }
    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_LOG_TABLE = ("CREATE TABLE " + LOG_TABLE + "("
                + DATE + " TEXT," + LOG + " TEXT)")
        db!!.execSQL(CREATE_LOG_TABLE)
    }

    fun addLog(log:Loghelper) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(DATE, log.date) // Contact Name
        values.put(LOG, log.log) // Contact Phone

        // Inserting Row
        db.insert(LOG_TABLE, null, values)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    fun getLogs():List<Loghelper>{
        val loglist = ArrayList<Loghelper>()
        // Select All Query
        val selectQuery = "SELECT  * FROM $LOG_TABLE"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val contact = Loghelper()
                contact.date=(cursor.getString(0))
                contact.log=(cursor.getString(1))
                // Adding contact to list
                loglist.add(contact)
            } while (cursor.moveToNext())
        }

         return loglist


    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}
