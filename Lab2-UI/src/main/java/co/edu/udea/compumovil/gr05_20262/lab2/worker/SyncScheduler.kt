package co.edu.udea.compumovil.gr05_20262.lab2.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Programa la tarea recurrente del [SyncPostsWorker].
 */
object SyncScheduler {

    private const val WORK_NAME = "sync_posts_periodic"

    /** Intervalo minimo permitido por WorkManager para tareas periodicas: 15 minutos. */
    private const val INTERVAL_MINUTES = 15L

    fun schedule(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncPostsWorker>(
            INTERVAL_MINUTES, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /** Fuerza una ejecucion inmediata (usado por el boton "Actualizar"). */
    fun syncNow(context: Context) {
        val request = androidx.work.OneTimeWorkRequestBuilder<SyncPostsWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
