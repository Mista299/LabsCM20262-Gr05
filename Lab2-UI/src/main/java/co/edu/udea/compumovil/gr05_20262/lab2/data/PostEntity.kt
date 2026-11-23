package co.edu.udea.compumovil.gr05_20262.lab2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad local (Room) que guarda un post descargado del servicio REST.
 * El worker se encarga de refrescar estos datos periodicamente.
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val syncedAt: Long = System.currentTimeMillis()
)
