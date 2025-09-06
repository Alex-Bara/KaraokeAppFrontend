package com.example.songappfrontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val songs: List<Song>
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songImage: ImageView = view.findViewById(R.id.songImage)
        val playButton: ImageButton = view.findViewById(R.id.playButton)
        val songTitle: TextView = view.findViewById(R.id.songTitle)
        val songAuthor: TextView = view.findViewById(R.id.songAuthor)
        val micButton: ImageButton = view.findViewById(R.id.micButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.songTitle.text = song.title
        holder.songAuthor.text = song.author

        // Показываем/скрываем play-кнопку при клике по картинке
        holder.songImage.setOnClickListener {
            holder.playButton.visibility =
                if (holder.playButton.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Обработка кнопки play
        holder.playButton.setOnClickListener {
            // пока просто тост
            android.widget.Toast.makeText(
                holder.itemView.context,
                "Играем: ${song.title}",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        // Обработка микрофона
        holder.micButton.setOnClickListener {
            android.widget.Toast.makeText(
                holder.itemView.context,
                "Запись вокала для ${song.title}",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        // Переход на PlayerActivity при клике на всю карточку
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("author", song.author)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = songs.size
}
