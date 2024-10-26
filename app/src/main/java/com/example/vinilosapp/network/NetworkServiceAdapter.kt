package com.example.vinyls_jetpack_application.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinilosapp.models.Album
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter private constructor(context: Context) {

    companion object {
        private const val BASE_URL = "https://backvynils-q6yc.onrender.com/"
        @Volatile
        private var instance: NetworkServiceAdapter? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also { instance = it }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getAlbums(onComplete: (List<Album>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(Album(
                        albumId = item.getInt("id"),
                        name = item.getString("name"),
                        cover = item.getString("cover"),
                        recordLabel = item.getString("recordLabel"),
                        releaseDate = item.getString("releaseDate"),
                        genre = item.getString("genre"),
                        description = item.getString("description")
                    ))
                }
                onComplete(list)
            },
            { error -> onError(error) } // Simplified lambda usage
        ))
    }

    fun getAlbumDetail(albumId: Int, onComplete: (Album) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val item = JSONObject(response)
                val album = Album(
                    albumId = item.getInt("id"),
                    name = item.getString("name"),
                    cover = item.getString("cover"),
                    recordLabel = item.getString("recordLabel"),
                    releaseDate = item.getString("releaseDate"),
                    genre = item.getString("genre"),
                    description = item.getString("description")
                )
                onComplete(album)
            },
            { error -> onError(error) } // Simplified lambda usage
        ))
    }

    private fun getRequest(path: String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }
}