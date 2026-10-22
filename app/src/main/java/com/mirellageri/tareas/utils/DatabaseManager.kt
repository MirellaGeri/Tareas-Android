package com.mirellageri.tareas.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mirellageri.tareas.data.Task

class DatabaseManager(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON;")
        db.execSQL(Task.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }
    fun onDestroy(db: SQLiteDatabase){
        //solo para cuando estas en desarrollo y no quieres actualizar las versiones
        db.execSQL(Task.SQL_DROP_TABLE)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Task.db"
    }
}