package co.edu.udea.compumovil.gr05_20262.lab2.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.edu.udea.compumovil.gr05_20262.lab2.data.DatabaseProvider
import co.edu.udea.compumovil.gr05_20262.lab2.network.RetrofitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker recurrente en segundo plano.
 *
 * Consume un servicio REST (https://jsonplaceholder.typicode.com/posts)
 * y guarda el resultado en la base de datos local (Room).
 *
 * Se programa cada 15 minutos con WorkManager (ver [SyncScheduler]).
 */
class SyncPostsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val posts = RetrofitProvider.apiService.getPosts()
            if (posts.isEmpty()) {
                Result.success()
            } else {
                // La marca de tiempo se asigna aqui porque el servicio REST
                // no la envia y el valor por defecto de Kotlin no se aplica
                // al deserializar con Gson.
                val syncedAt = System.currentTimeMillis()
                DatabaseProvider.get(applicationContext)
                    .postDao()
                    .insertAll(posts.map { it.copy(syncedAt = syncedAt) })
                Result.success()
            }
        } catch (e: Exception) {
            // Se reintenta ante fallos de red transitorios
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
