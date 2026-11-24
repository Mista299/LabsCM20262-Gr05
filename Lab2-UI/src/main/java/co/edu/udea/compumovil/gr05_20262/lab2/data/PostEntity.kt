package co.edu.udea.compumovil.gr05_20262.lab2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad local (Room) que guarda una publicacion descargada del servicio REST.
 *
 * El servicio REST utilizado es DummyJSON (https://dummyjson.com), que devuelve
 * posts con contenido real en ingles y sin necesidad de registro ni clave de API.
 *
 * El worker se encarga de refrescar estos datos periodicamente.
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val tags: String,
    val reactions: Int,
    val views: Int,
    val syncedAt: Long = System.currentTimeMillis()
)
