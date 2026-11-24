package co.edu.udea.compumovil.gr05_20262.lab2

import co.edu.udea.compumovil.gr05_20262.lab2.data.PostEntity
import co.edu.udea.compumovil.gr05_20262.lab2.network.PostDto
import co.edu.udea.compumovil.gr05_20262.lab2.network.ReactionsDto
import co.edu.udea.compumovil.gr05_20262.lab2.network.toEntity
import co.edu.udea.compumovil.gr05_20262.lab2.ui.Routes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias minimas del laboratorio.
 */
class Lab2UnitTest {

    @Test
    fun `postEntity_conservaLosCamposDelServicioRest`() {
        val post = PostEntity(
            id = 1,
            userId = 121,
            title = "His mother had always taught him",
            body = "His mother had always taught him not to ever think of himself as better than others.",
            tags = "history, american",
            reactions = 192,
            views = 305,
            syncedAt = 1000L
        )
        assertEquals(1, post.id)
        assertEquals(121, post.userId)
        assertEquals("His mother had always taught him", post.title)
        assertEquals("history, american", post.tags)
        assertEquals(192, post.reactions)
        assertEquals(305, post.views)
        assertEquals(1000L, post.syncedAt)
    }

    @Test
    fun `conversionDesdeElDto_conservaLaMarcaDeTiempo`() {
        val dto = PostDto(
            id = 7,
            userId = 42,
            title = "He was an expert but not in a discipline",
            body = "He was an expert but not in a discipline that anyone could fully appreciate.",
            tags = listOf("french", "fiction", "english"),
            views = 1200,
            reactions = ReactionsDto(likes = 859, dislikes = 32)
        )

        val entity = dto.toEntity(syncedAt = 5555L)

        assertEquals(7, entity.id)
        assertEquals("french, fiction, english", entity.tags)
        assertEquals(859, entity.reactions)
        assertEquals(1200, entity.views)
        assertEquals(5555L, entity.syncedAt)
    }

    @Test
    fun `rutaDeDetalleIncluyeElIdentificadorDelPost`() {
        val ruta = Routes.detail(42)
        assertEquals("post_detail/42", ruta)
        // La plantilla de la ruta declara el argumento entre llaves.
        assertTrue(Routes.DETAIL.contains("{${Routes.ARG_POST_ID}}"))
    }
}
