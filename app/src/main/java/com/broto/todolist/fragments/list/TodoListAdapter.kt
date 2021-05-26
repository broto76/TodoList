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
import kotlinx.android.synthetic.main.row_layout.view.*

class TodoListAdapter: RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    var dataList = emptyList<ToDoData>()

    class TodoHolder(view: View): RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        holder.itemView.tv_title.text = dataList[position].title
        holder.itemView.tv_description.text = dataList[position].description
        holder.itemView.row_background.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController()
                .navigate(action)
        }

        val priority = dataList[position].priority
        holder.itemView.card_priority_indicator.setCardBackgroundColor(
            ContextCompat.getColor(holder.itemView.context,
                when(priority) {
                    Priority.HIGH -> R.color.red
                    Priority.MEDIUM -> R.color.yellow
                    else -> R.color.green
                }
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newList: List<ToDoData>) {
        dataList = newList
        notifyDataSetChanged()
    }
}