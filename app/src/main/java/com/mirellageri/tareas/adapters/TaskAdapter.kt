package com.mirellageri.tareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mirellageri.tareas.R
import com.mirellageri.tareas.data.Task
import com.mirellageri.tareas.databinding.ItemTaskBinding

class TaskAdapter(
    var items: List<Task>,
    val onItemClick:(position : Int) -> Unit,
    val onItemCheck:(position : Int) -> Unit,
    val onItemEdit:(position : Int) -> Unit,
    val onItemDelete:(position : Int) -> Unit
): RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater,parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
        //para que no use los parametros del lambda solo poner  _ guion bajo para que las ignore
        holder.binding.doneCheckBox.setOnCheckedChangeListener { _, _ ->
            if (holder.binding.doneCheckBox.isPressed){
                onItemCheck(position)
            }
        }
        holder.binding.editButton.setOnClickListener {
            onItemEdit(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            onItemDelete(position)
        }
    }
    override fun getItemCount(): Int =items.size
    fun updateData(dataSet: List<Task>){
        this.items = dataSet
        notifyDataSetChanged()
    }
}

class TaskViewHolder(val binding: ItemTaskBinding ): RecyclerView.ViewHolder(binding.root){
    fun render(task: Task){
        binding.titleTextView.text = task.title
        binding.doneCheckBox.isChecked = task.done
    }
}