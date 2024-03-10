package com.example.task_manager_app

// ProjectAdapter.kt

import android.annotation.SuppressLint
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter(
    private val onItemClickListener: ((Project) -> Unit)? = null
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>(), SpinnerAdapter {

    private var projects: List<Project> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.bind(project)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(project)
        }
    }

    override fun getItemCount(): Int = projects.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProjects(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged() // You can consider using more specific notifyItemRangeChanged, notifyItemInserted, etc.
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val projectNameTextView: TextView = itemView.findViewById(R.id.textProjectName)
        private val projectDescriptionTextView: TextView = itemView.findViewById(R.id.textProjectDescription)
        private val projectDueDateTextView: TextView = itemView.findViewById(R.id.textProjectDueDate)
        private val projectPriorityTextView: TextView = itemView.findViewById(R.id.textProjectPriority)
        private val projectTasksTextView: TextView = itemView.findViewById(R.id.textProjectTasks)


        fun bind(project: Project) {
            projectNameTextView.text = project.name
            projectDescriptionTextView.text = project.description
            projectDueDateTextView.text = project.dueDate
            projectPriorityTextView.text = project.priority

            // Display the list of tasks associated with the project
            val tasksText = project.tasks.joinToString("\n") { it.name }
            projectTasksTextView.text = tasksText
        }
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        // Implementation here if needed
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        // Implementation here if needed
    }

    override fun getCount(): Int {
        return projects.size
    }

    override fun getItem(position: Int): Any {
        return projects[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val project = projects[position]
        val textView = TextView(parent?.context)
        textView.text = project.name // Modify this based on your project representation
        return textView
    }

    override fun getViewTypeCount(): Int {
        return 1 // Assuming you have only one type of view
    }

    override fun isEmpty(): Boolean {
        return projects.isEmpty()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val project = projects[position]
        val textView = TextView(parent?.context)
        textView.text = project.name // Modify this based on your project representation
        return textView
    }
}

