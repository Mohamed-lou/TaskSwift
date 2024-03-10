package com.example.task_manager_app

// CategoryListFragment.kt

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

class CategoryListFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)
        val categoryListRecyclerView: RecyclerView = view.findViewById(R.id.categoryListRecyclerView)

        // Initialize CategoryAdapter with a click listener
        categoryAdapter = CategoryAdapter { clickedCategory ->
            // Handle category click by showing tasks for the selected category
            showTasksForSelectedCategory(clickedCategory)
        }

        // Set up RecyclerView
        categoryListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryListRecyclerView.adapter = categoryAdapter

        // Load categories and update UI
        val categories = loadCategories()
        updateUI(categories)

        return view
    }



    private fun addCategory(category: Category) {
        val dataRepository = DataRepository(requireContext())
        val insertedId = dataRepository.insertCategory(category)

        if (insertedId != -1L) {
            // Insertion successful
            // Now you might want to update the UI or take any other actions
            // For example, reload the categories from the database and update the RecyclerView
            val updatedCategories = dataRepository.getAllCategories()
            updateUI(updatedCategories)
        } else {
            // Insertion failed
            // Handle the failure scenario
            Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT).show()
        }

        dataRepository.closeDatabase()
    }


    private fun onAddCategoryClicked() {
        // User clicked the button to add a new category
        showAddCategoryDialog()
    }

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_category, null)
        builder.setView(dialogView)
        builder.setTitle("Add New Category")

        val categoryNameEditText: EditText = dialogView.findViewById(R.id.editTextCategoryName)
        val categoryDescriptionEditText: EditText = dialogView.findViewById(R.id.editTextCategoryDescription)


        builder.setPositiveButton("Add") { _, _ ->
            val newCategory = Category(
                name = categoryNameEditText.text.toString(),
                description = categoryDescriptionEditText.text.toString(),

            )
            addCategory(newCategory)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // User cancelled the operation
        }

        builder.show()
    }

    private fun setupAddCategoryButton() {
        val addCategoryButton: Button = requireView().findViewById(R.id.addCategoryButton)
        addCategoryButton.setOnClickListener {
            onAddCategoryClicked()
        }
    }


    private fun loadCategories(): List<Category> {
        // Initialize DataRepository
        val dataRepository = DataRepository(requireContext())

        // Call the method to get all categories
        val categories = dataRepository.getAllCategories()

        // Close the database
        dataRepository.closeDatabase()

        return categories
    }

    private fun showTasksForSelectedCategory(selectedCategory: Category) {
        // Retrieve tasks for the selected category from the database
        val dataRepository = DataRepository(requireContext())
        val tasksForCategory = dataRepository.getTasksForCategory(selectedCategory.name)
        dataRepository.closeDatabase()

        // Display tasks in a dialog or any other way you prefer
        val taskNames = tasksForCategory.joinToString("\n") { it.name }
        val message = if (tasksForCategory.isEmpty()) {
            "No tasks found for ${selectedCategory.name}."
        } else {
            "Tasks for ${selectedCategory.name}:\n$taskNames"
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tasks for ${selectedCategory.name}")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun updateUI(categories: List<Category>) {
        // Update the UI with the loaded categories
        categoryAdapter.updateCategories(categories)
    }

    companion object {
        fun newInstance(categories: List<Category>): CategoryListFragment {
            val fragment = CategoryListFragment()
            val args = Bundle()
            // Pass the list of categories as arguments to the fragment
            args.putParcelableArrayList("categories", ArrayList(categories))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call the setup function to attach the click listener
        setupAddCategoryButton()

        // Assuming you have the spinner defined in your layout
        val categorySpinner: Spinner? = view.findViewById(R.id.spinnerCategory)

        if (categorySpinner != null) {
            // Call the setup function for the category spinner
            setupCategorySpinner(categorySpinner, loadCategories())
        } else {
            // Log an error or show a message if the spinner is not found
            Log.e("CategoryListFragment", "Category spinner not found in the layout")
        }
    }

    private fun setupCategorySpinner(categorySpinner: Spinner, categories: List<Category>) {
        // Set up ArrayAdapter for category spinner
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)

        // Set dropdown layout style
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter to category spinner
        categorySpinner.adapter = categoryAdapter
    }

}