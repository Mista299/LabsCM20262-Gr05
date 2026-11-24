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
            )
                // El contenido de la base de datos es una copia cache del
                // servicio REST, asi que se puede regenerar en el siguiente
                // ciclo del worker. Por eso la migracion es destructiva.
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
}
