package com.example.vinilos_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Track
import com.example.vinyls_jetpack_application.R

class TrackAdapter(private var tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView = itemView.findViewById(R.id.track_name)
        val trackDuration: TextView = itemView.findViewById(R.id.track_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.trackName.text = track.name
        holder.trackDuration.text = track.duration
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
    fun updateTracks(newTracks: List<Track>) {
        val oldSize = tracks.size
        tracks = newTracks
        val newSize = newTracks.size

        when {
            newSize > oldSize -> notifyItemRangeInserted(oldSize, newSize - oldSize)
            newSize < oldSize -> notifyItemRangeRemoved(newSize, oldSize - newSize)
            else -> newTracks.indices.forEach { i ->
                if (tracks[i] != newTracks[i]) notifyItemChanged(i)
            }
        }
    }
}