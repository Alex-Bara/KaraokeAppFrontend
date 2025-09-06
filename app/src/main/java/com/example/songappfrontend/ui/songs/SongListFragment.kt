package com.example.songappfrontend.ui.songs

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.songappfrontend.R
import com.example.songappfrontend.databinding.FragmentSongListBinding
import com.example.songappfrontend.util.SessionManager

data class SongUI(val title: String)

class SongListFragment : Fragment() {

    private var _binding: FragmentSongListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // скажем системе, что у этого экрана есть меню тулбара
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSongListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val songs = listOf(
            SongUI("Imagine Dragons - Believer"),
            SongUI("Queen - Bohemian Rhapsody"),
            SongUI("Metallica - Nothing Else Matters"),
            SongUI("Linkin Park - Numb")
        )
        binding.songRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.songRecyclerView.adapter = SimpleSongAdapter(songs)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_profile -> {
                findNavController().navigate(R.id.profileFragment)
                true
            }
            R.id.action_logout -> {
                SessionManager(requireContext()).clear()
                // уходим на логин и чистим back stack
                findNavController().navigate(R.id.loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
