package com.example.task_manager_app

// ProjectListFragment.kt

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProjectListFragment : Fragment() {

    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_project_list, container, false)
        val projectListRecyclerView: RecyclerView = view.findViewById(R.id.projectListRecyclerView)

        // Initialize ProjectAdapter
        projectAdapter = ProjectAdapter { clickedProject ->
            // Handle project click, open project details
            showTasksForProject(clickedProject)
        }

        // Set up RecyclerView for projects
        projectListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        projectListRecyclerView.adapter = projectAdapter

        // Load projects and update UI
        val projects = loadProjects()
        updateUI(projects)

        return view
    }

    private fun addProject(project: Project) {
        val dataRepository = DataRepository(requireContext())
        val insertedId = dataRepository.insertProject(project)

        if (insertedId != -1L) {
            // Insertion successful
            // Now you might want to update the UI or take any other actions
            // For example, reload the categories from the database and update the RecyclerView
            val updatedProjects = dataRepository.getAllProjects()
            updateUI(updatedProjects)
        } else {
            // Insertion failed
            // Handle the failure scenario
            Toast.makeText(requireContext(), "Failed to add project", Toast.LENGTH_SHORT).show()
        }

        dataRepository.closeDatabase()
    }

    private fun onAddProjectClicked() {
        // User clicked the button to add a new project
        showAddProjectDialog()
    }

    private fun showAddProjectDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_project, null)
        builder.setView(dialogView)
        builder.setTitle("Add New Project")

        val projectNameEditText: EditText = dialogView.findViewById(R.id.editTextProjectName)
        val projectDescriptionEditText: EditText = dialogView.findViewById(R.id.editTextProjectDescription)
        val projectDueDateEditText: EditText = dialogView.findViewById(R.id.editTextProjectDueDate)
        val projectPriorityEditText: EditText = dialogView.findViewById(R.id.editTextProjectPriority)

        builder.setPositiveButton("Add") { _, _ ->
            val newProject = Project(
                name = projectNameEditText.text.toString(),
                description = projectDescriptionEditText.text.toString(),
                dueDate = projectDueDateEditText.text.toString(),
                priority = projectPriorityEditText.text.toString()
            )

            // Assuming you have a method to add a project to your adapter
            addProject(newProject)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // User cancelled the operation
        }

        builder.show()
    }

    private fun setupAddProjectButton() {
        val addProjectButton: Button = requireView().findViewById(R.id.addProjectButton)
        addProjectButton.setOnClickListener {
            onAddProjectClicked()
        }
    }

    private fun loadProjects(): List<Project> {
        // Initialize DataRepository
        val dataRepository = DataRepository(requireContext())

        // Call the method to get all projects
        val projects = dataRepository.getAllProjects()

        // Close the database
        dataRepository.closeDatabase()

        return projects
    }

    private fun showTasksForProject(selectedProject: Project) {
        // Retrieve tasks for the selected project from the database
        val dataRepository = DataRepository(requireContext())
        val tasksForProject = dataRepository.getTasksForProject(selectedProject.name)
        dataRepository.closeDatabase()

        // Display tasks in a dialog or any other way you prefer
        val taskNames = tasksForProject.joinToString("\n") { it.name }
        val message = if (tasksForProject.isEmpty()) {
            "No tasks found for ${selectedProject.name}."
        } else {
            "Tasks for ${selectedProject.name}:\n$taskNames"
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tasks for ${selectedProject.name}")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }


    private fun updateUI(projects: List<Project>) {
        // Update the UI with the loaded projects
        projectAdapter.updateProjects(projects)
    }

    companion object {
        fun newInstance(projects: List<Project>): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle().apply {
                putParcelableArrayList("projects", ArrayList(projects))
            }
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call the setup function to attach the click listener
        setupAddProjectButton()
    }
}


