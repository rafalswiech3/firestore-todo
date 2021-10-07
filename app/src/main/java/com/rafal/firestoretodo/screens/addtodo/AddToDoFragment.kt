package com.rafal.firestoretodo.screens.addtodo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rafal.firestoretodo.R
import com.rafal.firestoretodo.databinding.FragmentAddToDoBinding
import com.rafal.firestoretodo.model.Todo
import com.rafal.firestoretodo.utils.ToDoValidator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddToDoFragment : Fragment() {

    private var _binding: FragmentAddToDoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerAddToDoButtonListener()
        observeAddToDoLiveData()
    }

    private fun registerAddToDoButtonListener() {
        binding.addBtn.setOnClickListener {
            val title = binding.addTitleTextInput.text.toString()
            val description = binding.addDescTextInput.text.toString()
            val url = binding.addUrlTextInput.text.toString()
            if (validateInput(title, description, url)) {
                viewModel.addToDo(
                    Todo(
                        title = title,
                        desc = description,
                        url = url
                    )
                )
                findNavController().popBackStack()
            }
        }
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