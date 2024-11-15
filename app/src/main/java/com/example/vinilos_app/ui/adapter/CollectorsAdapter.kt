package com.example.vinilos_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Collector
import com.example.vinyls_jetpack_application.databinding.CollectorsItemBinding


class CollectorsAdapter(private var collectors: List<Collector>) : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>() {

    inner class CollectorViewHolder(private val binding: CollectorsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.collectors = collector
            binding.executePendingBindings() // Asegurar que los cambios se reflejen en la vista
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding = CollectorsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.bind(collectors[position])
    }

    override fun getItemCount(): Int = collectors.size

    fun updateCollectors(newCollectors: List<Collector>) {
        val oldSize = collectors.size
        collectors = newCollectors
        val newSize = newCollectors.size

        when {
            newSize > oldSize -> {
                notifyItemRangeInserted(oldSize, newSize - oldSize) // Notifica la inserción de elementos
            }
            newSize < oldSize -> {
                notifyItemRangeRemoved(newSize, oldSize - newSize) // Notifica la eliminación de elementos
            }
            else -> {
                // Si los tamaños coinciden, verifica si hay elementos modificados
                for (i in newCollectors.indices) {
                    if (collectors[i] != newCollectors[i]) {
                        notifyItemChanged(i) // Notifica el cambio de un item específico
                    }
                }
            }
        }
    }

}
