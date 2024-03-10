package com.example.task_manager_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<Task> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onTaskClick(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(task: Task) {
            itemView.findViewById<TextView>(R.id.textName).text = task.name
            itemView.findViewById<TextView>(R.id.textDescription).text = task.description
            itemView.findViewById<TextView>(R.id.textDueDate).text = task.dueDate
            itemView.findViewById<TextView>(R.id.textPriority).text = task.priority
        }
    }

    interface OnItemClickListener {
        fun onTaskClick(task: Task)
    }
}