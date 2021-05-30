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
import com.broto.todolist.databinding.FragmentAddBinding
import com.broto.todolist.fragments.SharedViewModel

class AddFragment : Fragment() {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        binding.spinnerPriority.onItemSelectedListener = mSharedViewModel.mSpinnerTextListener
        return binding.root
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
        val title = binding.etTitle.text.toString()
        val priority = binding.spinnerPriority.selectedItem.toString()
        val description = binding.etDescription.text.toString()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}