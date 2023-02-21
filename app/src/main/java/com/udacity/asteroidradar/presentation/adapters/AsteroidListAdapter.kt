package com.udacity.asteroidradar.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.model.AsteroidModel

class AsteroidListAdapter: ListAdapter<AsteroidModel, AsteroidListAdapter.AsteroidViewHolder>(DiffUtilAsteroidItemCallBack()) {
    var onItemClickListener: (AsteroidModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val viewHolder = DataBindingUtil.inflate<AsteroidItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.asteroid_item,
            parent,
            false
        )
        return AsteroidViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid, onItemClickListener)
    }

    class AsteroidViewHolder(private val view: AsteroidItemBinding): RecyclerView.ViewHolder(view.root) {

        fun bind(asteroid: AsteroidModel, onItemClickListener: (AsteroidModel) -> Unit = {}) {
            view.asteroidItem = asteroid
            view.root.setOnClickListener {
                onItemClickListener(asteroid)
            }
        }
    }

    class DiffUtilAsteroidItemCallBack: DiffUtil.ItemCallback<AsteroidModel>() {
        override fun areItemsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
            return oldItem == newItem
        }

    }
}