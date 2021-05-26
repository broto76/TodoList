package com.broto.todolist.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.broto.todolist.R
import com.broto.todolist.data.models.Priority
import com.broto.todolist.data.models.ToDoData
import com.broto.todolist.databinding.RowLayoutBinding
import kotlinx.android.synthetic.main.row_layout.view.*

class TodoListAdapter: RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    private var dataList = emptyList<ToDoData>()

    class TodoHolder(private val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(todoData: ToDoData) {
            binding.todoData = todoData
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): TodoHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflator, parent, false)
                return TodoHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newList: List<ToDoData>) {
        dataList = newList
        notifyDataSetChanged()
    }
}