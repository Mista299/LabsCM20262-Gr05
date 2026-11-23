package co.edu.udea.compumovil.gr05_20262.lab2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.edu.udea.compumovil.gr05_20262.lab2.data.DatabaseProvider
import co.edu.udea.compumovil.gr05_20262.lab2.data.PostEntity
import co.edu.udea.compumovil.gr05_20262.lab2.worker.SyncScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Expone la lista de posts descargados por el worker y dispara sincronizaciones.
 */
class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = DatabaseProvider.get(application).postDao()

    val posts: StateFlow<List<PostEntity>> = dao.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        // La tarea recurrente se programa al abrir la app.
        SyncScheduler.schedule(application)
    }

    fun refreshNow() {
        SyncScheduler.syncNow(getApplication())
    }
}
