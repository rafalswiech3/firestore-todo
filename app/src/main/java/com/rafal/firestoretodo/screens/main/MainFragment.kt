package com.rafal.firestoretodo.screens.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.rafal.firestoretodo.R
import com.rafal.firestoretodo.databinding.FragmentAddToDoBinding
import com.rafal.firestoretodo.databinding.FragmentMainBinding
import com.rafal.firestoretodo.screens.addtodo.AddToDoViewModel
import com.rafal.firestoretodo.utils.IRecyclerViewClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "MainFragment"

@AndroidEntryPoint
class MainFragment : Fragment(), IRecyclerViewClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: TodoAdapter
    private lateinit var todosRegistration: ListenerRegistration

    @Inject
    @Named("todos_collection") lateinit var todos: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeToDoLiveData()
        observeRemoveToDoLiveData()
        registerFloatingButtonClickListener()
        getTodos()
        addSnapshotListener()
    }

    private fun prepareTodoAdapter() {
        adapter = TodoAdapter(this)
        adapter.addLoadStateListener { loadState ->
            binding.mainPb.isVisible = loadState.source.refresh is LoadState.Loading
            binding.errorGroup.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    private fun prepareRecyclerView() {
        prepareTodoAdapter()
        val recyclerView = binding.todoRv

        recyclerView.adapter = adapter
    }

    private fun observeToDoLiveData() {
        viewModel.todoLiveData.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun observeRemoveToDoLiveData() {
        viewModel.removeTodoLiveData.observe(viewLifecycleOwner) {
            when(it) {
                true -> Toast.makeText(context, getString(R.string.success_remove_todo), Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(context, getString(R.string.error_remove_todo), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTodos() {
        viewModel.getTodos()
    }

    private fun registerFloatingButtonClickListener() {
        binding.addTodoFb.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddToDoFragment()
            findNavController().navigate(action)
        }
    }

    private fun addSnapshotListener() {
        todosRegistration = todos.addSnapshotListener { snapshots, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documentChanges) {
                adapter.refresh()
            }
        }
    }

    override fun onStop() {
        todosRegistration.remove()
        super.onStop()
    }

    override fun longClick(id: String) {
        viewModel.removeTodo(id)
    }
}