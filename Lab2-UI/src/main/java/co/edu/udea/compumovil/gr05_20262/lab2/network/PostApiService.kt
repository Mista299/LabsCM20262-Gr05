package co.edu.udea.compumovil.gr05_20262.lab2.network

import com.google.gson.annotations.SerializedName
import co.edu.udea.compumovil.gr05_20262.lab2.data.PostEntity
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Respuesta del endpoint de DummyJSON.
 *
 * DummyJSON envuelve la coleccion en un objeto "posts" e incluye el total de
 * registros disponibles en el campo "total".
 */
data class PostsResponse(
    @SerializedName("posts") val posts: List<PostDto>,
    @SerializedName("total") val total: Int
)

/**
 * Publicacion tal como la devuelve el servicio, antes de convertirla a entidad.
 */
data class PostDto(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("views") val views: Int?,
    @SerializedName("reactions") val reactions: ReactionsDto?
)

data class ReactionsDto(
    @SerializedName("likes") val likes: Int?,
    @SerializedName("dislikes") val dislikes: Int?
)

/**
 * Servicio REST consumido por el worker.
 * Base URL: https://dummyjson.com/
 */
interface PostApiService {

    /**
     * Obtiene las publicaciones del servicio.
     *
     * @param limit cantidad maxima de publicaciones a descargar.
     */
    @GET("posts")
    suspend fun getPosts(@Query("limit") limit: Int = DEFAULT_LIMIT): PostsResponse

    companion object {
        const val DEFAULT_LIMIT = 100
    }
}

/**
 * Convierte la respuesta del servicio en las entidades que se guardan en Room.
 */
fun PostDto.toEntity(syncedAt: Long): PostEntity = PostEntity(
    id = id,
    userId = userId,
    title = title,
    body = body,
    tags = tags?.joinToString(", ") ?: "",
    reactions = reactions?.likes ?: 0,
    views = views ?: 0,
    syncedAt = syncedAt
)
