package com.mirellageri.tareas.data

data class Task(
    val id: Int,
    var title:String,
    var done: Boolean = false
){
    companion object{
        //Definir el nombre constantes de la tabla en SQL y sus datos
        const val TABLE_NAME = "Task"
        const val TABLE_NAME_ID = "id"
        const val TABLE_NAME_TITLE = "title"
        const val TABLE_NAME_DONE = "done"
        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
            "$TABLE_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$TABLE_NAME_TITLE TEXT," +
            "$TABLE_NAME_DONE BOOLEAN)"
        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
