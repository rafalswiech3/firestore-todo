package com.rafal.firestoretodo.view.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.rafal.firestoretodo.R
import com.rafal.firestoretodo.databinding.FragmentMainBinding
import com.rafal.firestoretodo.model.Todo
import com.rafal.firestoretodo.utils.IRecyclerViewClickListener
import com.rafal.firestoretodo.view.dialogs.DeleteToDoConfirmationDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "MainFragment"

@AndroidEntryPoint
class MainFragment : Fragment(), IRecyclerViewClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: TodoAdapter
    private lateinit var todosRegistration: ListenerRegistration

    @Inject
    @Named("todos_collection")
    lateinit var todos: CollectionReference

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
        observeRemoveTodoDialogLiveData()
        registerFloatingButtonClickListener()
        registerRetryButtonClickListener()
        getTodosInit()
        addSnapshotListener()
    }

    private fun registerRetryButtonClickListener() {
        binding.mainRetryBtn.setOnClickListener {
            viewModel.getTodos()
        }
    }

    private fun prepareTodoAdapter() {
        adapter = TodoAdapter(this)
        adapter.addLoadStateListener { loadState ->
            binding.mainPb.isVisible = loadState.source.refresh is LoadState.Loading
            binding.errorGroup.isVisible = loadState.source.refresh is LoadState.Error
        }
        adapter.withLoadStateHeaderAndFooter(
            header = ResultsLoadStateAdapter { adapter.retry() },
            footer = ResultsLoadStateAdapter { adapter.retry() }
        )

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
        viewModel.removeTodoLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> Toast.makeText(
                        context,
                        getString(R.string.success_remove_todo),
                        Toast.LENGTH_SHORT
                    ).show()
                    false -> Toast.makeText(
                        context,
                        getString(R.string.error_remove_todo),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeRemoveTodoDialogLiveData() {
        viewModel.removeTodoDialogLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                viewModel.removeTodo()
            }
        }
    }

    private fun getTodosInit() {
        viewModel.getTodosInit()
    }

    private fun registerFloatingButtonClickListener() {
        binding.addTodoFb.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddToDoFragment(null)
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

    override fun click(todo: Todo) {
        val action = MainFragmentDirections.actionMainFragmentToAddToDoFragment(todo)
        findNavController().navigate(action)
    }

    override fun longClick(id: String) {
        viewModel.todoToRemoveID = id
        val dialog = DeleteToDoConfirmationDialog()
        dialog.show(childFragmentManager, null)
    }

    override fun onStop() {
        todosRegistration.remove()
        super.onStop()
    }
}