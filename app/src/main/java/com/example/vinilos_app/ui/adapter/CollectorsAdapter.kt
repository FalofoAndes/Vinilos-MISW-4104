package com.example.vinilos_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Collector
import com.example.vinyls_jetpack_application.databinding.CollectorsItemBinding

class CollectorsAdapter(
    private var collectors: List<Collector>,
    private val onCollectorClick: (Collector) -> Unit // Listener para clics
) : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>() {

    inner class CollectorViewHolder(private val binding: CollectorsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.collectors = collector
            binding.executePendingBindings() // Reflejar cambios en la vista

            // Configurar el clic en la vista raíz del elemento
            binding.root.setOnClickListener {
                onCollectorClick(collector) // Invoca el listener al hacer clic
            }
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
        collectors = newCollectors
        notifyDataSetChanged() // Actualiza toda la lista
    }
}
