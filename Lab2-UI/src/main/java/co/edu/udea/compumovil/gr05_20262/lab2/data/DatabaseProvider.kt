package co.edu.udea.compumovil.gr05_20262.lab2.data

import android.content.Context
import androidx.room.Room

/**
 * Singleton de la base de datos Room.
 */
object DatabaseProvider {

    @Volatile
    private var instance: LabDatabase? = null

    fun get(context: Context): LabDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                LabDatabase::class.java,
                "lab2-db"
            ).build().also { instance = it }
        }
}
