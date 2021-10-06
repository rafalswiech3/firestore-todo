package com.rafal.firestoretodo.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rafal.firestoretodo.R
import com.rafal.firestoretodo.databinding.TodoListItemBinding
import com.rafal.firestoretodo.model.Todo
import com.rafal.firestoretodo.utils.IRecyclerViewClickListener
import java.text.SimpleDateFormat

class TodoAdapter(
    private val itemClickListener: IRecyclerViewClickListener
) : PagingDataAdapter<Todo, TodoAdapter.TodoViewHolder>(COMPARATOR) {

    inner class TodoViewHolder(private val binding: TodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.todoTitle.text = todo.title

            val date = SimpleDateFormat("dd.MM.yyyy HH:mm").format(todo.date)
            binding.todoDate.text = "${binding.todoDate.context.getString(R.string.created)} $date"

            loadIcon(todo.url)

            binding.root.setOnLongClickListener {
                itemClickListener.longClick(todo.id!!)
                true
            }
        }

        private fun loadIcon(url: String?) {
            if(!url.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load(url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.todoIv)
            } else {
                binding.todoIv.setImageResource(R.drawable.ic_baseline_business_center_24)
            }
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null)
            holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            TodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }

        }
    }
}