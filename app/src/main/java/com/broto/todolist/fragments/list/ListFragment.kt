package com.broto.todolist.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.broto.todolist.R
import com.broto.todolist.data.viewmodel.TodoViewModel
import com.broto.todolist.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private val TAG = "ListFragment"

    private val mAdapter: TodoListAdapter by lazy { TodoListAdapter() }
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        mTodoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            Log.d(TAG, "Data sent from livedata observer")
            mSharedViewModel.checkIfDatabaseEmpty(data)
            mAdapter.setData(data)
        })
        mSharedViewModel.mIsDatabaseEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                view.no_data_imageView.visibility = View.VISIBLE
                view.no_data_textView.visibility = View.VISIBLE
            } else {
                view.no_data_imageView.visibility = View.INVISIBLE
                view.no_data_textView.visibility = View.INVISIBLE
            }
        }

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // Set Menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                confirmRemoveAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoveAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTodoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed All Items",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") {_,_ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure with this operation?")
        builder.create().show()
    }

}