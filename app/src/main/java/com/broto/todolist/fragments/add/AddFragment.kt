package com.broto.todolist.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.broto.todolist.R
import com.broto.todolist.data.models.Priority
import com.broto.todolist.data.models.ToDoData
import com.broto.todolist.data.viewmodel.TodoViewModel
import com.broto.todolist.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        setHasOptionsMenu(true)
        view.spinner_priority.onItemSelectedListener = mSharedViewModel.mSpinnerTextListener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                insertDataToRepository()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToRepository() {
        val title = et_title.text.toString()
        val priority = spinner_priority.selectedItem.toString()
        val description = et_description.text.toString()

        if (mSharedViewModel.validateData(title, description)) {
           val data = ToDoData(
               0,
               title,
               mSharedViewModel.getPriority(priority),
               description
           )
            mTodoViewModel.insertData(data)
            Toast.makeText(
                requireContext(),
                "Successfully Added",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Invalid Input(s)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}