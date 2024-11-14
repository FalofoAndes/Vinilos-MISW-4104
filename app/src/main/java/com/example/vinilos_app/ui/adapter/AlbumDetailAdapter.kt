package com.example.vinilos_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Album
import com.example.vinyls_jetpack_application.databinding.AlbumDetailItemBinding

class AlbumDetailAdapter(
    private var album: Album // En lugar de una lista, solo un objeto `Album`
) : RecyclerView.Adapter<AlbumDetailAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(album)
    }

    override fun getItemCount(): Int = 1 // Solo un elemento en este caso

    inner class AlbumViewHolder(private val binding: AlbumDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.album = album
            binding.executePendingBindings() // Asegurar que los cambios se reflejen en la vista
        }
    }

}