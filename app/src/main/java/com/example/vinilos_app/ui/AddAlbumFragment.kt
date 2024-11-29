package com.example.vinilos_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.repository.AlbumRepository
import com.example.vinilos_app.viewmodels.AlbumViewModel
import com.example.vinyls_jetpack_application.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AddAlbumFragment : Fragment() {

    private lateinit var viewModel: AlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_album_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear la instancia de AlbumRepository
        val repository = AlbumRepository(requireContext())

        // Usar ViewModelProvider para inicializar AlbumCatalogViewModel con argumentos
        viewModel = ViewModelProvider(this, AlbumViewModelFactory(repository))
            .get(AlbumViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumName = view.findViewById<EditText>(R.id.albumName)
        val albumCover = view.findViewById<EditText>(R.id.albumCover)
        val albumReleaseDate = view.findViewById<EditText>(R.id.albumReleaseDate)
        val albumDescription = view.findViewById<EditText>(R.id.albumDescription)
        val saveAlbumButton = view.findViewById<Button>(R.id.saveAlbum)

        val genreSpinner = view.findViewById<Spinner>(R.id.albumGenre)
        val genres = arrayOf("Classical", "Salsa", "Rock", "Folk")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = adapter

        val recordLabelSpinner = view.findViewById<Spinner>(R.id.albumRecordLabel)
        val records = arrayOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")
        val adapterRecords = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, records)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        recordLabelSpinner.adapter = adapterRecords


        saveAlbumButton.setOnClickListener {
            val name = albumName.text.toString().trim()
            val cover = albumCover.text.toString().trim()
            val releaseDate = albumReleaseDate.text.toString().trim()
            val description = albumDescription.text.toString().trim()
            val genre = genreSpinner.selectedItem.toString().trim()
            val recordLabel = recordLabelSpinner.selectedItem.toString().trim()

            // Validar campos
            if (validateFields(name, cover, releaseDate, description, genre, recordLabel)) {
                val album = Album(
                    albumId = 0,
                    name = name,
                    cover =cover,
                    releaseDate = releaseDate,
                    description = description,
                    genre = genre,
                    recordLabel = recordLabel,
                    emptyList(),
                    emptyList(),
                    emptyList()
                )

                viewModel.addAlbum(
                    album,
                    onSuccess = { returnedAlbum ->
                        Toast.makeText(requireContext(), "Álbum creado: ${returnedAlbum?.albumId}", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        val cancelButton = view.findViewById<Button>(R.id.cancelAlbum)
        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun validateFields(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String
    ): Boolean {
        // Revisa si algún campo está vacío
        if (name.isEmpty() || cover.isEmpty() || releaseDate.isEmpty() || description.isEmpty() || genre.isEmpty() || recordLabel.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        }

        // Valida que la fecha sea válida
        if (!isValidDate(releaseDate)) {
            Toast.makeText(requireContext(), "La fecha debe estar en formato yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidDate(date: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date) // Intentar parsear la fecha
            true
        } catch (e: ParseException) {
            false
        }
    }


    class AlbumViewModelFactory(private val repository: AlbumRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}