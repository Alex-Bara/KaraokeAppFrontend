package com.example.songappfrontend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SongAdapter(private val songs: List<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.songTitle)
        val playBtn: ImageButton = itemView.findViewById(R.id.playButton)
        val micBtn: ImageButton = itemView.findViewById(R.id.micButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title

        holder.playBtn.setOnClickListener {
            // TODO: запуск плеера
        }
        holder.micBtn.setOnClickListener {
            // TODO: запись вокала
        }
    }

    override fun getItemCount() = songs.size
}
