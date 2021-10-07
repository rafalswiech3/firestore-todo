package com.rafal.firestoretodo.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rafal.firestoretodo.R
import com.rafal.firestoretodo.databinding.LoadStateFooterViewItemBinding

class ResultsLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ResultsLoadStateAdapter.ResultsLoadStateViewHolder>() {

    class ResultsLoadStateViewHolder(
        private val binding: LoadStateFooterViewItemBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = "Error loading..."
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): ResultsLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.load_state_footer_view_item, parent, false)
                val binding = LoadStateFooterViewItemBinding.bind(view)
                return ResultsLoadStateViewHolder(binding, retry)
            }
        }
    }

    override fun onBindViewHolder(holder: ResultsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ResultsLoadStateViewHolder {
        return ResultsLoadStateViewHolder.create(parent, retry)
    }
}