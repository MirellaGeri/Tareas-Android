package com.mirellageri.tareas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mirellageri.tareas.R
import com.mirellageri.tareas.adapters.TaskAdapter
import com.mirellageri.tareas.data.Task
import com.mirellageri.tareas.data.TaskDAO
import com.mirellageri.tareas.databinding.DialogTaskBinding
import com.mirellageri.tareas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var  binding: ActivityMainBinding
    lateinit var adapter: TaskAdapter
    lateinit var taskDAO: TaskDAO
    var taskList = listOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //inicializar las variables
        taskDAO = TaskDAO(this)

        taskList = taskDAO.findAll()
        adapter = TaskAdapter(taskList,::onItemClick,::onItemCheck,::onItemEdit,::onItemDelete)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addTaskButton.setOnClickListener {
            val task = Task(-1,"")
            showTaskDialog(task)
        }
    }
    fun reloadData(){
        //TODO:Se puede optimizar pero es para actualizar la lista
        taskList = taskDAO.findAll()
        adapter.updateData(taskList)
    }
    fun showTaskDialog(task: Task){
        //para utilizar el mismo dialogg en edit y new task
        val dialogBinding = DialogTaskBinding.inflate(layoutInflater)
        dialogBinding.titleTaskTextField.editText!!.setText(task.title)
        val dialogTitle = if (task.id == -1) getString(R.string.title_message_create) else getString(R.string.title_message_edit)

        val builder = MaterialAlertDialogBuilder(this)
            .setMessage(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.save_task)){_,_ ->
                //Vamos a sobreeescribir su boton positivo para que no me guarde campos vacios y cierr automaticamente
            }
            .setNegativeButton(getString(R.string.cancel_button_delete)){_,_ ->

            }
        val dialog = builder.create()
        dialog.show()
        //controlar cuando se cierra el dialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val title = dialogBinding.titleTaskTextField.editText!!.text.toString()
            if (title.isEmpty()){
                dialogBinding.titleTaskTextField.error = getString(R.string.warning_task_input)
            }else{
                task.title = title
                taskDAO.save(task)
                reloadData()
                dialog.dismiss()
            }
        }
    }
    fun onItemClick(position: Int){
        onItemCheck(position)
    }
    fun onItemCheck(position: Int){
        val task = taskList[position]
        task.done = !task.done
        taskDAO.update(task)
        reloadData()
    }
    fun onItemEdit(position: Int) {
        val task = taskList[position]
        showTaskDialog(task)
    }

    fun onItemDelete(position: Int) {
        val builder = MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.title_message_delete))
            .setTitle(getString(R.string.question_message_delete))
            .setPositiveButton(getString(R.string.positive_button_delete)){_,_ ->
                val task =taskList[position]
                taskDAO.delete(task)
                reloadData()
            }
            .setNegativeButton(getString(R.string.cancel_button_delete)){_,_ ->

            }
        val dialog = builder.create()
        dialog.show()
    }
}