package com.example.vinilos_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Album
import com.example.vinyls_jetpack_application.databinding.AlbumItemBinding


class AlbumAdapter(private var albums: List<Album>, private val onButtonClick: (Int) -> Unit) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(private val binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.album = album
            binding.executePendingBindings() // Asegurar que los cambios se reflejen en la vista
            binding.buttonVer.setOnClickListener {
                onButtonClick(album.albumId) // Llama a la acción del botón
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    fun updateAlbums(newAlbums: List<Album>) {
        // DiffUtil can be used here for efficient updates, or simply manage based on counts
        val oldSize = albums.size
        albums = newAlbums
        val newSize = newAlbums.size

        when {
            newSize > oldSize -> {
                notifyItemRangeInserted(oldSize, newSize - oldSize)
            }
            newSize < oldSize -> {
                notifyItemRangeRemoved(newSize, oldSize - newSize)
            }
            else -> {
                // When sizes match, check for individual changes
                for (i in newAlbums.indices) {
                    if (albums[i] != newAlbums[i]) {
                        notifyItemChanged(i)
                    }
                }
            }
        }
    }
}
