package com.example.songappfrontend.ui.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.songappfrontend.databinding.ItemSongSimpleBinding

class SimpleSongAdapter(private val items: List<SongUI>) :
    RecyclerView.Adapter<SimpleSongAdapter.VH>() {

    class VH(val binding: ItemSongSimpleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSongSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.title.text = items[position].title
    }

    override fun getItemCount() = items.size
}
