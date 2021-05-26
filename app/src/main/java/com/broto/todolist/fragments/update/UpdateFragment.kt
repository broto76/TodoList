package com.broto.todolist.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.broto.todolist.R
import com.broto.todolist.data.models.ToDoData
import com.broto.todolist.data.viewmodel.TodoViewModel
import com.broto.todolist.databinding.FragmentUpdateBinding
import com.broto.todolist.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_update, container, false)
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args.currentItem

        setHasOptionsMenu(true)

        binding.spinnerPriorityUpdate.onItemSelectedListener =
            mSharedViewModel.mSpinnerTextListener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                updateItem()
            }
            R.id.menu_delete -> {
                confirmItemRemoval()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTodoViewModel.deleteData(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed: ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_,_ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure with this operation?")
        builder.create().show()
    }

    private fun updateItem() {
        val title = et_title_update.text.toString()
        val priority = spinner_priority_update.selectedItem.toString()
        val description = et_description_update.text.toString()

        if (mSharedViewModel.validateData(title, description)) {
            val updatedToDo = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.getPriority(priority),
                description
            )

            mTodoViewModel.updateData(updatedToDo)

            Toast.makeText(
                requireContext(),
                "Todo Updated",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
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