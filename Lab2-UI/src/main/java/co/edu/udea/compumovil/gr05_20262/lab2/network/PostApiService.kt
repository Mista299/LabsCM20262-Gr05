package co.edu.udea.compumovil.gr05_20262.lab2.network

import co.edu.udea.compumovil.gr05_20262.lab2.data.PostEntity
import retrofit2.http.GET

/**
 * Servicio REST consumido por el worker.
 * Base URL: https://jsonplaceholder.typicode.com/
 */
interface PostApiService {

    @GET("posts")
    suspend fun getPosts(): List<PostEntity>
}
