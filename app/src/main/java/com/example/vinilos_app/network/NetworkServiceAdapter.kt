package com.example.vinilos_app.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.models.Collector
import com.example.vinilos_app.models.Comment
import com.example.vinilos_app.models.Performer
import com.example.vinilos_app.models.Track
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
        EspressoIdlingResource.increment()
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val album = getAlbum(resp.getJSONObject(i))

                    list.add(
                        album
                    )
                }
                onComplete(list)
                EspressoIdlingResource.decrement()
            },
            { error ->
                onError(error)
                EspressoIdlingResource.decrement()
            } // Simplified lambda usage
        ))
    }

    fun getAlbumDetail(albumId: Int, onComplete: (Album) -> Unit, onError: (error: VolleyError) -> Unit) {
        EspressoIdlingResource.increment()
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val item = JSONObject(response)
                val album = getAlbum(item)
                onComplete(album)
                EspressoIdlingResource.decrement()
            },
            { error ->
                onError(error)
                EspressoIdlingResource.decrement()
            } // Simplified lambda usage
        ))
    }

    private fun getRequest(path: String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

    private fun getAlbum(jsonObject: JSONObject): Album {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Parse tracks
        val tracksJsonArray = jsonObject.getJSONArray("tracks")
        val tracks = mutableListOf<Track>()
        for (i in 0 until tracksJsonArray.length()) {
            val trackObject = tracksJsonArray.getJSONObject(i)
            val track = Track(
                id = trackObject.getInt("id"),
                name = trackObject.getString("name"),
                duration = trackObject.getString("duration")
            )
            tracks.add(track)
        }

        // Parse performers
        val performersJsonArray = jsonObject.getJSONArray("performers")
        val performers = mutableListOf<Performer>()
        for (i in 0 until performersJsonArray.length()) {
            val performerObject = performersJsonArray.getJSONObject(i)
            val performer = Performer(
                id = performerObject.getInt("id"),
                name = performerObject.getString("name"),
                image = performerObject.getString("image"),
                description = performerObject.getString("description"),
                birthDate = getNullable(performerObject, "birthDate"),
                creationDate = getNullable(performerObject, "creationDate")
            )
            performers.add(performer)
        }

        // Parse comments
        val commentsJsonArray = jsonObject.getJSONArray("comments")
        val comments = mutableListOf<Comment>()
        for (i in 0 until commentsJsonArray.length()) {
            val commentObject = commentsJsonArray.getJSONObject(i)
            val comment = Comment(
                id = commentObject.getInt("id"),
                description = commentObject.getString("description"),
                rating = commentObject.getInt("rating")
            )
            comments.add(comment)
        }

        val album = Album(
            albumId = jsonObject.getInt("id"),
            name = jsonObject.getString("name"),
            cover = jsonObject.getString("cover"),
            recordLabel = jsonObject.getString("recordLabel"),
            releaseDate = jsonObject.getString("releaseDate"),
            genre = jsonObject.getString("genre"),
            description = jsonObject.getString("description"),
            tracks = tracks,
            comments = comments,
            performers = performers
        )
        return album
    }

    suspend fun getCollectors(): List<Collector> = suspendCoroutine { cont ->
        EspressoIdlingResource.increment()

        requestQueue.add(getRequest("collectors",
            { response ->
                try {
                    val resp = JSONArray(response)
                    val list = mutableListOf<Collector>()
                    for (i in 0 until resp.length()) {
                        val collector = getCollector(resp.getJSONObject(i))
                        list.add(collector)
                    }
                    cont.resume(list) // Resumes with the result
                } catch (e: Exception) {
                    cont.resumeWithException(e) // Resumes with exception
                } finally {
                    EspressoIdlingResource.decrement()
                }
            },
            { error ->
                cont.resumeWithException(error) // Resumes with VolleyError
                EspressoIdlingResource.decrement()
            }
        ))
    }

    private fun getCollector(jsonObject: JSONObject): Collector {
        val collector = Collector(
            collectorId = jsonObject.getInt("id"),
            name = jsonObject.getString("name"),
            telephone = jsonObject.getString("telephone"),
            email = jsonObject.getString("email")
        )
        return collector
    }

    private fun getNullable(
        jsonObject: JSONObject, param: String
    ): String = if (jsonObject.has(param)) jsonObject.getString(param) else ""


    suspend fun getPerformers(): List<Performer> = suspendCancellableCoroutine { continuation ->
        EspressoIdlingResource.increment()
        requestQueue.add(getRequest("musicians",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Performer>()
                for (i in 0 until resp.length()) {
                    val performer = getPerformer(resp.getJSONObject(i))
                    list.add(performer)
                }
                continuation.resume(list) // Retorna la lista en caso de éxito
                EspressoIdlingResource.decrement()
            },
            { error ->
                continuation.resumeWithException(error) // Lanza una excepción en caso de error
                EspressoIdlingResource.decrement()
            }
        ))
    }

    private fun getPerformer(jsonObject: JSONObject): Performer {
        return Performer(
            id = jsonObject.getInt("id"),
            name = jsonObject.getString("name"),
            image = jsonObject.getString("image"),
            description = jsonObject.getString("description"),
            birthDate = getNullable(jsonObject, "birthDate"),
            creationDate = getNullable(jsonObject, "creationDate")
        )
    }


}
