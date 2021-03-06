package com.rafal.firestoretodo.view.addtodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rafal.firestoretodo.R
import com.rafal.firestoretodo.databinding.FragmentAddToDoBinding
import com.rafal.firestoretodo.model.Todo
import com.rafal.firestoretodo.utils.ToDoValidator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddToDoFragment : Fragment() {

    private var _binding: FragmentAddToDoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddToDoViewModel by viewModels()

    private val navArgs: AddToDoFragmentArgs by navArgs()

    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isEditMode = navArgs.todo != null

        registerAddToDoButtonListener()
        observeAddToDoLiveData()
        fillDataIfEditMode()
    }

    private fun fillDataIfEditMode() {
        if (isEditMode) {
            binding.apply {
                addTitleTextInput.text?.append(navArgs.todo?.title)
                addDescTextInput.text?.append(navArgs.todo?.desc)
                addUrlTextInput.text?.append(navArgs.todo?.url)
            }
        }
    }

    private fun registerAddToDoButtonListener() {
        binding.addBtn.setOnClickListener {
            val title = binding.addTitleTextInput.text.toString()
            val description = binding.addDescTextInput.text.toString()
            val url = binding.addUrlTextInput.text.toString()
            if (validateInput(title, description, url)) {
                if (isEditMode) {
                    val todo = Todo(
                        title = title,
                        desc = description,
                        url = url
                    )
                    editToDo(navArgs.todo!!, todo)
                } else {
                    val todo = Todo(
                        title = title,
                        desc = description,
                        url = url
                    )
                    addToDo(todo)
                }
                findNavController().popBackStack()
            }
        }
    }

    private fun addToDo(todo: Todo) {
        viewModel.addToDo(todo)
    }

    private fun editToDo(todo: Todo, newTodo: Todo) {
        viewModel.editToDo(todo, newTodo)
    }

    private fun validateInput(title: String, desc: String, url: String): Boolean {
        var result = true
        if (!ToDoValidator.validateTitle(title)) {
            binding.addTitleTextInput.error = getString(R.string.title_error)
            result = false
        }

        if (!ToDoValidator.validateDescription(desc)) {
            binding.addDescTextInput.error = getString(R.string.desc_error)
            result = false
        }

        if (!ToDoValidator.validateUrl(url)) {
            binding.addUrlTextInput.error = getString(R.string.url_error)
            result = false
        }

        return result
    }

    private fun observeAddToDoLiveData() {
        viewModel.addToDoLiveData.observe(viewLifecycleOwner) {
            var toastText: String = if (it) {
                getString(R.string.successToAddToDo)
            } else {
                getString(R.string.failedToAddToDo)
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
        }
    }

}