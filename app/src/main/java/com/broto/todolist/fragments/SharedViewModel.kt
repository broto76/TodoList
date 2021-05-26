package com.broto.todolist.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.broto.todolist.R
import com.broto.todolist.data.models.Priority
import com.broto.todolist.data.models.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val mIsDatabaseEmpty: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDatabaseEmpty(data: List<ToDoData>) {
        mIsDatabaseEmpty.value = data.isEmpty()
    }

    val mSpinnerTextListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long) {

            when (position) {
                0 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.red))

                1 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.yellow))

                2 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.green))
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}

    }

    fun validateData(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    fun getPriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            else -> Priority.LOW
        }
    }

    fun parsePrioritytoInt(priority: Priority): Int {
        return when(priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            else -> 2
        }
    }
}