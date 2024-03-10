package com.example.task_manager_app


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskListFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        val taskListRecyclerView: RecyclerView = view.findViewById(R.id.taskListRecyclerView)

        // Initialize TaskAdapter with the click listener
        taskAdapter = TaskAdapter(object : TaskAdapter.OnItemClickListener {
            override fun onTaskClick(task: Task) {
                showEditTaskDialog(task)
            }
        })


        // Set up RecyclerView
        taskListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskListRecyclerView.adapter = taskAdapter

        // Load tasks, categories, and projects and update UI
        val tasks = loadTasks()
        updateUI(tasks)

        return view
    }


    private fun addTask(task: Task) {
        // Call the insertTask function from your DataRepository
        val dataRepository = DataRepository(requireContext())
        val insertedId = dataRepository.insertTask(task)

        if (insertedId != -1L) {
            // Insertion successful

            // Now load the updated tasks from the database
            val updatedTasks = dataRepository.getAllTasks()

            // Update the UI with the new tasks
            updateUI(updatedTasks)
        } else {
            // Insertion failed
            // Handle the failure scenario
            Toast.makeText(requireContext(), "Failed to add task", Toast.LENGTH_SHORT).show()
        }

        // Close the database connection in the finally block to ensure it's always closed
        dataRepository.closeDatabase()
    }

    private fun onAddTaskClicked() {
        // User clicked the button to add a new category
        showAddTaskDialog()
    }

    @SuppressLint("MissingInflatedId")
    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_task, null)
        builder.setView(dialogView)
        builder.setTitle("Add New Task")

        val taskIdEditText: EditText = dialogView.findViewById(R.id.editTextTaskID)
        val taskNameEditText: EditText = dialogView.findViewById(R.id.editTextTaskName)
        val taskDescriptionEditText: EditText = dialogView.findViewById(R.id.editTextTaskDescription)
        val taskDueDateEditText: EditText = dialogView.findViewById(R.id.editTextTaskDueDate)
        val taskPriorityEditText: EditText = dialogView.findViewById(R.id.editTextTaskPriority)
        val spinnerCategory: Spinner = dialogView.findViewById(R.id.spinnerCategory)
        val spinnerProject: Spinner = dialogView.findViewById(R.id.spinnerProject)


        // Call the setup functions for spinners
        setupCategorySpinner(spinnerCategory, loadCategories())
        setupProjectSpinner(spinnerProject, loadProjects())



        builder.setPositiveButton("Add") { _, _ ->
            val newTask = Task(
                id = try {
                    taskIdEditText.text.toString().toLong()
                } catch (e: NumberFormatException) {
                    // Handle the case where taskId is not a valid Long
                    -1L // or any default value
                },
                name = taskNameEditText.text.toString(),
                description = taskDescriptionEditText.text.toString(),
                dueDate = taskDueDateEditText.text.toString(),
                priority = taskPriorityEditText.text.toString(),
                category = spinnerCategory.selectedItem.toString(),
                project = spinnerProject.selectedItem.toString(),
                isCompleted = false
                )
            addTask(newTask)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // User cancelled the operation
        }

        builder.show()
    }



    private fun setupAddTaskButton() {
        val addTaskButton: Button = requireView().findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            onAddTaskClicked()
        }
    }

    private fun loadTasks(): List<Task> {
        val dataRepository = DataRepository(requireContext())
        val tasks = dataRepository.getAllTasks()

        // Log the size of the tasks list
        Log.d("TaskListFragment", "Number of tasks loaded: ${tasks.size}")

        // Log details of each task
        for (task in tasks) {
            Log.d("TaskListFragment", "Task details: $task")
        }

        dataRepository.closeDatabase()
        return tasks
    }

    private fun updateUI(tasks: List<Task>) {
        taskAdapter.updateTasks(tasks)
    }



    companion object {
        fun newInstance(tasks: List<Task>): TaskListFragment {
            val fragment = TaskListFragment()
            val args = Bundle().apply {
                putParcelableArrayList("tasks", ArrayList(tasks))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddTaskButton()

        // Set the TaskListFragment as the listener for item clicks
        taskAdapter = TaskAdapter(object : TaskAdapter.OnItemClickListener {
            override fun onTaskClick(task: Task) {
                showEditTaskDialog(task)
            }
        })

    }

    private fun setupCategorySpinner(categorySpinner: Spinner, categories: List<String>) {
        // Set up ArrayAdapter for category spinner
        val categoryAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)

        // Set dropdown layout style
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter to category spinner
        categorySpinner.adapter = categoryAdapter
    }

    private fun setupProjectSpinner(projectSpinner: Spinner, projects: List<String>) {
        // Set up ArrayAdapter for project spinner
        val projectAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, projects)

        // Set dropdown layout style
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter to project spinner
        projectSpinner.adapter = projectAdapter
    }

    private fun loadCategories(): List<String> {
        // Fetch categories from the database
        val dataRepository = DataRepository(requireContext())
        val categories = dataRepository.getAllCategoriesNames()
        dataRepository.closeDatabase()
        return categories
    }

    private fun loadProjects(): List<String> {
        // Fetch projects from the database
        val dataRepository = DataRepository(requireContext())
        val projects = dataRepository.getAllProjectsNames()
        dataRepository.closeDatabase()
        return projects
    }
        

    private fun showEditTaskDialog(task: Task) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_task, null)
        builder.setView(dialogView)
        builder.setTitle("Edit Task")

        val taskNameEditText: EditText = dialogView.findViewById(R.id.editTextTaskName)
        val taskDescriptionEditText: EditText = dialogView.findViewById(R.id.editTextTaskDescription)
        val taskDueDateEditText: EditText = dialogView.findViewById(R.id.editTextTaskDueDate)
        val taskPriorityEditText: EditText = dialogView.findViewById(R.id.editTextTaskPriority)
        val spinnerCategory: Spinner = dialogView.findViewById(R.id.spinnerCategory)
        val spinnerProject: Spinner = dialogView.findViewById(R.id.spinnerProject)

        // Set the existing task details in the dialog
        taskNameEditText.setText(task.name)
        taskDescriptionEditText.setText(task.description)
        taskDueDateEditText.setText(task.dueDate)
        taskPriorityEditText.setText(task.priority)

        // Set selected items for the spinners
        val selectedCategory = spinnerCategory.selectedItem?.toString() ?: ""
        val selectedProject = spinnerProject.selectedItem?.toString() ?: ""

        builder.setPositiveButton("Save") { _, _ ->
            // Update the task with the edited details
            val updatedTask = Task(
                id = task.id,
                name = taskNameEditText.text.toString(),
                description = taskDescriptionEditText.text.toString(),
                dueDate = taskDueDateEditText.text.toString(),
                priority = taskPriorityEditText.text.toString(),
                category = selectedCategory,
                project = selectedProject,
                isCompleted = task.isCompleted
            )
            updateTask(updatedTask)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // User cancelled the operation
        }

        builder.show()
    }

    // Function to update an existing task
    private fun updateTask(updatedTask: Task) {
        val dataRepository = DataRepository(requireContext())

        try {
            val success = dataRepository.updateTask(updatedTask)

            if (success) {
                // Update successful
                val updatedTasks = dataRepository.getAllTasks()
                updateUI(updatedTasks)
            } else {
                // Update failed
                Toast.makeText(requireContext(), "Failed to update task", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Handle exceptions if necessary
            Log.e("UpdateTask", "Error updating task", e)
        } finally {
            dataRepository.closeDatabase()
        }
    }


    private fun onTaskClick(task: Task) {
        showEditTaskDialog(task)
    }

    // Add this method to handle the click event from the adapter
    fun invoke(task: Task) {
        onTaskClick(task)
    }

}
