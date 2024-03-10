package com.example.task_manager_app

import android.annotation.SuppressLint
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val onItemClickListener: ((Category) -> Unit)? = null) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), SpinnerAdapter {

    private var categories: List<Category> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(category)
        }
    }

    override fun getItemCount(): Int = categories.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(category: Category) {
            // Update the TextView with category information
            itemView.findViewById<TextView>(R.id.textCategoryName).text = category.name
            itemView.findViewById<TextView>(R.id.textCategoryDescription).text = category.description
        }
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        // Implementation here if needed
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        // Implementation here if needed
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val category = categories[position]
        val textView = TextView(parent?.context)
        textView.text = category.name // Modify this based on your category representation
        return textView
    }

    override fun getViewTypeCount(): Int {
        return 1 // Assuming you have only one type of view
    }

    override fun isEmpty(): Boolean {
        return categories.isEmpty()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val category = categories[position]
        val textView = TextView(parent?.context)
        textView.text = category.name // Modify this based on your category representation
        return textView
    }


}

