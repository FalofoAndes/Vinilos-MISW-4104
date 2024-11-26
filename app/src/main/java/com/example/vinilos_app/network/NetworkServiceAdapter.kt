package com.example.vinilos_app.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.models.Collector
import com.example.vinilos_app.models.CollectorDetail
import com.example.vinilos_app.models.Comment
import com.example.vinilos_app.models.Performer
import com.example.vinilos_app.models.Track
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
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

    fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL + path, body, responseListener, errorListener)
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


    suspend fun getPerformers(): List<Performer> = withContext(Dispatchers.IO) {
        EspressoIdlingResource.increment()
        return@withContext try {
            val response = suspendCancellableCoroutine<String> { continuation ->
                requestQueue.add(getRequest("musicians",
                    { response ->
                        continuation.resume(response)
                    },
                    { error ->
                        continuation.resumeWithException(error)
                    }
                ))
            }
            val resp = JSONArray(response)
            val list = mutableListOf<Performer>()
            for (i in 0 until resp.length()) {
                val performer = getPerformer(resp.getJSONObject(i))
                list.add(performer)
            }
            EspressoIdlingResource.decrement()
            list // Returns the list of performers
        } catch (e: Exception) {
            EspressoIdlingResource.decrement()
            throw e // Rethrow exception if something goes wrong
        }
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

    fun getPerformerDetail(musicianId: Int, onComplete: (Performer) -> Unit, onError: (error: VolleyError) -> Unit) {
        EspressoIdlingResource.increment()
        requestQueue.add(getRequest("musicians/$musicianId",
            { response ->
                val item = JSONObject(response)
                val musician = getPerformer(item)
                onComplete(musician)
                EspressoIdlingResource.decrement()
            },
            { error ->
                onError(error)
                EspressoIdlingResource.decrement()
            }
        ))
    }

    fun postAlbums(
        album: Album,
        onComplete: (Album) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        EspressoIdlingResource.increment()

        val postParams = JSONObject().apply {
            put("name", album.name)
            put("cover", album.cover)
            put("releaseDate", album.releaseDate)
            put("description", album.description)
            put("genre", album.genre)
            put("recordLabel", album.recordLabel)
        }

        requestQueue.add(
            postRequest(
                "albums",
                postParams,
                { response ->
                    try {
                        val returnedAlbum = Album(
                            albumId = response.getInt("id"),
                            name = response.getString("name"),
                            cover = response.getString("cover"),
                            releaseDate = response.getString("releaseDate"),
                            description = response.getString("description"),
                            genre = response.getString("genre"),
                            recordLabel = response.getString("recordLabel"),
                            emptyList(),
                            emptyList(),
                            emptyList()
                        )
                        onComplete(returnedAlbum)
                    } catch (e: Exception) {
                        onError(VolleyError("Error parsing response: ${e.message}"))
                    } finally {
                        EspressoIdlingResource.decrement()
                    }
                },
                { error ->
                    onError(error)
                    EspressoIdlingResource.decrement()
                }
            )
        )
    }



    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = withContext(Dispatchers.IO) {
        suspendCoroutine { cont ->
            EspressoIdlingResource.increment()

            requestQueue.add(getRequest("collectors/$collectorId",
                { response ->
                    try {
                        val jsonObject = JSONObject(response)

                        // Parsear el detalle del collector
                        val collectorDetail = CollectorDetail(
                            collectorId = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            telephone = jsonObject.optString("telephone", ""),
                            email = jsonObject.optString("email", ""),
                            comments = jsonObject.getJSONArray("comments").let { commentsArray ->
                                List(commentsArray.length()) { i ->
                                    val commentObj = commentsArray.getJSONObject(i)
                                    Comment(
                                        id = commentObj.getInt("id"),
                                        description = commentObj.getString("description"),
                                        rating = commentObj.getInt("rating")
                                    )
                                }
                            },
                            favoritePerformers = jsonObject.getJSONArray("favoritePerformers").let { performersArray ->
                                List(performersArray.length()) { i ->
                                    val performerObj = performersArray.getJSONObject(i)
                                    Performer(
                                        id = performerObj.getInt("id"),
                                        name = performerObj.getString("name"),
                                        image = performerObj.optString("image", ""),
                                        description = performerObj.optString("description", ""),
                                        birthDate = performerObj.optString("birthDate", ""),
                                        creationDate = getNullable(jsonObject, "creationDate")
                                    )
                                }
                            },

                        )

                        cont.resume(collectorDetail) // Retorna el detalle del coleccionista
                    } catch (e: Exception) {
                        cont.resumeWithException(e) // Manejo de excepción
                    } finally {
                        EspressoIdlingResource.decrement()
                    }
                },
                { error ->
                    cont.resumeWithException(error) // Manejo de error de red
                    EspressoIdlingResource.decrement()
                }
            ))
        }
    }


}
