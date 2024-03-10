package com.example.task_manager_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var dataRepository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DataRepository
        try {
            dataRepository = DataRepository(this)
        } catch (e: Exception) {
            // Handle the exception (e.g., log it or show an error message)
            e.printStackTrace()
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            // Use a when expression to simplify the logic
            val fragment = when (menuItem.itemId) {
                R.id.navigation_tasks -> {
                    val tasks = dataRepository.getAllTasks()
                    TaskListFragment.newInstance(tasks)
                }
                R.id.navigation_categories -> {
                    val categories = dataRepository.getAllCategories()
                    CategoryListFragment.newInstance(categories)
                }
                R.id.navigation_projects -> {
                    val projects = dataRepository.getAllProjects()
                    ProjectListFragment.newInstance(projects)
                }
                else -> throw IllegalArgumentException("Invalid menu item ID")
            }

            replaceFragment(fragment)
            true
        }

        // Set the default fragment when the activity is created
        val tasks = dataRepository.getAllTasks()
        replaceFragment(TaskListFragment.newInstance(tasks))
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    override fun onDestroy() {
        super.onDestroy()

        // Close the database when the activity is destroyed
        dataRepository.closeDatabase()
    }
}
