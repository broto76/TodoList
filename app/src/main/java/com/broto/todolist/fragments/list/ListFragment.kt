package com.broto.todolist.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.broto.todolist.R
import com.broto.todolist.data.models.ToDoData
import com.broto.todolist.data.viewmodel.TodoViewModel
import com.broto.todolist.databinding.FragmentListBinding
import com.broto.todolist.fragments.SharedViewModel
import com.broto.todolist.fragments.list.adapter.TodoListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val TAG = "ListFragment"

    private val mAdapter: TodoListAdapter by lazy { TodoListAdapter() }
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setUpRecyclerView()

        mTodoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            Log.d(TAG, "Data sent from livedata observer")
            mSharedViewModel.checkIfDatabaseEmpty(data)
            mAdapter.setData(data)
        })

        // Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoData) {
        Snackbar.make(
            view,
            "Deleted ${deletedItem.title}",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            mTodoViewModel.insertData(deletedItem)
        }.show()
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object: SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = mAdapter.dataList[viewHolder.adapterPosition]
                mTodoViewModel.deleteData(item)

                // Show Snackbar to notify and undo action
                restoreDeletedItem(viewHolder.itemView, item)
            }
        }
        val swipeToDelete = ItemTouchHelper(swipeToDeleteCallback)
        swipeToDelete.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                confirmRemoveAll()
            }
            R.id.menu_priority_high -> {
                mTodoViewModel.getListSortedHighPriority.observe(this, Observer {
                    mAdapter.setData(it)
                })
            }
            R.id.menu_priority_low -> {
                mTodoViewModel.getListSortedLowPriority.observe(this, Observer {
                    mAdapter.setData(it)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabaseForQuery(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabaseForQuery(query)
        }
        return true
    }


    private fun searchDatabaseForQuery(query: String) {
        // Allow 0 or more characters at each end of the search string
        val searchQuery = "%$query%"
        mTodoViewModel.searchQuery(searchQuery).observe(this) { list ->
            list.let {
                mAdapter.setData(it)
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}