package com.mirellageri.tareas.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mirellageri.tareas.utils.DatabaseManager

class TaskDAO(val context : Context) {
    lateinit var db: SQLiteDatabase
    fun open(){
        db = DatabaseManager(context).writableDatabase
    }
    fun close(){
        db.close()
    }
    private fun cursorToEntity(cursor : Cursor): Task{
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(Task.TABLE_NAME_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.TABLE_NAME_TITLE))
        val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.TABLE_NAME_DONE)) != 0
        return Task(id,title,done)
    }
    private fun getContentValue(task: Task): ContentValues{
        val values = ContentValues()
        values.put(Task.TABLE_NAME_TITLE,task.title)
        values.put(Task.TABLE_NAME_DONE,task.done)
        return values
    }
    fun save(task : Task){
        if (task.id == -1){
            insert(task)
        }else{
            update(task)
        }
    }
    fun insert(task: Task){
        //Obtener la data del repositorio y escribi rel mode
        open()
        //crear una nueva map de valores, donde el nombre de las columnas son la llave
        val values = getContentValue(task)
        try {
            //Insertar la nueva fila, devuelve el Id o primary key de una fila
            val newRowId = db.insertOrThrow(Task.TABLE_NAME,null,values)
            Log.i("DATABASE", "New row inserted in table ${Task.TABLE_NAME} with id: $newRowId")
        }catch (e : Exception){
            Log.e("DATABASE",e.localizedMessage,e)
        }finally {
            //tiene que cerrarse
            close()
        }
    }
    fun update(task: Task){
        //abrir una coneccion con la base de datos
        open()
        //crear una nueva mao de valores, donde el nombre de las columnas son la llave
        val values = getContentValue(task)
        try {
            //Update y devuelve el numero de filas afectadas
            val updatedRows = db.update(Task.TABLE_NAME,values,"${Task.TABLE_NAME_ID} = ${task.id}", null)
            Log.i("DATABASE", "$updatedRows rows updated in table ${Task.TABLE_NAME}")
        }catch (e : Exception){
            Log.e("DATABASE",e.localizedMessage,e)
        }finally {
            //tiene que cerrarse
            close()
        }
    }
    fun delete(task: Task){
        delete(task.id)
    }
    fun delete(id :Int){
        open()
        try {
            //nos devuelve el numero de filas borradas
            val deletedRows = db.delete(Task.TABLE_NAME,"${Task.TABLE_NAME_ID}= $id",null)
            Log.i("DATABASE", "$deletedRows rows deleted in table ${Task.TABLE_NAME}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }
    fun findById(id :Int) : Task?{
        open()
        var task: Task? = null
        try {
            val cursor = db.query(
                Task.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                "${Task.TABLE_NAME_ID} = $id",              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )
            if (cursor.moveToNext()) {
                task = cursorToEntity(cursor)
            }
        }catch (e: Exception){
            Log.e("DATABASE", e.localizedMessage,e)
        }finally {
            close()
        }
        return task
    }
    fun findAll(): List<Task> {
        open()
        val taskList = mutableListOf<Task>()
        try {
            val cursor = db.query(
                Task.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                "${Task.TABLE_NAME_DONE} ASC"               // The sort order
            )
            while (cursor.moveToNext()) {
                val task = cursorToEntity(cursor)
                taskList.add(task)
            }
        }catch (e: Exception){
            Log.e("DATABASE", e.localizedMessage,e)
        }finally {
            close()
        }
        return taskList
    }
}