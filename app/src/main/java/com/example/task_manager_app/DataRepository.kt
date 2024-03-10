package com.example.task_manager_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log



class DataRepository(context: Context) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase = databaseHelper.writableDatabase

    // Get all Tasks
    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val cursor = db.query(
            DatabaseHelper.TABLE_TASKS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            val taskIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
            val taskNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_NAME)
            val taskDescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DESCRIPTION)
            val taskDueDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DUE_DATE)
            val taskPriorityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_PRIORITY)
            val taskCategoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CATEGORY)
            val taskProjectIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_PROJECT)
            val taskIsCompletedIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_IS_COMPLETED)


            if (taskIdIndex != -1 && taskNameIndex != -1 && taskDescriptionIndex != -1 && taskDueDateIndex != -1 && taskPriorityIndex != -1
                && taskCategoryIndex != -1 && taskProjectIndex != -1 && taskIsCompletedIndex != -1)
            {
                while (cursor.moveToNext()) {
                    val taskId = cursor.getLong(taskIdIndex)
                    val taskName = cursor.getString(taskNameIndex) ?: ""
                    val taskDescription = cursor.getString(taskDescriptionIndex) ?: ""
                    val taskDueDate = cursor.getString(taskDueDateIndex) ?: ""
                    val taskPriority = cursor.getString(taskPriorityIndex) ?: ""
                    val taskCategory = cursor.getString(taskCategoryIndex) ?: ""
                    val taskProject = cursor.getString(taskProjectIndex) ?: ""
                    val taskIsCompleted = cursor.getInt(taskIsCompletedIndex) != 0


                    val task = Task(
                        id = taskId,
                        name = taskName,
                        description = taskDescription,
                        dueDate = taskDueDate,
                        priority = taskPriority,
                        category = taskCategory,
                        project = taskProject,
                        isCompleted = taskIsCompleted
                    )

                    tasks.add(task)
                }
            } else {
                Log.e("DataRepository", "Columns not found in cursor.")
            }
        }

        return tasks
    }

    // Get all Categories
    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.use {
            val categoryNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_NAME)
            val categoryDescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION)


            if (categoryNameIndex != -1 && categoryDescriptionIndex != -1
            ) {
                while (cursor.moveToNext()) {
                    val categoryName = cursor.getString(categoryNameIndex) ?: ""
                    val categoryDescription = cursor.getString(categoryDescriptionIndex) ?: ""


                    // Create a Category instance and add it to the list of categories
                    val category = Category(
                        name = categoryName,
                        description = categoryDescription,
                    )
                    categories.add(category)
                }
            } else {
                Log.e("DataRepository", "Category columns not found in cursor.")
            }
        }
        return categories
    }

    // Get all Projects
    fun getAllProjects(): List<Project> {
        val projects = mutableListOf<Project>()
        val cursor = db.query(
            DatabaseHelper.TABLE_PROJECTS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            val projectNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROJECT_NAME)
            val projectDescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROJECT_DESCRIPTION)
            val projectDueDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROJECT_DUE_DATE)
            val projectPriorityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROJECT_PRIORITY)

            while (cursor.moveToNext()) {
                val projectName = cursor.getString(projectNameIndex) ?: ""
                val projectDescription = cursor.getString(projectDescriptionIndex) ?: ""
                val projectDueDate = cursor.getString(projectDueDateIndex) ?: ""
                val projectPriority = cursor.getString(projectPriorityIndex) ?: ""

                val project = Project(
                    name = projectName,
                    description = projectDescription,
                    dueDate = projectDueDate,
                    priority = projectPriority
                )
                projects.add(project)
            }
        }
        return projects
    }

    // Get all project names
    @SuppressLint("Range")
    fun getAllProjectsNames(): List<String> {
        val projects = mutableListOf<String>()
        val cursor = db.query(
            DatabaseHelper.TABLE_PROJECTS,
            arrayOf(DatabaseHelper.COLUMN_PROJECT_NAME),
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (cursor.moveToNext()) {
                val projectName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PROJECT_NAME))
                projects.add(projectName)
            }
        }

        return projects
    }

    // Get all category names
    @SuppressLint("Range")
    fun getAllCategoriesNames(): List<String> {
        val categories = mutableListOf<String>()
        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            arrayOf(DatabaseHelper.COLUMN_CATEGORY_NAME),
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (cursor.moveToNext()) {
                val categoryName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_NAME))
                categories.add(categoryName)
            }
        }

        return categories
    }

    fun getTasksForProject(projectId: String): List<Task> {
        val tasks = mutableListOf<Task>()
        val selection = "${DatabaseHelper.COLUMN_TASK_PROJECT} = ?"
        val selectionArgs = arrayOf(projectId)

        val cursor = db.query(
            DatabaseHelper.TABLE_TASKS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        cursor.use {
            val taskIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
            val taskNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_NAME)
            val taskDescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DESCRIPTION)
            val taskDueDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DUE_DATE)
            val taskPriorityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_PRIORITY)
            val taskCategoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CATEGORY)
            val taskProjectIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_PROJECT)
            val taskIsCompletedIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_IS_COMPLETED)

            if (taskIdIndex != -1 && taskNameIndex != -1 && taskDescriptionIndex != -1 && taskDueDateIndex != -1 && taskPriorityIndex != -1
                && taskCategoryIndex != -1 && taskProjectIndex != -1 && taskIsCompletedIndex != -1) {
                while (cursor.moveToNext()) {
                    val taskId = cursor.getLong(taskIdIndex)
                    val taskName = cursor.getString(taskNameIndex) ?: ""
                    val taskDescription = cursor.getString(taskDescriptionIndex) ?: ""
                    val taskDueDate = cursor.getString(taskDueDateIndex) ?: ""
                    val taskPriority = cursor.getString(taskPriorityIndex) ?: ""
                    val taskCategory = cursor.getString(taskCategoryIndex) ?: ""
                    val taskProject = cursor.getString(taskProjectIndex) ?: ""
                    val taskIsCompleted = cursor.getInt(taskIsCompletedIndex) != 0

                    val task = Task(
                        id = taskId,
                        name = taskName,
                        description = taskDescription,
                        dueDate = taskDueDate,
                        priority = taskPriority,
                        category = taskCategory,
                        project = taskProject,
                        isCompleted = taskIsCompleted
                    )

                    tasks.add(task)
                }
            } else {
                Log.e("DataRepository", "Columns not found in cursor.")
            }
        }

        return tasks
    }

    fun getTasksForCategory(categoryName: String): List<Task> {
        val tasks = mutableListOf<Task>()
        val selection = "${DatabaseHelper.COLUMN_TASK_CATEGORY} = ?"
        val selectionArgs = arrayOf(categoryName)

        val cursor = db.query(
            DatabaseHelper.TABLE_TASKS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        cursor.use {
            val taskIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
            val taskNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_NAME)
            val taskDescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DESCRIPTION)
            val taskDueDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DUE_DATE)
            val taskPriorityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_PRIORITY)
            val taskCategoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CATEGORY)
            val taskProjectIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_PROJECT)
            val taskIsCompletedIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_IS_COMPLETED)

            if (taskIdIndex != -1 && taskNameIndex != -1 && taskDescriptionIndex != -1 && taskDueDateIndex != -1 && taskPriorityIndex != -1
                && taskCategoryIndex != -1 && taskProjectIndex != -1 && taskIsCompletedIndex != -1) {
                while (cursor.moveToNext()) {
                    val taskId = cursor.getLong(taskIdIndex)
                    val taskName = cursor.getString(taskNameIndex) ?: ""
                    val taskDescription = cursor.getString(taskDescriptionIndex) ?: ""
                    val taskDueDate = cursor.getString(taskDueDateIndex) ?: ""
                    val taskPriority = cursor.getString(taskPriorityIndex) ?: ""
                    val taskCategory = cursor.getString(taskCategoryIndex) ?: ""
                    val taskProject = cursor.getString(taskProjectIndex) ?: ""
                    val taskIsCompleted = cursor.getInt(taskIsCompletedIndex) != 0

                    val task = Task(
                        id = taskId,
                        name = taskName,
                        description = taskDescription,
                        dueDate = taskDueDate,
                        priority = taskPriority,
                        category = taskCategory,
                        project = taskProject,
                        isCompleted = taskIsCompleted
                    )

                    tasks.add(task)
                }
            } else {
                Log.e("DataRepository", "Columns not found in cursor.")
            }
        }

        return tasks
    }




    // Insert a new Task
    fun insertTask(task: Task): Long {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TASK_NAME, task.name)
            put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, task.description)
            put(DatabaseHelper.COLUMN_TASK_DUE_DATE, task.dueDate)
            put(DatabaseHelper.COLUMN_TASK_PRIORITY, task.priority)
            put(DatabaseHelper.COLUMN_TASK_CATEGORY, task.category)
            put(DatabaseHelper.COLUMN_TASK_PROJECT, task.project)
            put(DatabaseHelper.COLUMN_TASK_IS_COMPLETED, task.isCompleted)
        }
        return db.insert(DatabaseHelper.TABLE_TASKS, null, values)
    }

    // Insert a new com.example.task_manager_app.Category
    fun insertCategory(category: Category): Long {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION, category.description)
        }
        return db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values)
    }

    fun insertProject(project: Project): Long {
        val values = ContentValues ().apply {
            put(DatabaseHelper.COLUMN_PROJECT_NAME, project.name)
            put(DatabaseHelper.COLUMN_PROJECT_DESCRIPTION, project.description)
            put(DatabaseHelper.COLUMN_PROJECT_DUE_DATE, project.dueDate)
            put(DatabaseHelper.COLUMN_PROJECT_PRIORITY, project.priority)
        }
        return db.insert(DatabaseHelper.TABLE_PROJECTS, null, values)
    }

    fun updateTask(updatedTask: Task): Boolean {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TASK_NAME, updatedTask.name)
            put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, updatedTask.description)
            put(DatabaseHelper.COLUMN_TASK_DUE_DATE, updatedTask.dueDate)
            put(DatabaseHelper.COLUMN_TASK_PRIORITY, updatedTask.priority)
            put(DatabaseHelper.COLUMN_TASK_CATEGORY, updatedTask.category)
            put(DatabaseHelper.COLUMN_TASK_PROJECT, updatedTask.project)
            put(DatabaseHelper.COLUMN_TASK_IS_COMPLETED, if (updatedTask.isCompleted) 1 else 0)
        }

        val whereClause = "${DatabaseHelper.COLUMN_ID} = ?"
        val whereArgs = arrayOf(updatedTask.id.toString())

        val rowsAffected = db.update(DatabaseHelper.TABLE_TASKS, values, whereClause, whereArgs)

        return rowsAffected > 0
    }

    // Close the database when it's no longer needed
    fun closeDatabase() {
        db.close()
    }

    // Database Helper class
    private class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            // Create Task table

            val createTasksTable =
                "CREATE TABLE $TABLE_TASKS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_TASK_NAME TEXT, $COLUMN_TASK_DESCRIPTION TEXT, " + "$COLUMN_PROJECT_DUE_DATE TEXT," +
                        "$COLUMN_TASK_PRIORITY TEXT,  $COLUMN_TASK_CATEGORY TEXT, " +
                        "$COLUMN_TASK_PROJECT TEXT, $COLUMN_TASK_IS_COMPLETED INTEGER);"
            db.execSQL(createTasksTable)

            // Create Categories table
            val createCategoriesTable =
                "CREATE TABLE $TABLE_CATEGORIES ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_CATEGORY_NAME TEXT, $COLUMN_CATEGORY_DESCRIPTION TEXT); "
            db.execSQL(createCategoriesTable)

            // Create Projects table
            val createProjectsTable =
                "CREATE TABLE $TABLE_PROJECTS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_PROJECT_NAME TEXT, $COLUMN_PROJECT_DESCRIPTION TEXT, " +
                        "$COLUMN_PROJECT_DUE_DATE TEXT, $COLUMN_PROJECT_PRIORITY TEXT);"
            db.execSQL(createProjectsTable)


        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Handle database upgrades, if needed
        }

        companion object {
            const val DATABASE_NAME = "task_manager_database"
            const val DATABASE_VERSION = 1

            // Table and column names
            const val TABLE_TASKS = "tasks"
            const val TABLE_CATEGORIES = "categories"
            const val TABLE_PROJECTS = "projects"
            const val COLUMN_ID = "_id"
            const val COLUMN_TASK_NAME = "name"
            const val COLUMN_TASK_DESCRIPTION = "description"
            const val COLUMN_CATEGORY_NAME = "name"
            const val COLUMN_PROJECT_NAME = "name"
            const val COLUMN_TASK_DUE_DATE = "due_date"
            const val COLUMN_TASK_PRIORITY = "priority"
            const val COLUMN_TASK_CATEGORY = "category"
            const val COLUMN_TASK_PROJECT = "project"
            const val COLUMN_TASK_IS_COMPLETED = "is_completed"
            const val COLUMN_PROJECT_DESCRIPTION = "description"
            const val COLUMN_PROJECT_DUE_DATE = "due_date"
            const val COLUMN_PROJECT_PRIORITY = "priority"
            const val COLUMN_CATEGORY_DESCRIPTION = "description"



        }
    }
}

