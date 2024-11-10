package com.example.vinilos_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Performer
import com.example.vinyls_jetpack_application.databinding.PerformerItemBinding

class PerformerAdapter(
    private var performers: List<Performer>,
    private val onButtonClick: (Int) -> Unit
) : RecyclerView.Adapter<PerformerAdapter.PerformerViewHolder>() {

    inner class PerformerViewHolder(private val binding: PerformerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(performer: Performer) {
            binding.performer = performer
            binding.executePendingBindings() // Asegura que los cambios se reflejen en la vista
            binding.buttonVer.setOnClickListener {
                onButtonClick(performer.id) // Llama a la acción del botón con el ID del performer
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val binding = PerformerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerformerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        holder.bind(performers[position])
    }

    override fun getItemCount(): Int = performers.size

    fun updatePerformers(newPerformers: List<Performer>) {
        performers = newPerformers.sortedBy { it.name } // Ordena alfabéticamente por el nombre
        notifyDataSetChanged()
    }

    // Método para obtener los performers ordenados
    fun getPerformers(): List<Performer> = performers
}
