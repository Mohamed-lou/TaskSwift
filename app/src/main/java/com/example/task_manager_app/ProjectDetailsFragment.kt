package com.example.task_manager_app// com.example.task_manager_app.ProjectDetailsFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

@Suppress("DEPRECATION")
class ProjectDetailsFragment : Fragment() {

    private lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            project = it.getParcelable("project")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_project_details, container, false)

        val projectNameTextView: TextView = view.findViewById(R.id.projectNameTextView)
        val projectDescriptionTextView: TextView = view.findViewById(R.id.projectDescriptionTextView)
        val projectDueDateTextView: TextView = view.findViewById(R.id.projectDueDateTextView)
        val projectPriorityTextView: TextView = view.findViewById(R.id.projectPriorityTextView)

        // Set project details to the TextViews
        projectNameTextView.text = project.name
        projectDescriptionTextView.text = project.description
        projectDueDateTextView.text = project.dueDate
        projectPriorityTextView.text = project.priority

        return view
    }

    companion object {
        fun newInstance(project: Project): ProjectDetailsFragment {
            val fragment = ProjectDetailsFragment()
            val args = Bundle().apply {
                putParcelable("project", project)
            }
            fragment.arguments = args
            return fragment
        }
    }
}

