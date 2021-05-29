package com.broto.todolist.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.broto.todolist.data.models.ToDoData
import com.broto.todolist.databinding.RowLayoutBinding

class TodoListAdapter: RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    var dataList = emptyList<ToDoData>()

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
        val todoDiffUtil = TodoDiffUtil(dataList, newList)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        dataList = newList
        todoDiffResult.dispatchUpdatesTo(this)
    }
}